import { useEffect, useState } from "react";
import { View, Text, FlatList, TouchableOpacity, Alert, ActivityIndicator } from "react-native";
import { useTheme } from "../../theme/ThemeContext";
import { api } from "../../api/client"; // Fixed import path

export default function CreatedPOs() {
    const { colors } = useTheme();
    const [pos, setPos] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);

    const load = async () => {
        setLoading(true);
        try {
            const data = await api.getCreatedPOs();
            setPos(data || []);
        } catch (e: any) {
            Alert.alert("Error", e.message || "Failed to load created orders");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { load(); }, []);

    const bill = async (id: number) => {
        try {
            await api.markBilled(id);
            Alert.alert("Success", "Purchase order marked as billed");
            load();
        } catch (e: any) {
            Alert.alert("Error", e.message || "Failed to mark as billed");
        }
    };

    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>Created orders</Text>

            {loading ? (
                <ActivityIndicator color={colors.accent} style={{ marginTop: 20 }} />
            ) : (
                <FlatList
                    data={pos}
                    keyExtractor={(item) => (item.poId || item.id || Math.random()).toString()}
                    ListEmptyComponent={
                        <Text style={{ color: colors.text2, marginTop: 12 }}>No created purchase orders found.</Text>
                    }
                    renderItem={({ item }) => (
                        <View style={{ padding: 14, backgroundColor: colors.surface, borderRadius: 10, borderWidth: 0.5, borderColor: colors.border, marginBottom: 8 }}>
                            <Text style={{ color: colors.text, fontWeight: "500" }}>
                                {item.vendorName || "Unknown Vendor"} — ${item.totalCost}
                            </Text>
                            <TouchableOpacity
                                onPress={() => bill(item.poId || item.id)}
                                style={{ marginTop: 8, alignSelf: "flex-start", paddingVertical: 6, paddingHorizontal: 12, backgroundColor: colors.accentBg, borderRadius: 8 }}
                            >
                                <Text style={{ color: colors.accent, fontSize: 12, fontWeight: "500" }}>Mark billed</Text>
                            </TouchableOpacity>
                        </View>
                    )}
                />
            )}
        </View>
    );
}