import { useEffect, useRef } from "react";
import { View, Text, Animated, StyleSheet } from "react-native";
import { router } from "expo-router";
import { useTheme } from "../theme/ThemeContext";

export default function SplashScreen() {
    const { colors } = useTheme();
    const fadeAnim = useRef(new Animated.Value(0)).current;

    useEffect(() => {
        // Animation Sequence: Fade In (800ms) -> Hold (1200ms) -> Fade Out (600ms)
        Animated.sequence([
            Animated.timing(fadeAnim, {
                toValue: 1,
                duration: 800,
                useNativeDriver: true,
            }),
            Animated.delay(1200),
            Animated.timing(fadeAnim, {
                toValue: 0,
                duration: 600,
                useNativeDriver: true,
            }),
        ]).start(() => {
            // Replaces the splash screen with the login screen so users can't navigate back
            router.replace("/role-select");
        });
    }, [fadeAnim]);

    return (
        <View style={[styles.container, { backgroundColor: colors.bg }]}>
            <Animated.View style={{ opacity: fadeAnim, alignItems: "center" }}>
                <Text style={[styles.title, { color: colors.text }]}>CampusFlow</Text>
                <Text style={[styles.subtitle, { color: colors.text2 }]}>
                    Automated Campus Management
                </Text>
            </Animated.View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: "center",
        alignItems: "center",
    },
    title: {
        fontSize: 36,
        fontWeight: "bold",
        letterSpacing: 1.2,
    },
    subtitle: {
        fontSize: 14,
        marginTop: 8,
        letterSpacing: 0.5,
    },
});