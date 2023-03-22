import * as React from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import Container from "@mui/material/Container";
import Radio from "@mui/material/Radio";
import RadioGroup from "@mui/material/RadioGroup";
import FormControlLabel from "@mui/material/FormControlLabel";
import Autocomplete from "@mui/material/Autocomplete";

export default function Create() {
  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    console.log({
      title: data.get("title"),
      description: data.get("description"),
    });
  };

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
        <h2>Create topic</h2>
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
                renderInput={(params) => (
                  <TextField {...params} label="Category" />
                )}
              />
            </Grid>
            <Grid item xs={12}>
              <RadioGroup
                aria-labelledby="demo-radio-buttons-group-label"
                defaultValue="private"
                name="radio-buttons-group"
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
          <Button fullWidth variant="text" sx={{ mt: 3, mb: 2 }}>
            Add Collaborators
          </Button>
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

const categories = [
  { label: "Business" },
  { label: "Education" },
  { label: "Science and Technology" },
  { label: "Health and Medicine" },
  { label: "Environment and Sustainability" },
  { label: "Arts and Culture" },
  { label: "Sports and Recreation" },
  { label: "Travel and Tourism" },
  { label: "Social Issues and Ethics" },
  { label: "Politics and Government" },
  { label: "History and Heritage" },
  { label: "Food and Beverage" },
  { label: "Fashion and Style" },
  { label: "Home and Design" },
  { label: "Personal Development and Self-Improvement" },
];
