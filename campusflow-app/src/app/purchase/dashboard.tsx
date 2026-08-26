import { View, Text, TouchableOpacity } from "react-native";
import { router } from "expo-router";
import { useTheme } from "../../../theme/ThemeContext";

export default function PurchaseDashboard() {
    const { colors } = useTheme();
    const actions = [
        { label: "Pending requests", path: "/purchase/pending-prs" },
        { label: "All orders", path: "/purchase/all-pos" },
    ];
    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 22, fontWeight: "500", color: colors.text, marginBottom: 20 }}>Purchase dashboard</Text>
            {actions.map((a) => (
                <TouchableOpacity key={a.path} onPress={() => router.push(a.path as any)}
                                  style={{ padding: 16, backgroundColor: colors.surface, borderRadius: 12, borderWidth: 0.5, borderColor: colors.border, marginBottom: 10 }}>
                    <Text style={{ color: colors.text }}>{a.label}</Text>
                </TouchableOpacity>
            ))}
        </View>
    );
}