import { AppBar, Button, Box } from "@mui/material";

const homeButtonStyle = {
  textTransform: "none",
  fontSize: 20,
  color: "white",
};

const buttonStyle = {
  fontSize: 16,
  color: "white",
};

const loginButtonStyle = {
  width: 100,
  borderRadius: 3,
  fontSize: 16,
};

const signupButtonStyle = {
  width: 100,
  borderRadius: 3,
  fontSize: 16,
  color: "black",
  background: "white",
};

export default function HeaderBar() {
  return (
    <AppBar position="fixed" sx={{ background: "#FF7020" }}>
      <Box
        display="flex"
        alignItems="center"
        justifyContent="space-between"
        sx={{ p: 1 }}
      >
        <Button href="/" sx={homeButtonStyle}>
          Brain Stormer
        </Button>
        <Box display="flex" sx={{ gap: 2 }}>
          <Button href="explore" sx={buttonStyle}>
            Explore
          </Button>
          <Button href="userTopics" sx={buttonStyle}>
            My topics
          </Button>
          <Button href="create" sx={buttonStyle}>
            Create
          </Button>
        </Box>
        <Box display="flex" sx={{ gap: 2 }}>
          <Button
            variant="outlined"
            color="inherit"
            href="login"
            sx={loginButtonStyle}
          >
            Login
          </Button>
          <Button
            variant="contained"
            color="secondary"
            href="signup"
            sx={signupButtonStyle}
          >
            Sign up
          </Button>
        </Box>
      </Box>
    </AppBar>
  );
}
