package repository;

import config.DatabaseConfig;
import model.ProductRequest;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductRequestRepository: CRUD for HOD requests
 * HODs submit requests → Stores reviews and processes
 */
public class ProductRequestRepository {

    /**
     * Create a new product request from HOD
     */
    public boolean createRequest(ProductRequest request) {
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
     * Get all PENDING requests (for Stores review)
     */
    public List<ProductRequest> getPendingRequests() {
        String sql = "SELECT * FROM product_requests WHERE status = 'PENDING' ORDER BY requested_date ASC";
        return executeQuery(sql);
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
     * Get all requests (for admin/dashboard overview)
     */
    public List<ProductRequest> getAllRequests() {
        String sql = "SELECT * FROM product_requests ORDER BY requested_date DESC";
        return executeQuery(sql);
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