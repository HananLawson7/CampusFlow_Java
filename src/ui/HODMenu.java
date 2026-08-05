package ui;

import model.User;
import model.ProductRequest;
import repository.ProductRequestRepository;
import repository.ResourceRepository;

import java.util.Scanner;

/**
 * HOD Menu: HODs submit requests for items from inventory
 * Flow: HOD → (Request) → Stores Department
 */
public class HODMenu {
    private final Scanner scanner;
    private final ProductRequestRepository productRequestRepo;
    private final ResourceRepository resourceRepo;

    public HODMenu(Scanner scanner) {
        this.scanner = scanner;
        this.productRequestRepo = new ProductRequestRepository();
        this.resourceRepo = new ResourceRepository();
    }

    public void display(User hod) {
        boolean running = true;

        while (running) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("🎓 HOD MENU - Department: " + hod.getDepartment());
            System.out.println("=".repeat(60));
            System.out.println("1. Submit Product Request");
            System.out.println("2. View My Requests");
            System.out.println("3. Logout");
            System.out.print("Select Option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> submitProductRequest(hod);
                case "2" -> viewMyRequests(hod);
                case "3" -> {
                    System.out.println("✅ Logged out successfully.");
                    running = false;
                }
                default -> System.out.println("❌ Invalid option.");
            }
        }
    }

    private void submitProductRequest(User hod) {
        System.out.println("\n--- SUBMIT PRODUCT REQUEST ---");
        System.out.println("Available Resources:");

        var resources = resourceRepo.getResourcesByDepartment(hod.getDepartment());
        if (resources.isEmpty()) {
            System.out.println("❌ No resources available for your department.");
            return;
        }

        // Display available resources
        System.out.printf("%-5s %-40s %-15s %-10s\n", "ID", "Resource Name", "Category", "In Stock");
        for (var r : resources) {
            System.out.printf("%-5d %-40s %-15s %-10d\n",
                    r.getResourceId(), r.getName(), r.getCategory(), r.getQuantity_in_stock());
        }

        System.out.print("\nEnter Resource ID: ");
        int resourceId = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter Quantity Requested: ");
        int quantity = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter Notes (optional): ");
        String notes = scanner.nextLine().trim();

        ProductRequest request = new ProductRequest(hod.getUser_id(), resourceId, quantity, notes);
        request.setStatus("PENDING");

        if (productRequestRepo.createRequest(request)) {
            System.out.println("✅ Product request submitted successfully!");
        } else {
            System.out.println("❌ Failed to submit request.");
        }
    }

    private void viewMyRequests(User hod) {
        System.out.println("\n--- MY PRODUCT REQUESTS ---");

        var requests = productRequestRepo.getRequestsByHod(hod.getUser_id());
        if (requests.isEmpty()) {
            System.out.println("No requests found.");
            return;
        }

        System.out.printf("%-8s %-12s %-12s %-10s %-20s %-30s\n",
                "Req ID", "Resource ID", "Quantity", "Status", "Date", "Notes");
        System.out.println("-".repeat(120));

        for (var req : requests) {
            System.out.printf("%-8d %-12d %-12d %-10s %-20s %-30s\n",
                    req.getRequestId(),
                    req.getResourceId(),
                    req.getQuantity(),
                    req.getStatus(),
                    req.getRequestedDate(),
                    req.getNotes() != null ? req.getNotes() : "");
        }
    }
}