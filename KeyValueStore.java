import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The KeyValueStore interface defines a remote interface for a key-value store.
 * Clients can interact with a remote KeyValueStore implementation using RMI (Remote Method Invocation).
 */
public interface KeyValueStore extends Remote {
    /**
     * Inserts a key-value pair into the store.
     *
     * @param key   The key to be inserted.
     * @param value The corresponding value to be associated with the key.
     * @throws RemoteException if there is a communication-related exception.
     */
    void put(String key, String value) throws RemoteException;

    /**
     * Retrieves the value associated with the specified key from the store.
     *
     * @param key The key whose value is to be retrieved.
     * @return The value associated with the key, or null if the key is not found.
     * @throws RemoteException if there is a communication-related exception.
     */
    String get(String key) throws RemoteException;

    /**
     * Deletes the key-value pair associated with the specified key from the store.
     *
     * @param key The key whose associated pair is to be deleted.
     * @throws RemoteException if there is a communication-related exception.
     */
    void delete(String key) throws RemoteException;
}
