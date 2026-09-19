package service;

import model.Booking;
import repository.BookingRepository;
import java.util.List;

public class BookingService1 {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Handles administrative workflows to approve pending reservations.
     */
    public boolean approveBooking(int bookingId, int adminUserId) {
        // Business Rule validation: Ensure zero values or missing tracking references are rejected
        if (bookingId <= 0 || adminUserId <= 0) {
            return false;
        }

        // Abstraction placeholder to update tracking parameters inside PostgreSQL
        System.out.println("Processing approval for Booking #" + bookingId + " by Admin User #" + adminUserId);
        return bookingRepository.updateBookingStatus(bookingId, "APPROVED", adminUserId);
    }

    /**
     * Handles systemic cancellation flows.
     */
    public boolean cancelBooking(int bookingId) {
        if (bookingId <= 0) return false;
        return bookingRepository.updateBookingStatus(bookingId, "CANCELLED", null);
    }
}