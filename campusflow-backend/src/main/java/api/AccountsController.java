package api;

import com.google.gson.Gson;
import model.PurchaseOrder;
import model.PurchaseRequest;
import repository.PurchaseOrderRepository;
import repository.PurchaseRequestRepository;
import repository.ProductRequestRepository;
import repository.ResourceRepository;
import java.util.Map;
import static spark.Spark.*;

public class AccountsController {
    static Gson gson = GsonProvider.gson;
    static PurchaseOrderRepository poRepo = new PurchaseOrderRepository();
    static PurchaseRequestRepository purchaseRequestRepo = new PurchaseRequestRepository();
    static ProductRequestRepository productRequestRepo = new ProductRequestRepository();
    static ResourceRepository resourceRepo = new ResourceRepository();

    public static void register() {
        get("/api/accounts/created", (req, res) -> gson.toJson(poRepo.getCreatedPOs()));
        get("/api/accounts/billed", (req, res) -> gson.toJson(poRepo.getBilledPOs()));
        get("/api/accounts/paid", (req, res) -> gson.toJson(poRepo.getPaidPOs()));

        patch("/api/accounts/orders/:id/bill", (req, res) -> {
            int poId = Integer.parseInt(req.params("id"));
            boolean success = poRepo.updatePOStatus(poId, "BILLED");
            return gson.toJson(Map.of("success", success));
        });

        patch("/api/accounts/orders/:id/pay", (req, res) -> {
            int poId = Integer.parseInt(req.params("id"));
            boolean success = poRepo.markAsPaid(poId, java.time.LocalDateTime.now());

            if (success) {
                try {
                    PurchaseOrder po = poRepo.getPOById(poId);
                    PurchaseRequest pr = purchaseRequestRepo.getPRById(po.getPrId());

                    resourceRepo.addInventory(pr.getResourceId(), pr.getQuantity());
                    productRequestRepo.updateStatus(pr.getRequestId(), "PENDING");
                } catch (Exception e) {
                    System.err.println("Error restocking after payment: " + e.getMessage());
                }
            }

            return gson.toJson(Map.of("success", success));
        });

        get("/api/accounts/vendor-spending", (req, res) -> gson.toJson(poRepo.getSpendingByVendor()));
    }
}