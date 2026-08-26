package ui;

import model.User;
import model.Role;
import repository.UserRepository;
import util.Validation;

import java.util.Scanner;

/**
 * SECURE ConsoleApp with Role Selection
 * Flow:
 *   1. Login → Choose Role
 *   2. Enter Username & Password
 *   3. Verify credentials for that role
 *   4. Route to role-specific menu
 */
public class ConsoleApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserRepository userRepo = new UserRepository();
    private boolean isRunning;

    public ConsoleApp() {
        this.isRunning = true;
    }

    public static void main(String[] args) {
        ConsoleApp app = new ConsoleApp();
        app.start();
    }

    public void start() {
        while (isRunning) {
            displayMainMenu();
        }
        System.out.println("\n✨ Thank you for using CampusFlow. Goodbye!");
    }

    /**
     * Main menu: Login, Signup, or Exit
     */
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(" 🔌 WELCOME TO CAMPUSFLOW - RESOURCE MANAGEMENT");
        System.out.println("=".repeat(50));
        System.out.println("1. Login");
        System.out.println("2. Signup as Student");
        System.out.println("3. Exit Application");
        System.out.print("Please select an option: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> handleLogin();
            case "2" -> handleSignup();
            case "3" -> {
                System.out.println("\n✨ Exiting CampusFlow. Secure logout complete.");
                isRunning = false;
            }
            default -> System.out.println("⚠️ Invalid input. Please enter 1, 2, or 3.");
        }
    }

    /**
     * LOGIN: Role selection → Username/Password entry → Authentication

    private void    handleLogin() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔐 USER LOGIN");
        System.out.println("=".repeat(60));

        // Step 1: Choose Role
        Role selectedRole = selectRole();
        if (selectedRole == null) {
            return; // User cancelled
        }

        // Step 2: Enter Credentials
        System.out.println("\n--- Login as " + selectedRole + " ---");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        // 🛡️ VALIDATION
        if (!Validation.isNotEmpty(username) || !Validation.isNotEmpty(password)) {
            System.out.println("⚠️ Fields cannot be empty.");
            return;
        }

        if (!Validation.isValidUsername(username)) {
            System.out.println("⚠️ Invalid username format.");
            return;
        }

        if (!Validation.isValidPassword(password)) {
            System.out.println("⚠️ Password does not meet requirements.");
            return;
        }

        // Step 3: Authenticate
        System.out.println("\n🚀 Authenticating...");
        User authenticatedUser = userRepo.findByUsernameAndPassword(username, password);

        if (authenticatedUser != null && authenticatedUser.getRole() == selectedRole) {
            // ✅ LOGIN SUCCESSFUL
            System.out.println("✅ Access Granted!");
            System.out.println("   Welcome, " + authenticatedUser.getUsername() + "!");
            System.out.println("   Role: " + selectedRole);

            // Route to role-specific menu
            routeToRoleMenu(authenticatedUser);
        } else {
            // ❌ LOGIN FAILED
            System.out.println("❌ Authentication failed!");
            if (authenticatedUser != null) {
                System.out.println("   Note: Your role (" + authenticatedUser.getRole() + ") doesn't match selected role (" + selectedRole + ")");
            } else {
                System.out.println("   Invalid username/password combination.");
            }
        }
    }
     */

    private void handleLogin() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔐 USER LOGIN");
        System.out.println("=".repeat(60));

        // Step 1: Choose Role
        Role selectedRole = selectRole();
        if (selectedRole == null) {
            return; // User cancelled
        }

        // Step 2: Enter Credentials (with retry for same role)
        int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            System.out.println("\n--- Login as " + selectedRole + " (Attempt " + attempt + "/" + maxAttempts + ") ---");
            System.out.print("Enter Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Enter Password: ");
            String password = scanner.nextLine().trim();

            System.out.println("\n🚀 Authenticating...");
            User authenticatedUser = userRepo.findByUsernameAndPassword(username, password);

            if (authenticatedUser != null && authenticatedUser.getRole() == selectedRole) {
                // ✅ LOGIN SUCCESSFUL
                System.out.println("✅ Access Granted!");
                System.out.println("   Welcome, " + authenticatedUser.getUsername() + "!");
                System.out.println("   Role: " + selectedRole);

                // Route to role-specific menu
                routeToRoleMenu(authenticatedUser);
                return;  // Exit after successful login
            } else {
                // ❌ LOGIN FAILED
                if (authenticatedUser == null) {
                    System.out.println("❌ Invalid username/password.");
                } else {
                    System.out.println("❌ Your role (" + authenticatedUser.getRole() + ") doesn't match selected role (" + selectedRole + ")");
                }

                // Retry message
                if (attempt < maxAttempts) {
                    System.out.println("   Try again. (" + (maxAttempts - attempt) + " attempts left)");
                } else {
                    System.out.println("❌ Max attempts exceeded. Returning to main menu.");
                }
            }
        }
    }

    /**
     * ROLE SELECTION: Show all 8 roles for user to choose from
     * Returns selected Role or null if cancelled
     */
    private Role selectRole() {
        System.out.println("\n📋 SELECT YOUR ROLE:");
        System.out.println("=".repeat(60));
        System.out.println("1.  🛡️  ADMIN            - System Administrator");
        System.out.println("2.  👨‍💼 HOD              - Head of Department");
        System.out.println("3.  👨‍🏫 FACULTY           - Faculty Member");
        System.out.println("4.  🎓 STUDENT           - Student");
        System.out.println("5.  📦 STORES            - Stores Manager");
        System.out.println("6.  🛒 PURCHASE          - Purchase Officer");
        System.out.println("7.  💳 ACCOUNTS          - Accounts Officer");
        System.out.println("8.  👥 BOARD_MEMBERS     - Board Member");
        System.out.println("9.  ❌ Cancel");
        System.out.println("=".repeat(60));
        System.out.print("Select role (1-9): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> {
                System.out.println("\n📌 Login Credentials:");
                System.out.println("   Username: sys_admin");
                System.out.println("   Password: adminpass");
                return Role.ADMIN;
            }
            case "2" -> {
                System.out.println("\n📌 Login Credentials:");
                System.out.println("   Username: cs_hod");
                System.out.println("   Password: hodpass");
                return Role.HOD;
            }
            case "3" -> {
                System.out.println("\n📌 Login Credentials:");
                System.out.println("   Username: prof_smith");
                System.out.println("   Password: profpass");
                return Role.FACULTY;
            }
            case "4" -> {
                System.out.println("\n📌 Login Credentials:");
                System.out.println("   Username: st_alex");
                System.out.println("   Password: studpass");
                return Role.STUDENT;
            }
            case "5" -> {
                System.out.println("\n📌 Login Credentials:");
                System.out.println("   Username: stores_mgr");
                System.out.println("   Password: storespass");
                return Role.STORES;
            }
            case "6" -> {
                System.out.println("\n📌 Login Credentials:");
                System.out.println("   Username: purchase_lead");
                System.out.println("   Password: purchasepass");
                return Role.PURCHASE;
            }
            case "7" -> {
                System.out.println("\n📌 Login Credentials:");
                System.out.println("   Username: acc_officer");
                System.out.println("   Password: accpass");
                return Role.ACCOUNTS;
            }
            case "8" -> {
                System.out.println("\n📌 Login Credentials:");
                System.out.println("   Username: board_chair");
                System.out.println("   Password: boardpass");
                return Role.BOARD_MEMBERS;
            }
            case "9" -> {
                System.out.println("❌ Login cancelled.");
                return null;
            }
            default -> {
                System.out.println("⚠️ Invalid selection.");
                return null;
            }
        }
    }

    /**
     * SIGNUP: Public registration as STUDENT only
     * 🛡️ SECURITY: Role is hardcoded to STUDENT (no privilege escalation)
     */
    private void handleSignup() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📝 STUDENT REGISTRATION - Public Signup");
        System.out.println("=".repeat(60));
        System.out.println("⚠️  NOTE: You can only register as a STUDENT.");
        System.out.println("    For other roles (HOD, Admin, etc.), contact:");
        System.out.println("    📧 sys_admin@campusflow.edu\n");

        // Step 1: Username
        System.out.print("Create Username: ");
        String username = scanner.nextLine().trim();

        if (userRepo.findByUsername(username) != null) {
            System.out.println("❌ Username already taken.");
            return;
        }

        // Step 2: Password
        System.out.print("Create Password (min 6 characters): ");
        String password = scanner.nextLine().trim();

        if (password.length() < 6) {
            System.out.println("❌ Password must be at least 6 characters.");
            return;
        }

        // Step 3: Department
        System.out.print("Enter Your Academic Department: ");
        String department = scanner.nextLine().trim();

        if (department.isEmpty()) {
            System.out.println("❌ Department cannot be empty.");
            return;
        }

        // Step 4: Year of Study
        System.out.print("Enter Current Academic Year (1-4): ");
        int yearOfStudy;
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

        // 🛡️ SECURITY: Hardcode role to STUDENT
        User newStudent = new User();
        newStudent.setUsername(username);
        newStudent.setPassword(password);
        newStudent.setRole(Role.STUDENT);  // 🔒 HARDCODED
        newStudent.setDepartment(department);
        newStudent.setYearOfStudy(yearOfStudy);

        // Step 5: Save
        if (userRepo.saveStudentUser(newStudent)) {
            System.out.println("\n🎉 Registration successful!");
            System.out.println("   ✅ Username: " + username);
            System.out.println("   ✅ Role: STUDENT (fixed)");
            System.out.println("   ✅ Department: " + department);
            System.out.println("   ✅ Year: " + yearOfStudy);
            System.out.println("\n   You can now login with your credentials.\n");
        } else {
            System.out.println("❌ Registration failed.");
        }
    }

    /**
     * Route user to their role-specific menu
     */
    private void routeToRoleMenu(User user) {
        switch (user.getRole()) {
            case ADMIN -> {
                AdminMenu adminMenu = new AdminMenu(scanner);
                adminMenu.display(user);
            }
            case FACULTY -> {
                FacultyMenu facultyMenu = new FacultyMenu(scanner);
                facultyMenu.display(user);
            }
            case STUDENT -> {
                StudentMenu studentMenu = new StudentMenu(scanner);
                studentMenu.display(user);
            }
            case HOD -> {
                HODMenu hodMenu = new HODMenu(scanner);
                hodMenu.display(user);
            }
            case STORES -> {
                StoresMenu storesMenu = new StoresMenu(scanner);
                storesMenu.display(user);
            }
            case PURCHASE -> {
                PurchaseMenu purchaseMenu = new PurchaseMenu(scanner);
                purchaseMenu.display(user);
            }
            case ACCOUNTS -> {
                AccountsMenu accountsMenu = new AccountsMenu(scanner);
                accountsMenu.display(user);
            }
            case BOARD_MEMBERS -> {
                BoardMembersMenu boardMenu = new BoardMembersMenu(scanner);
                boardMenu.display(user);
            }
            default -> System.out.println("⚠️ Unknown role.");
        }
    }
}