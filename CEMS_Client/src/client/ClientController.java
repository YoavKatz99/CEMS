package client;

import common.ChatIF;
import common.MissionPack;

/**
 * The ClientController class acts as a controller for the client-side of the
 * application. It handles the communication between the client and the server.
 */
public class ClientController implements ChatIF {

	private static MissionPack response;
	ClientHandler client;

	/**
	 * Constructor for the ClientController class.
	 *
	 * @param host The host name or IP address of the server.
	 * @param port The port number of the server.
	 */
	public ClientController(final String host, final int port) {
		try {
			client = new ClientHandler(host, port, this);
		} catch (Exception exception) {
			System.out.println("Error: Can't setup connection! Terminating client.");
			System.exit(1);
		}
	}

	/**
	 * Sends a message to the server.
	 *
	 * @param str The message to be sent.
	 */
	public void accept(Object str) {
		System.out.println("in accept");
		client.handleMessageFromClientUI(str);
	}

	/**
	 * Displays a message in the client's console.
	 *
	 * @param message The message to be displayed.
	 */
	@Override
	public void display(final String message) {
		System.out.println("> " + message);
	}

	/**
	 * Returns the response received from the server.
	 *
	 * @return The response from the server.
	 */
	public MissionPack getResponseFromServer() {
		return response;
	}

	/**
	 * Sets the response received from the server.
	 *
	 * @param obj The response object to be set.
	 */
	public static void setResponseFromServer(final MissionPack obj) {
		ClientController.response = obj;
	}

}
