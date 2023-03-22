import React from "react";
import IconButton from "@mui/material/IconButton";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";

export default function BackButton({ onClick }) {
  return (
    <IconButton onClick={onClick}>
      <ArrowBackIcon />
      Back
    </IconButton>
  );
}
