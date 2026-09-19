package repository;

import config.DatabaseConfig;
import model.PurchaseRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PurchaseRequestRepository: CRUD for Purchase Requests
 * Stores generates PRs when stock is low → Purchase creates POs
 */
public class PurchaseRequestRepository {

    /**
     * Create a new purchase request (triggered by Stores when stock low)
     */
    public boolean createPR(PurchaseRequest pr) {
        String sql = "INSERT INTO purchase_requests (request_id, resource_id, quantity, status, notes) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, pr.getRequestId());
            pstmt.setInt(2, pr.getResourceId());
            pstmt.setInt(3, pr.getQuantity());
            pstmt.setString(4, pr.getStatus());
            pstmt.setString(5, pr.getNotes());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating purchase request: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all PENDING_PO requests (for Purchase to create POs)
     */
    public List<PurchaseRequest> getPendingPRs() {
        String sql = "SELECT * FROM purchase_requests WHERE status = 'PENDING_PO' ORDER BY created_date ASC";
        return executeQuery(sql);
    }

    /**
     * Get PR by ID
     */
    public PurchaseRequest getPRById(int prId) {
        String sql = "SELECT * FROM purchase_requests WHERE pr_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, prId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return parseRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching PR by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get PRs linked to a specific ProductRequest
     */
    public List<PurchaseRequest> getPRsByProductRequest(int requestId) {
        String sql = "SELECT * FROM purchase_requests WHERE request_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, requestId);
            return parseResults(pstmt.executeQuery());
        } catch (SQLException e) {
            System.err.println("Error fetching PRs for product request: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Update PR status (PENDING_PO → PO_ISSUED)
     */
    public boolean updatePRStatus(int prId, String newStatus) {
        String sql = "UPDATE purchase_requests SET status = ? WHERE pr_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, prId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating PR status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all PRs (for dashboard/reporting)
     */
    public List<PurchaseRequest> getAllPRs() {
        String sql = "SELECT * FROM purchase_requests ORDER BY created_date DESC";
        return executeQuery(sql);
    }

    public java.util.List<model.ProductRequest> getAllPurchaseRequests() {
        java.util.List<model.ProductRequest> requests = new java.util.ArrayList<>();
        // Adjust table name if your purchase requests are stored in a table named 'purchase_requests' or similar
        String query = "SELECT request_id, resource_id, quantity, notes FROM purchase_requests";

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
            System.err.println("Error fetching purchase requests: " + e.getMessage());
        }
        return requests;
    }

    // ============ HELPER METHODS ============

    private List<PurchaseRequest> executeQuery(String sql) {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return parseResults(rs);
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<PurchaseRequest> parseResults(ResultSet rs) throws SQLException {
        List<PurchaseRequest> prs = new ArrayList<>();
        while (rs.next()) {
            prs.add(parseRow(rs));
        }
        return prs;
    }

    private PurchaseRequest parseRow(ResultSet rs) throws SQLException {
        PurchaseRequest pr = new PurchaseRequest();
        pr.setPrId(rs.getInt("pr_id"));
        pr.setRequestId(rs.getInt("request_id"));
        pr.setResourceId(rs.getInt("resource_id"));
        pr.setQuantity(rs.getInt("quantity"));
        pr.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
        pr.setStatus(rs.getString("status"));
        pr.setNotes(rs.getString("notes"));
        return pr;
    }
}