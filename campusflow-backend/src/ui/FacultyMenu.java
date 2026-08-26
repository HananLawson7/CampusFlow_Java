package ui;

import model.User;
import model.Resource;
import repository.BookingRepository;
import repository.ResourceRepository;
import service.ResourceService;
import service.SchedulingEngine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class FacultyMenu {
    private final ResourceService resourceService;
    private final SchedulingEngine schedulingEngine;
    private final Scanner scanner;

    // 🔑 Updated constructor: takes only Scanner and initializes services internally
    public FacultyMenu(Scanner scanner) {
        this.scanner = scanner;
        this.resourceService = new ResourceService(new ResourceRepository());
        this.schedulingEngine = new SchedulingEngine(new BookingRepository());
    }

    public void display(User faculty) {
        boolean running = true;
        while (running) {
            System.out.println("\n=========================================");
            System.out.println("            FACULTY PORTAL               ");
            System.out.println("=========================================");
            System.out.println(" [1] 📋 View All Registered Assets");
            System.out.println(" [2] 🎫 Express Booking Wizard");
            System.out.println(" [3] 🚪 Logout");
            System.out.print("\n👉 Select option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> showResources();
                case "2" -> expressBookingWizard(faculty);
                case "3" -> running = false;
                default -> System.out.println("⚠️ Please press 1, 2, or 3.");
            }
        }
    }

    private void showResources() {
        System.out.println("\n--- 📋 Campus Asset Registry ---");
        for (Resource r : resourceService.getAllResources()) {
            System.out.printf("   • %s (%s) | Location: %s%n", r.getName(), r.getType(), r.getLocation());
        }
    }

    private void expressBookingWizard(User faculty) {
        List<Resource> resources = resourceService.getAllResources();
        if (resources.isEmpty()) {
            System.out.println("❌ No assets found in system.");
            return;
        }

        // Dropdown Step 1: Pick Resource by Selection List
        System.out.println("\n👉 Select a resource to reserve:");
        for (int i = 0; i < resources.size(); i++) {
            System.out.printf("   [%d] %s (%s)%n", i + 1, resources.get(i).getName(), resources.get(i).getType());
        }
        System.out.print("Choice: ");
        int resourceIdx = readChoiceIndex(resources.size());
        if (resourceIdx == -1) return;
        Resource selectedResource = resources.get(resourceIdx);

        // Dropdown Step 2: Pick Duration (Zero Date Typing)
        System.out.println("\n👉 Select booking duration:");
        System.out.println("   [1] 30 Minutes");
        System.out.println("   [2] 1 Hour");
        System.out.println("   [3] 2 Hours");
        System.out.print("Choice: ");
        int durationChoice = readChoiceIndex(3);
        if (durationChoice == -1) return;

        long minutes = switch (durationChoice) {
            case 0 -> 30;
            case 1 -> 60;
            default -> 120;
        };

        // Construct booking times immediately starting from the current moment
        LocalDateTime start = LocalDateTime.now().withNano(0);
        LocalDateTime end = start.plusMinutes(minutes);

        System.out.printf("\n⚡ Instantiating priority reservation for %s...%n", selectedResource.getName());
        schedulingEngine.bookResource(faculty, selectedResource, start, end);
    }

    private int readChoiceIndex(int maxOptions) {
        try {
            int input = Integer.parseInt(scanner.nextLine().trim());
            if (input >= 1 && input <= maxOptions) return input - 1;
        } catch (NumberFormatException ignored) {}
        System.out.println("❌ Invalid choice. Aborting wizard.");
        return -1;
    }
}