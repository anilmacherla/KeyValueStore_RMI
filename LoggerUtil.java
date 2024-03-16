import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for logging messages with timestamps.
 */
public class LoggerUtil {
    /**
     * Logs a message with a timestamp to the console.
     * @param message The message to be logged.
     */
    public static void logWithTimestamp(String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String timestamp = dateFormat.format(new Date());
        System.out.println("[" + timestamp + "] " + message);
    }
}
