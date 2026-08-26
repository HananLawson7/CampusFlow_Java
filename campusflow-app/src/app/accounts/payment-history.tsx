import { useEffect, useState } from "react";
import { View, Text, FlatList, Alert, ActivityIndicator } from "react-native";
import { useTheme } from "../../theme/ThemeContext";
import { api } from "../../api/client"; // Fixed import path

export default function PaymentHistory() {
    const { colors } = useTheme();
    const [pos, setPos] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);

    const loadHistory = async () => {
        setLoading(true);
        try {
            const data = await api.getPaidPOs();
            setPos(data || []);
        } catch (e: any) {
            Alert.alert("Error", e.message || "Failed to load payment history");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadHistory();
    }, []);

    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>
                Payment history
            </Text>

            {loading ? (
                <ActivityIndicator color={colors.accent} style={{ marginTop: 20 }} />
            ) : (
                <FlatList
                    data={pos}
                    keyExtractor={(item) => (item.poId || item.id || Math.random()).toString()}
                    ListEmptyComponent={
                        <Text style={{ color: colors.text2, marginTop: 12 }}>No completed payments found.</Text>
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
                            {item.paymentDate && (
                                <Text style={{ color: colors.text2, fontSize: 12, marginTop: 4 }}>
                                    Paid: {item.paymentDate}
                                </Text>
                            )}
                        </View>
                    )}
                />
            )}
        </View>
    );
}