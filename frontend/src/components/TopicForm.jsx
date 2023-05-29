import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import Radio from "@mui/material/Radio";
import RadioGroup from "@mui/material/RadioGroup";
import FormControlLabel from "@mui/material/FormControlLabel";
import Autocomplete from "@mui/material/Autocomplete";
import CollaboratorsDialog from "../components/CollaboratorsDialog";

export const TopicForm = ({
  handleSubmit,
  categories,
  setSelectedCategory,
  collaborators,
  setCollaborators,
}) => {
  return (
    <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
      <Grid container spacing={2} sx={{ mb: 2 }}>
        <Grid item xs={12}>
          <TextField required fullWidth id="title" label="Title" name="title" />
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
            renderInput={(params) => <TextField {...params} label="Category" />}
          />
        </Grid>
        <Grid item xs={12} display="flex" justifyContent="center">
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
      <CollaboratorsDialog {...{ collaborators, setCollaborators }} />
      <Button type="submit" fullWidth variant="contained" sx={{ mt: 5, mb: 2 }}>
        Create
      </Button>
    </Box>
  );
};
