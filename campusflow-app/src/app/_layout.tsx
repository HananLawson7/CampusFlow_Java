import { Stack } from "expo-router";
import { ThemeProvider } from "../../theme/ThemeContext";
import { AuthProvider } from "../../store/AuthContext";

export default function RootLayout() {
    return (
        <ThemeProvider>
            <AuthProvider>
                <Stack screenOptions={{ headerShown: false }} />
            </AuthProvider>
        </ThemeProvider>
    );
}