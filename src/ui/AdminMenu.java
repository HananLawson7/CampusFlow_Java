package ui;

import model.User;
import repository.ResourceRepository;
import repository.BookingRepository;
import service.BookingService;
import service.ResourceService;
import java.util.Scanner;

public class AdminMenu {
    private final BookingService bookingService;
    private final ResourceService resourceService;
    private final Scanner scanner;

    public AdminMenu(Scanner scanner) {
        this.scanner = scanner;
        this.bookingService = new BookingService(new BookingRepository());
        this.resourceService = new ResourceService(new ResourceRepository());
    }

    public void display(User admin) {
        boolean running = true;
        while (running) {
            System.out.println("\n=========================================");
            System.out.println("         ADMINISTRATION PORTAL           ");
            System.out.println("=========================================");
            System.out.println(" [1] 🔓 Moderate Reservation Queues");
            System.out.println(" [2] 🚪 Logout");
            System.out.print("\n👉 Select option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> moderationWizard(admin);
                case "2" -> running = false;
                default -> System.out.println("⚠️ Please press 1 or 2.");
            }
        }
    }

    private void moderationWizard(User admin) {
        System.out.println("\n--- 🛡️ Queue Action Wizard ---");
        System.out.println(" [1] Approve a Reservation");
        System.out.println(" [2] Revoke / Cancel a Reservation");
        System.out.println(" [3] Go back");
        System.out.print("Choice: ");

        String action = scanner.nextLine().trim();
        if (!action.equals("1") && !action.equals("2")) return;

        // Zero typing execution: User inputs the ID directly into a strict verification check
        System.out.print("\nEnter System Booking ID to process: ");
        try {
            int targetId = Integer.parseInt(scanner.nextLine().trim());

            if (action.equals("1")) {
                if (bookingService.approveBooking(targetId, admin.getUserId())) {
                    System.out.println("✅ Status updated to APPROVED.");
                } else {
                    System.out.println("❌ System failed to approve target ID.");
                }
            } else {
                if (bookingService.cancelBooking(targetId)) {
                    System.out.println("🛑 Status updated to CANCELLED.");
                } else {
                    System.out.println("❌ System failed to cancel target ID.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Selection rejected: Enter numeric characters only.");
        }
    }
}