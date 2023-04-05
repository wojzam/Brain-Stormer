import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Box, Typography, Skeleton } from "@mui/material";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import BackButton from "../components/BackButton";
import Idea from "../components/Idea";
import AddIdeaButton from "../components/AddIdeaButton";

const Topic = () => {
  const { id } = useParams();
  const [topicData, setTopicData] = useState();
  const [isPending, setIsPending] = useState(true);
  const [ideas, setIdeas] = useState([]);

  useEffect(() => {
    const token = localStorage.getItem("token");
    const endpoint = token ? `/api/topic/${id}` : `/api/public/topic/${id}`;

    fetch(endpoint, {
      headers: {
        Authorization: token && `Bearer ${token}`,
      },
    })
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
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
          {topicData && topicData.readOnly && (
            <Box display="flex" flexDirection="row" alignItems="center" gap={1}>
              <LockOutlinedIcon color="disabled" />
              <Typography variant="h6" fontWeight="light">
                read-only
              </Typography>
            </Box>
          )}
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
              {isPending ? (
                <Skeleton width={200} />
              ) : (
                "Collaboratos: " + topicData.collaborators.length
              )}
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
        !topicData.readOnly && (
          <AddIdeaButton topicId={topicData.id} setIdeas={setIdeas} />
        )
      )}
      {ideas &&
        ideas.map((idea) => (
          <Idea
            key={idea.id}
            id={idea.id}
            title={idea.title}
            description={idea.description}
            votes={idea.votes}
            userVote={idea.userVote}
            readOnly={topicData.readOnly}
          />
        ))}
    </>
  );
};

export default Topic;
