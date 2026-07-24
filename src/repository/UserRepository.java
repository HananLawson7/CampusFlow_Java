package repository;

import model.User;
import model.Role;
import config.DatabaseConfig;
import java.sql.*;

public class UserRepository {

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
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

    // 🎯 Added the missing authenticate method to resolve the ConsoleApp error
    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
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
        String sql = "INSERT INTO users (username, password, role, department, year_of_study) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().name());
            stmt.setString(4, user.getDepartment());
            if (user.getYearOfStudy() != null) stmt.setInt(5, user.getYearOfStudy());
            else stmt.setNull(5, Types.INTEGER);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }
}