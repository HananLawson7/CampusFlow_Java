import { useEffect, useState } from "react";
import { View, Text, FlatList } from "react-native";
import { useTheme } from "../../../theme/ThemeContext";
import { api } from "../../../api/client";

const statusColor: any = { CREATED: "#EF9F27", BILLED: "#3C7EF8", PAID: "#1D9E75" };

export default function AllPOs() {
    const { colors } = useTheme();
    const [pos, setPos] = useState<any[]>([]);
    useEffect(() => { api.getAllPOs().then(setPos); }, []);
    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>All purchase orders</Text>
            <FlatList data={pos} keyExtractor={(item) => item.poId.toString()}
                      renderItem={({ item }) => (
                          <View style={{ padding: 14, backgroundColor: colors.surface, borderRadius: 10, borderWidth: 0.5, borderColor: colors.border, marginBottom: 8 }}>
                              <Text style={{ color: colors.text }}>{item.vendorName} — ${item.totalCost}</Text>
                              <Text style={{ color: statusColor[item.status] || colors.text2, fontSize: 12, marginTop: 4 }}>{item.status}</Text>
                          </View>
                      )} />
        </View>
    );
}