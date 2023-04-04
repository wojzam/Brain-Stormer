import { Link, Box, Typography } from "@mui/material";

export default function Topic({ id, title, description, category }) {
  return (
    <Link href={`/topic/${id}`} color="inherit" underline="none" width="100%">
      <Box
        sx={{
          my: 2,
          px: 5,
          py: 2,
          border: "solid",
          borderColor: "transparent",
          borderRadius: 8,
          background: "#ededed",
          boxShadow: "0px 3px 6px rgba(0, 0, 0, 0.16)",
          "&:hover": {
            background: "#dedede",
            borderColor: "#d1d1d1",
          },
        }}
      >
        <Box display="flex" flexDirection="row" justifyContent="space-between">
          <Typography component="h2" variant="h4" fontWeight="medium">
            {title}
          </Typography>
          <Typography variant="h6" fontWeight="light">
            {category}
          </Typography>
        </Box>
        <Typography variant="h6">{description}</Typography>
      </Box>
    </Link>
  );
}
