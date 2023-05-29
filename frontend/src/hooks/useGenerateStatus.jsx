import { useEffect } from "react";

const useIdeaUpdates = ({ topicId, setReady }) => {
  useEffect(() => {
    const token = localStorage.getItem("token");
    const socket = new WebSocket("ws://localhost:8080/ws/generate-status");

    socket.addEventListener("open", () => {
      socket.send(JSON.stringify({ token, topicId }));
    });

    socket.addEventListener("message", handleMessage);

    socket.addEventListener("close", () => {
      // TODO Attempt to reconnect
    });
  }, []);

  const handleMessage = (event) => {
    const status = JSON.parse(event.data).status;
    setReady(status === "READY");
  };
};

export default useIdeaUpdates;
