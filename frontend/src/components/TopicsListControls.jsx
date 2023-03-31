import { Box, FormControl, InputLabel, MenuItem, Select } from "@mui/material";
import PropTypes from "prop-types";
import SearchBar from "./SearchBar";

function TopicsListControls(props) {
  const { sortValue, filterValue, handleSortChange, handleFilterChange } =
    props;

  return (
    <Box
      display="flex"
      flex-direction="row"
      justifyContent="space-between"
      alignItems="end"
      width="100%"
      mb="2em"
    >
      <SearchBar />
      <Box>
        <FormControl sx={{ m: 1, minWidth: 120 }}>
          <InputLabel id="sort-label">Sort by</InputLabel>
          <Select
            labelId="sort-label"
            id="sort-select"
            value={sortValue}
            label="Sort by"
            onChange={handleSortChange}
          >
            <MenuItem value="date">Date</MenuItem>
            <MenuItem value="name">Title</MenuItem>
          </Select>
        </FormControl>
        <FormControl sx={{ m: 1, minWidth: 120 }}>
          <InputLabel id="filter-label">Filter by</InputLabel>
          <Select
            labelId="filter-label"
            id="filter-select"
            value={filterValue}
            label="Filter by"
            onChange={handleFilterChange}
          >
            <MenuItem value="category">Category</MenuItem>
          </Select>
        </FormControl>
      </Box>
    </Box>
  );
}

TopicsListControls.propTypes = {
  sortValue: PropTypes.string.isRequired,
  filterValue: PropTypes.string.isRequired,
  handleSortChange: PropTypes.func.isRequired,
  handleFilterChange: PropTypes.func.isRequired,
};

export default TopicsListControls;
