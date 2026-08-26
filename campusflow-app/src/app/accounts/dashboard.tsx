import { View, Text, TouchableOpacity } from "react-native";
import { router } from "expo-router";
import { useTheme } from "../../theme/ThemeContext";
import { useAuth } from "../../store/AuthContext";

export default function AccountsDashboard() {
    const { colors } = useTheme();
    const { logout } = useAuth();

    const actions = [
        { label: "Created orders", path: "/accounts/created-pos" },
        { label: "Billed orders", path: "/accounts/billed-pos" },
        { label: "Payment history", path: "/accounts/payment-history" },
    ];

    const handleLogout = () => {
        logout();
        router.replace("/login");
    };

    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 22, fontWeight: "500", color: colors.text, marginBottom: 20 }}>
                Accounts dashboard
            </Text>

            {actions.map((a) => (
                <TouchableOpacity
                    key={a.path}
                    onPress={() => router.push(a.path as any)}
                    style={{
                        padding: 16,
                        backgroundColor: colors.surface,
                        borderRadius: 12,
                        borderWidth: 0.5,
                        borderColor: colors.border,
                        marginBottom: 10,
                    }}
                >
                    <Text style={{ color: colors.text }}>{a.label}</Text>
                </TouchableOpacity>
            ))}

            <TouchableOpacity
                onPress={handleLogout}
                style={{
                    padding: 16,
                    backgroundColor: "#EF444420",
                    borderRadius: 12,
                    borderWidth: 0.5,
                    borderColor: "#EF4444",
                    marginTop: 20,
                }}
            >
                <Text style={{ color: "#EF4444", fontWeight: "500", textAlign: "center" }}>Sign Out</Text>
            </TouchableOpacity>
        </View>
    );
}