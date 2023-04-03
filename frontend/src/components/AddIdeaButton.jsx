import { useState } from "react";
import { Box, Typography, TextField, Button, IconButton } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import CloseIcon from "@mui/icons-material/Close";

export default function AddIdeaButton({ topicId, setIdeas }) {
  const [isFormVisible, setIsFormVisible] = useState(false);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");

  const handleAddIdea = () => {
    if (title.trim() !== "") {
      const token = localStorage.getItem("token");

      fetch("/api/idea", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          topicId: topicId,
          title: title,
          description: description,
        }),
      })
        .then((response) => response.json())
        .then((data) => {
          setIdeas((prevIdeas) => [data, ...prevIdeas]);
          setIsFormVisible(false);
          setTitle("");
          setDescription("");
        });
    }
  };

  return (
    <>
      {isFormVisible ? (
        <Box
          display="flex"
          flexDirection="column"
          width="100%"
          sx={{
            my: 2,
            px: 5,
            py: 2,
            border: "solid",
            borderColor: "#ebebeb",
            borderRadius: 8,
            background: "background",
          }}
        >
          <Box display="flex" justifyContent="end" mb="0.5em">
            <IconButton onClick={() => setIsFormVisible(false)}>
              <CloseIcon />
            </IconButton>
          </Box>
          <TextField
            id="title"
            label="Title"
            value={title}
            required
            onChange={(event) => setTitle(event.target.value)}
            sx={{ marginBottom: 2 }}
          />
          <TextField
            id="description"
            label="Description"
            value={description}
            onChange={(event) => setDescription(event.target.value)}
            multiline
            rows={3}
            sx={{ marginBottom: 2 }}
          />
          <Button
            variant="contained"
            onClick={handleAddIdea}
            sx={{ marginBottom: 2 }}
          >
            Add Idea
          </Button>
        </Box>
      ) : (
        <Box
          display="flex"
          flexDirection="row"
          justifyContent="center"
          alignItems="center"
          width="100%"
          onClick={() => setIsFormVisible(true)}
          sx={{
            my: 2,
            px: 5,
            py: 2,
            border: "dashed",
            borderColor: "#ebebeb",
            borderRadius: 8,
            background: "background",
            cursor: "pointer",
          }}
        >
          <AddIcon />
          <Typography variant="h5" fontWeight="regular">
            ADD IDEA
          </Typography>
        </Box>
      )}
    </>
  );
}
