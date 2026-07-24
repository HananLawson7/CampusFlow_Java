package repository;

import model.User;
import model.Role;
import config.DatabaseConfig;
import java.sql.*;

public class UserRepository {

    public User findByUsername(String username) {
        // Extract year_of_study safely from the JSONB metadata column
        String sql = "SELECT user_id, username, password, role, department, " +
                "NULLIF(metadata->>'year_of_study', '')::int AS year_of_study " +
                "FROM users WHERE username = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(Role.valueOf(rs.getString("role")));
                    user.setDepartment(rs.getString("department"));
                    user.setYearOfStudy((Integer) rs.getObject("year_of_study"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error tracking user: " + e.getMessage());
        }
        return null;
    }

    public User authenticate(String username, String password) {
        // Same JSON extraction for authentication
        String sql = "SELECT user_id, username, password, role, department, " +
                "NULLIF(metadata->>'year_of_study', '')::int AS year_of_study " +
                "FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(Role.valueOf(rs.getString("role")));
                    user.setDepartment(rs.getString("department"));
                    user.setYearOfStudy((Integer) rs.getObject("year_of_study"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Authentication database error: " + e.getMessage());
        }
        return null;
    }

    public boolean saveUser(User user) {
        // Target the metadata column instead of a standalone year_of_study column
        String sql = "INSERT INTO users (username, password, role, department, metadata) VALUES (?, ?, ?, ?, ?::jsonb)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().name());
            stmt.setString(4, user.getDepartment());

            // Format year_of_study into a JSON string if present, otherwise store null or empty JSON
            String metadataJson = null;
            if (user.getYearOfStudy() != null) {
                metadataJson = "{\"year_of_study\": " + user.getYearOfStudy() + "}";
            } else {
                metadataJson = "{}"; // Empty JSON object for admins/faculty
            }
            stmt.setString(5, metadataJson);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }
}