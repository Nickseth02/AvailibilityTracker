// ============================================================
//  FILE: LabSystem.java
//  OWNER: Friend 1 (Computer Lab Module)
//  PURPOSE: All computer-lab PC operations — view, occupy,
//           free, maintenance mode, search, stats
// ============================================================

package src.lab;

import src.storage.CSVHandler;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class LabSystem {

    private static final String DATA_FILE = "data/lab_pcs.csv";
    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm");

    private final List<ComputerPC> pcs = new ArrayList<>();

    public LabSystem() {
        loadPCs();
    }

    // ── STUDENT FEATURES ──────────────────────────────────────

    /** View all PCs */
    public List<ComputerPC> getAllPCs() {
        return Collections.unmodifiableList(pcs);
    }

    /** View available PCs only */
    public List<ComputerPC> getAvailablePCs() {
        return pcs.stream()
                .filter(ComputerPC::isAvailable)
                .collect(Collectors.toList());
    }

    /** Filter by lab name */
    public List<ComputerPC> getPCsByLab(String labName) {
        return pcs.stream()
                .filter(p -> p.getLabName().equalsIgnoreCase(labName))
                .collect(Collectors.toList());
    }

    /** List distinct lab names */
    public List<String> getLabNames() {
        return pcs.stream()
                .map(ComputerPC::getLabName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /** Find PC by ID */
    public Optional<ComputerPC> findById(String pcId) {
        return pcs.stream()
                .filter(p -> p.getPcId().equalsIgnoreCase(pcId))
                .findFirst();
    }

    /** Quick availability summary */
    public String getSummary() {
        long total       = pcs.size();
        long available   = pcs.stream().filter(ComputerPC::isAvailable).count();
        long occupied    = pcs.stream()
                .filter(p -> p.getStatus() == ComputerPC.Status.OCCUPIED).count();
        long maintenance = pcs.stream()
                .filter(p -> p.getStatus() == ComputerPC.Status.MAINTENANCE).count();
        return String.format(
                "Lab     — Total: %d | Available: %d | Occupied: %d | Maintenance: %d",
                total, available, occupied, maintenance);
    }

    // ── ADMIN FEATURES ────────────────────────────────────────

    /**
     * Mark a PC as occupied.
     * @return false if PC not found or already occupied/maintenance
     */
    public boolean occupyPC(String pcId, String studentName) {
        Optional<ComputerPC> opt = findById(pcId);
        if (opt.isEmpty()) return false;
        ComputerPC pc = opt.get();
        if (!pc.isAvailable()) return false;
        pc.occupy(studentName, LocalTime.now().format(TIME_FMT));
        savePCs();
        return true;
    }

    /** Free a PC */
    public boolean freePC(String pcId) {
        Optional<ComputerPC> opt = findById(pcId);
        if (opt.isEmpty()) return false;
        opt.get().free();
        savePCs();
        return true;
    }

    /** Mark PC under maintenance */
    public boolean setMaintenance(String pcId) {
        Optional<ComputerPC> opt = findById(pcId);
        if (opt.isEmpty()) return false;
        opt.get().setMaintenance();
        savePCs();
        return true;
    }

    /** Update software notes on a PC */
    public boolean updateSoftware(String pcId, String notes) {
        Optional<ComputerPC> opt = findById(pcId);
        if (opt.isEmpty()) return false;
        opt.get().setSoftwareNotes(notes);
        savePCs();
        return true;
    }

    /** Admin: add a new PC */
    public void addPC(String pcId, String labName) {
        pcs.add(new ComputerPC(pcId, labName));
        savePCs();
    }

    /** Admin: remove a PC */
    public boolean removePC(String pcId) {
        boolean removed = pcs.removeIf(p -> p.getPcId().equalsIgnoreCase(pcId));
        if (removed) savePCs();
        return removed;
    }

    // ── PERSISTENCE ───────────────────────────────────────────

    private void loadPCs() {
        List<String[]> rows = CSVHandler.readAll(DATA_FILE);
        if (rows.isEmpty()) {
            seedDefaultPCs();
            return;
        }
        for (String[] row : rows) {
            if (row.length < 3) continue;
            pcs.add(ComputerPC.fromCSV(row));
        }
    }

    private void savePCs() {
        List<String[]> rows = pcs.stream()
                .map(p -> p.toCSV().split(",", -1))
                .collect(Collectors.toList());
        CSVHandler.writeAll(DATA_FILE, rows);
    }

    private void seedDefaultPCs() {
        String[] labs = {"Lab A", "Lab A", "Lab A", "Lab A", "Lab A",
                "Lab B", "Lab B", "Lab B", "Lab B", "Lab B"};
        String[] sw   = {"Python,Java", "Python,Java", "MATLAB", "MATLAB",
                "C++", "Python,Java", "Java", "Photoshop", "MATLAB", "C++"};
        for (int i = 1; i <= 10; i++) {
            String id = String.format("PC-%02d", i);
            ComputerPC pc = new ComputerPC(id, labs[i - 1]);
            pc.setSoftwareNotes(sw[i - 1]);
            pcs.add(pc);
        }
        savePCs();
    }
}
