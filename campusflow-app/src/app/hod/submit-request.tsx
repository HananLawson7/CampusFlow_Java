import { useState } from "react";
import { View, Text, TextInput, TouchableOpacity, Alert, ScrollView } from "react-native";
import { useTheme } from "../../../theme/ThemeContext";
import { useAuth } from "../../../store/AuthContext";
import { api } from "../../../api/client";

export default function SubmitRequest() {
    const { colors } = useTheme();
    const { user } = useAuth();
    const [resourceId, setResourceId] = useState("");
    const [quantity, setQuantity] = useState("");
    const [notes, setNotes] = useState("");

    const submit = async () => {
        try {
            await api.submitRequest({ hodId: user.userId, resourceId: parseInt(resourceId), quantity: parseInt(quantity), notes });
            Alert.alert("Success", "Request submitted");
        } catch (e: any) {
            Alert.alert("Error", e.message);
        }
    };

    return (
        <ScrollView style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>Submit request</Text>
            <TextInput placeholder="Resource ID" value={resourceId} onChangeText={setResourceId} keyboardType="numeric"
                       style={{ height: 44, borderWidth: 0.5, borderColor: colors.border, borderRadius: 10, paddingHorizontal: 14, marginBottom: 12, color: colors.text }} placeholderTextColor={colors.text2} />
            <TextInput placeholder="Quantity" value={quantity} onChangeText={setQuantity} keyboardType="numeric"
                       style={{ height: 44, borderWidth: 0.5, borderColor: colors.border, borderRadius: 10, paddingHorizontal: 14, marginBottom: 12, color: colors.text }} placeholderTextColor={colors.text2} />
            <TextInput placeholder="Notes" value={notes} onChangeText={setNotes}
                       style={{ height: 44, borderWidth: 0.5, borderColor: colors.border, borderRadius: 10, paddingHorizontal: 14, marginBottom: 12, color: colors.text }} placeholderTextColor={colors.text2} />
            <TouchableOpacity onPress={submit} style={{ height: 46, backgroundColor: colors.accent, borderRadius: 10, justifyContent: "center", alignItems: "center", marginTop: 8 }}>
                <Text style={{ color: "#fff", fontWeight: "500" }}>Submit</Text>
            </TouchableOpacity>
        </ScrollView>
    );
}