import { useState } from "react";
import { InputBase, IconButton, Paper } from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";

export default function SearchBar() {
  const [searchQuery, setSearchQuery] = useState("");

  const handleInputChange = (event) => {
    setSearchQuery(event.target.value);
  };

  const handleSearch = () => {
    console.log(`Searching for "${searchQuery}"...`);
  };

  return (
    <Paper
      component="form"
      sx={{
        p: "2px 4px",
        display: "flex",
        alignItems: "center",
        height: 50,
        width: 300,
      }}
    >
      <InputBase
        sx={{ ml: 1, flex: 1 }}
        placeholder="Search..."
        value={searchQuery}
        onChange={handleInputChange}
      />
      <IconButton type="submit" sx={{ p: "10px" }} onClick={handleSearch}>
        <SearchIcon />
      </IconButton>
    </Paper>
  );
}
