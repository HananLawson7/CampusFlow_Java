package util;

public class Validation {

    /**
     * Checks if a string is null, empty, or just blank spaces.
     */
    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Validates that the username meets corporate format rules.
     * (e.g., lowercase letters, numbers, underscores allowed, length 3-20)
     */
    public static boolean isValidUsername(String username) {
        if (!isNotEmpty(username)) return false;
        String usernameRegex = "^[a-zA-Z0-9_]{3,20}$";
        return username.matches(usernameRegex);
    }

    /**
     * Basic defensive sanity check for password length.
     */
    public static boolean isValidPassword(String password) {
        if (!isNotEmpty(password)) return false;
        // Ensures the password isn't dangerously short before passing to auth
        return password.length() >= 4;
    }
}