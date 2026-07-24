package dao;

import model.Booking;
import config.DatabaseConfig;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class BookingDAO {

    /**
     * Updates booking status using Postgres 18's OLD and NEW modifier capabilities.
     */
    public void updateBookingStatus(UUID bookingId, String newStatus, Integer adminId) {
        // Postgres 18 lets us target both OLD (pre-update) and NEW (post-update) state data natively!
        String sql = "UPDATE bookings SET status = ?, approved_by = ? WHERE booking_id = ? " +
                "RETURNING OLD.status AS old_state, NEW.status AS new_state;";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            if (adminId != null) {
                stmt.setInt(2, adminId);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setObject(3, bookingId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String oldState = rs.getString("old_state");
                    String newState = rs.getString("new_state");

                    System.out.println("⚡ [Postgres 18 Audit] State successfully modified!");
                    System.out.println("   Transitioned From: " + oldState + " -> To: " + newState);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Natively inserts a new room reservation into the database.
     */
    public void saveBooking(Booking booking) {
        // Let the database implicitly handle the uuidv7 generation via table default settings
        String sql = "INSERT INTO bookings (resource_id, user_id, booking_period, status) " +
                "VALUES (?, ?, tsrange(?, ?), ?) RETURNING booking_id;";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, booking.getResourceId());
            stmt.setInt(2, booking.getUserId());
            stmt.setTimestamp(3, Timestamp.valueOf(booking.getStartTime()));
            stmt.setTimestamp(4, Timestamp.valueOf(booking.getEndTime()));
            stmt.setString(5, booking.getStatus());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UUID generatedId = (UUID) rs.getObject("booking_id");
                    booking.setBookingId(generatedId);
                    System.out.println("✅ Booking logged successfully with Time-Sorted UUIDv7: " + generatedId);
                }
            }
        } catch (SQLException e) {
            // Catches the native GIST constraint rejection if rooms overlap
            if ("23505".equals(e.getSQLState()) || e.getMessage().contains("overlapping")) {
                System.out.println("❌ Database Core Rule Conflict: The selected room is already reserved at that time!");
            } else {
                e.printStackTrace();
            }
        }
    }
}