package api;

import com.google.gson.Gson;
import model.User;
import model.Role;
import repository.UserRepository;
import java.util.List;
import java.util.Map;
import static spark.Spark.*;

public class AdminController {
    static Gson gson = GsonProvider.gson;
    static UserRepository userRepo = new UserRepository();

    public static void register() {
        // GET all users
        get("/api/admin/users", (req, res) -> {
            List<User> users = userRepo.getAllUsers();
            return gson.toJson(users);
        });

        // CREATE user (any role)
        post("/api/admin/users", (req, res) -> {
            Map<String, Object> body = gson.fromJson(req.body(), Map.class);

            String username = (String) body.get("username");
            if (userRepo.findByUsername(username) != null) {
                res.status(409);
                return gson.toJson(Map.of("error", "Username already exists"));
            }

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword((String) body.get("password"));
            newUser.setRole(Role.valueOf((String) body.get("role")));
            newUser.setDepartment((String) body.get("department"));

            if (body.get("yearOfStudy") != null) {
                newUser.setYearOfStudy(((Double) body.get("yearOfStudy")).intValue());
            }

            // admin identity passed from client (from logged-in session)
            User admin = new User();
            admin.setRole(Role.ADMIN);

            boolean success = userRepo.createUserByAdmin(newUser, admin);
            if (!success) { res.status(400); return gson.toJson(Map.of("error", "Failed to create user")); }

            res.status(201);
            return gson.toJson(Map.of("success", true, "username", username));
        });

        // DEACTIVATE user
        patch("/api/admin/users/:id/deactivate", (req, res) -> {
            int userId = Integer.parseInt(req.params("id"));
            boolean success = userRepo.deactivateUser(userId);
            return gson.toJson(Map.of("success", success));
        });

        // RESET password
        patch("/api/admin/users/:id/password", (req, res) -> {
            int userId = Integer.parseInt(req.params("id"));
            Map<String, String> body = gson.fromJson(req.body(), Map.class);
            boolean success = userRepo.updatePassword(userId, body.get("newPassword"));
            return gson.toJson(Map.of("success", success));
        });
    }
}