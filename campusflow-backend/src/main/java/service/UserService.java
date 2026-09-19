package service;

import model.User;
import repository.UserRepository;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Business logic boundary for user authentication.
     * Protects downstream layers from processing empty or malicious input text strings.
     */
    public Optional<User> authenticate(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return Optional.empty();
        }

        // Delegate lookup to database layer
        User user = userRepository.findByUsernameAndPassword(username.trim(), password);

        // Return wrapped in an Optional container to protect the UI layer from handling raw null values
        return Optional.ofNullable(user);
    }
}