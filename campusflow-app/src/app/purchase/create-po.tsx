import { useState } from "react";
import { View, Text, TextInput, TouchableOpacity, Alert, ScrollView } from "react-native";
import { useLocalSearchParams, router } from "expo-router";
import { useTheme } from "../../theme/ThemeContext";
import { api } from "../../api/client"; // Fixed import path

export default function CreatePO() {
    const { prId } = useLocalSearchParams();
    const { colors } = useTheme();
    const [vendorName, setVendorName] = useState("");
    const [totalCost, setTotalCost] = useState("");
    const [submitting, setSubmitting] = useState(false);

    const submit = async () => {
        const parsedPrId = parseInt(prId as string, 10);
        const parsedCost = parseFloat(totalCost);

        if (!prId || isNaN(parsedPrId)) {
            return Alert.alert("Error", "Missing or invalid Purchase Request ID (prId)");
        }
        if (!vendorName.trim()) {
            return Alert.alert("Error", "Please enter a vendor name");
        }
        if (isNaN(parsedCost) || parsedCost <= 0) {
            return Alert.alert("Error", "Please enter a valid total cost");
        }

        setSubmitting(true);
        try {
            await api.createPO({
                prId: parsedPrId,
                vendorName: vendorName.trim(),
                totalCost: parsedCost
            });
            Alert.alert("Success", "Purchase order created", [
                { text: "OK", onPress: () => router.back() } // Go back after creation
            ]);
        } catch (e: any) {
            Alert.alert("Error", e.message || "Failed to create PO");
            setSubmitting(false); // Only reset on error so they can't double-click during navigation
        }
    };

    return (
        <ScrollView style={{ flex: 1, backgroundColor: colors.bg, padding: 24, paddingTop: 60 }}>
            <Text style={{ fontSize: 20, fontWeight: "500", color: colors.text, marginBottom: 16 }}>
                Create purchase order {prId ? `(#${prId})` : ""}
            </Text>

            <TextInput
                placeholder="Vendor name"
                value={vendorName}
                onChangeText={setVendorName}
                style={{ height: 44, borderWidth: 0.5, borderColor: colors.border, borderRadius: 10, paddingHorizontal: 14, marginBottom: 12, color: colors.text, backgroundColor: colors.surface }}
                placeholderTextColor={colors.text2}
            />

            <TextInput
                placeholder="Total cost"
                value={totalCost}
                onChangeText={setTotalCost}
                keyboardType="decimal-pad"
                style={{ height: 44, borderWidth: 0.5, borderColor: colors.border, borderRadius: 10, paddingHorizontal: 14, marginBottom: 12, color: colors.text, backgroundColor: colors.surface }}
                placeholderTextColor={colors.text2}
            />

            <TouchableOpacity
                onPress={submit}
                disabled={submitting}
                style={{ height: 46, backgroundColor: colors.accent, borderRadius: 10, justifyContent: "center", alignItems: "center", marginTop: 8, opacity: submitting ? 0.7 : 1 }}
            >
                <Text style={{ color: "#fff", fontWeight: "500" }}>
                    {submitting ? "Creating..." : "Create PO"}
                </Text>
            </TouchableOpacity>
        </ScrollView>
    );
}