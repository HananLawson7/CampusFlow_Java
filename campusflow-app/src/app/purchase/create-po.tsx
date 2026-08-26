import { useState } from "react";
import { View, Text, TextInput, TouchableOpacity, Alert, ScrollView } from "react-native";
import { useLocalSearchParams } from "expo-router";
import { useTheme } from "../../../theme/ThemeContext";
import { api } from "../../../api/client";

export default function CreatePO() {
    const { prId } = useLocalSearchParams();
    const { colors } = useTheme();
    const [vendorName, setVendorName] = useState("");
    const [totalCost, setTotalCost] = useState("");

    const submit = async () => {
        try {
            await api.createPO({ prId: parseInt(prId as string), vendorName, totalCost: parseFloat(totalCost) });
            Alert.alert("Success", "Purchase order created");
        } catch (e: any) {
            Alert.alert("Error", e.message);
        }
    };

    return (
        <ScrollView style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>Create purchase order</Text>
            <TextInput placeholder="Vendor name" value={vendorName} onChangeText={setVendorName}
                       style={{ height: 44, borderWidth: 0.5, borderColor: colors.border, borderRadius: 10, paddingHorizontal: 14, marginBottom: 12, color: colors.text }} placeholderTextColor={colors.text2} />
            <TextInput placeholder="Total cost" value={totalCost} onChangeText={setTotalCost} keyboardType="numeric"
                       style={{ height: 44, borderWidth: 0.5, borderColor: colors.border, borderRadius: 10, paddingHorizontal: 14, marginBottom: 12, color: colors.text }} placeholderTextColor={colors.text2} />
            <TouchableOpacity onPress={submit} style={{ height: 46, backgroundColor: colors.accent, borderRadius: 10, justifyContent: "center", alignItems: "center", marginTop: 8 }}>
                <Text style={{ color: "#fff", fontWeight: "500" }}>Create PO</Text>
            </TouchableOpacity>
        </ScrollView>
    );
}