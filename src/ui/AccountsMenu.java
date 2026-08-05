package ui;

import model.User;
import model.PurchaseOrder;
import repository.PurchaseOrderRepository;

import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Accounts Menu: Accounts Department tracks billing and payments
 * Flow: Accounts (receives PO) → Tracks CREATED → BILLED → PAID
 */
public class AccountsMenu {
    private final Scanner scanner;
    private final PurchaseOrderRepository purchaseOrderRepo;

    public AccountsMenu(Scanner scanner) {
        this.scanner = scanner;
        this.purchaseOrderRepo = new PurchaseOrderRepository();
    }

    public void display(User accountsUser) {
        boolean running = true;

        while (running) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("💳 ACCOUNTS DEPARTMENT MENU");
            System.out.println("=".repeat(60));
            System.out.println("1. View CREATED Purchase Orders (For Billing)");
            System.out.println("2. Mark PO as BILLED");
            System.out.println("3. View BILLED Purchase Orders (Awaiting Payment)");
            System.out.println("4. Mark PO as PAID");
            System.out.println("5. View Payment History");
            System.out.println("6. View Vendor Spending Summary");
            System.out.println("7. Logout");
            System.out.print("Select Option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> viewCreatedPOs();
                case "2" -> markAsBilled();
                case "3" -> viewBilledPOs();
                case "4" -> markAsPaid();
                case "5" -> viewPaymentHistory();
                case "6" -> viewVendorSpending();
                case "7" -> {
                    System.out.println("✅ Logged out successfully.");
                    running = false;
                }
                default -> System.out.println("❌ Invalid option.");
            }
        }
    }

    private void viewCreatedPOs() {
        System.out.println("\n--- CREATED PURCHASE ORDERS (Ready for Billing) ---");

        var pos = purchaseOrderRepo.getCreatedPOs();
        if (pos.isEmpty()) {
            System.out.println("✅ No POs awaiting billing.");
            return;
        }

        displayPOTable(pos);
    }

    private void markAsBilled() {
        System.out.println("\n--- MARK PURCHASE ORDER AS BILLED ---");

        System.out.print("Enter PO ID: ");
        int poId = Integer.parseInt(scanner.nextLine().trim());

        PurchaseOrder po = purchaseOrderRepo.getPOById(poId);
        if (po == null) {
            System.out.println("❌ PO not found.");
            return;
        }

        if (!po.getStatus().equals("CREATED")) {
            System.out.println("❌ PO is not in CREATED status (current: " + po.getStatus() + ")");
            return;
        }

        System.out.println("\nPO Details:");
        System.out.println("Vendor: " + po.getVendorName());
        System.out.println("Amount: ₹" + po.getTotalCost());

        if (purchaseOrderRepo.updatePOStatus(poId, "BILLED")) {
            System.out.println("✅ PO marked as BILLED successfully!");
        } else {
            System.out.println("❌ Failed to update PO status.");
        }
    }

    private void viewBilledPOs() {
        System.out.println("\n--- BILLED PURCHASE ORDERS (Awaiting Payment) ---");

        var pos = purchaseOrderRepo.getBilledPOs();
        if (pos.isEmpty()) {
            System.out.println("✅ No POs awaiting payment.");
            return;
        }

        displayPOTable(pos);
    }

    private void markAsPaid() {
        System.out.println("\n--- MARK PURCHASE ORDER AS PAID ---");

        System.out.print("Enter PO ID: ");
        int poId = Integer.parseInt(scanner.nextLine().trim());

        PurchaseOrder po = purchaseOrderRepo.getPOById(poId);
        if (po == null) {
            System.out.println("❌ PO not found.");
            return;
        }

        if (!po.getStatus().equals("BILLED")) {
            System.out.println("❌ PO is not in BILLED status (current: " + po.getStatus() + ")");
            return;
        }

        System.out.println("\nPO Details:");
        System.out.println("Vendor: " + po.getVendorName());
        System.out.println("Amount: ₹" + po.getTotalCost());

        System.out.print("Confirm payment (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (confirm.equals("Y")) {
            if (purchaseOrderRepo.markAsPaid(poId, LocalDateTime.now())) {
                System.out.println("✅ PO marked as PAID successfully!");
            } else {
                System.out.println("❌ Failed to mark PO as paid.");
            }
        } else {
            System.out.println("❌ Payment cancelled.");
        }
    }

    private void viewPaymentHistory() {
        System.out.println("\n--- PAYMENT HISTORY (PAID POs) ---");

        var pos = purchaseOrderRepo.getPaidPOs();
        if (pos.isEmpty()) {
            System.out.println("✅ No payments recorded yet.");
            return;
        }

        System.out.printf("%-6s %-30s %-15s %-20s %-20s\n",
                "PO ID", "Vendor", "Amount", "Payment Date", "Created Date");
        System.out.println("-".repeat(110));

        for (var po : pos) {
            System.out.printf("%-6d %-30s %-15.2f %-20s %-20s\n",
                    po.getPoId(),
                    po.getVendorName(),
                    po.getTotalCost(),
                    po.getPaymentDate() != null ? po.getPaymentDate() : "N/A",
                    po.getCreatedDate());
        }
    }

    private void viewVendorSpending() {
        System.out.println("\n--- VENDOR SPENDING SUMMARY ---");

        var spending = purchaseOrderRepo.getSpendingByVendor();
        if (spending.isEmpty()) {
            System.out.println("✅ No spending data available.");
            return;
        }

        System.out.printf("%-40s %-15s\n", "Vendor Name", "Total Spent (₹)");
        System.out.println("-".repeat(60));

        for (var row : spending) {
            System.out.printf("%-40s %-15.2f\n", row[0], row[1]);
        }
    }

    // ============ HELPER METHOD ============

    private void displayPOTable(java.util.List<PurchaseOrder> pos) {
        System.out.printf("%-6s %-30s %-15s %-12s %-20s\n",
                "PO ID", "Vendor", "Total Cost", "Status", "Created Date");
        System.out.println("-".repeat(100));

        for (var po : pos) {
            System.out.printf("%-6d %-30s %-15.2f %-12s %-20s\n",
                    po.getPoId(),
                    po.getVendorName(),
                    po.getTotalCost(),
                    po.getStatus(),
                    po.getCreatedDate());
        }
    }
}