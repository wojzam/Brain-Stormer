import { Box, IconButton, Typography } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import BackButton from "../components/BackButton";
import Idea from "../components/Idea";

const Topic = () => {
  return (
    <>
      <BackButton />
      <Box
        display="flex"
        justifyContent="space-between"
        gap="4em"
        width="100%"
        my="2em"
      >
        <Box>
          <Box
            display="flex"
            flexDirection="row"
            justifyContent="space-between"
          >
            <Typography
              component="h2"
              variant="h2"
              fontWeight="regular"
              gutterBottom
            >
              Topic title
            </Typography>
            <Typography variant="h5" fontWeight="regular" noWrap>
              Collaborators: 4
            </Typography>
          </Box>
          <Typography variant="h5" fontWeight="regular">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi
            laoreet massa non lacinia bibendum. Aenean sollicitudin bibendum
            nisi. Phasellus sed sollicitudin ipsum. Vivamus id eros eu lacus
            bibendum pharetra. Phasellus a magna commodo ex sagittis dictum.
            Duis egestas elit in porttitor lobortis. Maecenas tortor tellus,
            lobortis non dolor nec, eleifend blandit neque. Aliquam ac
            ullamcorper lacus, sed faucibus erat.
          </Typography>
        </Box>
      </Box>
      <Idea title="Test Idea 1" description="description" />
      <Idea title="Test Idea 2" description="description" />
      <Idea
        title="Test Idea 3"
        description=" Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi
            laoreet massa non lacinia bibendum. Aenean sollicitudin bibendum
            nisi. Phasellus sed sollicitudin ipsum. Vivamus id eros eu lacus
            bibendum pharetra. Phasellus a magna commodo ex sagittis dictum.
            Duis egestas elit in porttitor lobortis. Maecenas tortor tellus,
            lobortis non dolor nec, eleifend blandit neque. Aliquam ac
            ullamcorper lacus, sed faucibus erat."
      />
      <Box display="flex" justifyContent="center" width="100%" my="1em">
        <IconButton>
          <AddIcon />
          <Typography variant="h5" fontWeight="regular">
            ADD IDEA
          </Typography>
        </IconButton>
      </Box>
    </>
  );
};

export default Topic;
