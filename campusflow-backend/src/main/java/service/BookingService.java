package service;

import repository.BookingRepository;
import java.util.UUID;

public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Handles administrative workflows to approve pending reservations using a UUID booking ID.
     */
    public boolean approveBooking(UUID bookingId, int adminUserId) {
        // Business Rule validation: Ensure reference parameters are present
        if (bookingId == null || adminUserId <= 0) {
            return false;
        }

        System.out.println("Processing approval for Booking #" + bookingId + " by Admin User #" + adminUserId);
        return bookingRepository.updateBookingStatus(bookingId, "APPROVED", adminUserId);
    }

    /**
     * Handles systemic cancellation flows using a UUID booking ID.
     */
    public boolean cancelBooking(UUID bookingId) {
        if (bookingId == null) return false;
        return bookingRepository.updateBookingStatus(bookingId, "CANCELLED", null);
    }
}