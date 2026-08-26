import { useEffect, useState } from "react";
import { View, Text, FlatList } from "react-native";
import { useTheme } from "../../../theme/ThemeContext";
import { useAuth } from "../../../store/AuthContext";
import { api } from "../../../api/client";

const statusColor: any = { PENDING: "#EF9F27", FULFILLED_FROM_STOCK: "#1D9E75", FORWARDED_TO_STORES: "#D85A30" };

export default function MyRequests() {
    const { colors } = useTheme();
    const { user } = useAuth();
    const [requests, setRequests] = useState<any[]>([]);
    useEffect(() => { api.getMyRequests(user.userId).then(setRequests); }, []);
    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>My requests</Text>
            <FlatList data={requests} keyExtractor={(item) => item.requestId.toString()}
                      renderItem={({ item }) => (
                          <View style={{ padding: 14, backgroundColor: colors.surface, borderRadius: 10, borderWidth: 0.5, borderColor: colors.border, marginBottom: 8 }}>
                              <Text style={{ color: colors.text }}>Resource #{item.resourceId} — qty {item.quantity}</Text>
                              <Text style={{ color: statusColor[item.status] || colors.text2, fontSize: 12, marginTop: 4 }}>{item.status}</Text>
                          </View>
                      )} />
        </View>
    );
}