import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Box, Typography, Skeleton } from "@mui/material";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import BackButton from "../components/BackButton";
import Idea from "../components/Idea";
import AddIdeaButton from "../components/AddIdeaButton";
import useIdeaUpdates from "../hooks/useIdeaUpdates";
import TopicControlPanel from "../components/TopicControlPanel";

const Topic = () => {
  const { id } = useParams();
  const [topicData, setTopicData] = useState();
  const [isPending, setIsPending] = useState(true);
  const [ideas, setIdeas] = useState([]);
  const [collaborators, setCollaborators] = useState([]);

  useIdeaUpdates({ topicId: id, setIdeas });

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
        setTopicData(data);
        setIdeas(data.ideas);
        setCollaborators(data.collaborators);
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
            <TopicControlPanel
              {...{ topicData, setTopicData, collaborators, setCollaborators }}
            />
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
            {...idea}
            readOnly={topicData.readOnly}
            setIdeas={setIdeas}
          />
        ))}
    </>
  );
};

export default Topic;
