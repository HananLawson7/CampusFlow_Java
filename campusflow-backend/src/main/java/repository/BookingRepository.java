package repository;

import config.DatabaseConfig;
import model.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BookingRepository {

    /**
     * Attempts to save a new booking into the PostgreSQL database.
     * @return true if successfully saved, false if a double-booking conflict is detected.
     */
    public boolean saveBooking(Booking booking) {
        // Construct the row insert string using explicit PostgreSQL native tsrange formatting
        String query = "INSERT INTO bookings (resource_id, user_id, booking_period, status) " +
                "VALUES (?, ?, tsrange(?, ?), ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, booking.getResourceId());
            stmt.setInt(2, booking.getUserId());

            // Map our local date structures to standard SQL timestamps
            stmt.setTimestamp(3, Timestamp.valueOf(booking.getStartTime()));
            stmt.setTimestamp(4, Timestamp.valueOf(booking.getEndTime()));

            stmt.setString(5, booking.getStatus());

            stmt.executeUpdate();
            return true; // Successfully reserved!

        } catch (SQLException e) {
            // '23P01' is the strict PostgreSQL error code for an Exclusion Constraint violation
            if ("23P01".equals(e.getSQLState())) {
                System.out.println("\n🛑 [SCHEDULING CONFLICT]: The target asset is already reserved during this window!");
                return false;
            }

            System.err.println("Database transmission processing error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateBookingStatus(int bookingId, String status, Integer approvedBy) {
        String query = "UPDATE bookings SET status = ?, approved_by = ? WHERE booking_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            if (approvedBy != null) {
                stmt.setInt(2, approvedBy);
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            stmt.setInt(3, bookingId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating booking status: " + e.getMessage());
            return false;
        }
    }
}