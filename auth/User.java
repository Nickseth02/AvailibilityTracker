// ============================================================
//  FILE: User.java
//  OWNER: Friend 3 (All Features / Integration Lead)
//  PURPOSE: Represents a logged-in user (Student or Admin)
// ============================================================

package src.auth;

public class User {

    public enum Role {
        STUDENT, ADMIN
    }

    private final String username;
    private final Role role;

    public User(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() { return username; }
    public Role getRole()       { return role; }
    public boolean isAdmin()    { return role == Role.ADMIN; }

    @Override
    public String toString() {
        return username + " [" + role + "]";
    }
}
