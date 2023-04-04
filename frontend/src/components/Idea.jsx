import { useState } from "react";
import { Box, IconButton, Typography } from "@mui/material";
import ThumbUpOutlinedIcon from "@mui/icons-material/ThumbUpOutlined";
import ThumbDownOutlinedIcon from "@mui/icons-material/ThumbDownOutlined";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";
import DeleteOutlinedIcon from "@mui/icons-material/DeleteOutlined";

export default function Idea({
  id,
  title,
  description,
  votes,
  userVote,
  readOnly,
}) {
  const [localVotes, setLocalVotes] = useState(userVote);

  const totalVotes = () => {
    return votes - userVote + localVotes;
  };

  const isLiked = () => {
    return localVotes === 1;
  };

  const isDisliked = () => {
    return localVotes === -1;
  };

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
    if (isLiked()) {
      deleteVote();
    } else {
      handleVote(1);
    }
  };

  const handleDislikeClick = () => {
    if (isDisliked()) {
      deleteVote();
    } else {
      handleVote(-1);
    }
  };

  const getVotesColor = () => {
    if (totalVotes() > 0) {
      return "green";
    } else if (totalVotes() < 0) {
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
        {!readOnly && (
          <Box
            display="flex"
            flexDirection="row"
            justifyContent="space-between"
            width={100}
            mt="2em"
          >
            <IconButton
              color={isDisliked() ? "secondary" : "default"}
              onClick={handleDislikeClick}
            >
              <ThumbDownOutlinedIcon />
            </IconButton>
            <Typography variant="h6" color={getVotesColor}>
              {totalVotes()}
            </Typography>
            <IconButton
              color={isLiked() ? "secondary" : "default"}
              onClick={handleLikeClick}
            >
              <ThumbUpOutlinedIcon />
            </IconButton>
          </Box>
        )}
      </Box>
      {!readOnly && (
        <Box>
          <IconButton>
            <EditOutlinedIcon />
          </IconButton>
          <IconButton>
            <DeleteOutlinedIcon />
          </IconButton>
        </Box>
      )}
    </Box>
  );
}
