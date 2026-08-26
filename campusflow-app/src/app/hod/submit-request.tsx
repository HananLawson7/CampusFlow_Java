import { useState } from "react";
import { View, Text, TextInput, TouchableOpacity, Alert, ScrollView } from "react-native";
import { useTheme } from "../../theme/ThemeContext";
import { useAuth } from "../../store/AuthContext"; // Fixed import path
import { api } from "../../api/client"; // Fixed import path

export default function SubmitRequest() {
    const { colors } = useTheme();
    const { user } = useAuth();
    const [resourceId, setResourceId] = useState("");
    const [quantity, setQuantity] = useState("");
    const [notes, setNotes] = useState("");
    const [submitting, setSubmitting] = useState(false);

    const submit = async () => {
        const parsedResId = parseInt(resourceId, 10);
        const parsedQty = parseInt(quantity, 10);
        const hodId = user?.userId || user?.id;

        if (!resourceId || isNaN(parsedResId)) {
            return Alert.alert("Error", "Please enter a valid Resource ID");
        }
        if (!quantity || isNaN(parsedQty) || parsedQty <= 0) {
            return Alert.alert("Error", "Please enter a valid quantity greater than 0");
        }

        setSubmitting(true);
        try {
            await api.submitRequest({
                hodId,
                resourceId: parsedResId,
                quantity: parsedQty,
                notes: notes.trim(),
            });
            Alert.alert("Success", "Request submitted successfully");
            setResourceId("");
            setQuantity("");
            setNotes("");
        } catch (e: any) {
            Alert.alert("Error", e.message || "Failed to submit request");
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <ScrollView style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>
                Submit request
            </Text>

            <TextInput
                placeholder="Resource ID"
                value={resourceId}
                onChangeText={setResourceId}
                keyboardType="numeric"
                style={{ height: 44, borderWidth: 0.5, borderColor: colors.border, borderRadius: 10, paddingHorizontal: 14, marginBottom: 12, color: colors.text, backgroundColor: colors.surface }}
                placeholderTextColor={colors.text2}
            />

            <TextInput
                placeholder="Quantity"
                value={quantity}
                onChangeText={setQuantity}
                keyboardType="numeric"
                style={{ height: 44, borderWidth: 0.5, borderColor: colors.border, borderRadius: 10, paddingHorizontal: 14, marginBottom: 12, color: colors.text, backgroundColor: colors.surface }}
                placeholderTextColor={colors.text2}
            />

            <TextInput
                placeholder="Notes (optional)"
                value={notes}
                onChangeText={setNotes}
                style={{ height: 44, borderWidth: 0.5, borderColor: colors.border, borderRadius: 10, paddingHorizontal: 14, marginBottom: 12, color: colors.text, backgroundColor: colors.surface }}
                placeholderTextColor={colors.text2}
            />

            <TouchableOpacity
                onPress={submit}
                disabled={submitting}
                style={{ height: 46, backgroundColor: colors.accent, borderRadius: 10, justifyContent: "center", alignItems: "center", marginTop: 8, opacity: submitting ? 0.7 : 1 }}
            >
                <Text style={{ color: "#fff", fontWeight: "500" }}>
                    {submitting ? "Submitting..." : "Submit"}
                </Text>
            </TouchableOpacity>
        </ScrollView>
    );
}