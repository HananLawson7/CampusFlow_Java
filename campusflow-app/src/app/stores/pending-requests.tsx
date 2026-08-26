import { useEffect, useState } from "react";
import { View, Text, FlatList, TouchableOpacity, Alert } from "react-native";
import { useTheme } from "../../../theme/ThemeContext";
import { api } from "../../../api/client";

export default function PendingRequests() {
    const { colors } = useTheme();
    const [requests, setRequests] = useState<any[]>([]);
    const load = () => api.getPendingRequests().then(setRequests);
    useEffect(() => { load(); }, []);

    const process = async (id: number) => {
        try {
            const result = await api.processRequest(id);
            Alert.alert("Processed", result.status);
            load();
        } catch (e: any) {
            Alert.alert("Error", e.message);
        }
    };

    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>Pending requests</Text>
            <FlatList data={requests} keyExtractor={(item) => item.requestId.toString()}
                      renderItem={({ item }) => (
                          <View style={{ padding: 14, backgroundColor: colors.surface, borderRadius: 10, borderWidth: 0.5, borderColor: colors.border, marginBottom: 8 }}>
                              <Text style={{ color: colors.text }}>Resource #{item.resourceId} — qty {item.quantity}</Text>
                              <TouchableOpacity onPress={() => process(item.requestId)}
                                                style={{ marginTop: 8, alignSelf: "flex-start", paddingVertical: 6, paddingHorizontal: 12, backgroundColor: colors.accentBg, borderRadius: 8 }}>
                                  <Text style={{ color: colors.accent, fontSize: 12 }}>Process</Text>
                              </TouchableOpacity>
                          </View>
                      )} />
        </View>
    );
}