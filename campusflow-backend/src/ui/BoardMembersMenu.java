package ui;

import model.User;
import repository.ResourceRepository;

import java.util.Scanner;

/**
 * Board Members Menu: Read-only access to inventory overview
 * Role: BOARD_MEMBERS can only view resources (no modifications)
 */
public class BoardMembersMenu {
    private final Scanner scanner;
    private final ResourceRepository resourceRepo;

    public BoardMembersMenu(Scanner scanner) {
        this.scanner = scanner;
        this.resourceRepo = new ResourceRepository();
    }

    public void display(User boardMember) {
        boolean running = true;

        while (running) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("👥 BOARD MEMBERS MENU - Read-Only Access");
            System.out.println("=".repeat(60));
            System.out.println("1. View Complete Inventory Overview");
            System.out.println("2. View Inventory by Department");
            System.out.println("3. View Low Stock Items");
            System.out.println("4. Logout");
            System.out.print("Select Option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> viewCompleteInventory();
                case "2" -> viewByDepartment();
                case "3" -> viewLowStockItems();
                case "4" -> {
                    System.out.println("✅ Logged out successfully.");
                    running = false;
                }
                default -> System.out.println("❌ Invalid option.");
            }
        }
    }

    private void viewCompleteInventory() {
        System.out.println("\n--- COMPLETE INVENTORY OVERVIEW ---");
        System.out.println("(Read-Only Access)\n");

        var resources = resourceRepo.getAllResources();
        if (resources.isEmpty()) {
            System.out.println("No resources in system.");
            return;
        }

        System.out.printf("%-6s %-40s %-15s %-12s %-12s %-12s\n",
                "ID", "Resource Name", "Category", "In Stock", "Unit Price", "Department");
        System.out.println("-".repeat(120));

        double totalValue = 0;

        for (var r : resources) {
            double itemValue = r.getQuantity_in_stock() * r.getUnit_price().doubleValue();
            totalValue += itemValue;

            System.out.printf("%-6d %-40s %-15s %-12d %-12.2f %-12s\n",
                    r.getResourceId(),
                    r.getName(),
                    r.getCategory(),
                    r.getQuantity_in_stock(),
                    r.getUnit_price(),
                    r.getDepartment());
        }

        System.out.println("-".repeat(120));
        System.out.printf("Total Inventory Value: ₹%.2f\n", totalValue);
        System.out.printf("Total Items in System: %d\n", resources.size());
    }

    private void viewByDepartment() {
        System.out.println("\n--- INVENTORY BY DEPARTMENT ---");

        System.out.print("Enter Department Name (or leave blank for all): ");
        String department = scanner.nextLine().trim();

        var resources = department.isEmpty()
                ? resourceRepo.getAllResources()
                : resourceRepo.getResourcesByDepartment(department);

        if (resources.isEmpty()) {
            System.out.println("No resources found for department: " + (department.isEmpty() ? "All" : department));
            return;
        }

        System.out.printf("\n%-6s %-40s %-15s %-12s %-12s\n",
                "ID", "Resource Name", "Category", "In Stock", "Unit Price");
        System.out.println("-".repeat(100));

        double departmentValue = 0;

        for (var r : resources) {
            double itemValue = r.getQuantity_in_stock() * r.getUnit_price().doubleValue();
            departmentValue += itemValue;

            System.out.printf("%-6d %-40s %-15s %-12d %-12.2f\n",
                    r.getResourceId(),
                    r.getName(),
                    r.getCategory(),
                    r.getQuantity_in_stock(),
                    r.getUnit_price());
        }

        System.out.println("-".repeat(100));
        System.out.printf("Department Inventory Value: ₹%.2f\n", departmentValue);
    }

    private void viewLowStockItems() {
        System.out.println("\n--- LOW STOCK ALERT (≤ 5 Units) ---");

        var resources = resourceRepo.getAllResources();
        var lowStockItems = resources.stream()
                .filter(r -> r.getQuantity_in_stock() <= 5)
                .toList();

        if (lowStockItems.isEmpty()) {
            System.out.println("✅ No low stock items. All inventory levels are healthy.");
            return;
        }

        System.out.printf("%-6s %-40s %-15s %-12s %-12s %-12s\n",
                "ID", "Resource Name", "Category", "Stock", "Unit Price", "Department");
        System.out.println("-".repeat(120));

        for (var r : lowStockItems) {
            String alert = r.getQuantity_in_stock() == 0 ? "🔴 OUT OF STOCK" : "🟡 CRITICAL";
            System.out.printf("%-6d %-40s %-15s %-12d %-12.2f %-12s %s\n",
                    r.getResourceId(),
                    r.getName(),
                    r.getCategory(),
                    r.getQuantity_in_stock(),
                    r.getUnit_price(),
                    r.getDepartment(),
                    alert);
        }

        System.out.println("\n⚠️ Total Low Stock Items: " + lowStockItems.size());
    }
}