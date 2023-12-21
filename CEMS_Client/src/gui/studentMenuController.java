package gui;

import client.ClientMissionHandler;
import common.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * The controller class for the student menu GUI.
 */

public class studentMenuController {
	@FXML
	private Button MenualtestButton;
	@FXML
	private Button OnlinetestsButton;
	@FXML
	private Button GradesButton;
	@FXML
	private Button LogoutButton;
	@FXML
	private Label welcomeLabel = new Label();

	private User user = LoginController.getLoggedInUser();

	private double xoffset;
	private double yoffset;

    /**
     * Starts the student menu GUI.
     *
     * @param primaryStage the primary stage for the GUI
     * @throws Exception if an error occurs during GUI initialization
     */
	public void start(final Stage primaryStage) throws Exception {
		System.out.println(user.getName());
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/StudentMenuPage.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS Student");
		primaryStage.setScene(scene);
		// event handler for when the mouse is pressed AND dragged to move the window
		root.setOnMousePressed(event1 -> {
			xoffset = event1.getSceneX();
			yoffset = event1.getSceneY();
		});
		// event handler for when the mouse is pressed AND dragged to move the window
		root.setOnMouseDragged(event1 -> {
			primaryStage.setX(event1.getScreenX() - xoffset);
			primaryStage.setY(event1.getScreenY() - yoffset);
		});
		primaryStage.show();

	}

    /**
     * Event handler for the ManualtestButton click event.
     *
     * @param event the action event
     * @throws Exception if an error occurs during handling the event
     */
	@FXML
	public void handleManualtestClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		ManualTestController clientController = new ManualTestController();
		clientController.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

    /**
     * Event handler for the GradesButton click event.
     *
     * @param event the action event
     * @throws Exception if an error occurs during handling the event
     */
	@FXML
	public void handleGradesClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		GradesViewController clientController = new GradesViewController();
		clientController.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

    /**
     * Event handler for the LogoutButton click event.
     *
     * @param event the action event
     * @throws Exception if an error occurs during handling the event
     */
	@FXML
	public void handleLogoutClick(ActionEvent event) throws Exception {
		if (ClientMissionHandler.LOGOUT(user)) {
			Stage primaryStage = new Stage();
			LoginController clientController = new LoginController();
			clientController.start(primaryStage);
			((Node) event.getSource()).getScene().getWindow().hide();
		} else
			System.out.println("Encounterd logout issue");
	}
	
    /**
     * Event handler for the OnlinetestsButton click event.
     *
     * @param event the action event
     * @throws Exception if an error occurs during handling the event
     */
	@FXML
	public void handleOnlinetestClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		OnlineTestEnterController clientController = new OnlineTestEnterController();
		clientController.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();//hide the screen 
	}

    /**
     * Initializes the student menu GUI.
     */

	public void initialize() {
		welcomeLabel.setText("Welcome " + user.getName() + " " + user.getSurname());
		welcomeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 25;");
		welcomeLabel.setFont(Font.font("Helvetica"));
		welcomeLabel.setTextFill(Color.ORANGE);
		welcomeLabel.setWrapText(true);
		welcomeLabel.setPadding(new Insets(10));
	}

}
