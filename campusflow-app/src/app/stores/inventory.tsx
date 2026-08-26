import { useEffect, useState } from "react";
import { View, Text, FlatList, ActivityIndicator, Alert } from "react-native";
import { useTheme } from "../../theme/ThemeContext";
import { api } from "../../api/client"; // Fixed import path

export default function Inventory() {
    const { colors } = useTheme();
    const [resources, setResources] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);

    const loadInventory = async () => {
        setLoading(true);
        try {
            const data = await api.getInventory();
            setResources(data || []);
        } catch (e: any) {
            Alert.alert("Error", e.message || "Failed to load inventory");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadInventory();
    }, []);

    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>
                Inventory
            </Text>

            {loading ? (
                <ActivityIndicator color={colors.accent} style={{ marginTop: 20 }} />
            ) : (
                <FlatList
                    data={resources}
                    keyExtractor={(item) => (item.resourceId || item.id || Math.random()).toString()}
                    ListEmptyComponent={
                        <Text style={{ color: colors.text2, marginTop: 12 }}>No inventory items found.</Text>
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
                                flexDirection: "row",
                                justifyContent: "space-between",
                                alignItems: "center",
                            }}
                        >
                            <Text style={{ color: colors.text, fontWeight: "500" }}>
                                {item.name || `Resource #${item.resourceId}`}
                            </Text>
                            <Text
                                style={{
                                    color: (item.quantityInStock ?? 0) <= 5 ? "#D85A30" : colors.text2,
                                    fontWeight: "500",
                                }}
                            >
                                {item.quantityInStock ?? 0} in stock
                            </Text>
                        </View>
                    )}
                />
            )}
        </View>
    );
}