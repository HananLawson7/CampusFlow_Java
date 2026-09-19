package api;

import com.google.gson.Gson;
import model.User;
import repository.UserRepository;
import java.util.Map;
import static spark.Spark.post;
import static spark.Spark.get;

public class AuthController {
    static Gson gson = GsonProvider.gson;
    static UserRepository userRepo = new UserRepository();

    public static void register() {
        post("/api/login", (req, res) -> {
            Map<String, String> body = gson.fromJson(req.body(), Map.class);
            User user = userRepo.findByUsernameAndPassword(body.get("username"), body.get("password"));

            if (user == null) {
                res.status(401);
                return gson.toJson(Map.of("error", "Invalid credentials"));
            }
            return gson.toJson(user);
        });

        get("/api/users/:id", (req, res) -> {
            User user = userRepo.findById(Integer.parseInt(req.params("id")));
            if (user == null) { res.status(404); return gson.toJson(Map.of("error", "Not found")); }
            return gson.toJson(user);
        });
    }
}