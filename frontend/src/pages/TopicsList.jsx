import { useEffect, useState } from "react";
import { Typography } from "@mui/material";
import Topic from "../components/Topic";
import TopicsListControls from "../components/TopicsListControls";

const TopicsList = ({ isUserTopics }) => {
  const [topics, setTopics] = useState();
  const [sortValue, setSortValue] = useState("");
  const [filterValue, setFilterValue] = useState("");

  const handleSortChange = (event) => {
    setSortValue(event.target.value);
  };

  const handleFilterChange = (event) => {
    setFilterValue(event.target.value);
  };

  useEffect(() => {
    fetch(isUserTopics ? "/api/topic" : "/api/public/topic")
      .then((response) => response.json())
      .then((data) => setTopics(data));
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
      {topics &&
        topics.map((topic) => (
          <Topic
            key={topic.id}
            id={topic.id}
            title={topic.title}
            description={topic.description}
          />
        ))}
      <Topic title="Dummy Topic" description="description" />
    </>
  );
};

export default TopicsList;
