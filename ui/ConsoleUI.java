// ============================================================
//  FILE: ConsoleUI.java
//  OWNER: Friend 3 (All Features / Integration Lead)
//  PURPOSE: Text-based menu system — ties Auth, Library, Lab
//           together. Student menus and Admin menus live here.
// ============================================================

package src.ui;

import src.auth.AuthManager;
import src.auth.User;
import src.lab.ComputerPC;
import src.lab.LabSystem;
import src.library.LibrarySeat;
import src.library.LibrarySystem;
import src.storage.ActivityLogger;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private final Scanner      scanner = new Scanner(System.in);
    private final AuthManager  auth    = new AuthManager();
    private final LibrarySystem library = new LibrarySystem();
    private final LabSystem    lab     = new LabSystem();

    // ── ENTRY ─────────────────────────────────────────────────

    public void start() {
        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();
            if ("2".equals(choice)) {
                System.out.println("Goodbye!");
                break;
            }
            if ("1".equals(choice)) {
                User user = loginPrompt();
                if (user == null) {
                    System.out.println("Invalid credentials. Try again.\n");
                } else {
                    System.out.println("\nWelcome, " + user + "!\n");
                    if (user.isAdmin()) adminMenu(user);
                    else               studentMenu(user);
                }
            }
        }
    }

    // ── LOGIN ─────────────────────────────────────────────────

    private User loginPrompt() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        return auth.login(username, password);
    }

    // ── STUDENT MENU ──────────────────────────────────────────

    private void studentMenu(User user) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- STUDENT MENU ---");
            System.out.println("1. View Library Summary");
            System.out.println("2. View All Library Seats");
            System.out.println("3. View Available Library Seats");
            System.out.println("4. Search Library by Zone");
            System.out.println("5. View Computer Lab Summary");
            System.out.println("6. View All PCs");
            System.out.println("7. View Available PCs");
            System.out.println("8. Search PCs by Lab");
            System.out.println("9. Check Specific Seat / PC");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> System.out.println("\n" + library.getSummary());
                case "2" -> printList(library.getAllSeats());
                case "3" -> printList(library.getAvailableSeats());
                case "4" -> {
                    System.out.print("Enter zone keyword: ");
                    printList(library.getSeatsByZone(scanner.nextLine().trim()));
                }
                case "5" -> System.out.println("\n" + lab.getSummary());
                case "6" -> printList(lab.getAllPCs());
                case "7" -> printList(lab.getAvailablePCs());
                case "8" -> {
                    System.out.println("Labs: " + lab.getLabNames());
                    System.out.print("Enter lab name: ");
                    printList(lab.getPCsByLab(scanner.nextLine().trim()));
                }
                case "9" -> checkSpecificAsset();
                case "0" -> running = false;
                default  -> System.out.println("Invalid option.");
            }
        }
    }

    // ── ADMIN MENU ────────────────────────────────────────────

    private void adminMenu(User user) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("=== LIBRARY ===");
            System.out.println(" 1. View all library seats");
            System.out.println(" 2. Occupy a seat");
            System.out.println(" 3. Reserve a seat");
            System.out.println(" 4. Free a seat");
            System.out.println(" 5. Add new seat");
            System.out.println(" 6. Remove a seat");
            System.out.println("=== COMPUTER LAB ===");
            System.out.println(" 7. View all PCs");
            System.out.println(" 8. Occupy a PC");
            System.out.println(" 9. Free a PC");
            System.out.println("10. Set PC to Maintenance");
            System.out.println("11. Update PC software notes");
            System.out.println("12. Add new PC");
            System.out.println("13. Remove a PC");
            System.out.println(" 0. Logout");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                // ---- Library ----
                case "1" -> printList(library.getAllSeats());
                case "2" -> {
                    System.out.print("Seat ID: ");  String sid  = scanner.nextLine().trim();
                    System.out.print("Student name: "); String sn = scanner.nextLine().trim();
                    boolean ok = library.occupySeat(sid, sn);
                    System.out.println(ok ? "Seat occupied." : "Failed — seat not found or not available.");
                    if (ok) ActivityLogger.log(user.getUsername(), "OCCUPY_SEAT", sid);
                }
                case "3" -> {
                    System.out.print("Seat ID: ");    String sid   = scanner.nextLine().trim();
                    System.out.print("Student name: "); String sn  = scanner.nextLine().trim();
                    System.out.print("Reserved until (HH:MM): "); String until = scanner.nextLine().trim();
                    boolean ok = library.reserveSeat(sid, sn, until);
                    System.out.println(ok ? "Seat reserved." : "Failed.");
                    if (ok) ActivityLogger.log(user.getUsername(), "RESERVE_SEAT", sid);
                }
                case "4" -> {
                    System.out.print("Seat ID to free: "); String sid = scanner.nextLine().trim();
                    boolean ok = library.freeSeat(sid);
                    System.out.println(ok ? "Seat freed." : "Seat not found.");
                    if (ok) ActivityLogger.log(user.getUsername(), "FREE_SEAT", sid);
                }
                case "5" -> {
                    System.out.print("New Seat ID: "); String sid  = scanner.nextLine().trim();
                    System.out.print("Zone: ");        String zone = scanner.nextLine().trim();
                    library.addSeat(sid, zone);
                    System.out.println("Seat added.");
                    ActivityLogger.log(user.getUsername(), "ADD_SEAT", sid);
                }
                case "6" -> {
                    System.out.print("Seat ID to remove: "); String sid = scanner.nextLine().trim();
                    boolean ok = library.removeSeat(sid);
                    System.out.println(ok ? "Seat removed." : "Not found.");
                    if (ok) ActivityLogger.log(user.getUsername(), "REMOVE_SEAT", sid);
                }
                // ---- Lab ----
                case "7"  -> printList(lab.getAllPCs());
                case "8"  -> {
                    System.out.print("PC ID: ");       String pid = scanner.nextLine().trim();
                    System.out.print("Student name: ");String sn  = scanner.nextLine().trim();
                    boolean ok = lab.occupyPC(pid, sn);
                    System.out.println(ok ? "PC occupied." : "Failed — not found or unavailable.");
                    if (ok) ActivityLogger.log(user.getUsername(), "OCCUPY_PC", pid);
                }
                case "9"  -> {
                    System.out.print("PC ID to free: "); String pid = scanner.nextLine().trim();
                    boolean ok = lab.freePC(pid);
                    System.out.println(ok ? "PC freed." : "Not found.");
                    if (ok) ActivityLogger.log(user.getUsername(), "FREE_PC", pid);
                }
                case "10" -> {
                    System.out.print("PC ID: "); String pid = scanner.nextLine().trim();
                    boolean ok = lab.setMaintenance(pid);
                    System.out.println(ok ? "PC set to maintenance." : "Not found.");
                    if (ok) ActivityLogger.log(user.getUsername(), "MAINTENANCE_PC", pid);
                }
                case "11" -> {
                    System.out.print("PC ID: ");       String pid   = scanner.nextLine().trim();
                    System.out.print("Software notes: "); String notes = scanner.nextLine().trim();
                    boolean ok = lab.updateSoftware(pid, notes);
                    System.out.println(ok ? "Notes updated." : "Not found.");
                }
                case "12" -> {
                    System.out.print("New PC ID: ");  String pid  = scanner.nextLine().trim();
                    System.out.print("Lab name: ");   String lname= scanner.nextLine().trim();
                    lab.addPC(pid, lname);
                    System.out.println("PC added.");
                    ActivityLogger.log(user.getUsername(), "ADD_PC", pid);
                }
                case "13" -> {
                    System.out.print("PC ID to remove: "); String pid = scanner.nextLine().trim();
                    boolean ok = lab.removePC(pid);
                    System.out.println(ok ? "PC removed." : "Not found.");
                    if (ok) ActivityLogger.log(user.getUsername(), "REMOVE_PC", pid);
                }
                case "0"  -> running = false;
                default   -> System.out.println("Invalid option.");
            }
        }
    }

    // ── SHARED ────────────────────────────────────────────────

    private void checkSpecificAsset() {
        System.out.print("Enter asset ID (e.g. L-01 or PC-03): ");
        String id = scanner.nextLine().trim();
        library.findById(id).ifPresentOrElse(
                s -> System.out.println("\nLibrary Seat: " + s),
                () -> lab.findById(id).ifPresentOrElse(
                        p -> System.out.println("\nPC: " + p),
                        () -> System.out.println("Asset not found.")
                )
        );
    }

    private <T> void printList(List<T> items) {
        if (items.isEmpty()) { System.out.println("  (none found)"); return; }
        System.out.println();
        items.forEach(item -> System.out.println("  " + item));
    }
}
