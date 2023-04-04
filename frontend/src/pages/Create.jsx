import { useState, useEffect } from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import Container from "@mui/material/Container";
import Radio from "@mui/material/Radio";
import RadioGroup from "@mui/material/RadioGroup";
import FormControlLabel from "@mui/material/FormControlLabel";
import Autocomplete from "@mui/material/Autocomplete";
import Typography from "@mui/material/Typography";
import CollaboratorsDialog from "../components/CollaboratorsDialog";

export default function CreateTopic() {
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState();

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const token = localStorage.getItem("token");

    fetch("/api/topic", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        title: data.get("title"),
        description: data.get("description"),
        publicVisibility: data.get("publicVisibility") === "public",
        categoryName: selectedCategory.name,
      }),
    })
      .then((response) => response.json())
      .then((data) => {
        window.location.href = `/topic/${data.id}`;
      });
  };

  useEffect(() => {
    fetch("/api/public/category")
      .then((response) => response.json())
      .then((data) => {
        setCategories(data);
      });
  }, []);

  return (
    <Container component="main" maxWidth="xs">
      <Box
        sx={{
          marginTop: 8,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <Typography component="h1" variant="h4" gutterBottom>
          Create topic
        </Typography>
        <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                id="title"
                label="Title"
                name="title"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                name="description"
                label="Description"
                id="description"
                multiline
                rows={5}
              />
            </Grid>
            <Grid item xs={12}>
              <Autocomplete
                disablePortal
                id="category"
                options={categories}
                onChange={(event, value) => setSelectedCategory(value)}
                renderInput={(params) => (
                  <TextField {...params} label="Category" />
                )}
              />
            </Grid>
            <Grid item xs={12}>
              <RadioGroup
                aria-labelledby="radio-buttons-group-label"
                defaultValue="private"
                name="publicVisibility"
                row
              >
                <FormControlLabel
                  value="private"
                  control={<Radio />}
                  label="Private"
                />
                <FormControlLabel
                  value="public"
                  control={<Radio />}
                  label="Public"
                />
              </RadioGroup>
            </Grid>
          </Grid>
          <CollaboratorsDialog />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            Create
          </Button>
        </Box>
      </Box>
    </Container>
  );
}
