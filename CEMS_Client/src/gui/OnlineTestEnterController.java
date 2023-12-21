package gui;

import javafx.stage.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.control.TreeTableView;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import common.Question;
import common.Test;
import common.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Node;
import javafx.util.Callback;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import client.ClientMissionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Controller class for the Online Test Enterance GUI. This class handles the
 * user interface and logic for entering an online test.
 */
public class OnlineTestEnterController implements Initializable {
	@FXML
	private Button BackBtn;

	@FXML
	private Button EnterCodeBtn;

	@FXML
	private TextField codeField;

	@FXML
	private Label codeStatus;

	@FXML
	private Button enterIDBtn;

	@FXML
	private TextField idField;

	@FXML
	private Label idStatus;

	@FXML
	private Label instruction;
	private static String testId = null;
	private String testCode = null;
	private static Test test = null;

	/**
	 * Handles the button click event for entering the test code.
	 *
	 * @param event the action event triggered by the button click
	 */
	@FXML
	void handleCodeEnter(ActionEvent event) {
		testId = (String) ClientMissionHandler.GET_ID_FROM_CODE(codeField.getText());
		if (testId != null) {
			test = (Test) ClientMissionHandler.GET_SINGLE_TEST(testId);
			if (test != null) {
				if (!test.getStatus().equals("Active")) {
					codeStatus.setTextFill(Color.RED);
					codeStatus.setText("Test is not available. Please contact your lecturer.");
				} else {
					codeStatus.setTextFill(Color.GREEN);
					codeStatus.setText("Test found!");
					idField.setOpacity(1);
					enterIDBtn.setOpacity(1);
					instruction.setOpacity(1);
				}
				codeStatus.setOpacity(1);

			}

		} else {
			PopUpController pop = new PopUpController();
			pop.showAlert("TEST NOT EXIST!");
		}

	}

	/**
	 * Handles the button click event for entering the user ID.
	 *
	 * @param event the action event triggered by the button click
	 * @throws Exception if an error occurs during the test start
	 */
	@FXML
	void handleIDEnter(ActionEvent event) throws Exception {
		User user = LoginController.getLoggedInUser();
		if (idField.getText().equals(user.getUserID())) {
			Stage primaryStage = new Stage();
			OnlineTestController clientController = new OnlineTestController();
			clientController.start(primaryStage);
			((Node) event.getSource()).getScene().getWindow().hide();

		} else {
			idStatus.setOpacity(1);
			idStatus.setTextFill(Color.RED);
			idStatus.setText("Wrong ID. Please try again.");
		}

	}

	/**
	 * Initializes the controller. This method is automatically called after the
	 * FXML file has been loaded.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		codeStatus.setOpacity(0);
		instruction.setOpacity(0);
		enterIDBtn.setOpacity(0);
		idField.setOpacity(0);
		idStatus.setOpacity(0);

	}

	/**
	 * Starts the JavaFX application. This method is called to display the Online
	 * Test Enter GUI.
	 *
	 * @param primaryStage the primary stage for the application
	 * @throws Exception if an error occurs during application startup
	 */
	public void start(final Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(this.getClass().getResource("/gui/OnlineTestEnterPage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Online Test");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * Handles the button click event for going back to the student menu.
	 *
	 * @param event the action event triggered by the button click
	 * @throws Exception if an error occurs during the menu start
	 */
	@FXML
	public void handleBackClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		studentMenuController clientController = new studentMenuController();
		clientController.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	/**
	 * Retrieves the current test.
	 *
	 * @return the current test
	 */
	public static Test getTest() {
		return test;
	}

	/**
	 * Retrieves the ID of the current test.
	 *
	 * @return the ID of the current test
	 */
	public static String getTestId() {

		return testId;
	}

}
