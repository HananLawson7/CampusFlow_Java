import { View, Text, TouchableOpacity, ScrollView, StyleSheet } from "react-native";
import { router } from "expo-router";
import { useTheme } from "../theme/ThemeContext";

const ROLES = [
    { label: "Admin", value: "ADMIN", desc: "System management & users" },
    { label: "HOD / Faculty", value: "HOD", desc: "Requests & approvals" },
    { label: "Stores", value: "STORES", desc: "Inventory & stock control" },
    { label: "Purchase", value: "PURCHASE", desc: "Purchase orders & vendors" },
    { label: "Accounts", value: "ACCOUNTS", desc: "Billing & payment history" },
];

export default function RoleSelect() {
    const { colors } = useTheme();

    const handleSelectRole = (roleValue: string) => {
        router.push({
            pathname: "/login",
            params: { role: roleValue },
        });
    };

    return (
        <View style={[styles.container, { backgroundColor: colors.bg }]}>
            <Text style={[styles.headerTitle, { color: colors.text }]}>Let's get started!</Text>
            <Text style={[styles.headerSubtitle, { color: colors.text2 }]}>
                Select your role to proceed to sign in
            </Text>

            <ScrollView contentContainerStyle={styles.roleList} showsVerticalScrollIndicator={false}>
                {ROLES.map((item) => (
                    <TouchableOpacity
                        key={item.value}
                        onPress={() => handleSelectRole(item.value)}
                        activeOpacity={0.7}
                        style={[
                            styles.card,
                            { backgroundColor: colors.surface, borderColor: colors.border },
                        ]}
                    >
                        <View style={styles.cardContent}>
                            <Text style={[styles.roleLabel, { color: colors.text }]}>
                                {item.label}
                            </Text>
                            <Text style={[styles.roleDesc, { color: colors.text2 }]}>
                                {item.desc}
                            </Text>
                        </View>
                        <Text style={[styles.arrow, { color: colors.accent }]}>→</Text>
                    </TouchableOpacity>
                ))}
            </ScrollView>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        paddingHorizontal: 24,
        paddingTop: 80,
    },
    headerTitle: {
        fontSize: 26,
        fontWeight: "bold",
        marginBottom: 6,
    },
    headerSubtitle: {
        fontSize: 14,
        marginBottom: 28,
    },
    roleList: {
        gap: 14,
        paddingBottom: 40,
    },
    card: {
        flexDirection: "row",
        alignItems: "center",
        justifyContent: "space-between",
        padding: 18,
        borderRadius: 14,
        borderWidth: 0.5,
    },
    cardContent: {
        flex: 1,
    },
    roleLabel: {
        fontSize: 18,
        fontWeight: "600",
        marginBottom: 4,
    },
    roleDesc: {
        fontSize: 13,
    },
    arrow: {
        fontSize: 20,
        fontWeight: "bold",
        marginLeft: 12,
    },
});