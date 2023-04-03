import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Box, Typography, Skeleton } from "@mui/material";

import BackButton from "../components/BackButton";
import Idea from "../components/Idea";
import AddIdeaButton from "../components/AddIdeaButton";

const Topic = () => {
  const { id } = useParams();
  const [topicData, setTopicData] = useState();
  const [isPending, setIsPending] = useState(true);
  const [ideas, setIdeas] = useState([]);

  useEffect(() => {
    fetch(`/api/public/topic/${id}`)
      .then((response) => response.json())
      .then((data) => {
        setTopicData(data);
        setIdeas(data.ideas);
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
      {isPending ? (
        <Skeleton height={200} width="100%" />
      ) : (
        <AddIdeaButton topicId={topicData.id} setIdeas={setIdeas} />
      )}
      {ideas &&
        ideas.map((idea) => (
          <Idea
            key={idea.id}
            id={idea.id}
            title={idea.title}
            description={idea.description}
            votes={idea.votes}
          />
        ))}
    </>
  );
};

export default Topic;
