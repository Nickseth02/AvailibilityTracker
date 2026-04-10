// ============================================================
//  FILE: ActivityLogger.java
//  OWNER: Friend 3 (All Features / Integration Lead)
//  PURPOSE: Appends every admin action to activity_log.csv
//           Format: timestamp,user,action,target
// ============================================================

package src.storage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActivityLogger {

    private static final String LOG_FILE = "data/activity_log.csv";
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String username, String action, String target) {
        String timestamp = LocalDateTime.now().format(FMT);
        CSVHandler.appendRow(LOG_FILE, new String[]{timestamp, username, action, target});
    }
}
