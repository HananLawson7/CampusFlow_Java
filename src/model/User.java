package model;

public class User {
    private int userId;
    private String username;
    private String password;
    private Role role; // Uses the Role enum we just made
    private String department;

    public User(int userId, String username, String password, Role role, String department) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.department = department;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public String getDepartment() { return department; }
}