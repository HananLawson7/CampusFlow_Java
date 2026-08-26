import { useState } from "react";
import { View, Text, TextInput, TouchableOpacity, Alert } from "react-native";
import { useLocalSearchParams, router } from "expo-router";
import { useTheme } from "../../theme/ThemeContext";
import { useAuth } from "../../store/AuthContext";
import { api } from "../../api/client";

export default function Login() {
    const { role } = useLocalSearchParams();
    const { colors } = useTheme();
    const { setUser } = useAuth();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = async () => {
        try {
            const user = await api.login(username, password);
            setUser(user);
            router.replace(`/${role.toString().toLowerCase()}/dashboard`);
        } catch (e: any) {
            Alert.alert("Login failed", e.message);
        }
    };

    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 80 }}>
            <Text style={{ fontSize: 22, fontWeight: "500", color: colors.text }}>Welcome back</Text>
            <Text style={{ color: colors.text2, marginBottom: 24 }}>Signing in as {role}</Text>
            <TextInput placeholder="Username" value={username} onChangeText={setUsername}
                       style={{ height: 44, borderWidth: 0.5, borderColor: colors.border, borderRadius: 10, paddingHorizontal: 14, marginBottom: 12, color: colors.text, backgroundColor: colors.surface }}
                       placeholderTextColor={colors.text2} />
            <TextInput placeholder="Password" value={password} onChangeText={setPassword} secureTextEntry
                       style={{ height: 44, borderWidth: 0.5, borderColor: colors.border, borderRadius: 10, paddingHorizontal: 14, marginBottom: 20, color: colors.text, backgroundColor: colors.surface }}
                       placeholderTextColor={colors.text2} />
            <TouchableOpacity onPress={handleLogin} style={{ height: 46, backgroundColor: colors.accent, borderRadius: 10, justifyContent: "center", alignItems: "center" }}>
                <Text style={{ color: "#fff", fontWeight: "500" }}>Sign in</Text>
            </TouchableOpacity>
        </View>
    );
}