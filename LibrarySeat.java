// ============================================================
//  FILE: LibrarySeat.java
//  OWNER: Friend 2 (Library Module)
//  PURPOSE: Represents a single library seat / study table slot
// ============================================================

package src.library;

public class LibrarySeat {

    public enum Status { AVAILABLE, OCCUPIED, RESERVED }

    private final String seatId;      // e.g. "L-01"
    private final String zone;        // e.g. "Quiet Zone", "Group Study"
    private Status status;
    private String occupiedBy;        // student name / "" when free
    private String reservedUntil;     // HH:MM or ""

    public LibrarySeat(String seatId, String zone) {
        this.seatId       = seatId;
        this.zone         = zone;
        this.status       = Status.AVAILABLE;
        this.occupiedBy   = "";
        this.reservedUntil= "";
    }

    // ── getters ────────────────────────────────────────────────
    public String getSeatId()        { return seatId; }
    public String getZone()          { return zone; }
    public Status getStatus()        { return status; }
    public String getOccupiedBy()    { return occupiedBy; }
    public String getReservedUntil() { return reservedUntil; }

    // ── setters used by Admin ──────────────────────────────────
    public void occupy(String studentName) {
        this.status     = Status.OCCUPIED;
        this.occupiedBy = studentName;
    }

    public void reserve(String studentName, String until) {
        this.status        = Status.RESERVED;
        this.occupiedBy    = studentName;
        this.reservedUntil = until;
    }

    public void free() {
        this.status        = Status.AVAILABLE;
        this.occupiedBy    = "";
        this.reservedUntil = "";
    }

    public boolean isAvailable() { return status == Status.AVAILABLE; }

    /** CSV row: seatId,zone,status,occupiedBy,reservedUntil */
    public String toCSV() {
        return String.join(",", seatId, zone, status.name(), occupiedBy, reservedUntil);
    }

    /** Reconstruct from CSV row */
    public static LibrarySeat fromCSV(String[] cols) {
        LibrarySeat s = new LibrarySeat(cols[0], cols[1]);
        s.status        = Status.valueOf(cols[2]);
        s.occupiedBy    = cols.length > 3 ? cols[3] : "";
        s.reservedUntil = cols.length > 4 ? cols[4] : "";
        return s;
    }

    @Override
    public String toString() {
        String base = String.format("[%s] %-14s Zone: %-14s  Status: %s",
                seatId, "", zone, status);
        if (!occupiedBy.isEmpty())    base += "  User: " + occupiedBy;
        if (!reservedUntil.isEmpty()) base += "  Until: " + reservedUntil;
        return base;
    }
}
