import { useEffect, useState } from "react";
import { Typography } from "@mui/material";
import Topic from "../components/Topic";
import TopicsListControls from "../components/TopicsListControls";

const TopicsList = ({ isUserTopics }) => {
  const [sortValue, setSortValue] = useState("");
  const [filterValue, setFilterValue] = useState("");

  const handleSortChange = (event) => {
    setSortValue(event.target.value);
  };

  const handleFilterChange = (event) => {
    setFilterValue(event.target.value);
  };

  return (
    <>
      <Typography component="h1" variant="h1" fontWeight="medium" gutterBottom>
        {isUserTopics ? "My topics" : "Explore"}
      </Typography>
      <TopicsListControls
        sortValue={sortValue}
        filterValue={filterValue}
        handleSortChange={handleSortChange}
        handleFilterChange={handleFilterChange}
      />
      <Topic
        title="Topic 1"
        description="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi
            laoreet massa non lacinia bibendum. Aenean sollicitudin bibendum
            nisi. Phasellus sed sollicitudin ipsum. Vivamus id eros eu lacus
            bibendum pharetra. Phasellus a magna commodo ex sagittis dictum.
            Duis egestas elit in porttitor lobortis. Maecenas tortor tellus,
            lobortis non dolor nec, eleifend blandit neque. Aliquam ac
            ullamcorper lacus, sed faucibus erat."
      />
      <Topic title="Topic 2" description="description" />
    </>
  );
};

export default TopicsList;
