import { useState } from "react";
import Button from "@mui/material/Button";
import Link from "@mui/material/Link";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import TextField from "@mui/material/TextField";
import ValidatedTextField from "../components/ValidatedTextField";

export default function SignUp() {
  const [errorMessage, setErrorMessage] = useState("");
  const [passwordErrorMessage, setPasswordErrorMessage] = useState("");

  const handlePasswordsChange = (e) => {
    const password = e.target.form.password.value;
    const passwordRepeated = e.target.form.passwordRepeated.value;
    setPasswordErrorMessage(
      password !== passwordRepeated ? "Passwords do not match" : ""
    );
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const password = data.get("password");
    const passwordRepeated = data.get("passwordRepeated");

    if (passwordRepeated.trim() === "") {
      setErrorMessage("Repeated password is required");
      return;
    }

    if (password !== passwordRepeated) {
      setPasswordErrorMessage("Passwords do not match");
      return;
    }

    fetch("/api/auth/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: data.get("username"),
        email: data.get("email"),
        password: password,
      }),
    })
      .then((response) => {
        if (!response.ok) {
          return response.text().then((text) => {
            throw new Error(Object.values(JSON.parse(text)).join(", "));
          });
        }
        return response.json();
      })
      .then((data) => {
        localStorage.setItem("token", data.token);
        window.location.href = `/userTopics`;
      })
      .catch((error) => {
        setErrorMessage(error.message);
      });
  };

  return (
    <Container component="main" maxWidth="xs">
      <Box
        sx={{
          marginTop: 8,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <Typography component="h1" variant="h4" gutterBottom>
          Sign up
        </Typography>
        {errorMessage && (
          <Typography component="h5" variant="h5" color="error" gutterBottom>
            {errorMessage}
          </Typography>
        )}
        <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <ValidatedTextField
                id="username"
                label="Username"
                name="username"
                maxLength={64}
              />
            </Grid>
            <Grid item xs={12}>
              <ValidatedTextField
                id="email"
                label="Email"
                name="email"
                autoComplete="email"
                validateEmail={true}
                maxLength={64}
              />
            </Grid>
            <Grid item xs={12}>
              <ValidatedTextField
                name="password"
                label="Password"
                type="password"
                id="password"
                autoComplete="new-password"
                onChange={handlePasswordsChange}
                minLength={8}
                maxLength={64}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                name="passwordRepeated"
                label="Repeat password"
                type="password"
                id="passwordRepeated"
                error={passwordErrorMessage !== ""}
                helperText={passwordErrorMessage}
                onChange={handlePasswordsChange}
              />
            </Grid>
          </Grid>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            Sign Up
          </Button>
          <Grid container justifyContent="flex-end">
            <Grid item>
              <Link href="/login" variant="body2">
                Already have an account? Log in
              </Link>
            </Grid>
          </Grid>
        </Box>
      </Box>
    </Container>
  );
}
