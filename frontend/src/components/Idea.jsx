import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";

export default function Idea({ text }) {
  return (
    <Box
      sx={{
        width: "100%",
        overflow: "hidden",
        "& > :not(style)": {
          m: 1,
          flexGrow: 1,
          height: 128,
        },
      }}
    >
      <Paper variant="outlined" elevation={3}>
        <h2>{text}</h2>
      </Paper>
    </Box>
  );
}
