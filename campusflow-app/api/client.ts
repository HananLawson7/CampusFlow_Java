const BASE_URL = "http://YOUR_LOCAL_IP:4567/api"; // your machine's LAN IP

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
    getAllUsers: () => request("/admin/users"),
    createUser: (payload: any) =>
        request("/admin/users", { method: "POST", body: JSON.stringify(payload) }),
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

    getCreatedPOs: () => request("/accounts/created"),
    getBilledPOs: () => request("/accounts/billed"),
    getPaidPOs: () => request("/accounts/paid"),
    markBilled: (id: number) => request(`/accounts/orders/${id}/bill`, { method: "PATCH" }),
    markPaid: (id: number) => request(`/accounts/orders/${id}/pay`, { method: "PATCH" }),
    getVendorSpending: () => request("/accounts/vendor-spending"),
};