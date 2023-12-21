package gui;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Application;
import javafx.stage.Stage;
import server.ServerConfiguration;

/**
 * The server user interface class. This class extends the JavaFX Application
 * class.
 */
public class ServerUI extends Application {
	static ServerConfiguration sv;

	/**
	 * The main method of the server application. It launches the JavaFX
	 * application.
	 *
	 * @param args The command-line arguments.
	 * @throws Exception If an error occurs during application startup.
	 */
	public static void main(String[] args) throws Exception {
		launch(args);
	}

	/**
	 * Starts the server user interface.
	 *
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If an error occurs during application startup.
	 */
	public void start(Stage primaryStage) throws Exception {
		ServerController serverGui = new ServerController();
		serverGui.start(primaryStage);
	}

	/**
	 * Runs the server with the specified port.
	 *
	 * @param p The port number to listen on.
	 */
	public static void runServer(String p) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(p); // Set port to 5555

		} catch (Throwable t) {
			System.out.println("ERROR - Could not connect!");
		}

		sv = new ServerConfiguration(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ERROR - Could not listen for clients!");
		}
	}

	/**
	 * Disconnects the server.
	 */
	public static void disconnect() {
		if (sv == null)
			sv.stopListening();
		else
			try {
				sv.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		System.out.println("Server Disconnected");
	}

}
