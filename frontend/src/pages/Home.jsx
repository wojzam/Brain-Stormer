import { Button, Container, Grid, Typography } from "@mui/material";
import lightBulb from "./../assets/light-bulb.svg";

const Home = () => {
  return (
    <Container>
      <Grid container spacing={10}>
        <Grid item xs={12}>
          <Typography
            component="h1"
            variant="h1"
            align="left"
            fontSize="5.6em"
            gutterBottom
          >
            Welcome to <span style={{ fontWeight: "bold" }}>Brain Stormer</span>
          </Typography>
          <Typography component="h2" variant="h4" align="left" gutterBottom>
            unleash your creativity and collaborate to bring ideas to life
          </Typography>
        </Grid>
        <Grid item xs={8}>
          <Button
            variant="contained"
            href="/signup"
            color="secondary"
            sx={{
              fontSize: 26,
              fontWeight: "bold",
              borderRadius: 5,
              paddingX: 4,
            }}
          >
            Start Here
          </Button>
        </Grid>
        <Grid item xs={4}>
          <img
            src={lightBulb}
            alt="light bulb"
            style={{ height: "auto", maxWidth: "100%" }}
          />
        </Grid>
      </Grid>
    </Container>
  );
};

export default Home;
