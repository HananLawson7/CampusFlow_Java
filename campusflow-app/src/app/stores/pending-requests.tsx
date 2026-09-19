import { useEffect, useState } from "react";
import { View, Text, FlatList, TouchableOpacity, Alert, ActivityIndicator } from "react-native";
import { useTheme } from "../../theme/ThemeContext";
import { api } from "../../api/client"; // Fixed import path

export default function PendingRequests() {
    const { colors } = useTheme();
    const [requests, setRequests] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);

    const load = async () => {
        setLoading(true);
        try {
            const data = await api.getPendingRequests();
            setRequests(data || []);
        } catch (e: any) {
            Alert.alert("Error", e.message || "Failed to load pending requests");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        load();
    }, []);

    const process = async (id: number) => {
        try {
            const result = await api.processRequest(id);
            const statusMessages: any = {
                FULFILLED_FROM_STOCK: "Request fulfilled from stock",
                FORWARDED_TO_PURCHASE: "Awaiting Purchase Approval",
                ALREADY_FORWARDED: "This request has already been forwarded to Purchase",
            };
            Alert.alert("Success", statusMessages[result?.status] || result?.status || "Request processed");
            load();
        } catch (e: any) {
            Alert.alert("Error", e.message || "Failed to process request");
        }
    };

    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>
                Pending requests
            </Text>

            {loading ? (
                <ActivityIndicator color={colors.accent} style={{ marginTop: 20 }} />
            ) : (
                <FlatList
                    data={requests}
                    keyExtractor={(item) => (item.requestId || item.id || Math.random()).toString()}
                    ListEmptyComponent={
                        <Text style={{ color: colors.text2, marginTop: 12 }}>No pending requests found.</Text>
                    }
                    renderItem={({ item }) => {
                        const canProcess = item.quantityInStock >= item.quantity;
                        return (
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
                                    {item.resourceName || `Resource #${item.resourceId}`} — Qty: {item.quantity}
                                </Text>
                                <Text style={{ color: colors.text2, fontSize: 12, marginTop: 2 }}>
                                    In stock: {item.quantityInStock}
                                </Text>

                                <TouchableOpacity
                                    onPress={() => process(item.requestId || item.id)}
                                    style={{
                                        marginTop: 8,
                                        alignSelf: "flex-start",
                                        paddingVertical: 6,
                                        paddingHorizontal: 12,
                                        backgroundColor: canProcess ? colors.accentBg : "#F5D5C0",
                                        borderRadius: 8,
                                    }}
                                >
                                    <Text style={{ color: canProcess ? colors.accent : "#B85C2E", fontSize: 12, fontWeight: "500" }}>
                                        {canProcess ? "Process" : "Forward to Purchase"}
                                    </Text>
                                </TouchableOpacity>
                            </View>
                        );
                    }}
                />
            )}
        </View>
    );
}