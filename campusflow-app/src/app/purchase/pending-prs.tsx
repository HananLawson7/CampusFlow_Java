import { useEffect, useState } from "react";
import { View, Text, FlatList, TouchableOpacity } from "react-native";
import { router } from "expo-router";
import { useTheme } from "../../../theme/ThemeContext";
import { api } from "../../../api/client";

export default function PendingPRs() {
    const { colors } = useTheme();
    const [prs, setPrs] = useState<any[]>([]);
    useEffect(() => { api.getPendingPRs().then(setPrs); }, []);
    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>Pending purchase requests</Text>
            <FlatList data={prs} keyExtractor={(item) => item.prId.toString()}
                      renderItem={({ item }) => (
                          <View style={{ padding: 14, backgroundColor: colors.surface, borderRadius: 10, borderWidth: 0.5, borderColor: colors.border, marginBottom: 8 }}>
                              <Text style={{ color: colors.text }}>Resource #{item.resourceId} — qty {item.quantity}</Text>
                              <TouchableOpacity onPress={() => router.push({ pathname: "/purchase/create-po", params: { prId: item.prId } })}
                                                style={{ marginTop: 8, alignSelf: "flex-start", paddingVertical: 6, paddingHorizontal: 12, backgroundColor: colors.accentBg, borderRadius: 8 }}>
                                  <Text style={{ color: colors.accent, fontSize: 12 }}>Create PO</Text>
                              </TouchableOpacity>
                          </View>
                      )} />
        </View>
    );
}