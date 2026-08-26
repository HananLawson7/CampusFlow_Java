import { useState } from "react";
import { View, Text, TextInput, TouchableOpacity, Alert, ActivityIndicator } from "react-native";
import { useLocalSearchParams, router } from "expo-router";
import { useTheme } from "../theme/ThemeContext";
import { useAuth } from "../../store/AuthContext";
import { api } from "../../api/client";

export default function Login() {
    const searchParams = useLocalSearchParams();
    const roleParam = searchParams.role ? searchParams.role.toString() : "";

    const { colors } = useTheme();
    const { setUser } = useAuth();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);

    const handleLogin = async () => {
        const cleanUsername = username.trim();
        const cleanPassword = password.trim();

        if (!cleanUsername || !cleanPassword) {
            Alert.alert("Validation Error", "Please enter both username and password.");
            return;
        }

        setLoading(true);

        try {
            console.log(`[LOGIN ATTEMPT] User: ${cleanUsername}`);

            // 1. Authenticate with backend API
            const response = await api.login(cleanUsername, cleanPassword);
            console.log("[LOGIN RESPONSE]", response);

            // Extract user payload correctly
            const userData = response.user || response;

            // 2. Persist user session to AuthContext
            setUser(userData);

            // 3. Map backend role to existing route folders
            const userRole = (userData.role || roleParam || "").trim().toUpperCase();
            console.log(`[USER ROLE DETECTED]: "${userRole}"`);

            const roleRouteMap: Record<string, string> = {
                ADMIN: "/admin/dashboard",
                HOD: "/hod/dashboard",
                FACULTY: "/hod/dashboard",
                STUDENT: "/hod/dashboard",
                STORES: "/stores/dashboard",
                PURCHASE: "/purchase/dashboard",
                ACCOUNTS: "/accounts/dashboard",
                BOARD_MEMBERS: "/accounts/dashboard",
            };

            const targetRoute = roleRouteMap[userRole] || "/admin/dashboard";
            console.log(`[ROUTING] Navigating to: ${targetRoute}`);

            router.replace(targetRoute as any);

        } catch (e: any) {
            console.error("[LOGIN ERROR]", e);
            Alert.alert(
                "Login Failed",
                e.message || "Unable to reach server. Please check network connection and backend status."
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 80 }}>
            <Text style={{ fontSize: 22, fontWeight: "500", color: colors.text }}>Welcome back</Text>
            <Text style={{ color: colors.text2, marginBottom: 24 }}>
                {roleParam ? `Signing in as ${roleParam}` : "Sign in to CampusFlow"}
            </Text>

            <TextInput
                placeholder="Username"
                value={username}
                onChangeText={setUsername}
                autoCapitalize="none"
                autoCorrect={false}
                style={{
                    height: 44,
                    borderWidth: 0.5,
                    borderColor: colors.border,
                    borderRadius: 10,
                    paddingHorizontal: 14,
                    marginBottom: 12,
                    color: colors.text,
                    backgroundColor: colors.surface
                }}
                placeholderTextColor={colors.text2}
            />

            <TextInput
                placeholder="Password"
                value={password}
                onChangeText={setPassword}
                secureTextEntry
                autoCapitalize="none"
                style={{
                    height: 44,
                    borderWidth: 0.5,
                    borderColor: colors.border,
                    borderRadius: 10,
                    paddingHorizontal: 14,
                    marginBottom: 20,
                    color: colors.text,
                    backgroundColor: colors.surface
                }}
                placeholderTextColor={colors.text2}
            />

            <TouchableOpacity
                onPress={handleLogin}
                disabled={loading}
                style={{
                    height: 46,
                    backgroundColor: colors.accent,
                    borderRadius: 10,
                    justifyContent: "center",
                    alignItems: "center",
                    opacity: loading ? 0.7 : 1
                }}
            >
                {loading ? (
                    <ActivityIndicator color="#fff" />
                ) : (
                    <Text style={{ color: "#fff", fontWeight: "500" }}>Sign in</Text>
                )}
            </TouchableOpacity>
        </View>
    );
}