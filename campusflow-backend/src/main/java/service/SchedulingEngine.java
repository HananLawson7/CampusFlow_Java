package service;

import model.User;
import model.Resource;
import model.Booking;
import repository.BookingRepository;
import java.time.LocalDateTime;

public class SchedulingEngine1 {
    private final BookingRepository bookingRepository;

    public SchedulingEngine(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Attempts to book a campus resource using advanced DB exclusion validation.
     */
    public boolean bookResource(User user, Resource resource, LocalDateTime startTime, LocalDateTime endTime) {
        // 1. Core Security & Input Validation
        if (user == null || resource == null || startTime == null || endTime == null) {
            System.err.println("⚠️ Scheduling Failed: Invalid booking parameters.");
            return false;
        }

        // 2. Approval Restriction Check
        if (resource.isRequiresApproval() && !user.getRole().toString().equals("FACULTY")) {
            System.out.println("❌ Access Denied: " + user.getUsername() + " does not have permission to instantly book " + resource.getName() + " without administrative approval.");
            return false;
        }

        // 3. Determine the baseline status based on resource configuration
        String baselineStatus = resource.isRequiresApproval() ? "PENDING" : "APPROVED";

        // 4. Construct the Booking object matching all 7 expected parameters
        Booking newBooking = new Booking(
                null,                       // 🎯 bookingId: null tells the system the PostgreSQL 18 uuidv7 engine will generate it
                resource.getResourceId(),   // resourceId
                user.getUserId(),           // userId
                startTime,                  // startTime
                endTime,                    // endTime
                baselineStatus,             // status
                null                        // approvedBy: null because it hasn't been reviewed yet
        );

        // 5. Delegate to the Repository
        boolean success = bookingRepository.saveBooking(newBooking);

        if (success) {
            System.out.println("✅ Success! " + resource.getName() + " successfully scheduled from " + startTime + " to " + endTime);
        }
        return success;
    }
}