import { useEffect, useState } from "react";
import { View, Text, FlatList, ActivityIndicator, Alert } from "react-native";
import { useTheme } from "../../theme/ThemeContext";
import { api } from "../../api/client"; // Fixed import path

const statusColor: Record<string, string> = {
    CREATED: "#EF9F27",
    BILLED: "#3C7EF8",
    PAID: "#1D9E75",
};

export default function AllPOs() {
    const { colors } = useTheme();
    const [pos, setPos] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);

    const loadPOs = async () => {
        setLoading(true);
        try {
            const data = await api.getAllPOs();
            setPos(data || []);
        } catch (e: any) {
            Alert.alert("Error", e.message || "Failed to load purchase orders");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadPOs();
    }, []);

    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>
                All purchase orders
            </Text>

            {loading ? (
                <ActivityIndicator color={colors.accent} style={{ marginTop: 20 }} />
            ) : (
                <FlatList
                    data={pos}
                    keyExtractor={(item) => (item.poId || item.id || Math.random()).toString()}
                    ListEmptyComponent={
                        <Text style={{ color: colors.text2, marginTop: 12 }}>No purchase orders found.</Text>
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
                                {item.vendorName || "Unknown Vendor"} — ${item.totalCost}
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