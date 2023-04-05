import { useState } from "react";
import { Box, TextField, Button, IconButton } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";

const createMode = "create";

export const IdeaForm = ({
  mode,
  setIdeas,
  setIsFormVisible,
  topicId,
  id,
  title: initialTitle = "",
  description: initialDescription = "",
}) => {
  const [title, setTitle] = useState(initialTitle);
  const [description, setDescription] = useState(initialDescription);

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

  const handleUpdateIdea = () => {
    if (title.trim() !== "") {
      const token = localStorage.getItem("token");

      fetch(`/api/idea/${id}?title=${title}&description=${description}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }).then(() => {
        setIdeas((prevIdeas) =>
          prevIdeas.map((idea) =>
            idea.id === id ? { ...idea, title, description } : idea
          )
        );
        setIsFormVisible(false);
      });
    }
  };

  return (
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
      {mode === createMode ? (
        <Button
          variant="contained"
          onClick={handleAddIdea}
          sx={{ marginBottom: 2 }}
        >
          Add Idea
        </Button>
      ) : (
        <Button
          variant="contained"
          onClick={handleUpdateIdea}
          sx={{ marginBottom: 2 }}
        >
          Update
        </Button>
      )}
    </Box>
  );
};
