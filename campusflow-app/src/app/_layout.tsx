import { Stack } from "expo-router";
// Fixed imports: step up 1 level (../) to reach src/
import { ThemeProvider } from "../theme/ThemeContext";
import { AuthProvider } from "../store/AuthContext";

export default function RootLayout() {
    return (
        <AuthProvider>
            <ThemeProvider>
                <Stack screenOptions={{ headerShown: false }}>
                    <Stack.Screen name="index" options={{ animation: "fade" }} />
                    <Stack.Screen name="login" options={{ animation: "fade" }} />
                    <Stack.Screen name="role-select" options={{ animation: "slide_from_right" }} />

                    {/* Module Dashboards */}
                    <Stack.Screen name="admin/dashboard" />
                    <Stack.Screen name="accounts/dashboard" />
                    <Stack.Screen name="hod/dashboard" />
                    <Stack.Screen name="purchase/dashboard" />
                    <Stack.Screen name="stores/dashboard" />
                </Stack>
            </ThemeProvider>
        </AuthProvider>
    );
}