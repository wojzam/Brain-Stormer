import React, { useState, useEffect } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  Typography,
  TextField,
  Select,
  MenuItem,
} from "@mui/material";
import { Delete as DeleteIcon } from "@mui/icons-material";

function AdminDashboard() {
  const [users, setUsers] = useState([]);
  const [searchText, setSearchText] = useState("");

  useEffect(() => {
    fetchUsers();
  }, [searchText]);

  const fetchUsers = async () => {
    const token = localStorage.getItem("token");

    fetch(`/api/admin/user?username=${searchText}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setUsers(data);
      });
  };

  const handleChangeRole = (userId, newRole) => {
    const token = localStorage.getItem("token");

    fetch(`/api/admin/user/${userId}?role=${newRole}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    }).then(() => {
      setUsers((prevUsers) =>
        prevUsers.map((user) =>
          user.id === userId ? { ...user, role: newRole } : user
        )
      );
    });
  };

  const deleteUser = (userId) => {
    const token = localStorage.getItem("token");

    fetch(`/api/admin/user/${userId}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }).then(() => {
      setUsers((user) => user.filter((user) => user.id !== userId));
    });
  };

  const isCurrentUser = (id) => {
    return id === localStorage.getItem("id");
  };

  return (
    <div>
      <Typography component="h1" variant="h2" fontWeight="medium" gutterBottom>
        Admin Dashboard
      </Typography>
      <TextField
        label="Search user"
        variant="outlined"
        value={searchText}
        onChange={(e) => setSearchText(e.target.value)}
        sx={{ mb: 3 }}
      />
      <TableContainer component={Paper}>
        <Table sx={{ width: 650 }}>
          <TableHead>
            <TableRow>
              <TableCell>Username</TableCell>
              <TableCell>Email</TableCell>
              <TableCell>Role</TableCell>
              <TableCell></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {users.map((user) => (
              <TableRow key={user.id}>
                <TableCell>{user.username}</TableCell>
                <TableCell>{user.email}</TableCell>
                <TableCell>
                  <Select
                    value={user.role}
                    disabled={isCurrentUser(user.id)}
                    onChange={(event) =>
                      handleChangeRole(user.id, event.target.value)
                    }
                  >
                    <MenuItem value="USER">User</MenuItem>
                    <MenuItem value="ADMIN">Admin</MenuItem>
                  </Select>
                </TableCell>
                <TableCell>
                  <IconButton
                    aria-label="delete"
                    onClick={() => deleteUser(user.id)}
                    disabled={isCurrentUser(user.id)}
                  >
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}

export default AdminDashboard;
