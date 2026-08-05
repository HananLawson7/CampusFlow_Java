package ui;

import model.User;
import model.Role;
import repository.UserRepository;
import util.Validation;

import java.util.Optional;
import java.util.Scanner;

/**
 * UPDATED ConsoleMenu: Full supply chain routing for 7 roles
 * - ADMIN, HOD, STUDENT, FACULTY (Existing)
 * - STORES, PURCHASE, ACCOUNTS, BOARD_MEMBERS (New Supply Chain)
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

    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔌 WELCOME TO CAMPUSFLOW - SUPPLY CHAIN MANAGEMENT");
        System.out.println("=".repeat(60));
        System.out.println("1. Login to System");
        System.out.println("2. Exit Application");
        System.out.print("Please select an option: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> handleLoginFlow();
            case "2" -> isRunning = false;
            default -> System.out.println("⚠️ Invalid input. Please enter 1 or 2.");
        }
    }

    private void handleLoginFlow() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔐 USER LOGIN");
        System.out.println("=".repeat(60));

        System.out.print("Enter Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        // 🛡️ VALIDATION LAYER 1: Empty/Blank Check
        if (!Validation.isNotEmpty(username) || !Validation.isNotEmpty(password)) {
            System.out.println("⚠️ Validation Error: Fields cannot be empty or blank.");
            return;
        }

        // 🛡️ VALIDATION LAYER 2: Format & Character Sanity Check
        if (!Validation.isValidUsername(username)) {
            System.out.println("⚠️ Validation Error: Username contains invalid characters or length rules.");
            return;
        }

        // 🛡️ VALIDATION LAYER 3: Core Length Verification Check
        if (!Validation.isValidPassword(password)) {
            System.out.println("⚠️ Validation Error: Password does not meet security length minimums.");
            return;
        }

        System.out.println("🚀 Attempting connection for user: " + username);

        // Authenticate against database
        User authenticatedUser = userRepo.findByUsernameAndPassword(username, password);

        if (authenticatedUser != null) {
            System.out.println("✅ Access Granted! Role verified: " + authenticatedUser.getRole());
            routeToRoleMenu(authenticatedUser);
        } else {
            System.out.println("❌ Authentication failed: Invalid credentials.");
        }
    }

    /**
     * 🔑 CORE ROUTING LOGIC: Routes user to their role-specific menu
     * Supports all 8 roles (7 supply chain + admin)
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

            default -> System.out.println("⚠️ Unknown user role. Aborting workflow entry.");
        }
    }

    public static void main(String[] args) {
        ConsoleMenu app = new ConsoleMenu();
        app.start();
        System.out.println("\n✨ CampusFlow session terminated. Goodbye!");
    }
}