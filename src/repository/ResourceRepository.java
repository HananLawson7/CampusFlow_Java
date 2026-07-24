package repository;

import config.DatabaseConfig;
import model.Resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResourceRepository {

    /**
     * Fetches all resources from the database.
     */
    public List<Resource> getAllResources() {
        List<Resource> resources = new ArrayList<>();
        String query = "SELECT * FROM resources";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Resource res = new Resource();
                res.setResourceId(rs.getInt("resource_id"));
                res.setName(rs.getString("name"));
                res.setType(rs.getString("type"));
                res.setLocation(rs.getString("location"));
                res.setDepartment(rs.getString("department")); // 🎯 Capture the department
                res.setRequiresApproval(rs.getBoolean("requires_approval"));

                resources.add(res);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all resources: " + e.getMessage());
        }
        return resources;
    }

    /**
     * 🎯 NEW: Natively fetches only the resources belonging to a specific department.
     * This avoids pulling unnecessary data into memory.
     */
    public List<Resource> getResourcesByDepartment(String department) {
        List<Resource> resources = new ArrayList<>();
        String query = "SELECT * FROM resources WHERE department = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, department);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Resource res = new Resource();
                    res.setResourceId(rs.getInt("resource_id"));
                    res.setName(rs.getString("name"));
                    res.setType(rs.getString("type"));
                    res.setLocation(rs.getString("location"));
                    res.setDepartment(rs.getString("department"));
                    res.setRequiresApproval(rs.getBoolean("requires_approval"));

                    resources.add(res);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching department resources: " + e.getMessage());
        }
        return resources;
    }
}