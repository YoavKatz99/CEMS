package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import client.ClientMissionHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The controller class for the client's opening screen.
 */
public class ClientOpeningScreenController {
	@FXML
	private TextField IpTxt = new TextField();
	@FXML
	private TextField PortTxt;

	@FXML
	private Button loginButton;

	public String ip = null;

	/**
	 * Event handler for the login button click event. Connects to the server using
	 * the entered IP address and port.
	 *
	 * @param event The mouse event triggered by clicking the login button.
	 * @throws Exception If an error occurs during the connection process.
	 */
	@FXML
	void clickLogin(MouseEvent event) throws Exception {
		String ipAddress = IpTxt.getText();
		String port = PortTxt.getText();
		ClientMissionHandler.CONNECT_TO_SERVER(event, ipAddress, port);
	}

	/**
	 * Starts the client's opening screen stage.
	 *
	 * @param primaryStage The primary stage for the opening screen.
	 * @throws Exception If an error occurs while loading the FXML file.
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ClientOpeningScreen.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS Connect To Server");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);

	}

	/**
	 * Sets the IP address in the IP text field.
	 */
	@FXML
	public void setIp() {
		IpTxt.setText(ip);
	}

	/**
	 * Initializes the controller. Retrieves the IP address from the
	 * `ClientMissionHandler` and sets it in the IP text field.
	 */
	public void initialize() {
		ip = ClientMissionHandler.GET_IP();
		setIp();
	}

}
