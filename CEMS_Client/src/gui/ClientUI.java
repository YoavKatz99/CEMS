package gui;

import client.ClientController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main class for the client UI application.
 */
public class ClientUI extends Application {
	public static ClientController chat;

	/**
	 * The main entry point of the application.
	 *
	 * @param args The command-line arguments.
	 * @throws Exception If an error occurs during application execution.
	 */
	public static void main(final String[] args) throws Exception {
		launch(args);
	}

	/**
	 * Starts the client UI by launching the opening screen stage.
	 *
	 * @param primaryStage The primary stage for the application.
	 * @throws Exception If an error occurs while starting the opening screen.
	 */
	public void start(final Stage primaryStage) throws Exception {
		ClientOpeningScreenController cl = new ClientOpeningScreenController();
		cl.start(primaryStage);
	}
}
