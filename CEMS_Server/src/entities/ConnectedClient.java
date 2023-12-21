package entities;

import common.ClientStatus;

/**
 * The ConnectedClient class represents a connected client in the system. It
 * stores information such as the client's IP address, host name, and status.
 */
public class ConnectedClient {
	private String ip; // The client's IP address
	private String host; // The client's host name
	private ClientStatus status; // The client's status

	/**
	 * Constructs a ConnectedClient object with the specified IP address, host name,
	 * and status.
	 *
	 * @param ip     The IP address of the client
	 * @param host   The host name of the client
	 * @param status The status of the client
	 */
	public ConnectedClient(String ip, String host, ClientStatus status) {
		this.ip = ip;
		this.host = host;
		this.status = status;
	}

	/**
	 * Sets the IP address of the client.
	 *
	 * @param ip The IP address to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * Returns the IP address of the client.
	 *
	 * @return The IP address of the client
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Sets the host name of the client.
	 *
	 * @param host The host name to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Returns the host name of the client.
	 *
	 * @return The host name of the client
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets the status of the client.
	 *
	 * @param status The status to set
	 */
	public void setStatus(ClientStatus status) {
		this.status = status;
	}

	/**
	 * Returns the status of the client.
	 *
	 * @return The status of the client
	 */
	public ClientStatus getStatus() {
		return status;
	}

	/**
	 * Returns a string representation of the ConnectedClient object.
	 *
	 * @return A string representation of the ConnectedClient object
	 */
	@Override
	public String toString() {
		return "[ip=" + ip + ", host=" + host + ", status=" + status + "]";
	}
}