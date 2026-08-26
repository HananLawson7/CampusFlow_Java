import { View, Text, TouchableOpacity } from "react-native";
import { router } from "expo-router";
import { useTheme } from "../../theme/ThemeContext";
import { useAuth } from "../../store/AuthContext";

export default function HODDashboard() {
    const { colors } = useTheme();
    const actions = [
        { label: "Submit request", path: "/hod/submit-request" },
        { label: "My requests", path: "/hod/my-requests" },
    ];
    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 22, fontWeight: "500", color: colors.text, marginBottom: 20 }}>HOD dashboard</Text>
            {actions.map((a) => (
                <TouchableOpacity key={a.path} onPress={() => router.push(a.path as any)}
                                  style={{ padding: 16, backgroundColor: colors.surface, borderRadius: 12, borderWidth: 0.5, borderColor: colors.border, marginBottom: 10 }}>
                    <Text style={{ color: colors.text }}>{a.label}</Text>
                </TouchableOpacity>
            ))}
        </View>
    );
}