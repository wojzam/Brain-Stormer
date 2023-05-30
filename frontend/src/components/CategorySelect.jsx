import React, { useState, useEffect } from "react";
import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";

const CategorySelect = ({ selectedCategory, setSelectedCategory, sx = {} }) => {
  const [categories, setCategories] = useState([]);
  const [error, setError] = useState(false);

  useEffect(() => {
    fetch("/api/public/category")
      .then((response) => response.json())
      .then((data) => {
        setCategories(data);
      });
  }, []);

  const handleInputChange = (e, value) => {
    setSelectedCategory(value);
    setError(!categories.includes(value));
  };

  return (
    <Autocomplete
      disablePortal
      id="category"
      value={selectedCategory}
      options={categories}
      onChange={handleInputChange}
      clearIcon={false}
      renderInput={(params) => (
        <TextField {...params} error={error} label="Category" />
      )}
      sx={sx}
    />
  );
};

export default CategorySelect;
