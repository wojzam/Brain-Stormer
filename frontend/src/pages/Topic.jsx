import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Box, IconButton, Typography, Skeleton } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import BackButton from "../components/BackButton";
import Idea from "../components/Idea";

const Topic = () => {
  const { id } = useParams();
  const [topicData, setTopicData] = useState();
  const [isPending, setIsPending] = useState(true);

  useEffect(() => {
    fetch(`/api/public/topic/${id}`)
      .then((response) => response.json())
      .then((data) => {
        setTopicData(data);
        setIsPending(false);
      });
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
              {isPending ? <Skeleton width={300} /> : topicData.title}
            </Typography>
            <Typography variant="h5" fontWeight="regular" noWrap>
              {isPending ? <Skeleton width={200} /> : "Collaboratos: 4"}
            </Typography>
          </Box>
          <Typography variant="h5" fontWeight="regular">
            {isPending ? <Skeleton width={600} /> : topicData.description}
          </Typography>
        </Box>
      </Box>
      {isPending && <Skeleton height={200} width="100%" />}
      {topicData &&
        topicData.ideas.map((idea) => (
          <Idea
            key={idea.id}
            id={idea.id}
            title={idea.title}
            description={idea.description}
            votes={idea.votes}
          />
        ))}
      <Idea title="Dummy Idea" description="description" votes={0} />
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
