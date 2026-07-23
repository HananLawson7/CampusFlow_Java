package model;

public class Resource {
    private int resourceId;
    private String name;
    private String type; // CLASSROOM, LABORATORY, PROJECTOR, etc.
    private String location;
    private boolean requiresApproval;

    public Resource(int resourceId, String name, String type, String location, boolean requiresApproval) {
        this.resourceId = resourceId;
        this.name = name;
        this.type = type;
        this.location = location;
        this.requiresApproval = requiresApproval;
    }

    // Getters
    public int getResourceId() { return resourceId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public boolean isRequiresApproval() { return requiresApproval; }
}