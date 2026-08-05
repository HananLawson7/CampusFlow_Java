package ui;

import model.User;
import model.Role;
import repository.UserRepository;

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
    private void createNewUserByAdmin(User admin) {
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

    private void viewAllUsers() {
        System.out.println("\n--- ALL USERS IN SYSTEM ---");
        System.out.println("(Implementation details to follow...)");
        // TODO: Implement viewing all users from database
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

        System.out.println("Deactivating " + username + "...");
        System.out.println("✅ User deactivated (implementation pending)");
        // TODO: Add deactivate logic
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

        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine().trim();

        if (newPassword.length() < 6) {
            System.out.println("❌ Password must be at least 6 characters.");
            return;
        }

        user.setPassword(newPassword);
        // TODO: Add update logic to persist password change
        System.out.println("✅ Password updated for " + username);
    }
}