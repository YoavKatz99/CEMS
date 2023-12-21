package common;

/**
 * The ClientStatus enum represents the status of a client connection.
 * It can be either CONNECTED or DISCONNECTED.
 */
public enum ClientStatus {
	CONNECTED("Connected", 0), 
	DISCONNECTED("Disconnected", 1);

	private ClientStatus(final String mission, final int serialNumber) {
	}
}
