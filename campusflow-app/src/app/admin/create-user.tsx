import { useState } from "react";
import { View, Text, TextInput, TouchableOpacity, Alert, ScrollView } from "react-native";
import { useTheme } from "../../theme/ThemeContext";
import { api } from "../../api/client"; // Fixed import path

const ROLES = ["ADMIN", "HOD", "FACULTY", "STUDENT", "STORES", "PURCHASE", "ACCOUNTS", "BOARD_MEMBERS"];

export default function CreateUser() {
    const { colors } = useTheme();
    const [role, setRole] = useState("STUDENT");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [department, setDepartment] = useState("");
    const [year, setYear] = useState("");

    const submit = async () => {
        if (!username.trim() || !password.trim()) {
            Alert.alert("Validation Error", "Username and password are required.");
            return;
        }

        try {
            await api.createUser({
                username: username.trim(),
                password: password.trim(),
                department: department.trim(),
                role,
                yearOfStudy: role === "STUDENT" && year ? parseInt(year, 10) : null,
            });

            Alert.alert("Success", `${role} account created`);

            // Clear inputs on success
            setUsername("");
            setPassword("");
            setDepartment("");
            setYear("");
        } catch (e: any) {
            Alert.alert("Error", e.message || "Failed to create user");
        }
    };

    return (
        <ScrollView style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>Create user</Text>

            <Text style={{ color: colors.text2, marginBottom: 8 }}>Role</Text>
            <View style={{ flexDirection: "row", flexWrap: "wrap", gap: 8, marginBottom: 16 }}>
                {ROLES.map((r) => (
                    <TouchableOpacity
                        key={r}
                        onPress={() => setRole(r)}
                        style={{
                            paddingVertical: 6,
                            paddingHorizontal: 12,
                            borderRadius: 8,
                            backgroundColor: role === r ? colors.accentBg : colors.surface,
                            borderWidth: 0.5,
                            borderColor: colors.border,
                        }}
                    >
                        <Text style={{ color: role === r ? colors.accent : colors.text2, fontSize: 12 }}>{r}</Text>
                    </TouchableOpacity>
                ))}
            </View>

            <TextInput
                placeholder="Username"
                value={username}
                onChangeText={setUsername}
                autoCapitalize="none"
                style={{ height: 44, borderWidth: 0.5, borderColor: colors.border, borderRadius: 10, paddingHorizontal: 14, marginBottom: 12, color: colors.text }}
                placeholderTextColor={colors.text2}
            />

            <TextInput
                placeholder="Password"
                value={password}
                onChangeText={setPassword}
                secureTextEntry
                autoCapitalize="none"
                style={{ height: 44, borderWidth: 0.5, borderColor: colors.border, borderRadius: 10, paddingHorizontal: 14, marginBottom: 12, color: colors.text }}
                placeholderTextColor={colors.text2}
            />

            <TextInput
                placeholder="Department"
                value={department}
                onChangeText={setDepartment}
                style={{ height: 44, borderWidth: 0.5, borderColor: colors.border, borderRadius: 10, paddingHorizontal: 14, marginBottom: 12, color: colors.text }}
                placeholderTextColor={colors.text2}
            />

            {role === "STUDENT" && (
                <TextInput
                    placeholder="Year of study (1-4)"
                    value={year}
                    onChangeText={setYear}
                    keyboardType="numeric"
                    style={{ height: 44, borderWidth: 0.5, borderColor: colors.border, borderRadius: 10, paddingHorizontal: 14, marginBottom: 12, color: colors.text }}
                    placeholderTextColor={colors.text2}
                />
            )}

            <TouchableOpacity
                onPress={submit}
                style={{ height: 46, backgroundColor: colors.accent, borderRadius: 10, justifyContent: "center", alignItems: "center", marginTop: 8 }}
            >
                <Text style={{ color: "#fff", fontWeight: "500" }}>Create user</Text>
            </TouchableOpacity>
        </ScrollView>
    );
}