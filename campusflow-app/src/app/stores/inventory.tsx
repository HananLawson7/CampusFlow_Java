import { useEffect, useState } from "react";
import { View, Text, FlatList } from "react-native";
import { useTheme } from "../../../theme/ThemeContext";
import { api } from "../../../api/client";

export default function Inventory() {
    const { colors } = useTheme();
    const [resources, setResources] = useState<any[]>([]);
    useEffect(() => { api.getInventory().then(setResources); }, []);
    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>Inventory</Text>
            <FlatList data={resources} keyExtractor={(item) => item.resourceId.toString()}
                      renderItem={({ item }) => (
                          <View style={{ padding: 14, backgroundColor: colors.surface, borderRadius: 10, borderWidth: 0.5, borderColor: colors.border, marginBottom: 8, flexDirection: "row", justifyContent: "space-between" }}>
                              <Text style={{ color: colors.text }}>{item.name}</Text>
                              <Text style={{ color: item.quantityInStock <= 5 ? "#D85A30" : colors.text2 }}>{item.quantityInStock} in stock</Text>
                          </View>
                      )} />
        </View>
    );
}