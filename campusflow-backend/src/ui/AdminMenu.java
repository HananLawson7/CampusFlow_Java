package ui;

import model.User;
import model.Role;
import repository.UserRepository;
import java.util.List;

import java.util.Scanner;

public class AdminMenu {
    private final Scanner scanner;
    private final UserRepository userRepo;

    public AdminMenu(Scanner scanner) {
        this.scanner = scanner;
        this.userRepo = new UserRepository();
    }

    public void display(User admin) {
        boolean running = true;

        while (running) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("🛡️ ADMIN MENU - System Administration");
            System.out.println("=".repeat(60));
            System.out.println("1. Create New User (Any Role)");
            System.out.println("2. View All Users");
            System.out.println("3. Deactivate User");
            System.out.println("4. Reset User Password");
            System.out.println("5. Logout");
            System.out.print("Select Option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> createNewUserByAdmin(admin);
                case "2" -> viewAllUsers();
                case "3" -> deactivateUser(admin);
                case "4" -> resetUserPassword(admin);
                case "5" -> {
                    System.out.println("✅ Admin logged out.");
                    running = false;
                }
                default -> System.out.println("❌ Invalid option.");
            }
        }
    }

    /**
     * 🔑 ADMIN ONLY: Create users with any role
     * This is the ONLY way to create HOD, STORES, PURCHASE, etc. accounts
     */
    /*private void createNewUserByAdmin(User admin) {
        System.out.println("\n--- CREATE NEW USER (Admin Only) ---");

        System.out.print("Enter Username: ");
        String username = scanner.nextLine().trim();

        // Check if username exists
        if (userRepo.findByUsername(username) != null) {
            System.out.println("❌ Username already taken.");
            return;
        }

        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        if (password.length() < 6) {
            System.out.println("❌ Password must be at least 6 characters.");
            return;
        }

        System.out.print("Enter Department: ");
        String department = scanner.nextLine().trim();

        System.out.println("\nSelect Role:");
        System.out.println("1. ADMIN");
        System.out.println("2. HOD");
        System.out.println("3. FACULTY");
        System.out.println("4. STUDENT");
        System.out.println("5. STORES");
        System.out.println("6. PURCHASE");
        System.out.println("7. ACCOUNTS");
        System.out.println("8. BOARD_MEMBERS");
        System.out.print("Select Role (1-8): ");

        Role selectedRole = Role.STUDENT; // Default
        String roleChoice = scanner.nextLine().trim();

        switch (roleChoice) {
            case "1" -> selectedRole = Role.ADMIN;
            case "2" -> selectedRole = Role.HOD;
            case "3" -> selectedRole = Role.FACULTY;
            case "4" -> selectedRole = Role.STUDENT;
            case "5" -> selectedRole = Role.STORES;
            case "6" -> selectedRole = Role.PURCHASE;
            case "7" -> selectedRole = Role.ACCOUNTS;
            case "8" -> selectedRole = Role.BOARD_MEMBERS;
            default -> System.out.println("⚠️ Invalid role. Defaulting to STUDENT.");
        }

        Integer yearOfStudy = null;
        if (selectedRole == Role.STUDENT) {
            System.out.print("Enter Year of Study (1-4): ");
            try {
                yearOfStudy = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                yearOfStudy = 1;
            }
        }

        // Create user
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole(selectedRole);
        newUser.setDepartment(department);
        newUser.setYearOfStudy(yearOfStudy);

        // Save via admin-only method
        if (userRepo.createUserByAdmin(newUser, admin)) {
            System.out.println("\n✅ User created successfully!");
            System.out.println("   Username: " + username);
            System.out.println("   Role: " + selectedRole);
            System.out.println("   Department: " + department);
        } else {
            System.out.println("❌ Failed to create user.");
        }
    }
    */
    private void createNewUserByAdmin(User admin) {
        System.out.println("\n--- CREATE NEW USER ---");

        // Step 1: Choose Role
        System.out.println("\nSelect Role:");
        System.out.println("1. ADMIN");
        System.out.println("2. HOD");
        System.out.println("3. FACULTY");
        System.out.println("4. STUDENT");
        System.out.println("5. STORES");
        System.out.println("6. PURCHASE");
        System.out.println("7. ACCOUNTS");
        System.out.println("8. BOARD_MEMBERS");
        System.out.print("Select (1-8): ");

        Role selectedRole = Role.STUDENT;
        String roleChoice = scanner.nextLine().trim();

        switch (roleChoice) {
            case "1" -> selectedRole = Role.ADMIN;
            case "2" -> selectedRole = Role.HOD;
            case "3" -> selectedRole = Role.FACULTY;
            case "4" -> selectedRole = Role.STUDENT;
            case "5" -> selectedRole = Role.STORES;
            case "6" -> selectedRole = Role.PURCHASE;
            case "7" -> selectedRole = Role.ACCOUNTS;
            case "8" -> selectedRole = Role.BOARD_MEMBERS;
            default -> {
                System.out.println("❌ Invalid selection.");
                return;
            }
        }

        // Step 2: Show selected role and get credentials
        System.out.println("\n🔑 ROLE: " + selectedRole);
        System.out.println("-".repeat(40));

        System.out.print("Enter Username: ");
        String username = scanner.nextLine().trim();

        if (userRepo.findByUsername(username) != null) {
            System.out.println("❌ Username already exists.");
            return;
        }

        System.out.print("Enter Password (min 6 chars): ");
        String password = scanner.nextLine().trim();

        if (password.length() < 6) {
            System.out.println("❌ Password must be at least 6 characters.");
            return;
        }

        System.out.print("Enter Department: ");
        String department = scanner.nextLine().trim();

        if (department.isEmpty()) {
            System.out.println("❌ Department cannot be empty.");
            return;
        }

        // Step 3: Only ask for year_of_study if STUDENT
        Integer yearOfStudy = null;
        if (selectedRole == Role.STUDENT) {
            System.out.print("Enter Year of Study (1-4): ");
            try {
                yearOfStudy = Integer.parseInt(scanner.nextLine().trim());
                if (yearOfStudy < 1 || yearOfStudy > 4) {
                    System.out.println("❌ Year must be between 1 and 4.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Year must be a number.");
                return;
            }
        }

        // Step 4: Create and save user
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole(selectedRole);
        newUser.setDepartment(department);
        newUser.setYearOfStudy(yearOfStudy);

        if (userRepo.createUserByAdmin(newUser, admin)) {
            System.out.println("\n✅ User created successfully!");
            System.out.println("   Role: " + selectedRole);
            System.out.println("   Username: " + username);
            System.out.println("   Department: " + department);
            if (yearOfStudy != null) {
                System.out.println("   Year of Study: " + yearOfStudy);
            }
        } else {
            System.out.println("❌ Failed to create user.");
        }
    }
    private void viewAllUsers() {
        System.out.println("\n--- ALL USERS IN SYSTEM ---");
        List<User> users = userRepo.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.printf("%-8s %-15s %-12s %-15s %-20s %-5s\n",
                "ID", "Username", "Role", "Department", "Status", "Year");
        System.out.println("-".repeat(100));

        for (User u : users) {
            String year = u.getYearOfStudy() != null ? u.getYearOfStudy().toString() : "-";
            System.out.printf("%-8d %-15s %-12s %-15s %-20s %-5s\n",
                    u.getUserId(), u.getUsername(), u.getRole(),
                    u.getDepartment(), "ACTIVE", year);
        }
    }

    private void deactivateUser(User admin) {
        System.out.println("\n--- DEACTIVATE USER ---");
        System.out.print("Enter username to deactivate: ");
        String username = scanner.nextLine().trim();

        User user = userRepo.findByUsername(username);
        if (user == null) {
            System.out.println("❌ User not found.");
            return;
        }

        if (userRepo.deactivateUser(user.getUserId())) {
            System.out.println("✅ User deactivated: " + username);
        } else {
            System.out.println("❌ Failed to deactivate user.");
        }
    }

    private void resetUserPassword(User admin) {
        System.out.println("\n--- RESET USER PASSWORD ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        User user = userRepo.findByUsername(username);
        if (user == null) {
            System.out.println("❌ User not found.");
            return;
        }

        System.out.print("Enter new password (min 6 chars): ");
        String newPassword = scanner.nextLine().trim();

        if (newPassword.length() < 6) {
            System.out.println("❌ Password must be at least 6 characters.");
            return;
        }

        if (userRepo.updatePassword(user.getUserId(), newPassword)) {
            System.out.println("✅ Password reset for: " + username);
        } else {
            System.out.println("❌ Failed to reset password.");
        }
    }
}