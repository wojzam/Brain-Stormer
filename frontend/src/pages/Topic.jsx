import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Box, IconButton, Typography } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import BackButton from "../components/BackButton";
import Idea from "../components/Idea";

const Topic = () => {
  const { id } = useParams();
  const [topicData, setTopicData] = useState({
    title: "",
    description: "",
    ideas: [],
  });

  useEffect(() => {
    fetch(`/api/public/topic/${id}`)
      .then((response) => response.json())
      .then((data) => setTopicData(data));
  }, []);

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
        <Box width="100%">
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
              {topicData.title}
            </Typography>
            <Typography variant="h5" fontWeight="regular" noWrap>
              Collaborators: 4
            </Typography>
          </Box>
          <Typography variant="h5" fontWeight="regular">
            {topicData.description}
          </Typography>
        </Box>
      </Box>
      {topicData.ideas.map((idea) => (
        <Idea key={idea.id} title={idea.title} description={idea.description} />
      ))}
      <Idea title="Dummy Idea" description="description" />
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
