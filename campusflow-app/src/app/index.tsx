import { View, Text, TouchableOpacity, FlatList } from "react-native";
import { router } from "expo-router";
import { useTheme } from "../../theme/ThemeContext";

const ROLES = ["ADMIN", "HOD", "FACULTY", "STUDENT", "STORES", "PURCHASE", "ACCOUNTS", "BOARD_MEMBERS"];

export default function RoleSelect() {
  const { colors, toggle } = useTheme();
  return (
      <View style={{ flex: 1, backgroundColor: colors.bg, padding: 20, paddingTop: 60 }}>
        <TouchableOpacity onPress={toggle} style={{ alignSelf: "flex-end", marginBottom: 20 }}>
          <Text style={{ color: colors.text2 }}>Toggle theme</Text>
        </TouchableOpacity>
        <Text style={{ fontSize: 22, fontWeight: "500", color: colors.text, marginBottom: 24 }}>
          Select your role
        </Text>
        <FlatList
            data={ROLES}
            numColumns={2}
            keyExtractor={(r) => r}
            renderItem={({ item }) => (
                <TouchableOpacity
                    onPress={() => router.push({ pathname: "/login", params: { role: item } })}
                    style={{ flex: 1, margin: 6, padding: 20, borderRadius: 12, backgroundColor: colors.surface, borderWidth: 0.5, borderColor: colors.border }}
                >
                  <Text style={{ color: colors.text, fontWeight: "500" }}>{item}</Text>
                </TouchableOpacity>
            )}
        />
      </View>
  );
}