package api;

import com.google.gson.Gson;
import model.ProductRequest;
import repository.ProductRequestRepository;
import java.util.List;
import java.util.Map;
import static spark.Spark.*;

public class HODController {
    static Gson gson = GsonProvider.gson;
    static ProductRequestRepository prRepo = new ProductRequestRepository();

    public static void register() {
        post("/api/hod/requests", (req, res) -> {
            Map<String, Object> body = gson.fromJson(req.body(), Map.class);
            ProductRequest pr = new ProductRequest();
            pr.setHodId(((Double) body.get("hodId")).intValue());
            pr.setResourceId(((Double) body.get("resourceId")).intValue());
            pr.setQuantity(((Double) body.get("quantity")).intValue());
            pr.setStatus("PENDING");
            pr.setNotes((String) body.get("notes"));

            boolean success = prRepo.createRequest(pr);
            if (!success) { res.status(400); return gson.toJson(Map.of("error", "That resource doesn't exist. Check the resource ID and try again.")); }
            res.status(201);
            return gson.toJson(Map.of("success", true));
        });

        get("/api/hod/requests/:hodId", (req, res) -> {
            int hodId = Integer.parseInt(req.params("hodId"));
            List<ProductRequest> requests = prRepo.getRequestsByHod(hodId);
            return gson.toJson(requests);
        });
    }
}