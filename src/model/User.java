package model;

public class User {
    private int userId;
    private String username;
    private String password;
    private Role role;
    private String department;
    private Integer yearOfStudy;

    // Standard Getters and Setters (CamelCase)
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    // Compatibility aliases for snake_case method calls (e.g., in HODMenu)
    public int getUser_id() { return userId; }
    public void setUser_id(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Integer getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; }
}