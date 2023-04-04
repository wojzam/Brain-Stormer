import { useEffect, useState } from "react";
import { Typography, CircularProgress } from "@mui/material";
import Topic from "../components/Topic";
import TopicsListControls from "../components/TopicsListControls";

const TopicsList = ({ isUserTopics }) => {
  const [topics, setTopics] = useState();
  const [isPending, setIsPending] = useState(true);
  const [sortValue, setSortValue] = useState("");
  const [filterValue, setFilterValue] = useState("");

  const handleSortChange = (event) => {
    setSortValue(event.target.value);
  };

  const handleFilterChange = (event) => {
    setFilterValue(event.target.value);
  };

  useEffect(() => {
    const endpoint = isUserTopics ? "/api/topic" : "/api/public/topic";
    const token = localStorage.getItem("token");

    fetch(endpoint, {
      headers: {
        Authorization: isUserTopics && `Bearer ${token}`,
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setTopics(data);
        setIsPending(false);
      });
  }, []);

  return (
    <>
      <Typography component="h1" variant="h2" fontWeight="medium" gutterBottom>
        {isUserTopics ? "My topics" : "Explore"}
      </Typography>
      <TopicsListControls
        sortValue={sortValue}
        filterValue={filterValue}
        handleSortChange={handleSortChange}
        handleFilterChange={handleFilterChange}
      />
      {isPending && <CircularProgress />}
      {topics &&
        topics.map((topic) => (
          <Topic
            key={topic.id}
            id={topic.id}
            title={topic.title}
            description={topic.description}
            category={topic.category}
          />
        ))}
    </>
  );
};

export default TopicsList;
