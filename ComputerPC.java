// ============================================================
//  FILE: ComputerPC.java
//  OWNER: Friend 1 (Computer Lab Module)
//  PURPOSE: Represents a single PC in the computer lab
// ============================================================

package src.lab;

public class ComputerPC {

    public enum Status { AVAILABLE, OCCUPIED, MAINTENANCE }

    private final String pcId;        // e.g. "PC-01"
    private final String labName;     // e.g. "Lab A"
    private Status status;
    private String occupiedBy;        // student name / ""
    private String sessionStart;      // HH:MM or ""
    private String softwareNotes;     // e.g. "MATLAB, Python"

    public ComputerPC(String pcId, String labName) {
        this.pcId          = pcId;
        this.labName       = labName;
        this.status        = Status.AVAILABLE;
        this.occupiedBy    = "";
        this.sessionStart  = "";
        this.softwareNotes = "";
    }

    // ── getters ────────────────────────────────────────────────
    public String getPcId()          { return pcId; }
    public String getLabName()       { return labName; }
    public Status getStatus()        { return status; }
    public String getOccupiedBy()    { return occupiedBy; }
    public String getSessionStart()  { return sessionStart; }
    public String getSoftwareNotes() { return softwareNotes; }

    // ── state transitions ──────────────────────────────────────
    public void occupy(String studentName, String startTime) {
        this.status       = Status.OCCUPIED;
        this.occupiedBy   = studentName;
        this.sessionStart = startTime;
    }

    public void setMaintenance() {
        this.status       = Status.MAINTENANCE;
        this.occupiedBy   = "";
        this.sessionStart = "";
    }

    public void free() {
        this.status       = Status.AVAILABLE;
        this.occupiedBy   = "";
        this.sessionStart = "";
    }

    public void setSoftwareNotes(String notes) { this.softwareNotes = notes; }

    public boolean isAvailable() { return status == Status.AVAILABLE; }

    /** CSV row: pcId,labName,status,occupiedBy,sessionStart,softwareNotes */
    public String toCSV() {
        return String.join(",", pcId, labName, status.name(),
                occupiedBy, sessionStart, softwareNotes);
    }

    public static ComputerPC fromCSV(String[] cols) {
        ComputerPC pc = new ComputerPC(cols[0], cols[1]);
        pc.status        = Status.valueOf(cols[2]);
        pc.occupiedBy    = cols.length > 3 ? cols[3] : "";
        pc.sessionStart  = cols.length > 4 ? cols[4] : "";
        pc.softwareNotes = cols.length > 5 ? cols[5] : "";
        return pc;
    }

    @Override
    public String toString() {
        String base = String.format("[%s] Lab: %-8s  Status: %-12s",
                pcId, labName, status);
        if (!occupiedBy.isEmpty())    base += "  User: "  + occupiedBy;
        if (!sessionStart.isEmpty())  base += "  Since: " + sessionStart;
        if (!softwareNotes.isEmpty()) base += "  SW: "    + softwareNotes;
        return base;
    }
}
