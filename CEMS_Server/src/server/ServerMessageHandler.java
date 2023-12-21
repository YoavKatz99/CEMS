package server;

import java.io.IOException;
import common.ClientStatus;
import common.MissionPack;
import entities.ConnectedClient;
import entities.DatabaseConnector;
import javafx.collections.ObservableList;
import ocsf.server.ConnectionToClient;

/**
 * Server message handler class that handles incoming messages from clients.
 */
public class ServerMessageHandler {
	/**
	 * The response the server make
	 */

	private static ServerMessageHandler messageHandler = null;

	private ServerMessageHandler() {
	}

	/**
	 * Retrieves the instance of the server message handler.
	 *
	 * @return The server message handler instance.
	 */
	public static ServerMessageHandler getMessageHandlerInstance() {
		if (messageHandler == null) {
			messageHandler = new ServerMessageHandler();
		}
		return messageHandler;
	}

	/**
	 * Handles incoming messages from clients.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessages(Object msg, ConnectionToClient client) {
		final MissionPack mission = (MissionPack) msg;
		System.out.println("Message received: " + mission + " from " + client);
		DatabaseConnector.parsingToData(mission, client);
		try {
			System.out.println(mission);
			client.sendToClient(mission);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the list of connected clients.
	 *
	 * @param client           The connection to the client.
	 * @param connectionStatus The connection status of the client.
	 */
	static void updateClientList(ConnectionToClient client, ClientStatus connectionStatus) {
		ObservableList<ConnectedClient> clientList = ServerConfiguration.getClientList();

		for (int i = 0; i < clientList.size(); i++) {
			/* Comparing clients by IP addresses */
			if (clientList.get(i).getIp().equals(client.getInetAddress().getHostAddress()))
				clientList.remove(i);
		}

		/*
		 * In both cases of Connect and Disconnected we will need to add Client into the
		 * list so this function covers both of them simultaneously
		 */
		clientList.add(new ConnectedClient(client.getInetAddress().getHostAddress(),
				client.getInetAddress().getHostName(), connectionStatus));
		ServerConfiguration.setClientList(clientList);
	}

}
