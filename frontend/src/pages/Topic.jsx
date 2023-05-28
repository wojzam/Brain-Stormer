import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Box, Typography, Skeleton } from "@mui/material";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import BackButton from "../components/BackButton";
import Idea from "../components/Idea";
import AddIdeaButton from "../components/AddIdeaButton";
import CollaboratorsDialog from "../components/CollaboratorsDialog";

const Topic = () => {
  const { id } = useParams();
  const [topicData, setTopicData] = useState();
  const [isPending, setIsPending] = useState(true);
  const [ideas, setIdeas] = useState([]);
  const [collaborators, setCollaborators] = useState([]);

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

    if (token) {
      const socket = new WebSocket("ws://localhost:8080/ws/idea-updates");

      socket.addEventListener("open", () => {
        socket.send(JSON.stringify({ token: token, topicId: id }));
      });

      socket.addEventListener("message", (event) => {
        const receivedIdea = JSON.parse(event.data).idea;
        const action = JSON.parse(event.data).action;
        switch (action) {
          case "CREATE":
            setIdeas((prevIdeas) => [receivedIdea, ...prevIdeas]);
            break;
          case "UPDATE":
            setIdeas((prevIdeas) =>
              prevIdeas.map((prevIdea) =>
                prevIdea.id === receivedIdea.id
                  ? {
                      ...prevIdea,
                      title: receivedIdea.title,
                      description: receivedIdea.description,
                    }
                  : prevIdea
              )
            );
            break;
          case "VOTE":
            setIdeas((prevIdeas) =>
              prevIdeas.map((prevIdea) =>
                prevIdea.id === receivedIdea.id
                  ? {
                      ...prevIdea,
                      votes: prevIdea.votes + receivedIdea.votes,
                    }
                  : prevIdea
              )
            );
            break;
          case "DELETE":
            setIdeas((prevIdeas) =>
              prevIdeas.filter((idea) => idea.id !== receivedIdea.id)
            );
            break;
          default:
            break;
        }
      });

      socket.addEventListener("close", () => {
        // TODO Attempt to reconnect
      });
    }
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
              {topicData && !topicData.readOnly && (
                <CollaboratorsDialog
                  updateMode={true}
                  topicId={topicData.id}
                  {...{ collaborators, setCollaborators }}
                />
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
            {...idea}
            readOnly={topicData.readOnly}
            setIdeas={setIdeas}
          />
        ))}
    </>
  );
};

export default Topic;
