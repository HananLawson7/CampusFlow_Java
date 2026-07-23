package ui;

import model.User;
import service.UserService;
import service.ResourceService;
import service.BookingService;
import service.SchedulingEngine;

import java.util.Optional;
import java.util.Scanner;

public class ConsoleMenu {
    private final Scanner scanner;
    private final UserService userService;
    private final ResourceService resourceService;
    private final BookingService bookingService;
    private final SchedulingEngine schedulingEngine;
    private boolean isRunning;

    // Dependency injection constructor wiring up all core engines
    public ConsoleMenu(UserService userService, ResourceService resourceService,
                       BookingService bookingService, SchedulingEngine schedulingEngine) {
        this.scanner = new Scanner(System.in);
        this.userService = userService;
        this.resourceService = resourceService;
        this.bookingService = bookingService;
        this.schedulingEngine = schedulingEngine;
        this.isRunning = true;
    }

    public void start() {
        while (isRunning) {
            displayMainMenu();
        }
    }

    private void displayMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
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
        System.out.println("\n--- USER LOGIN ---");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("⚠️ Username or password cannot be empty.");
            return;
        }

        System.out.println("🚀 Attempting connection for user: " + username);

        // 1. Authenticate against database via UserService
        Optional<User> authenticatedUser = userService.authenticate(username, password);

        if (authenticatedUser.isPresent()) {
            User user = authenticatedUser.get();
            System.out.println("✅ Access Granted! Role verified: " + user.getRole());

            // 2. Traffic control: Route user to their corresponding menu type
            routeToRoleMenu(user);
        } else {
            System.out.println("❌ Authentication failed: Invalid credentials.");
        }
    }

    private void routeToRoleMenu(User user) {
        switch (user.getRole()) {
            case ADMIN -> {
                AdminMenu adminMenu = new AdminMenu(bookingService, resourceService, scanner);
                adminMenu.display(user);
            }
            case FACULTY -> {
                FacultyMenu facultyMenu = new FacultyMenu(resourceService, schedulingEngine, scanner);
                facultyMenu.display(user);
            }
            case STUDENT -> {
                StudentMenu studentMenu = new StudentMenu(resourceService, schedulingEngine, scanner);
                studentMenu.display(user);
            }
            default -> System.out.println("⚠️ Unknown user role role-clearance. Aborting workflow entry.");
        }
    }
}