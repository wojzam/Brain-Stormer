import Button from "@mui/material/Button";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import Radio from "@mui/material/Radio";
import RadioGroup from "@mui/material/RadioGroup";
import FormControlLabel from "@mui/material/FormControlLabel";
import CollaboratorsDialog from "../components/CollaboratorsDialog";
import ValidatedTextField from "./ValidatedTextField";
import CategorySelect from "./CategorySelect";

export const TopicForm = ({
  handleSubmit,
  setSelectedCategory,
  collaborators,
  setCollaborators,
}) => {
  return (
    <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
      <Grid container spacing={2} sx={{ mb: 2 }}>
        <Grid item xs={12}>
          <ValidatedTextField
            id="title"
            label="Title"
            name="title"
            maxLength={50}
          />
        </Grid>
        <Grid item xs={12}>
          <ValidatedTextField
            name="description"
            label="Description"
            id="description"
            multiline
            rows={5}
            minLength={0}
            maxLength={1000}
          />
        </Grid>
        <Grid item xs={12}>
          <CategorySelect {...{ selectedCategory: "", setSelectedCategory }} />
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
