import { useState } from "react";
import { Box, IconButton, Typography } from "@mui/material";
import ThumbUpOutlinedIcon from "@mui/icons-material/ThumbUpOutlined";
import ThumbDownOutlinedIcon from "@mui/icons-material/ThumbDownOutlined";
import EditIcon from "@mui/icons-material/Edit";

export default function Idea({ id, title, description, votes }) {
  const [likeClicked, setLikeClicked] = useState(false);
  const [dislikeClicked, setDislikeClicked] = useState(false);
  const [localVotes, setLocalVotes] = useState(0);

  const handleVote = (voteValue) => {
    const token = localStorage.getItem("token");
    fetch(`/api/idea/${id}/vote?value=${voteValue}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    }).then(() => setLocalVotes(voteValue));
  };

  const deleteVote = () => {
    const token = localStorage.getItem("token");
    fetch(`/api/idea/${id}/vote`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    }).then(() => setLocalVotes(0));
  };

  const handleLikeClick = () => {
    if (likeClicked) {
      setLikeClicked(false);
      deleteVote();
    } else {
      setLikeClicked(true);
      setDislikeClicked(false);
      handleVote(1);
    }
  };

  const handleDislikeClick = () => {
    if (dislikeClicked) {
      setDislikeClicked(false);
      deleteVote();
    } else {
      setDislikeClicked(true);
      setLikeClicked(false);
      handleVote(-1);
    }
  };

  const getVotesColor = () => {
    const sum = votes + localVotes;
    if (sum > 0) {
      return "green";
    } else if (sum < 0) {
      return "red";
    } else {
      return "gray";
    }
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
        <Box display="flex" flexDirection="row" mt="2em">
          <IconButton
            color={dislikeClicked ? "secondary" : "default"}
            onClick={handleDislikeClick}
          >
            <ThumbDownOutlinedIcon />
          </IconButton>
          <Typography variant="h6" color={getVotesColor}>
            {votes + localVotes}
          </Typography>
          <IconButton
            color={likeClicked ? "secondary" : "default"}
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
