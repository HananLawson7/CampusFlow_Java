package ui;

import model.User;
import model.PurchaseRequest;
import model.PurchaseOrder;
import repository.PurchaseRequestRepository;
import repository.PurchaseOrderRepository;
import repository.ResourceRepository;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Purchase Menu: Purchase Department creates POs from PRs
 * Flow: Purchase (receives PR) → Creates PO → Accounts Department
 */
public class PurchaseMenu {
    private final Scanner scanner;
    private final PurchaseRequestRepository purchaseRequestRepo;
    private final PurchaseOrderRepository purchaseOrderRepo;
    private final ResourceRepository resourceRepo;

    public PurchaseMenu(Scanner scanner) {
        this.scanner = scanner;
        this.purchaseRequestRepo = new PurchaseRequestRepository();
        this.purchaseOrderRepo = new PurchaseOrderRepository();
        this.resourceRepo = new ResourceRepository();
    }

    public void display(User purchaseUser) {
        boolean running = true;

        while (running) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("🛒 PURCHASE DEPARTMENT MENU");
            System.out.println("=".repeat(60));
            System.out.println("1. View Pending Purchase Requests");
            System.out.println("2. Create Purchase Order from PR");
            System.out.println("3. View All Purchase Orders");
            System.out.println("4. Logout");
            System.out.print("Select Option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> viewPendingPRs();
                case "2" -> createPurchaseOrder();
                case "3" -> viewAllPOs();
                case "4" -> {
                    System.out.println("✅ Logged out successfully.");
                    running = false;
                }
                default -> System.out.println("❌ Invalid option.");
            }
        }
    }

    private void viewPendingPRs() {
        System.out.println("\n--- PENDING PURCHASE REQUESTS ---");

        var prs = purchaseRequestRepo.getPendingPRs();
        if (prs.isEmpty()) {
            System.out.println("✅ No pending purchase requests.");
            return;
        }

        System.out.printf("%-6s %-10s %-12s %-10s %-20s\n",
                "PR ID", "Req ID", "Resource", "Qty", "Created Date");
        System.out.println("-".repeat(70));

        for (var pr : prs) {
            System.out.printf("%-6d %-10d %-12d %-10d %-20s\n",
                    pr.getPrId(),
                    pr.getRequestId(),
                    pr.getResourceId(),
                    pr.getQuantity(),
                    pr.getCreatedDate());
        }
    }

    private void createPurchaseOrder() {
        System.out.println("\n--- CREATE PURCHASE ORDER ---");

        System.out.print("Enter Purchase Request ID: ");
        int prId = Integer.parseInt(scanner.nextLine().trim());

        PurchaseRequest pr = purchaseRequestRepo.getPRById(prId);
        if (pr == null) {
            System.out.println("❌ Purchase Request not found.");
            return;
        }

        if (!pr.getStatus().equals("PENDING_PO")) {
            System.out.println("❌ This PR is no longer pending (status: " + pr.getStatus() + ")");
            return;
        }

        var resource = resourceRepo.getResourceById(pr.getResourceId());
        if (resource == null) {
            System.out.println("❌ Resource not found.");
            return;
        }

        System.out.println("\n📋 PURCHASE REQUEST DETAILS:");
        System.out.println("PR ID: " + pr.getPrId());
        System.out.println("Resource: " + resource.getName());
        System.out.println("Quantity: " + pr.getQuantity());
        System.out.println("Unit Price: ₹" + resource.getUnit_price());

        // Calculate total cost
        BigDecimal unitPrice = resource.getUnit_price();
        BigDecimal totalCost = unitPrice.multiply(BigDecimal.valueOf(pr.getQuantity()));
        System.out.println("Total Cost: ₹" + totalCost);

        System.out.print("\nEnter Vendor Name: ");
        String vendorName = scanner.nextLine().trim();

        System.out.print("Enter any notes (optional): ");
        String notes = scanner.nextLine().trim();

        // Create Purchase Order
        PurchaseOrder po = new PurchaseOrder(prId, vendorName, totalCost, notes);

        if (purchaseOrderRepo.createPO(po)) {
            purchaseRequestRepo.updatePRStatus(prId, "PO_ISSUED");
            System.out.println("✅ Purchase Order created successfully!");
            System.out.println("PO Details: Vendor=" + vendorName + " | Amount=₹" + totalCost);
        } else {
            System.out.println("❌ Failed to create purchase order.");
        }
    }

    private void viewAllPOs() {
        System.out.println("\n--- ALL PURCHASE ORDERS ---");

        var pos = purchaseOrderRepo.getAllPOs();
        if (pos.isEmpty()) {
            System.out.println("No purchase orders found.");
            return;
        }

        System.out.printf("%-6s %-12s %-30s %-15s %-12s %-20s\n",
                "PO ID", "PR ID", "Vendor", "Total Cost", "Status", "Created Date");
        System.out.println("-".repeat(110));

        for (var po : pos) {
            System.out.printf("%-6d %-12d %-30s %-15.2f %-12s %-20s\n",
                    po.getPoId(),
                    po.getPrId(),
                    po.getVendorName(),
                    po.getTotalCost(),
                    po.getStatus(),
                    po.getCreatedDate());
        }
    }
}