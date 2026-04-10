// ============================================================
//  FILE: Main.java
//  OWNER: Friend 3 (All Features / Integration Lead)
//  PURPOSE: Entry point — launches the application
// ============================================================

package src;

import src.auth.AuthManager;
import src.auth.User;
import src.ui.ConsoleUI;

public class Main {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  College Library & Computer Lab Tracker ");
        System.out.println("==========================================\n");

        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }
}
