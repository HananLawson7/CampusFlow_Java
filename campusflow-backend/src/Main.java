/*import ui.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("    Welcome to CampusFlow Resource Mgmt  ");
        System.out.println("=========================================");

        // Initialize the main terminal menu router
        ConsoleMenu mainMenu = new ConsoleMenu();
        mainMenu.start();

        System.out.println("\nSystem shut down cleanly. Goodbye!");
    }
}
*/

// Temporary
/*
import config.DatabaseConfig;
import repository.UserRepository;
import model.User;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== RUNNING PRE-FLIGHT DIAGNOSTICS ===");

        // Test 1: Low-Level Database Connection Check
        System.out.print("Testing PostgreSQL Connection... ");
        try (Connection con = DatabaseConfig.getConnection()) {
            if (con != null && !con.isClosed()) {
                System.out.println("✅ SUCCESS! Connected to campus_flow database.");
            } else {
                System.out.println("❌ FAILED! Connection object is null or closed.");
            }
        } catch (Exception e) {
            System.out.println("❌ FAILED!");
            System.err.println("Error details: " + e.getMessage());
            System.out.println("\n💡 Tip: Make sure PostgreSQL is running and your password in DatabaseConfig is correct.");
            return; // Stop execution if connection fails
        }
        // Test 2: Repository and Data Mapping Check
        System.out.print("Testing UserRepository Authentication... ");
        UserRepository userRepo = new UserRepository();

        // This attempts to log in using the test account we seeded earlier
        User testUser = userRepo.authenticate("alice_stud", "password123");

        if (testUser != null) {
            System.out.println("✅ SUCCESS!");
            System.out.println("   Successfully retrieved user: " + testUser.getUsername());
            System.out.println("   Mapped Role: " + testUser.getRole());
            System.out.println("   Mapped Department: " + testUser.getDepartment());
        } else {
            System.out.println("❌ FAILED!");
            System.err.println("   Could not authenticate 'alice_stud'. Ensure your database has been seeded with test data.");
        }

        System.out.println("\n=====================================");
        System.out.println("DIAGNOSTICS COMPLETE. READY TO BUILD!");
        System.out.println("=====================================");
    }
}
*/
/*
import repository.UserRepository;
import repository.ResourceRepository;
import repository.BookingRepository;
import service.UserService;
import service.ResourceService;
import service.BookingService;
import service.SchedulingEngine;
import ui.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("    Welcome to CampusFlow Resource Mgmt  ");
        System.out.println("=========================================");

        // 1. Initialize all Database Repositories
        UserRepository userRepo = new UserRepository();
        ResourceRepository resourceRepo = new ResourceRepository();
        BookingRepository bookingRepo = new BookingRepository();

        // 2. Inject Repositories into Core Service Layers
        UserService userService = new UserService(userRepo);
        ResourceService resourceService = new ResourceService(resourceRepo);
        BookingService bookingService = new BookingService(bookingRepo);
        SchedulingEngine schedulingEngine = new SchedulingEngine(bookingRepo);

        // 3. Initialize the Main Router and Pass the Engines
        ConsoleMenu mainMenu = new ConsoleMenu(userService, resourceService, bookingService, schedulingEngine);

        // 4. Fire up the interactive runtime loop
        mainMenu.start();

        System.out.println("\nSystem shut down cleanly. Goodbye!");
    }
}*/

import ui.ConsoleApp;

public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 Booting CampusFlow Core Systems...");

        // Hand off control directly to the console interface engine
        ConsoleApp.main(args);
    }
}

