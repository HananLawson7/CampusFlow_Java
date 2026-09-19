package api;

import com.google.gson.Gson;
import repository.ProductRequestRepository;
import repository.ResourceRepository;
import java.util.Map;
import static spark.Spark.*;

public class StoresController {
    static Gson gson = GsonProvider.gson;
    static ProductRequestRepository prRepo = new ProductRequestRepository();
    static ResourceRepository resourceRepo = new ResourceRepository();

    public static void register() {
        get("/api/stores/pending", (req, res) -> gson.toJson(prRepo.getPendingRequests()));

        patch("/api/stores/requests/:id/process", (req, res) -> {
            int requestId = Integer.parseInt(req.params("id"));
            String result = prRepo.processRequest(requestId, resourceRepo);
            return gson.toJson(Map.of("status", result));
        });

        get("/api/stores/inventory", (req, res) -> gson.toJson(resourceRepo.getAllResources()));
    }
}