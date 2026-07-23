package ui;

import model.User;
import model.Resource;
import service.ResourceService;
import service.SchedulingEngine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class StudentMenu {
    private final ResourceService resourceService;
    private final SchedulingEngine schedulingEngine;
    private final Scanner scanner;

    public StudentMenu(ResourceService resourceService, SchedulingEngine schedulingEngine, Scanner scanner) {
        this.resourceService = resourceService;
        this.schedulingEngine = schedulingEngine;
        this.scanner = scanner;
    }

    public void display(User student) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- STUDENT PORTAL ---");
            System.out.println("1. View All Campus Assets");
            System.out.println("2. Book a Standard Resource");
            System.out.println("3. Logout");
            System.out.print("Select choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> showResources();
                case "2" -> bookResourceWorkflow(student);
                case "3" -> running = false;
                default -> System.out.println("⚠️ Invalid option.");
            }
        }
    }

    private void showResources() {
        System.out.println("\n--- Available Assets ---");
        for (Resource r : resourceService.getAllResources()) {
            System.out.printf("[%d] %s (%s) - Loc: %s [Requires Approval: %b]%n",
                    r.getResourceId(), r.getName(), r.getType(), r.getLocation(), r.isRequiresApproval());
        }
    }

    private void bookResourceWorkflow(User student) {
        showResources();
        System.out.print("\nEnter Resource ID to book: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Resource target = resourceService.getAllResources().stream()
                    .filter(r -> r.getResourceId() == id).findFirst().orElse(null);

            if (target == null) {
                System.out.println("❌ Resource not found.");
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            System.out.print("Start Time (yyyy-MM-dd HH:mm): ");
            LocalDateTime start = LocalDateTime.parse(scanner.nextLine(), formatter);
            System.out.print("End Time (yyyy-MM-dd HH:mm): ");
            LocalDateTime end = LocalDateTime.parse(scanner.nextLine(), formatter);

            schedulingEngine.bookResource(student, target, start, end);
        } catch (Exception e) {
            System.out.println("⚠️ Processing Error: Verify numeric IDs and date formatting.");
        }
    }
}