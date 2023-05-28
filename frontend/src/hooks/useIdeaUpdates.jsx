import { useEffect } from "react";

const useIdeaUpdates = ({ topicId, setIdeas }) => {
  useEffect(() => {
    const token = localStorage.getItem("token");
    const socket = new WebSocket("ws://localhost:8080/ws/idea-updates");

    socket.addEventListener("open", () => {
      socket.send(JSON.stringify({ token, topicId }));
    });

    socket.addEventListener("message", handleMessage);

    socket.addEventListener("close", () => {
      // TODO Attempt to reconnect
    });
  }, []);

  const handleMessage = (event) => {
    const data = JSON.parse(event.data);
    const receivedIdea = data.idea;
    const action = data.action;

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
  };
};

export default useIdeaUpdates;
