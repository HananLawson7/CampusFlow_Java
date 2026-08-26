import { useEffect, useState } from "react";
import { View, Text, FlatList, TouchableOpacity, Alert, Modal, TextInput } from "react-native";
import { useTheme } from "../../theme/ThemeContext";
import { api } from "../../api/client"; // Fixed import path

export default function AllUsers() {
    const { colors } = useTheme();
    const [users, setUsers] = useState<any[]>([]);

    // Modal state for resetting password
    const [selectedUser, setSelectedUser] = useState<any>(null);
    const [newPassword, setNewPassword] = useState("");

    const loadUsers = () => {
        api.getAllUsers().then(setUsers).catch((e) => Alert.alert("Error", e.message));
    };

    useEffect(() => { loadUsers(); }, []);

    const handleDeactivate = (userId: number, username: string) => {
        Alert.alert("Deactivate User", `Deactivate ${username}?`, [
            { text: "Cancel", style: "cancel" },
            {
                text: "Deactivate",
                style: "destructive",
                onPress: async () => {
                    try {
                        await api.deactivateUser(userId);
                        Alert.alert("Success", `${username} deactivated.`);
                        loadUsers();
                    } catch (e: any) {
                        Alert.alert("Error", e.message);
                    }
                },
            },
        ]);
    };

    const handleResetPassword = async () => {
        if (!newPassword.trim()) return Alert.alert("Error", "Enter a password");
        try {
            await api.resetPassword(selectedUser.userId || selectedUser.id, newPassword.trim());
            Alert.alert("Success", `Password updated for ${selectedUser.username}`);
            setSelectedUser(null);
            setNewPassword("");
        } catch (e: any) {
            Alert.alert("Error", e.message);
        }
    };

    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>All users</Text>

            <FlatList
                data={users}
                keyExtractor={(item) => (item.userId || item.id || Math.random()).toString()}
                renderItem={({ item }) => (
                    <View style={{ padding: 14, backgroundColor: colors.surface, borderRadius: 10, borderWidth: 0.5, borderColor: colors.border, marginBottom: 8 }}>
                        <View style={{ flexDirection: "row", justifyContent: "space-between", alignItems: "center" }}>
                            <Text style={{ color: colors.text, fontWeight: "500" }}>{item.username}</Text>
                            <Text style={{ color: colors.accent, fontSize: 12 }}>{item.role}</Text>
                        </View>
                        <Text style={{ color: colors.text2, fontSize: 12, marginTop: 2, marginBottom: 10 }}>{item.department || "No Dept"}</Text>

                        {/* Action buttons */}
                        <View style={{ flexDirection: "row", gap: 8 }}>
                            <TouchableOpacity
                                onPress={() => setSelectedUser(item)}
                                style={{ flex: 1, paddingVertical: 6, backgroundColor: colors.bg, borderRadius: 6, borderWidth: 0.5, borderColor: colors.border, alignItems: "center" }}
                            >
                                <Text style={{ color: colors.text, fontSize: 12 }}>Reset Password</Text>
                            </TouchableOpacity>

                            <TouchableOpacity
                                onPress={() => handleDeactivate(item.userId || item.id, item.username)}
                                style={{ flex: 1, paddingVertical: 6, backgroundColor: "#EF444420", borderRadius: 6, borderWidth: 0.5, borderColor: "#EF4444", alignItems: "center" }}
                            >
                                <Text style={{ color: "#EF4444", fontSize: 12, fontWeight: "500" }}>Deactivate</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                )}
            />

            {/* Password Reset Modal */}
            <Modal visible={!!selectedUser} transparent animationType="fade">
                <View style={{ flex: 1, backgroundColor: "#00000080", justifyContent: "center", padding: 24 }}>
                    <View style={{ backgroundColor: colors.surface, padding: 20, borderRadius: 12, borderWidth: 0.5, borderColor: colors.border }}>
                        <Text style={{ fontSize: 16, fontWeight: "500", color: colors.text, marginBottom: 12 }}>
                            Reset Password ({selectedUser?.username})
                        </Text>

                        <TextInput
                            placeholder="New password"
                            value={newPassword}
                            onChangeText={setNewPassword}
                            secureTextEntry
                            style={{ height: 44, borderWidth: 0.5, borderColor: colors.border, borderRadius: 8, paddingHorizontal: 12, marginBottom: 16, color: colors.text, backgroundColor: colors.bg }}
                            placeholderTextColor={colors.text2}
                        />

                        <View style={{ flexDirection: "row", gap: 8 }}>
                            <TouchableOpacity onPress={() => setSelectedUser(null)} style={{ flex: 1, height: 40, justifyContent: "center", alignItems: "center", borderRadius: 8, borderWidth: 0.5, borderColor: colors.border }}>
                                <Text style={{ color: colors.text }}>Cancel</Text>
                            </TouchableOpacity>
                            <TouchableOpacity onPress={handleResetPassword} style={{ flex: 1, height: 40, backgroundColor: colors.accent, justifyContent: "center", alignItems: "center", borderRadius: 8 }}>
                                <Text style={{ color: "#fff", fontWeight: "500" }}>Save</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </View>
            </Modal>
        </View>
    );
}