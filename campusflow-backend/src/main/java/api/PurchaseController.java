package api;

import com.google.gson.Gson;
import model.PurchaseOrder;
import repository.PurchaseRequestRepository;
import repository.PurchaseOrderRepository;
import java.math.BigDecimal;
import java.util.Map;
import static spark.Spark.*;

public class PurchaseController {
    static Gson gson = GsonProvider.gson;
    static PurchaseRequestRepository prRepo = new PurchaseRequestRepository();
    static PurchaseOrderRepository poRepo = new PurchaseOrderRepository();

    public static void register() {
        get("/api/purchase/pending", (req, res) -> gson.toJson(prRepo.getPendingPRs()));

        post("/api/purchase/orders", (req, res) -> {
            Map<String, Object> body = gson.fromJson(req.body(), Map.class);
            int prId = ((Double) body.get("prId")).intValue();

            PurchaseOrder po = new PurchaseOrder();
            po.setPrId(prId);
            po.setVendorName((String) body.get("vendorName"));
            po.setTotalCost(BigDecimal.valueOf(((Number) body.get("totalCost")).doubleValue()));

            boolean success = poRepo.createPO(po);
            if (!success) { res.status(400); return gson.toJson(Map.of("error", "Failed to create PO")); }

            prRepo.updatePRStatus(prId, "PO_ISSUED");   // ← added

            res.status(201);
            return gson.toJson(Map.of("success", true));
        });

        get("/api/purchase/orders", (req, res) -> gson.toJson(poRepo.getAllPOs()));
    }
}