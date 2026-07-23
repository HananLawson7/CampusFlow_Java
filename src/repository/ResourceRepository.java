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

    public List<Resource> getAllResources() {
        List<Resource> resources = new ArrayList<>();
        String query = "SELECT * FROM resources";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                resources.add(new Resource(
                        rs.getInt("resource_id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("location"),
                        rs.getBoolean("requires_approval")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching resources: " + e.getMessage());
        }
        return resources;
    }
}