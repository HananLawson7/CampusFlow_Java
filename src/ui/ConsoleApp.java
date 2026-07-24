package ui;

import model.User;
import model.Role;
import model.Resource;
import repository.UserRepository;
import repository.ResourceRepository;

import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserRepository userRepo = new UserRepository();
    private static final ResourceRepository resourceRepo = new ResourceRepository();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== 🔌 WELCOME TO CAMPUSFLOW ===");
            System.out.println("1. Login");
            System.out.println("2. Signup");
            System.out.println("3. Exit System");
            System.out.print("Select Option: ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) handleLogin();
            else if (choice.equals("2")) handleSignup();
            else if (choice.equals("3")) {
                System.out.println("✨ Exiting CampusFlow. Secure logout complete.");
                break;
            } else System.out.println("❌ Invalid selection.");
        }
    }

    private static void handleLogin() {
        System.out.println("\n--- 🔐 SYSTEM LOGIN ---");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        // System automatically searches and matches profile criteria
        User user = userRepo.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            System.out.println("✅ Access Granted! Role verified: " + user.getRole());
            loadDashboard(user);
        } else {
            System.out.println("❌ Invalid credentials. Anti-foul play protocol active.");
        }
    }

    private static void handleSignup() {
        System.out.println("\n--- 📝 SYSTEM REGISTRATION ---");
        System.out.print("Create Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Create Password: ");
        String password = scanner.nextLine().trim();

        System.out.println("Select Account Classification Type:");
        System.out.println("1. Student\n2. Faculty\n3. HOD");
        System.out.print("Selection: ");
        String roleChoice = scanner.nextLine().trim();

        Role selectedRole = Role.STUDENT;
        if (roleChoice.equals("2")) selectedRole = Role.FACULTY;
        else if (roleChoice.equals("3")) selectedRole = Role.HOD;

        System.out.print("Enter Your Academic Department: ");
        String department = scanner.nextLine().trim();

        Integer yearOfStudy = null;
        if (selectedRole == Role.STUDENT) {
            System.out.print("Enter Current Academic Year of Study (1-4): ");
            yearOfStudy = Integer.parseInt(scanner.nextLine().trim());
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole(selectedRole);
        newUser.setDepartment(department);
        newUser.setYearOfStudy(yearOfStudy);

        if (userRepo.saveUser(newUser)) {
            System.out.println("🎉 Account registered successfully! You can now login.");
        } else {
            System.out.println("❌ Registration failed. Username might already be taken.");
        }
    }

    private static void loadDashboard(User user) {
        while (true) {
            System.out.println("\n==================================================");
            System.out.println("🔑 USER SESSION: " + user.getUsername().toUpperCase() + " | DEPT: " + user.getDepartment().toUpperCase());
            System.out.println("==================================================");

            System.out.println("\n📍 ASSIGNED DEPARTMENT RESOURCES:");

            // 🎯 FIX 1: Changed findByDepartment to getResourcesByDepartment
            List<Resource> localResources = resourceRepo.getResourcesByDepartment(user.getDepartment());

            if (localResources.isEmpty()) {
                System.out.println("   (No resources indexed for your department yet)");
            } else {
                // 🎯 FIX 2: Swapped out "Capacity" columns to match your actual Type and Location fields
                System.out.printf("   %-5s %-30s %-15s %-15s %-15s\n", "ID", "Resource Name", "Type", "Location", "Approval Req.");
                for (Resource r : localResources) {
                    System.out.printf("   %-5d %-30s %-15s %-15s %-15b\n",
                            r.getResourceId(), r.getName(), r.getType(), r.getLocation(), r.isRequiresApproval());
                }
            }

            System.out.println("\n🛠️ AVAILABLE MANAGEMENT ACTIONS:");
            if (user.getRole() == Role.STUDENT) {
                System.out.println("1. Request Resource Booking\n2. View My Booking Slips\n3. Logout");
            } else if (user.getRole() == Role.FACULTY) {
                System.out.println("1. Fast-Track Resource Reservation\n2. View My Bookings\n3. Logout");
            } else if (user.getRole() == Role.HOD) {
                System.out.println("1. View Pending Department Requests Queue\n2. Review Actions\n3. Logout");
            }

            System.out.print("Select Action: ");
            String action = scanner.nextLine().trim();
            if (action.equals("3")) {
                System.out.println("🔒 Active session terminated securely.");
                break;
            }

            System.out.println("💡 Simulation Action selected. Processing logic path...");
            break;
        }
    }
}