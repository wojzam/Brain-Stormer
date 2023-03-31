import { createContext, useState, useMemo } from "react";
import { createTheme } from "@mui/material/styles";
import { teal } from "@mui/material/colors";

export const themeSettings = (mode) => {
  return {
    palette: {
      mode: mode,
      ...(mode === "light"
        ? {
            // light mode
            primary: {
              main: teal[500],
            },
            secondary: {
              main: "#ff7020",
            },
            background: {
              default: "#fcfcfc",
            },
          }
        : {
            // dark mode
            primary: {
              main: "#ff7020",
            },
            secondary: {
              main: teal[500],
            },
            background: {
              default: "#004940",
            },
          }),
    },
    typography: {
      fontFamily: ["Quicksand", "sans-serif"].join(","),
    },
  };
};

export const ColorModeContext = createContext({
  toggleColorMode: () => {},
});

export const useMode = () => {
  const [mode, setMode] = useState("light");

  const colorMode = useMemo(
    () => ({
      toggleColorMode: () =>
        setMode((prev) => (prev === "dark" ? "light" : "dark")),
    }),
    []
  );

  const theme = useMemo(() => createTheme(themeSettings(mode)), [mode]);
  return [theme, colorMode];
};
