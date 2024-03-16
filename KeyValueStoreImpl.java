import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Implementation of the KeyValueStore interface using RMI.
 * This class provides methods to PUT, GET, and DELETE key-value pairs in a distributed key-value store.
 */
public class KeyValueStoreImpl extends UnicastRemoteObject implements KeyValueStore {
    private HashMap<String, String> store;

    /**
     * Constructor to initialize the key-value store implementation.
     * @throws RemoteException if there is a communication-related exception.
     */
    protected KeyValueStoreImpl() throws RemoteException {
        super();
        store = new HashMap<>();
    }

    /**
     * Inserts a key-value pair into the store.
     * @param key The key to be inserted.
     * @param value The corresponding value to be associated with the key.
     * @throws RemoteException if there is a communication-related exception.
     */
    @Override
    public synchronized void put(String key, String value) throws RemoteException {
        String clientIP = getClientIP();
        store.put(key, value);
        LoggerUtil.logWithTimestamp("PUT Request Received: Key=" + key + ", Value=" + value + ", Client IP=" + clientIP);
        LoggerUtil.logWithTimestamp("PUT Response Sent: Success");
    }

    /**
     * Retrieves the value associated with the specified key from the store.
     * @param key The key whose value is to be retrieved.
     * @return The value associated with the key, or null if the key is not found.
     * @throws RemoteException if there is a communication-related exception.
     */
    @Override
    public synchronized String get(String key) throws RemoteException {
        String clientIP = getClientIP();
        String value = store.get(key);
        LoggerUtil.logWithTimestamp("GET Request Received: Key=" + key + ", Client IP=" + clientIP);
        if (value != null) {
            LoggerUtil.logWithTimestamp("GET Response Sent: Value=" + value);
        } else {
            LoggerUtil.logWithTimestamp("GET Response Sent: Key not found");
        }
        return value;
    }

    /**
     * Deletes the key-value pair associated with the specified key from the store.
     * @param key The key whose associated pair is to be deleted.
     * @throws RemoteException if there is a communication-related exception.
     */
    @Override
    public synchronized void delete(String key) throws RemoteException {
        String clientIP = getClientIP();
        String value = store.remove(key);
        LoggerUtil.logWithTimestamp("DELETE Request Received: Key=" + key + ", Client IP=" + clientIP);
        if (value != null) {
            LoggerUtil.logWithTimestamp("DELETE Response Sent: Success");
        } else {
            LoggerUtil.logWithTimestamp("DELETE Response Sent: Key not found");
        }
    }

    /**
     * Utility method to retrieve the client's IP address.
     * @return The client's IP address or "Unknown" if the address cannot be determined.
     */
    private String getClientIP() {
        try {
            return java.rmi.server.RemoteServer.getClientHost();
        } catch (Exception e) {
            return "Unknown";
        }
    }

    /**
     * Main method to start the KeyValueStore server.
     * @param args Command-line arguments (not used in this implementation).
     */
    public static void main(String[] args) {
        try {
            KeyValueStoreImpl server = new KeyValueStoreImpl();
            java.rmi.registry.LocateRegistry.createRegistry(Integer.parseInt(args[0]));
            java.rmi.registry.Registry registry = java.rmi.registry.LocateRegistry.getRegistry(Integer.parseInt(args[0]));
            registry.rebind("KeyValueStore", server);
            LoggerUtil.logWithTimestamp("Server started...");
        } catch (Exception e) {
            LoggerUtil.logWithTimestamp("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
