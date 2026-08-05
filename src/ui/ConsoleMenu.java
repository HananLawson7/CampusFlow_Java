package ui;

import model.User;
import model.Role;
import repository.UserRepository;
import util.Validation;

import java.util.Scanner;

/**
 * SECURE ConsoleMenu: Restricted signup (STUDENT only)
 * All privileged roles (HOD, STORES, PURCHASE, etc.) are pre-seeded only
 *
 * Login Flow:
 *   - Shows 8 pre-seeded accounts
 *   - Authenticates against database
 *   - Routes to role-specific menu
 *
 * Signup Flow:
 *   - Only allows STUDENT registration
 *   - Role is hardcoded (no privilege escalation)
 *   - Department and year of study required
 */
public class ConsoleMenu {
    private final Scanner scanner;
    private final UserRepository userRepo;
    private boolean isRunning;

    public ConsoleMenu() {
        this.scanner = new Scanner(System.in);
        this.userRepo = new UserRepository();
        this.isRunning = true;
    }

    public void start() {
        while (isRunning) {
            displayMainMenu();
        }
    }

    /**
     * Main menu: Login, Signup, or Exit
     */
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔌 WELCOME TO CAMPUSFLOW - SUPPLY CHAIN MANAGEMENT");
        System.out.println("=".repeat(60));
        System.out.println("1. Login (Pre-seeded accounts)");
        System.out.println("2. Signup as Student (Self-registration)");
        System.out.println("3. Exit Application");
        System.out.print("Please select an option: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> handleLoginFlow();
            case "2" -> handleSignupFlow();
            case "3" -> {
                System.out.println("\n✨ Thank you for using CampusFlow. Goodbye!");
                isRunning = false;
            }
            default -> System.out.println("⚠️ Invalid input. Please enter 1, 2, or 3.");
        }
    }

    /**
     * LOGIN: Only works with pre-seeded accounts
     * Shows list of available credentials
     * Authenticates against database
     * Routes to role-specific menu
     */
    private void handleLoginFlow() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔐 USER LOGIN - Pre-seeded Accounts Only");
        System.out.println("=".repeat(60));

        System.out.println("\n📋 AVAILABLE ROLES (Pre-seeded Test Accounts):");
        System.out.println("  🛡️  Admin:          sys_admin / adminpass");
        System.out.println("  👨‍💼 HOD:             cs_hod / hodpass");
        System.out.println("  📦 Stores:         stores_mgr / storespass");
        System.out.println("  🛒 Purchase:       purchase_lead / purchasepass");
        System.out.println("  💳 Accounts:       acc_officer / accpass");
        System.out.println("  👥 Board Members:  board_chair / boardpass");
        System.out.println("  👨‍🏫 Faculty:        prof_smith / profpass");
        System.out.println("  🎓 Student:        st_alex / studpass\n");

        System.out.print("Enter Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        // 🛡️ VALIDATION LAYER 1: Empty/Blank Check
        if (!Validation.isNotEmpty(username) || !Validation.isNotEmpty(password)) {
            System.out.println("⚠️ Validation Error: Fields cannot be empty or blank.");
            return;
        }

        // 🛡️ VALIDATION LAYER 2: Format Check
        if (!Validation.isValidUsername(username)) {
            System.out.println("⚠️ Validation Error: Username contains invalid characters.");
            return;
        }

        // 🛡️ VALIDATION LAYER 3: Password Length Check
        if (!Validation.isValidPassword(password)) {
            System.out.println("⚠️ Validation Error: Password does not meet security requirements.");
            return;
        }

        System.out.println("\n🚀 Authenticating user: " + username);

        // 🔑 AUTHENTICATE: Search database for matching credentials
        User authenticatedUser = userRepo.findByUsernameAndPassword(username, password);

        if (authenticatedUser != null) {
            // ✅ LOGIN SUCCESSFUL
            System.out.println("✅ Access Granted! Role verified: " + authenticatedUser.getRole());
            System.out.println("   Welcome, " + authenticatedUser.getUsername() + "!");

            // Route to role-specific menu
            routeToRoleMenu(authenticatedUser);
        } else {
            // ❌ LOGIN FAILED
            System.out.println("❌ Authentication failed: Invalid credentials.");
            System.out.println("   Tip: Check the pre-seeded accounts list above.");
        }
    }

    /**
     * SIGNUP: Public registration as STUDENT only
     * 🛡️ SECURITY: Role is hardcoded to STUDENT (no privilege escalation)
     *
     * Process:
     *   1. Get username (check if unique)
     *   2. Get password (min 6 chars)
     *   3. Get department
     *   4. Get year of study (1-4)
     *   5. Save to database with hardcoded STUDENT role
     */
    private void handleSignupFlow() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📝 STUDENT REGISTRATION - Public Signup");
        System.out.println("=".repeat(60));
        System.out.println("⚠️  NOTE: You can only register as a STUDENT.");
        System.out.println("    For other roles (HOD, Admin, etc.), contact:");
        System.out.println("    📧 sys_admin@campusflow.edu\n");

        // Step 1: Username
        System.out.print("Create Username: ");
        String username = scanner.nextLine().trim();

        // Check if username already exists
        if (userRepo.findByUsername(username) != null) {
            System.out.println("❌ Username already taken. Please choose another.");
            return;
        }

        // Step 2: Password
        System.out.print("Create Password (min 6 characters): ");
        String password = scanner.nextLine().trim();

        // 🛡️ VALIDATION: Passwords must be at least 6 characters
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
            System.out.println("❌ Year must be a number between 1 and 4.");
            return;
        }

        // 🛡️ SECURITY SAFEGUARD: Hardcode role to STUDENT
        // User cannot choose or escalate to Admin/HOD/etc.
        User newStudent = new User();
        newStudent.setUsername(username);
        newStudent.setPassword(password);
        newStudent.setRole(Role.STUDENT);  // 🔒 HARDCODED - Cannot be changed
        newStudent.setDepartment(department);
        newStudent.setYearOfStudy(yearOfStudy);

        // Step 5: Save to database
        if (userRepo.saveStudentUser(newStudent)) {
            System.out.println("\n🎉 Registration successful!");
            System.out.println("   ✅ Username: " + username);
            System.out.println("   ✅ Role: STUDENT (fixed)");
            System.out.println("   ✅ Department: " + department);
            System.out.println("   ✅ Year: " + yearOfStudy);
            System.out.println("\n   You can now login with your credentials.\n");
        } else {
            System.out.println("❌ Registration failed. Please try again.");
        }
    }

    /**
     * Route user to their role-specific menu
     * Each role gets its own dedicated menu class
     */
    private void routeToRoleMenu(User user) {
        switch (user.getRole()) {
            // ========== ACADEMIC ROLES ==========
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

            // ========== SUPPLY CHAIN ROLES ==========
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

            default -> System.out.println("⚠️ Unknown user role. Access denied.");
        }
    }

    /**
     * Entry point for the application
     */
    public static void main(String[] args) {
        ConsoleMenu app = new ConsoleMenu();
        app.start();
        System.out.println("\n✨ CampusFlow session terminated. Goodbye!");
    }
}