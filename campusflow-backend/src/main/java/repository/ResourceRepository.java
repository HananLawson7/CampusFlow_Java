package repository;

import config.DatabaseConfig;
import model.Resource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ResourceRepository: CRUD for Resources (Inventory)
 * ⚠️ REPLACE your existing ResourceRepository with this updated version
 * Added methods for supply chain: deductInventory, getResourceById, getAllResources
 */
public class ResourceRepository {

    /**
     * Get resource by ID
     */
    public Resource getResourceById(int resourceId) {
        String sql = "SELECT * FROM resources WHERE resource_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, resourceId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return parseRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching resource by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get resources by department
     */
    public List<Resource> getResourcesByDepartment(String department) {
        String sql = "SELECT * FROM resources WHERE department = ? ORDER BY name ASC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, department);
            return parseResults(pstmt.executeQuery());
        } catch (SQLException e) {
            System.err.println("Error fetching resources by department: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Get all resources
     */
    public List<Resource> getAllResources() {
        String sql = "SELECT * FROM resources ORDER BY name ASC";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return parseResults(rs);
        } catch (SQLException e) {
            System.err.println("Error fetching all resources: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 🔑 SUPPLY CHAIN CRITICAL: Deduct inventory after fulfillment
     * Called by Stores when request is fulfilled from stock
     */
    public boolean deductInventory(int resourceId, int quantity) {
        String sql = "UPDATE resources SET quantity_in_stock = quantity_in_stock - ? " +
                "WHERE resource_id = ? AND quantity_in_stock >= ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantity);
            pstmt.setInt(2, resourceId);
            pstmt.setInt(3, quantity);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deducting inventory: " + e.getMessage());
            return false;
        }
    }

    /**
     * Add inventory (after purchase order is received/delivered)
     * Called when new stock arrives from vendor
     */
    public boolean addInventory(int resourceId, int quantity) {
        String sql = "UPDATE resources SET quantity_in_stock = quantity_in_stock + ? WHERE resource_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantity);
            pstmt.setInt(2, resourceId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding inventory: " + e.getMessage());
            return false;
        }
    }

    /**
     * Create a new resource (Admin/Manager only)
     */
    public boolean createResource(Resource resource) {
        String sql = "INSERT INTO resources (name, category, quantity_in_stock, unit_price, department, requires_approval) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, resource.getName());
            pstmt.setString(2, resource.getCategory());
            pstmt.setInt(3, resource.getQuantity_in_stock());
            pstmt.setBigDecimal(4, resource.getUnit_price());
            pstmt.setString(5, resource.getDepartment());
            pstmt.setBoolean(6, resource.isRequires_approval());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating resource: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update resource details
     */
    public boolean updateResource(Resource resource) {
        String sql = "UPDATE resources SET name = ?, category = ?, quantity_in_stock = ?, " +
                "unit_price = ?, department = ?, requires_approval = ? WHERE resource_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, resource.getName());
            pstmt.setString(2, resource.getCategory());
            pstmt.setInt(3, resource.getQuantity_in_stock());
            pstmt.setBigDecimal(4, resource.getUnit_price());
            pstmt.setString(5, resource.getDepartment());
            pstmt.setBoolean(6, resource.isRequires_approval());
            pstmt.setInt(7, resource.getResourceId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating resource: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete resource
     */
    public boolean deleteResource(int resourceId) {
        String sql = "DELETE FROM resources WHERE resource_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, resourceId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting resource: " + e.getMessage());
            return false;
        }
    }

    // ============ HELPER METHODS ============

    private List<Resource> parseResults(ResultSet rs) throws SQLException {
        List<Resource> resources = new ArrayList<>();
        while (rs.next()) {
            resources.add(parseRow(rs));
        }
        return resources;
    }

    private Resource parseRow(ResultSet rs) throws SQLException {
        Resource r = new Resource();
        r.setResourceId(rs.getInt("resource_id"));
        r.setName(rs.getString("name"));
        r.setCategory(rs.getString("category"));
        r.setQuantity_in_stock(rs.getInt("quantity_in_stock"));
        r.setUnit_price(rs.getBigDecimal("unit_price"));
        r.setDepartment(rs.getString("department"));
        r.setRequires_approval(rs.getBoolean("requires_approval"));
        return r;
    }
}