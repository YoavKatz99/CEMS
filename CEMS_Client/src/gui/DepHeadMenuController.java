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
 * The controller class for the DepHeadMenuPage.fxml GUI.
 * It handles the menu options and actions for a department head user.
 */
public class DepHeadMenuController {
	@FXML
	private Button RequestsButton;
	@FXML
	private Button systemDataButton;
	@FXML
	private Button reportsButton;
	@FXML
	private Button LogoutButton;
	@FXML
	private Label welcomeLabel = new Label();

	private User user = LoginController.getLoggedInUser();

	private static String department;

	  /**
     * Starts the DepHeadMenuPage GUI.
     *
     * @param primaryStage The primary stage for the GUI.
     * @throws Exception if an error occurs during GUI loading.
     */
	public void start(final Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(this.getClass().getResource("/gui/DepHeadMenuPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS Student");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	   /**
     * Initializes the controller after the GUI is loaded.
     * Sets the welcome message with the user's name and department.
     */
	public void initialize() {
		welcomeLabel.setText("Welcome " + user.getName() + " " + user.getSurname() + "\n" + department.toUpperCase()
				+ " Department");
		welcomeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
		welcomeLabel.setFont(Font.font("Helvetica"));
		welcomeLabel.setTextFill(Color.ORANGE);
		welcomeLabel.setWrapText(true);
		welcomeLabel.setPadding(new Insets(10));
	}

    /**
     * Handles the action when the Requests button is clicked.
     * Opens the DepHeadRequestPage GUI.
     *
     * @param event The action event.
     * @throws Exception if an error occurs during the transition to the requests page.
     */
	@FXML
	public void handleRequestsClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		DepHeadRequestController controller = new DepHeadRequestController();
		controller.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	 /**
     * Handles the action when the System Data button is clicked.
     * Opens the DepHeadSysemDataPage GUI.
     *
     * @param event The action event.
     * @throws Exception if an error occurs during the transition to the system data page.
     */
	@FXML
	public void handlesystemDataClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		DepHeadSysemDataController controller = new DepHeadSysemDataController();
		controller.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	 /**
     * Handles the action when the Reports button is clicked.
     * Opens the DepHeadReportsPage GUI.
     *
     * @param event The action event.
     * @throws Exception if an error occurs during the transition to the reports page.
     */
	@FXML
	void handleReportsClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		DepHeadReportsController controller = new DepHeadReportsController();
		controller.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}


    /**
     * Handles the action when the Logout button is clicked.
     * Logs out the user and opens the login page.
     *
     * @param event The action event.
     * @throws Exception if an error occurs during the transition to the login page.
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
     * Gets the department associated with the menu.
     *
     * @return The department name.
     */
	public static String getDepartment() {
		return department;
	}


    /**
     * Sets the department for the menu.
     *
     * @param department The department name.
     */
	public static void setDepartment(String department) {
		DepHeadMenuController.department = department;
	}

}
