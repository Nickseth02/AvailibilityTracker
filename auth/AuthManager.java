// ============================================================
//  FILE: AuthManager.java
//  OWNER: Friend 3 (All Features / Integration Lead)
//  PURPOSE: Handles login / logout. Reads users from users.csv
//           Format: username,password,role
// ============================================================

package src.auth;

import src.storage.CSVHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthManager {

    private static final String USERS_FILE = "data/users.csv";

    // username -> [password, role]
    private final Map<String, String[]> userStore = new HashMap<>();

    public AuthManager() {
        loadUsers();
    }

    /** Returns a User object on success, null on failure. */
    public User login(String username, String password) {
        String[] info = userStore.get(username.toLowerCase());
        if (info == null) return null;

        String storedPass = info[0];
        String role       = info[1];

        if (!storedPass.equals(password)) return null;

        return new User(username, User.Role.valueOf(role.toUpperCase()));
    }

    // ── private helpers ────────────────────────────────────────

    private void loadUsers() {
        List<String[]> rows = CSVHandler.readAll(USERS_FILE);
        for (String[] row : rows) {
            if (row.length < 3) continue;
            String uname = row[0].trim().toLowerCase();
            String pass  = row[1].trim();
            String role  = row[2].trim();
            userStore.put(uname, new String[]{pass, role});
        }
        // seed defaults if file was empty
        if (userStore.isEmpty()) {
            userStore.put("admin",   new String[]{"admin123",   "ADMIN"});
            userStore.put("student", new String[]{"student123", "STUDENT"});
            persistDefaults();
        }
    }

    private void persistDefaults() {
        CSVHandler.writeAll(USERS_FILE, new java.util.ArrayList<>() {{
            add(new String[]{"admin",   "admin123",   "ADMIN"});
            add(new String[]{"student", "student123", "STUDENT"});
        }});
    }
}
