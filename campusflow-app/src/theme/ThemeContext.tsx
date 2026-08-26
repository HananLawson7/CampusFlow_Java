import React, { createContext, useContext, useState } from "react";
import { useColorScheme } from "react-native";

const lightColors = {
    bg: "#F4F3EF", surface: "#FFFFFF", surface2: "#F7F6F2", border: "#E4E2D9",
    text: "#1C1B18", text2: "#6B6A63", accent: "#3C3489", accentBg: "#EEEDFE",
};
const darkColors = {
    bg: "#141310", surface: "#1E1D19", surface2: "#26251F", border: "#38362E",
    text: "#EDEBE3", text2: "#B4B2A9", accent: "#AFA9EC", accentBg: "#3C3489",
};

const ThemeContext = createContext<any>(null);

export function ThemeProvider({ children }: any) {
    const system = useColorScheme();
    const [mode, setMode] = useState(system || "light");
    const colors = mode === "dark" ? darkColors : lightColors;
    const toggle = () => setMode(mode === "dark" ? "light" : "dark");
    return (
        <ThemeContext.Provider value={{ colors, mode, toggle }}>
            {children}
        </ThemeContext.Provider>
    );
}

export const useTheme = () => useContext(ThemeContext);