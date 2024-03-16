import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * This class implements a client for interacting with a remote KeyValueStore server using RMI.
 * It provides methods to connect to the server, send commands (PUT/GET/DELETE), and handle user input.
 */
public class KeyValueClient {
    /**
     * The main method to start the KeyValueClient.
     * @param args Command-line arguments: <host> <port>
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            LoggerUtil.logWithTimestamp("Usage: java KeyValueClient <host> <port>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        boolean prepopulate = true; // Flag to track if prepopulation is required

        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            KeyValueStore stub = (KeyValueStore) registry.lookup("KeyValueStore");
            LoggerUtil.logWithTimestamp("Connected to server.");

            Scanner scanner = new Scanner(System.in);

            // Ask user if prepopulation is needed
            System.out.print("Do you want to prepopulate values? (yes/no): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (!input.equals("yes")) {
                prepopulate = false;
            }

            // Prepopulate values if requested
            if (prepopulate) {
                prepopulateValues(stub);
            }

            // Start the main loop for user commands
            while (true) {
                System.out.print("Enter command (PUT/GET/DELETE/QUIT): ");
                input = scanner.nextLine().trim().toLowerCase();

                if (input.equalsIgnoreCase("quit")) {
                    break;
                }

                String[] parts = input.split("\\s+", 3);
                String command = parts[0].toUpperCase();
                String key = null;
                String value = null;

                if (parts.length > 1) {
                    key = parts[1];
                }
                if (parts.length > 2) {
                    value = parts[2];
                }

                switch (command) {
                    case "PUT":
                        if (key != null && value != null) {
                            stub.put(key, value);
                            LoggerUtil.logWithTimestamp("PUT request sent.");
                        } else {
                            LoggerUtil.logWithTimestamp("Invalid PUT command format. Usage: PUT <Key> <Value>");
                        }
                        break;
                    case "GET":
                        if (key != null) {
                            String result = stub.get(key);
                            LoggerUtil.logWithTimestamp("GET result: " + (result!=null ? result : "Key: "+key+ " does not exists!" ) );
                        } else {
                            LoggerUtil.logWithTimestamp("Invalid GET command format. Usage: GET <Key>");
                        }
                        break;
                    case "DELETE":
                        if (key != null) {
                            stub.delete(key);
                            LoggerUtil.logWithTimestamp("DELETE request sent.");
                        } else {
                            LoggerUtil.logWithTimestamp("Invalid DELETE command format. Usage: DELETE <Key>");
                        }
                        break;
                    default:
                        LoggerUtil.logWithTimestamp("Unknown command: " + command);
                }
            }
            scanner.close();
        } catch (Exception e) {
            LoggerUtil.logWithTimestamp("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Prepopulates the KeyValueStore with default values.
     * @param stub The KeyValueStore stub to use for prepopulation.
     */
    private static void prepopulateValues(KeyValueStore stub) {
        String[] keys = {"Angorra", "Bahrain", "Belgium", "Cuba", "Egypt", "India"};
        String[] values = {"Luanda", "Manama", "Brussels", "Havana", "Cairo", "Delhi"};

        for (int i = 0; i < keys.length; i++) {
            try {
                stub.put(keys[i], values[i]);
                LoggerUtil.logWithTimestamp("Prepopulated: Key=" + keys[i] + ", Value=" + values[i]);
            } catch (Exception e) {
                LoggerUtil.logWithTimestamp("Error prepopulating: " + e.toString());
            }
        }
    }
}
