import { useState } from "react";
import { Box, IconButton, Typography } from "@mui/material";
import ThumbUpOutlinedIcon from "@mui/icons-material/ThumbUpOutlined";
import ThumbDownOutlinedIcon from "@mui/icons-material/ThumbDownOutlined";
import EditIcon from "@mui/icons-material/Edit";

export default function Idea({ title, description }) {
  const [likeClicked, setLikeClicked] = useState(false);
  const [dislikeClicked, setDislikeClicked] = useState(false);

  const handleLikeClick = () => {
    setLikeClicked(true);
    setDislikeClicked(false);
  };

  const handleDislikeClick = () => {
    setDislikeClicked(true);
    setLikeClicked(false);
  };

  return (
    <Box
      display="flex"
      justifyContent="space-between"
      sx={{
        width: "100%",
        my: 2,
        px: 5,
        py: 2,
        border: "solid",
        borderColor: "#292929",
        borderRadius: 8,
        background: "#f5f5f5",
      }}
    >
      <Box>
        <Typography component="h2" variant="h4" fontWeight="medium">
          {title}
        </Typography>
        <Typography variant="h6">{description}</Typography>
        <Box>
          <IconButton
            color={dislikeClicked ? "primary" : "default"}
            onClick={handleDislikeClick}
          >
            <ThumbDownOutlinedIcon />
          </IconButton>
          <IconButton
            color={likeClicked ? "primary" : "default"}
            onClick={handleLikeClick}
          >
            <ThumbUpOutlinedIcon />
          </IconButton>
        </Box>
      </Box>
      <Box>
        <IconButton>
          <EditIcon />
        </IconButton>
      </Box>
    </Box>
  );
}
