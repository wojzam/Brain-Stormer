import { Box, IconButton, Typography } from "@mui/material";
import DeleteOutlinedIcon from "@mui/icons-material/DeleteOutlined";
import CollaboratorsDialog from "../components/CollaboratorsDialog";
import TopicEditDialog from "../components/TopicEditDialog";

export default function TopicControlPanel({
  topicData,
  setTopicData,
  collaborators,
  setCollaborators,
}) {
  const handleDeleteClick = () => {
    const token = localStorage.getItem("token");
    fetch(`/api/topic/${topicData.id}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    }).then(() => {
      window.location.href = `/userTopics`;
    });
  };

  return (
    <Box>
      <Typography variant="h5" fontWeight="regular" noWrap>
        {topicData && (
          <CollaboratorsDialog
            updateMode={true}
            topicId={topicData.id}
            disabled={!topicData.canEdit}
            {...{ collaborators, setCollaborators }}
          />
        )}
      </Typography>
      {topicData && topicData.canEdit && (
        <Box display="flex" justifyContent="end" my={1}>
          <TopicEditDialog
            {...{
              id: topicData.id,
              title: topicData.title,
              description: topicData.description,
              category: topicData.category,
              publicVisibility: topicData.publicVisibility,
              setTopicData,
            }}
          />
          <IconButton onClick={handleDeleteClick}>
            <DeleteOutlinedIcon />
          </IconButton>
        </Box>
      )}
    </Box>
  );
}
