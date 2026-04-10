// ============================================================
//  FILE: LibrarySystem.java
//  OWNER: Friend 2 (Library Module)
//  PURPOSE: All library seat operations — view, occupy, free,
//           reserve, search by zone, stats
// ============================================================

package src.library;

import src.storage.CSVHandler;

import java.util.*;
import java.util.stream.Collectors;

public class LibrarySystem {

    private static final String DATA_FILE = "data/library_seats.csv";

    private final List<LibrarySeat> seats = new ArrayList<>();

    public LibrarySystem() {
        loadSeats();
    }

    // ── STUDENT FEATURES ──────────────────────────────────────

    /** View all seats */
    public List<LibrarySeat> getAllSeats() {
        return Collections.unmodifiableList(seats);
    }

    /** View only available seats */
    public List<LibrarySeat> getAvailableSeats() {
        return seats.stream()
                .filter(LibrarySeat::isAvailable)
                .collect(Collectors.toList());
    }

    /** Filter by zone name (case-insensitive partial match) */
    public List<LibrarySeat> getSeatsByZone(String zone) {
        return seats.stream()
                .filter(s -> s.getZone().toLowerCase().contains(zone.toLowerCase()))
                .collect(Collectors.toList());
    }

    /** Find a seat by its ID */
    public Optional<LibrarySeat> findById(String seatId) {
        return seats.stream()
                .filter(s -> s.getSeatId().equalsIgnoreCase(seatId))
                .findFirst();
    }

    /** Quick availability summary */
    public String getSummary() {
        long total     = seats.size();
        long available = seats.stream().filter(LibrarySeat::isAvailable).count();
        long occupied  = seats.stream()
                .filter(s -> s.getStatus() == LibrarySeat.Status.OCCUPIED).count();
        long reserved  = seats.stream()
                .filter(s -> s.getStatus() == LibrarySeat.Status.RESERVED).count();
        return String.format(
                "Library — Total: %d | Available: %d | Occupied: %d | Reserved: %d",
                total, available, occupied, reserved);
    }

    // ── ADMIN FEATURES ────────────────────────────────────────

    /**
     * Mark a seat as occupied.
     * @return true if seat found and was available, false otherwise
     */
    public boolean occupySeat(String seatId, String studentName) {
        Optional<LibrarySeat> opt = findById(seatId);
        if (opt.isEmpty()) return false;
        LibrarySeat seat = opt.get();
        if (!seat.isAvailable()) return false;
        seat.occupy(studentName);
        saveSeats();
        return true;
    }

    /**
     * Reserve a seat.
     */
    public boolean reserveSeat(String seatId, String studentName, String until) {
        Optional<LibrarySeat> opt = findById(seatId);
        if (opt.isEmpty()) return false;
        LibrarySeat seat = opt.get();
        if (!seat.isAvailable()) return false;
        seat.reserve(studentName, until);
        saveSeats();
        return true;
    }

    /**
     * Free a seat (admin clears it).
     */
    public boolean freeSeat(String seatId) {
        Optional<LibrarySeat> opt = findById(seatId);
        if (opt.isEmpty()) return false;
        opt.get().free();
        saveSeats();
        return true;
    }

    /**
     * Admin: add a brand new seat.
     */
    public void addSeat(String seatId, String zone) {
        seats.add(new LibrarySeat(seatId, zone));
        saveSeats();
    }

    /**
     * Admin: remove a seat.
     */
    public boolean removeSeat(String seatId) {
        boolean removed = seats.removeIf(s -> s.getSeatId().equalsIgnoreCase(seatId));
        if (removed) saveSeats();
        return removed;
    }

    // ── PERSISTENCE ───────────────────────────────────────────

    private void loadSeats() {
        List<String[]> rows = CSVHandler.readAll(DATA_FILE);
        if (rows.isEmpty()) {
            seedDefaultSeats();
            return;
        }
        for (String[] row : rows) {
            if (row.length < 3) continue;
            seats.add(LibrarySeat.fromCSV(row));
        }
    }

    private void saveSeats() {
        List<String[]> rows = seats.stream()
                .map(s -> s.toCSV().split(",", -1))
                .collect(Collectors.toList());
        CSVHandler.writeAll(DATA_FILE, rows);
    }

    private void seedDefaultSeats() {
        String[] zones = {"Quiet Zone", "Quiet Zone", "Quiet Zone",
                "Group Study", "Group Study",
                "Reference", "Reference",
                "Periodicals", "Periodicals", "Digital Corner"};
        for (int i = 1; i <= 10; i++) {
            String id = String.format("L-%02d", i);
            seats.add(new LibrarySeat(id, zones[i - 1]));
        }
        saveSeats();
    }
}
