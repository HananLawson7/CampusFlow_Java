import { useEffect, useState } from "react";
import { View, Text, FlatList, TouchableOpacity } from "react-native";
import { useTheme } from "../../../theme/ThemeContext";
import { api } from "../../../api/client";

export default function CreatedPOs() {
    const { colors } = useTheme();
    const [pos, setPos] = useState<any[]>([]);
    const load = () => api.getCreatedPOs().then(setPos);
    useEffect(() => { load(); }, []);

    const bill = async (id: number) => { await api.markBilled(id); load(); };

    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>Created orders</Text>
            <FlatList data={pos} keyExtractor={(item) => item.poId.toString()}
                      renderItem={({ item }) => (
                          <View style={{ padding: 14, backgroundColor: colors.surface, borderRadius: 10, borderWidth: 0.5, borderColor: colors.border, marginBottom: 8 }}>
                              <Text style={{ color: colors.text }}>{item.vendorName} — ${item.totalCost}</Text>
                              <TouchableOpacity onPress={() => bill(item.poId)}
                                                style={{ marginTop: 8, alignSelf: "flex-start", paddingVertical: 6, paddingHorizontal: 12, backgroundColor: colors.accentBg, borderRadius: 8 }}>
                                  <Text style={{ color: colors.accent, fontSize: 12 }}>Mark billed</Text>
                              </TouchableOpacity>
                          </View>
                      )} />
        </View>
    );
}