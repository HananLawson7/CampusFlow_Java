package api;

import config.DatabaseConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.*;

public class ApiServer {
    // 🎯 Gson configured to seamlessly handle java.util.UUID objects as Strings
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(UUID.class, new TypeAdapter<UUID>() {
                @Override
                public void write(JsonWriter out, UUID value) throws IOException {
                    out.value(value != null ? value.toString() : null);
                }
                @Override
                public UUID read(JsonReader in) throws IOException {
                    String val = in.nextString();
                    return val != null ? UUID.fromString(val) : null;
                }
            })
            .create();

    public static void main(String[] args) {
        // 1. Initialize DB tables
        // DatabaseConfig.initDatabase();

        // 2. Spark listen settings
        ipAddress("0.0.0.0");
        port(4567);

        // 3. Enable CORS
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            response.status(200);
            return "OK";
        });

        before((request, response) -> {
            System.out.println("📩 Request Received: [" + request.requestMethod() + "] " + request.uri());
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Headers", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.type("application/json");
        });

        // 4. Health Check
        get("/api/health", (req, res) -> "{\"status\":\"OK\"}");

        // 5. Login Route
        post("/api/login", (req, res) -> {
            res.type("application/json");
            System.out.println("🔑 Processing Login Payload: " + req.body());

            Map<String, String> body;
            try {
                body = gson.fromJson(req.body(), Map.class);
            } catch (Exception e) {
                res.status(400);
                return gson.toJson(Map.of("error", "Invalid JSON body"));
            }

            if (body == null || !body.containsKey("username") || !body.containsKey("password")) {
                res.status(400);
                return gson.toJson(Map.of("error", "Missing username or password"));
            }

            String username = body.get("username").trim();
            String password = body.get("password").trim();

            String sql = "SELECT user_id, username, role, department FROM users WHERE username = ? AND password = ?";

            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, username);
                stmt.setString(2, password);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Map<String, Object> user = new HashMap<>();
                        user.put("id", rs.getInt("user_id"));
                        user.put("username", rs.getString("username"));
                        user.put("role", rs.getString("role"));
                        user.put("department", rs.getString("department"));

                        Map<String, Object> responseData = new HashMap<>();
                        responseData.put("success", true);
                        responseData.put("user", user);

                        System.out.println("✅ Login SUCCESS for user: " + username);
                        res.status(200);
                        return gson.toJson(responseData);
                    } else {
                        System.out.println("❌ Invalid Credentials for user: " + username);
                        res.status(401);
                        return gson.toJson(Map.of("error", "Invalid username or password"));
                    }
                }
            } catch (Exception e) {
                System.err.println("🔥 Database Error: " + e.getMessage());
                res.status(500);
                return gson.toJson(Map.of("error", "Database error: " + e.getMessage()));
            }
        });

        // ==========================================
        // 6. USERS ENDPOINTS (Admin / Accounts screens)
        // ==========================================
        get("/api/users", (req, res) -> {
            repository.UserRepository repo = new repository.UserRepository();
            return gson.toJson(repo.getAllUsers());
        });

        // ==========================================
        // 7. RESOURCES ENDPOINTS (Campus Bookings)
        // ==========================================
        get("/api/resources", (req, res) -> {
            repository.ResourceRepository repo = new repository.ResourceRepository();
            return gson.toJson(repo.getAllResources());
        });

        // ==========================================
        // 8. PURCHASE REQUESTS ENDPOINTS (HOD / Purchase / Stores)
        // ==========================================
        get("/api/purchase-requests", (req, res) -> {
            repository.PurchaseRequestRepository repo = new repository.PurchaseRequestRepository();
            return gson.toJson(repo.getAllPurchaseRequests());
        });

        // ==========================================
        // 9. PRODUCT REQUESTS ENDPOINTS
        // ==========================================
        get("/api/product-requests", (req, res) -> {
            repository.ProductRequestRepository repo = new repository.ProductRequestRepository();
            return gson.toJson(repo.getAllProductRequests());
        });

        // ==========================================
        // 10. PURCHASE ORDERS ENDPOINTS
        // ==========================================
        get("/api/purchase-orders", (req, res) -> {
            repository.PurchaseOrderRepository repo = new repository.PurchaseOrderRepository();
            return gson.toJson(repo.getAllPurchaseOrders());
        });
        // ==========================================
        // ROLE-SPECIFIC CONTROLLERS
        // ==========================================
        AdminController.register();
        HODController.register();
        StoresController.register();
        PurchaseController.register();
        AccountsController.register();
        System.out.println("🚀 CampusFlow API is running on http://192.168.1.6:4567");
    }
}