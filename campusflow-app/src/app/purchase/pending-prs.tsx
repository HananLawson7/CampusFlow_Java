import { useState, useCallback } from "react";
import { View, Text, FlatList, TouchableOpacity, ActivityIndicator, Alert } from "react-native";
import { router, useFocusEffect } from "expo-router";
import { useTheme } from "../../theme/ThemeContext";
import { api } from "../../api/client"; // Fixed import path

export default function PendingPRs() {
    const { colors } = useTheme();
    const [prs, setPrs] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);

    const loadPRs = async () => {
        setLoading(true);
        try {
            const data = await api.getPendingPRs();
            setPrs(data || []);
        } catch (e: any) {
            Alert.alert("Error", e.message || "Failed to load pending purchase requests");
        } finally {
            setLoading(false);
        }
    };

    useFocusEffect(
        useCallback(() => {
            loadPRs();
        }, [])
    );

    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>
                Pending purchase requests
            </Text>

            {loading ? (
                <ActivityIndicator color={colors.accent} style={{ marginTop: 20 }} />
            ) : (
                <FlatList
                    data={prs}
                    keyExtractor={(item) => (item.prId || item.id || Math.random()).toString()}
                    ListEmptyComponent={
                        <Text style={{ color: colors.text2, marginTop: 12 }}>No pending purchase requests.</Text>
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
                            <TouchableOpacity
                                onPress={() =>
                                    router.push({
                                        pathname: "/purchase/create-po",
                                        params: { prId: item.prId || item.id },
                                    })
                                }
                                style={{
                                    marginTop: 8,
                                    alignSelf: "flex-start",
                                    paddingVertical: 6,
                                    paddingHorizontal: 12,
                                    backgroundColor: colors.accentBg,
                                    borderRadius: 8,
                                }}
                            >
                                <Text style={{ color: colors.accent, fontSize: 12, fontWeight: "500" }}>
                                    Create PO
                                </Text>
                            </TouchableOpacity>
                        </View>
                    )}
                />
            )}
        </View>
    );
}