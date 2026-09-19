package repository;

import config.DatabaseConfig;
import model.ProductRequest;
import model.PurchaseRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ProductRequestRepository: CRUD for HOD requests
 * HODs submit requests → Stores reviews and processes
 */
public class ProductRequestRepository {

    /**
     * Create a new product request from HOD
     */
    public boolean createRequest(ProductRequest request) {
        // Check resource exists first
        String checkSql = "SELECT 1 FROM resources WHERE resource_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, request.getResourceId());
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                System.err.println("Resource not found: " + request.getResourceId());
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error checking resource: " + e.getMessage());
            return false;
        }

        String sql = "INSERT INTO product_requests (hod_id, resource_id, quantity, status, notes) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, request.getHodId());
            pstmt.setInt(2, request.getResourceId());
            pstmt.setInt(3, request.getQuantity());
            pstmt.setString(4, request.getStatus());
            pstmt.setString(5, request.getNotes());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating product request: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all PENDING requests (for Stores review) — includes resource name + current stock
     * so the frontend can decide whether to show "Process" or "Forward to Purchase"
     */
    public List<Map<String, Object>> getPendingRequests() {
        String sql = "SELECT pr.request_id, pr.hod_id, pr.resource_id, pr.quantity, pr.status, pr.notes, " +
                "r.name as resource_name, r.quantity_in_stock " +
                "FROM product_requests pr " +
                "JOIN resources r ON r.resource_id = pr.resource_id " +
                "WHERE pr.status = 'PENDING' " +
                "ORDER BY pr.requested_date ASC";

        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("requestId", rs.getInt("request_id"));
                row.put("hodId", rs.getInt("hod_id"));
                row.put("resourceId", rs.getInt("resource_id"));
                row.put("resourceName", rs.getString("resource_name"));
                row.put("quantity", rs.getInt("quantity"));
                row.put("quantityInStock", rs.getInt("quantity_in_stock"));
                row.put("status", rs.getString("status"));
                row.put("notes", rs.getString("notes"));
                results.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching pending requests: " + e.getMessage());
        }
        return results;
    }

    /**
     * Get requests by HOD ID
     */
    public List<ProductRequest> getRequestsByHod(int hodId) {
        String sql = "SELECT * FROM product_requests WHERE hod_id = ? ORDER BY requested_date DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, hodId);
            return parseResults(pstmt.executeQuery());
        } catch (SQLException e) {
            System.err.println("Error fetching requests for HOD: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Get request by ID
     */
    public ProductRequest getRequestById(int requestId) {
        String sql = "SELECT * FROM product_requests WHERE request_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, requestId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return parseRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching request by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Update request status (e.g., PENDING → FULFILLED_FROM_STOCK or FORWARDED_TO_STORES)
     */
    public boolean updateStatus(int requestId, String newStatus) {
        String sql = "UPDATE product_requests SET status = ? WHERE request_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, requestId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating request status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Fulfill a HOD request from stock or forward it to Purchase when stock is insufficient.
     */
    public String processRequest(int requestId, ResourceRepository resourceRepository) {
        ProductRequest request = getRequestById(requestId);
        if (request == null) {
            return "NOT_FOUND";
        }

        if (resourceRepository.deductInventory(request.getResourceId(), request.getQuantity())) {
            return updateStatus(requestId, "FULFILLED_FROM_STOCK")
                    ? "FULFILLED_FROM_STOCK"
                    : "FAILED";
        }

        PurchaseRequestRepository purchaseRequestRepository = new PurchaseRequestRepository();
        if (!purchaseRequestRepository.getPRsByProductRequest(requestId).isEmpty()) {
            return "ALREADY_FORWARDED";
        }

        PurchaseRequest purchaseRequest = new PurchaseRequest(
                requestId,
                request.getResourceId(),
                request.getQuantity(),
                request.getNotes()
        );
        if (!purchaseRequestRepository.createPR(purchaseRequest)) {
            return "FAILED";
        }

        return updateStatus(requestId, "FORWARDED_TO_PURCHASE")
                ? "FORWARDED_TO_PURCHASE"
                : "FAILED";
    }

    /**
     * Get all requests (for admin/dashboard overview)
     */
    public List<ProductRequest> getAllRequests() {
        String sql = "SELECT * FROM product_requests ORDER BY requested_date DESC";
        return executeQuery(sql);
    }

    public java.util.List<model.ProductRequest> getAllProductRequests() {
        java.util.List<model.ProductRequest> requests = new java.util.ArrayList<>();
        String query = "SELECT * FROM product_requests";

        try (java.sql.Connection conn = config.DatabaseConfig.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                model.ProductRequest req = new model.ProductRequest(
                        rs.getInt("request_id"),
                        rs.getInt("resource_id"),
                        rs.getInt("quantity"),
                        rs.getString("notes")
                );
                requests.add(req);
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error fetching product requests: " + e.getMessage());
        }
        return requests;
    }

    // ============ HELPER METHODS ============

    private List<ProductRequest> executeQuery(String sql) {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return parseResults(rs);
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<ProductRequest> parseResults(ResultSet rs) throws SQLException {
        List<ProductRequest> requests = new ArrayList<>();
        while (rs.next()) {
            requests.add(parseRow(rs));
        }
        return requests;
    }

    private ProductRequest parseRow(ResultSet rs) throws SQLException {
        ProductRequest req = new ProductRequest();
        req.setRequestId(rs.getInt("request_id"));
        req.setHodId(rs.getInt("hod_id"));
        req.setResourceId(rs.getInt("resource_id"));
        req.setQuantity(rs.getInt("quantity"));
        req.setRequestedDate(rs.getTimestamp("requested_date").toLocalDateTime());
        req.setStatus(rs.getString("status"));
        req.setNotes(rs.getString("notes"));
        return req;
    }
}