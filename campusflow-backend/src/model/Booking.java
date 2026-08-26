package model;

import java.time.LocalDateTime;
import java.util.UUID; // 🛡️ Added standard Java UUID support

public class Booking {
    private UUID bookingId;      // 🎯 Changed from int to UUID for PostgreSQL 18 compatibility
    private int resourceId;
    private int userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;       // PENDING, APPROVED, REJECTED, CANCELLED
    private Integer approvedBy;  // Uses Integer instead of int to allow for null values

    // 🎯 Constructor updated to accept java.util.UUID
    public Booking(UUID bookingId, int resourceId, int userId, LocalDateTime startTime, LocalDateTime endTime, String status, Integer approvedBy) {
        this.bookingId = bookingId;
        this.resourceId = resourceId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.approvedBy = approvedBy;
    }

    // 🎯 Getter and Setter updated to handle UUID objects
    public UUID getBookingId() { return bookingId; }
    public void setBookingId(UUID bookingId) { this.bookingId = bookingId; }

    public int getResourceId() { return resourceId; }
    public void setResourceId(int resourceId) { this.resourceId = resourceId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Integer approvedBy) { this.approvedBy = approvedBy; }
}