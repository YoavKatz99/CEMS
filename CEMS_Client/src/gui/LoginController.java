package gui;

import client.ClientMissionHandler;
import common.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * The controller class for the login GUI.
 * This class is responsible for handling user interactions and events in the login screen of the application.
 * It provides methods for validating user input, performing login operations, and navigating to different screens based on the user type.
 */
public class LoginController {

	@FXML
	private TextField usernameField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private Button loginButton;

	@FXML
	private Button exitButton;

	@FXML
	public Label ErrorLabel = new Label();

	private String userName = null;
	private String Password = null;
	private static User loggedInUser;

	private double xoffset;
	private double yoffset;

	public LoginController() {
		super();
	}

	/**
	 * Initializes the controller.
	 */
	public void initialize() {
		// Set the initial style for the error label
		ErrorLabel.setTextAlignment(TextAlignment.CENTER);
		ErrorLabel.setAlignment(Pos.CENTER);
		ErrorLabel.setTextFill(Color.RED);
		ErrorLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
		usernameField.getStyleClass().add("text-field-error");
		passwordField.getStyleClass().add("text-field-error");
	}

	/**
	 * Starts the login GUI.
	 *
	 * @param primaryStage the primary stage for the GUI
	 * @throws Exception if an error occurs during GUI initialization
	 */
	public void start(final Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(this.getClass().getResource("/gui/login.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS Login");
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
	 * Event handler for the loginButton click event.
	 *
	 * @param event the action event
	 * @throws Exception if an error occurs during handling the event
	 */
	@FXML
	void handleLoginButtonClicked(ActionEvent event) throws Exception {
		userName = usernameField.getText();
		Password = passwordField.getText();
		if (userName.equals("") && Password.equals("")) {
			missingDetailsError();
		} else if (Password.equals(""))
			missingPasswordError();
		else if (userName.equals(""))
			missingUserNameError();
		else if (ClientMissionHandler.LOGIN(this.userName, this.Password, this))
			((Node) event.getSource()).getScene().getWindow().hide();

	}

	/**
	 * Sets the page based on the logged-in user.
	 *
	 * @param user the logged-in user
	 * @throws Exception if an error occurs during setting the page
	 */
	@SuppressWarnings("static-access")
	public void setPage(User user) throws Exception {
		setLoggedInUser(user);
		Stage primaryStage = new Stage();
		if (user.getUserType().equals("lecturer")) {
			LecturerController clientController = new LecturerController();
			clientController.start(primaryStage);
		} else if (user.getUserType().equals("student")) {
			studentMenuController clientController = new studentMenuController();
			clientController.start(primaryStage);
		} else if (user.getUserType().equals("math_dep_head")) {
			DepHeadMenuController clientController = new DepHeadMenuController();
			clientController.setDepartment("math");
			clientController.start(primaryStage);
		} else if (user.getUserType().equals("programming_dep_head")) {
			DepHeadMenuController clientController = new DepHeadMenuController();
			clientController.setDepartment("programming");
			clientController.start(primaryStage);
		} else if (user.getUserType().equals("physics_dep_head")) {
			DepHeadMenuController clientController = new DepHeadMenuController();
			clientController.setDepartment("physics");
			clientController.start(primaryStage);
		}

		else {
			System.out.println("Invalid user type");
		}
	}

	/**
	 * Displays an error message for a non-existent user.
	 */
	public void userNotExistsError() {
		passwordField.setStyle("-fx-border-color: gray;");
		usernameField.setStyle("-fx-border-color: gray;");
		ErrorLabel.setText("User Not Found!");
	}

	/**
	 * Displays an error message for a user who is already logged in.
	 */
	public void userAlreadyLoggenInError() {
		passwordField.setStyle("-fx-border-color: gray;");
		usernameField.setStyle("-fx-border-color: gray;");
		ErrorLabel.setText("User is Logged in!");
	}

	/**
	 * Displays an error message for a missing username.
	 */
	public void missingUserNameError() {
		passwordField.setStyle("-fx-border-color: gray;");
		usernameField.setStyle("-fx-border-color: red;");
		ErrorLabel.setText("UserName field is Empty!");
	}

	/**
	 * Displays an error message for a missing password.
	 */
	public void missingPasswordError() {
		usernameField.setStyle("-fx-border-color: gray;");
		passwordField.setStyle("-fx-border-color: red;");
		ErrorLabel.setText("Password field is Empty!");

	}

	/**
	 * Displays an error message for missing username and password.
	 */
	public void missingDetailsError() {
		usernameField.getStyleClass().add("text-field-error");
		usernameField.setStyle("-fx-border-color: red;");
		passwordField.getStyleClass().add("text-field-error");
		passwordField.setStyle("-fx-border-color: red;");
		ErrorLabel.setText("UserName & Password fields are Empty!");
	}

	/**
	 * Event handler for the exitButton click event.
	 *
	 * @param event the action event
	 * @throws Exception if an error occurs during handling the event
	 */
	@FXML
	void handleExitButtonClicked(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		System.out.println("exit CEMS");
		ClientMissionHandler.DISCONNECT_FROM_SERVER();
	}

	/**
	 * Retrieves the currently logged-in user.
	 *
	 * @return the logged-in user
	 */
	public static User getLoggedInUser() {
		return loggedInUser;
	}

	/**
	 * Sets the currently logged-in user.
	 *
	 * @param user the logged-in user
	 */
	public static void setLoggedInUser(User user) {
		loggedInUser = user;
	}

}
