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
 * The controller class for the Lecturer GUI.
 */
public class LecturerTestsMenuController {
	@FXML
	private Button manageTestsButton;

	@FXML
	private Button CheckButton;

	@FXML
	private Button CreateButton;

	@FXML
	private Button BackButton;

	private User user = LoginController.getLoggedInUser();

	/**
	 * Starts the Lecturer GUI.
	 *
	 * @param primaryStage the primary stage for the GUI
	 * @throws Exception if an error occurs during GUI initialization
	 */
	public void start(final Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(this.getClass().getResource("/gui/LecturerTestsMenuPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS Lecturer");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Event handler for the manageTestsButton click event.
	 *
	 * @param event the action event
	 * @throws Exception if an error occurs during handling the event
	 */
	@FXML
	public void handlemanageTestsClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		LecturerManageTestsController controller = new LecturerManageTestsController();
		controller.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	
  

	@FXML
	public void handleCheckClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		LecturerGradesCheck controller = new LecturerGradesCheck();
		controller.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	/**
	 * Event handler for the TestsButton click event.
	 *
	 * @param event the action event
	 * @throws Exception if an error occurs during handling the event
	 */
	@FXML
	public void handleTestClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		CreateTest1Controller controller = new CreateTest1Controller();
		controller.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	/**
	 * Initializes the controller.
	 */
	public void initialize() {

	}

	  /**
     * Handles the action when the Back button is clicked.
     *
     * @param event The action event.
     * @throws Exception if an error occurs during the transition to the lecturer menu.
     */
	@FXML
	public void handleBackClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		LecturerController clientController = new LecturerController();
		clientController.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	
}
