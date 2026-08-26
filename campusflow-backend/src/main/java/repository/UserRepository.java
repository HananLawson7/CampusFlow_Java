package repository;

import config.DatabaseConfig;
import model.User;
import model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserRepository: CRUD operations for User model
 * Handles authentication, registration, and user management
 */
public class UserRepository {

    /**
     * Find user by exact username and password match
     * Used for login authentication
     */
    public User findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return parseUserRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by credentials: " + e.getMessage());
        }
        return null;
    }

    /**
     * Find user by username only
     * Used to check if username already exists during signup
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return parseUserRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by username: " + e.getMessage());
        }
        return null;
    }

    /**
     * Find user by ID
     */
    public User findById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return parseUserRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * 🛡️ SECURE: Save a new STUDENT user only
     * Role is always STUDENT, cannot be overridden
     * Used by public signup
     */
    public boolean saveStudentUser(User student) {
        // 🔒 Safety check: Ensure role is STUDENT
        if (student.getRole() != Role.STUDENT) {
            System.err.println("⚠️ Security: Only STUDENT accounts can self-register.");
            return false;
        }

        String sql = "INSERT INTO users (username, password, role, department, year_of_study) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getUsername());
            pstmt.setString(2, student.getPassword());
            pstmt.setString(3, Role.STUDENT.name());  // 🔒 Hardcoded
            pstmt.setString(4, student.getDepartment());
            pstmt.setInt(5, student.getYearOfStudy() != null ? student.getYearOfStudy() : 1);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error registering student: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🔑 ADMIN ONLY: Create users with any role
     * Should only be called from AdminMenu after permission check
     * Used by admin to create HOD, STORES, PURCHASE, ACCOUNTS, BOARD_MEMBERS, etc.
     */
    public boolean createUserByAdmin(User user, User adminCreator) {
        // 🔒 Permission check: Only ADMIN can create users
        if (adminCreator.getRole() != Role.ADMIN) {
            System.err.println("❌ Only ADMIN users can create new accounts.");
            return false;
        }

        String sql = "INSERT INTO users (username, password, role, department, year_of_study) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole().name());  // Can be ANY role
            pstmt.setString(4, user.getDepartment());
            pstmt.setInt(5, user.getYearOfStudy() != null ? user.getYearOfStudy() : 0);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Admin " + adminCreator.getUsername() + " created user: " + user.getUsername());
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Generic save method (backward compatible with existing code)
     * For general user creation
     */
    public boolean saveUser(User user) {
        String sql = "INSERT INTO users (username, password, role, department, year_of_study) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole().name());
            pstmt.setString(4, user.getDepartment());
            pstmt.setInt(5, user.getYearOfStudy() != null ? user.getYearOfStudy() : 0);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update user password
     * Used by admin password reset
     */
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all users (for admin dashboard)
     */
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users ORDER BY username";
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(parseUserRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Get users by role (e.g., all HODs, all STUDENTs)
     */
    public List<User> getUsersByRole(Role role) {
        String sql = "SELECT * FROM users WHERE role = ? ORDER BY username";
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, role.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(parseUserRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching users by role: " + e.getMessage());
        }
        return users;
    }

    /**
     * Get users by department
     */
    public List<User> getUsersByDepartment(String department) {
        String sql = "SELECT * FROM users WHERE department = ? ORDER BY username";
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, department);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(parseUserRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching users by department: " + e.getMessage());
        }
        return users;
    }

    /**
     * Delete user (for admin)
     */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    // ========== HELPER METHOD ==========

    /**
     * Parse ResultSet row into User object
     * Maps database columns to User properties
     */
    private User parseUserRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(Role.valueOf(rs.getString("role")));
        user.setDepartment(rs.getString("department"));

        // 🛡️ Handle nullable year_of_study column
        // Only STUDENT roles have year_of_study; others are NULL
        try {
            int year = rs.getInt("year_of_study");
            if (rs.wasNull()) {
                user.setYearOfStudy(null);  // NULL for non-students
            } else {
                user.setYearOfStudy(year);
            }
        } catch (SQLException e) {
            // Column might not exist, set to null
            user.setYearOfStudy(null);
            System.err.println("Warning: year_of_study column not found, defaulting to null");
        }

        return user;
    }
    public boolean deactivateUser(int userId) {
        String sql = "UPDATE users SET status = 'INACTIVE' WHERE user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deactivating user: " + e.getMessage());
            return false;
        }
    }
}