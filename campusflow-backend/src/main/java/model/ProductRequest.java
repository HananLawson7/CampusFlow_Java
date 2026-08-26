package model;

import java.time.LocalDateTime;

/**
 * ProductRequest: HOD submits request for items from inventory
 * Workflow: HOD → Stores Department (checks stock)
 */
public class ProductRequest {
    private int requestId;
    private int hodId;
    private int resourceId;
    private int quantity;
    private LocalDateTime requestedDate;
    private String status; // PENDING, FULFILLED_FROM_STOCK, FORWARDED_TO_STORES
    private String notes;

    // Constructors
    public ProductRequest() {}

    public ProductRequest(int hodId, int resourceId, int quantity, String notes) {
        this.hodId = hodId;
        this.resourceId = resourceId;
        this.quantity = quantity;
        this.status = "PENDING";
        this.notes = notes;
    }

    // Getters & Setters
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public int getHodId() { return hodId; }
    public void setHodId(int hodId) { this.hodId = hodId; }

    public int getResourceId() { return resourceId; }
    public void setResourceId(int resourceId) { this.resourceId = resourceId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public LocalDateTime getRequestedDate() { return requestedDate; }
    public void setRequestedDate(LocalDateTime requestedDate) { this.requestedDate = requestedDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return String.format("[ID: %d] Resource#%d | Qty: %d | Status: %s | Date: %s",
                requestId, resourceId, quantity, status, requestedDate);
    }
}