// ============================================================
//  FILE: CSVHandler.java
//  OWNER: Friend 3 (All Features / Integration Lead)
//  PURPOSE: Generic CSV read/write utility used by both
//           LibrarySystem and LabSystem
// ============================================================

package src.storage;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CSVHandler {

    /**
     * Read every row from a CSV file.
     * Returns an empty list (never null) if file doesn't exist yet.
     */
    public static List<String[]> readAll(String filePath) {
        List<String[]> result = new ArrayList<>();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) return result;

        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                result.add(line.split(",", -1));
            }
        } catch (IOException e) {
            System.err.println("[CSVHandler] Error reading " + filePath + ": " + e.getMessage());
        }
        return result;
    }

    /**
     * Overwrite the file with the given rows.
     * Creates parent directories automatically.
     */
    public static void writeAll(String filePath, List<String[]> rows) {
        Path path = Paths.get(filePath);
        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(path.toFile()))) {
                for (String[] row : rows) {
                    bw.write(String.join(",", row));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("[CSVHandler] Error writing " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Append a single row to a CSV file (useful for logging).
     */
    public static void appendRow(String filePath, String[] row) {
        Path path = Paths.get(filePath);
        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter bw = new BufferedWriter(
                    new FileWriter(path.toFile(), true))) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("[CSVHandler] Error appending to " + filePath + ": " + e.getMessage());
        }
    }
}
