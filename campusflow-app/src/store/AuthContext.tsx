import React, { createContext, useContext, useState, useEffect } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";

interface AuthContextType {
    user: any;
    setUser: (user: any) => Promise<void>;
    logout: () => Promise<void>;
    isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: React.ReactNode }) {
    const [user, setUserState] = useState<any>(null);
    const [isLoading, setIsLoading] = useState(true);

    // Restore saved user session when the app boots
    useEffect(() => {
        const restoreSession = async () => {
            try {
                const storedUser = await AsyncStorage.getItem("@auth_user");
                if (storedUser) {
                    setUserState(JSON.parse(storedUser));
                }
            } catch (e) {
                console.error("Failed to restore auth state", e);
            } finally {
                setIsLoading(false);
            }
        };

        restoreSession();
    }, []);

    const setUser = async (newUser: any) => {
        try {
            if (newUser) {
                await AsyncStorage.setItem("@auth_user", JSON.stringify(newUser));
            } else {
                await AsyncStorage.removeItem("@auth_user");
            }
            setUserState(newUser);
        } catch (e) {
            console.error("Failed to update auth storage", e);
        }
    };

    const logout = async () => {
        await setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, setUser, logout, isLoading }}>
            {children}
        </AuthContext.Provider>
    );
}

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
};