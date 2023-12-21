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
public class LecturerController {
	@FXML
	private Button manageTestsButton;
	@FXML
	private Button TestsButton;
	@FXML
	private Button QuestionsButton;
	@FXML
	private Button ReportsButton;
	@FXML
	private Button LogoutButton;
	@FXML
	private Label welcomeLabel = new Label();

	private User user = LoginController.getLoggedInUser();

    /**
     * Starts the Lecturer GUI.
     *
     * @param primaryStage the primary stage for the GUI
     * @throws Exception if an error occurs during GUI initialization
     */
	public void start(final Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(this.getClass().getResource("/gui/LecturerMenuPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS Lecturer");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	

	
    /**
     * Event handler for the ReportsButton click event.
     *
     * @param event the action event
     * @throws Exception if an error occurs during handling the event
     */
	@FXML
	public void handleReportsClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		LecturerReportController controller = new LecturerReportController();
		controller.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	
    /**
     * Event handler for the QuestionsButton click event.
     *
     * @param event the action event
     * @throws Exception if an error occurs during handling the event
     */
	public void handleQuestionsClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		CreateQuestionController controller = new CreateQuestionController();
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
		LecturerTestsMenuController controller = new LecturerTestsMenuController();
		controller.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	
	  /**
     * Initializes the controller.
     */
	public void initialize() {
		welcomeLabel.setText("Welcome " + user.getName() + " " + user.getSurname());
		welcomeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 25;");
		welcomeLabel.setFont(Font.font("Helvetica"));
		welcomeLabel.setTextFill(Color.ORANGE);
		welcomeLabel.setWrapText(true);
		welcomeLabel.setPadding(new Insets(10));
	}

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
}
