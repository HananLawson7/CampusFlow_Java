package model;

import java.math.BigDecimal;

public class Resource {
    private int resourceId;
    private String name;
    private String type;
    private String category;
    private String location;
    private String department;
    private int quantityInStock;
    private BigDecimal unitPrice;
    private boolean requiresApproval;

    // Standard Getters and Setters
    public int getResourceId() { return resourceId; }
    public void setResourceId(int resourceId) { this.resourceId = resourceId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    // Methods matching BoardMembersMenu and ResourceRepository expectations
    public int getQuantity_in_stock() { return quantityInStock; }
    public void setQuantity_in_stock(int quantityInStock) { this.quantityInStock = quantityInStock; }

    public BigDecimal getUnit_price() { return unitPrice; }
    public void setUnit_price(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    // Supports both camelCase and snake_case method signatures for approval flags
    public boolean isRequiresApproval() { return requiresApproval; }
    public void setRequiresApproval(boolean requiresApproval) { this.requiresApproval = requiresApproval; }

    public boolean isRequires_approval() { return requiresApproval; }
    public void setRequires_approval(boolean requiresApproval) { this.requiresApproval = requiresApproval; }
}