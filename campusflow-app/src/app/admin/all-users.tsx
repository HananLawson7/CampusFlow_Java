import { useEffect, useState } from "react";
import { View, Text, FlatList } from "react-native";
import { useTheme } from "../../../theme/ThemeContext";
import { api } from "../../../api/client";

export default function AllUsers() {
    const { colors } = useTheme();
    const [users, setUsers] = useState<any[]>([]);
    useEffect(() => { api.getAllUsers().then(setUsers); }, []);
    return (
        <View style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>All users</Text>
            <FlatList data={users} keyExtractor={(item) => item.userId.toString()}
                      renderItem={({ item }) => (
                          <View style={{ padding: 14, backgroundColor: colors.surface, borderRadius: 10, borderWidth: 0.5, borderColor: colors.border, marginBottom: 8 }}>
                              <Text style={{ color: colors.text }}>{item.username}</Text>
                              <Text style={{ color: colors.text2, fontSize: 12, marginTop: 2 }}>{item.role} · {item.department}</Text>
                          </View>
                      )} />
        </View>
    );
}