package config;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConfig {
    private static final String URL = getSetting(
            "CAMPUSFLOW_DB_URL",
            "jdbc:postgresql://localhost:5432/campus_flow"
    );
    private static final String USER = getSetting("CAMPUSFLOW_DB_USER", "postgres");
    private static final String PASSWORD = getSetting("CAMPUSFLOW_DB_PASSWORD", "LawsonDB");

    private DatabaseConfig() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC driver is not available.", e);
        }

        Properties props = new Properties();
        props.setProperty("user", USER);
        props.setProperty("password", PASSWORD);
        props.setProperty("connectTimeout", "5"); // Fail after 5 seconds if DB unreachable
        props.setProperty("socketTimeout", "5");  // Fail after 5 seconds if DB stops responding

        return DriverManager.getConnection(URL, props);
    }

    public static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            InputStream is = DatabaseConfig.class.getClassLoader().getResourceAsStream("schema.sql");
            if (is != null) {
                String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                stmt.execute(sql);
                System.out.println("⚡ Database schema & seed data initialized successfully!");
            } else {
                System.err.println("❌ Could not find schema.sql in src/main/resources!");
            }

        } catch (Exception e) {
            System.err.println("⚠️ Failed to initialize schema: " + e.getMessage());
        }
    }

    private static String getSetting(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}