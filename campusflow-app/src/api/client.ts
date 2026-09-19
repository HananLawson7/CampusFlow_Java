/*
const BASE_URL = "http://192.168.56.1:4567/api"; // your machine's LAN IP

async function request(path: string, options: RequestInit = {}) {
    const res = await fetch(`${BASE_URL}${path}`, {
        ...options,
        headers: { "Content-Type": "application/json", ...options.headers },
    });
    const data = await res.json();
    if (!res.ok) throw new Error(data.error || "Request failed");
    return data;
}

export const api = {
    login: (username: string, password: string) =>
        request("/login", { method: "POST", body: JSON.stringify({ username, password }) }),
    getAllUsers: () => request("/admin/all-users"),
    createUser: (payload: any) =>
        request("/admin/create-user", { method: "POST", body: JSON.stringify(payload) }),
    deactivateUser: (id: number) =>
        request(`/admin/users/${id}/deactivate`, { method: "PATCH" }),
    resetPassword: (id: number, newPassword: string) =>
        request(`/admin/users/${id}/password`, { method: "PATCH", body: JSON.stringify({ newPassword }) }),

    submitRequest: (payload: any) =>
        request("/hod/requests", { method: "POST", body: JSON.stringify(payload) }),
    getMyRequests: (hodId: number) => request(`/hod/requests/${hodId}`),

    getPendingRequests: () => request("/stores/pending"),
    processRequest: (id: number) => request(`/stores/requests/${id}/process`, { method: "PATCH" }),
    getInventory: () => request("/stores/inventory"),

    getPendingPRs: () => request("/purchase/pending"),
    createPO: (payload: any) => request("/purchase/orders", { method: "POST", body: JSON.stringify(payload) }),
    getAllPOs: () => request("/purchase/orders"),

    getCreatedPOs: () => request("/accounts/created-pos"),
    getBilledPOs: () => request("/accounts/billed-pos"),
    getPaidPOs: () => request("/accounts/paid"),
    markBilled: (id: number) => request(`/accounts/orders/${id}/bill`, { method: "PATCH" }),
    markPaid: (id: number) => request(`/accounts/orders/${id}/pay`, { method: "PATCH" }),
    getVendorSpending: () => request("/accounts/vendor-spending"),
};

*/
/*
import { Platform } from "react-native";

// Automatically pick the right IP address depending on where the app runs
const getBaseUrl = () => {
    if (Platform.OS === "android") {
        // Option A: If using Android Studio Emulator, use 10.0.2.2
        return "http://10.0.2.2:4567";

        // Option B: If using a PHYSICAL ANDROID PHONE over Expo Go, uncomment line below:
        // return "http://192.168.1.6:4567";
    }
    // Web browser or iOS Simulator uses standard localhost
    return "http://localhost:4567";
};

const BASE_URL = getBaseUrl();

async function request(path: string, options: RequestInit = {}) {
    try {
        const res = await fetch(`${BASE_URL}${path}`, {
            ...options,
            headers: {
                "Content-Type": "application/json",
                ...options.headers,
            },
        });

        const data = await res.json();
        if (!res.ok) throw new Error(data.error || "Request failed");
        return data;
    } catch (error: any) {
        console.error(`API Fetch Error on [${options.method || 'GET'}] ${BASE_URL}${path}:`, error.message);
        throw error;
    }
}

export const api = {
    // Auth
    login: (username: string, password: string) =>
        request("/login", { method: "POST", body: JSON.stringify({ username, password }) }),

    // Admin
    getAllUsers: () => request("/admin/all-users"),
    createUser: (payload: any) =>
        request("/admin/create-user", { method: "POST", body: JSON.stringify(payload) }),
    deactivateUser: (id: number) =>
        request(`/admin/users/${id}/deactivate`, { method: "PATCH" }),
    resetPassword: (id: number, newPassword: string) =>
        request(`/admin/users/${id}/password`, { method: "PATCH", body: JSON.stringify({ newPassword }) }),

    // HOD
    submitRequest: (payload: any) =>
        request("/hod/requests", { method: "POST", body: JSON.stringify(payload) }),
    getMyRequests: (hodId: number) => request(`/hod/requests/${hodId}`),

    // Stores
    getPendingRequests: () => request("/stores/pending"),
    processRequest: (id: number) => request(`/stores/requests/${id}/process`, { method: "PATCH" }),
    getInventory: () => request("/stores/inventory"),

    // Purchase
    getPendingPRs: () => request("/purchase/pending"),
    createPO: (payload: any) => request("/purchase/orders", { method: "POST", body: JSON.stringify(payload) }),
    getAllPOs: () => request("/purchase/orders"),

    // Accounts
    getCreatedPOs: () => request("/accounts/created-pos"),
    //getBilledPOs: () => request("/accounts/billed-pos"),
    //getPaidPOs: () => request("/accounts/paid"),
    markBilled: (id: number) => request(`/accounts/orders/${id}/bill`, { method: "PATCH" }),
    markPaid: (id: number) => request(`/accounts/orders/${id}/pay`, { method: "PATCH" }),
    getVendorSpending: () => request("/accounts/vendor-spending"),
};
*/
/*
import { Platform } from "react-native";

// Resolves backend address based on target platform
const getBaseUrl = () => {
    if (Platform.OS === "android") {
        // Use 10.0.2.2 for Android Emulator, or "http://192.168.1.6:4567" for physical phone over Expo Go
        //return "http://10.0.2.2:4567";
        return "http://192.168.1.6:4567";
    }
    return "http://localhost:4567";
};

const BASE_URL = getBaseUrl();

async function request(path: string, options: RequestInit = {}) {
    try {
        const res = await fetch(`${BASE_URL}${path}`, {
            ...options,
            headers: {
                "Content-Type": "application/json",
                ...options.headers,
            },
        });

        const data = await res.json();
        if (!res.ok) throw new Error(data.error || "Request failed");
        return data;
    } catch (error: any) {
        console.error(`API Fetch Error [${options.method || "GET"}] ${BASE_URL}${path}:`, error.message);
        throw error;
    }
}

export const api = {
    login: (username: string, password: string) =>
        request("/login", {
            method: "POST",
            body: JSON.stringify({ username, password }),
        }),
};
*/

import { Platform } from "react-native";

const getBaseUrl = () => {
    if (Platform.OS === "android") {
        // Physical Android device connected to the same Wi-Fi network
        // as the computer running the Java backend.
        return "http://192.168.1.6:4567/api";
    }

    // Web / iOS Simulator
    return "http://localhost:4567/api";
};

const BASE_URL = getBaseUrl();

async function request(path: string, options: RequestInit = {}) {
    const url = `${BASE_URL}${path}`;

    try {
        console.log(`[API] ${options.method || "GET"} ${url}`);

        const res = await fetch(url, {
            ...options,
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json",
                ...options.headers,
            },
        });

        // Read as text first so a non-JSON backend response
        // doesn't cause an unexplained SyntaxError.
        const rawResponse = await res.text();

        console.log(`[API] Response ${res.status}:`, rawResponse);

        let data: any = null;

        if (rawResponse.trim()) {
            try {
                data = JSON.parse(rawResponse);
            } catch {
                throw new Error(
                    `Server returned invalid JSON (HTTP ${res.status}): ${rawResponse}`
                );
            }
        }

        if (!res.ok) {
            throw new Error(
                data?.error ||
                data?.message ||
                `Request failed with HTTP ${res.status}`
            );
        }

        return data;
    } catch (error: any) {
        console.error(
            `[API ERROR] ${options.method || "GET"} ${url}:`,
            error?.message || error
        );

        throw error;
    }
}
export const api = {
    login: (username: string, password: string) =>
        request("/login", {
            method: "POST",
            body: JSON.stringify({ username, password }),
        }),

    // Admin
    getAllUsers: () => request("/admin/users"),
    createUser: (payload: any) =>
        request("/admin/users", { method: "POST", body: JSON.stringify(payload) }),
    deactivateUser: (id: number) =>
        request(`/admin/users/${id}/deactivate`, { method: "PATCH" }),
    resetPassword: (id: number, newPassword: string) =>
        request(`/admin/users/${id}/password`, { method: "PATCH", body: JSON.stringify({ newPassword }) }),

    // HOD
    submitRequest: (payload: any) =>
        request("/hod/requests", { method: "POST", body: JSON.stringify(payload) }),
    getMyRequests: (hodId: number) => request(`/hod/requests/${hodId}`),

    // Stores
    getPendingRequests: () => request("/stores/pending"),
    processRequest: (id: number) =>
        request(`/stores/requests/${id}/process`, { method: "PATCH" }),
    getInventory: () => request("/stores/inventory"),

    // Purchase
    getPendingPRs: () => request("/purchase/pending"),
    createPO: (payload: any) =>
        request("/purchase/orders", { method: "POST", body: JSON.stringify(payload) }),
    getAllPOs: () => request("/purchase/orders"),

    // Accounts
    getCreatedPOs: () => request("/accounts/created"),
    getBilledPOs: () => request("/accounts/billed"),
    getPaidPOs: () => request("/accounts/paid"),
    markBilled: (id: number) => request(`/accounts/orders/${id}/bill`, { method: "PATCH" }),
    markPaid: (id: number) => request(`/accounts/orders/${id}/pay`, { method: "PATCH" }),
    getVendorSpending: () => request("/accounts/vendor-spending"),
};