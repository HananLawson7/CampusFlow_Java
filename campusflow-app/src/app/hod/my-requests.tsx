import { useEffect, useState } from "react";
import { View, Text, FlatList, ActivityIndicator, Alert } from "react-native";
import { useTheme } from "../../theme/ThemeContext";
import { useAuth } from "../../store/AuthContext"; // Fixed import path
import { api } from "../../api/client"; // Fixed import path

const statusColor: Record<string, string> = {
    PENDING: "#EF9F27",
    FULFILLED_FROM_STOCK: "#1D9E75",
    FORWARDED_TO_STORES: "#D85A30",
};

export default function MyRequests() {
    const { colors } = useTheme();
    const { user } = useAuth();
    const [requests, setRequests] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);

    const loadRequests = async () => {
        const userId = user?.userId || user?.id;
        if (!userId) return;
        setLoading(true);
        try {
            const data = await api.getMyRequests(userId);
            setRequests(data || []);
        } catch (e: any) {
            Alert.alert("Error", e.message || "Failed to load requests");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadRequests();
    }, [user]);

    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>
                My requests
            </Text>

            {loading ? (
                <ActivityIndicator color={colors.accent} style={{ marginTop: 20 }} />
            ) : (
                <FlatList
                    data={requests}
                    keyExtractor={(item) => (item.requestId || item.id || Math.random()).toString()}
                    ListEmptyComponent={
                        <Text style={{ color: colors.text2, marginTop: 12 }}>No request history found.</Text>
                    }
                    renderItem={({ item }) => (
                        <View
                            style={{
                                padding: 14,
                                backgroundColor: colors.surface,
                                borderRadius: 10,
                                borderWidth: 0.5,
                                borderColor: colors.border,
                                marginBottom: 8,
                            }}
                        >
                            <Text style={{ color: colors.text, fontWeight: "500" }}>
                                Resource #{item.resourceId} — Qty: {item.quantity}
                            </Text>
                            <Text
                                style={{
                                    color: statusColor[item.status] || colors.text2,
                                    fontSize: 12,
                                    marginTop: 4,
                                    fontWeight: "500",
                                }}
                            >
                                {item.status}
                            </Text>
                        </View>
                    )}
                />
            )}
        </View>
    );
}