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
 * The controller class for the Manual Test screen.
 */
public class ManualTestController implements EventHandler<WindowEvent> {

	@FXML
	private Button LogoutButton;

	@FXML
	private Button submitButton;
	@FXML
	private Button BackBtn;

	@FXML
	private Button downloadButtom;

	@FXML
	private Button uploadButtom;

	@FXML
	public TextField codeField;

	@FXML
	private TextArea outputTextArea;

	@FXML
	private TableColumn<Test, String> idcol;

	@FXML
	private TableColumn<Test, String> subjectcol;

	@FXML
	private TableColumn<Test, String> coursecol;

	@FXML
	private TableColumn<Test, String> authorcol;

	@FXML
	private TableColumn<Test, Integer> duration;

	@FXML
	private TableColumn<Test, String> codeCol;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TableView<Test> table;

	@FXML
	private Label timerLabel;
	@FXML
	private Button Hidebutton;

	private Timeline timeline;
	private int secondsElapsed;
	private Test selectedTest;
	private Timer timer = new Timer();
	String enteredCode;
	String id;
	String request = null;
	int flag = 1;
	Test test;
	ObservableList<Test> Test = FXCollections.observableArrayList();
	User user = LoginController.getLoggedInUser();

	/**
	 * Starts the Manual Test screen.
	 * 
	 * @param primaryStage The primary stage for the application.
	 * @throws Exception if an error occurs during the loading of the FXML file.
	 */
	public void start(final Stage primaryStage) throws Exception {
		System.out.println("ManuallTestController");
		Parent root = FXMLLoader.load(this.getClass().getResource("/gui/manualTestController.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS Student");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setColumnsInTable() {
		idcol.setCellValueFactory((Callback) new PropertyValueFactory<Test, String>("ID"));
		subjectcol.setCellValueFactory((Callback) new PropertyValueFactory<Test, String>("Subject"));
		coursecol.setCellValueFactory((Callback) new PropertyValueFactory<Test, String>("Course"));
		authorcol.setCellValueFactory((Callback) new PropertyValueFactory<Test, String>("Author"));
		duration.setCellValueFactory((Callback) new PropertyValueFactory<Test, Integer>("Duration"));
		codeCol.setCellValueFactory((Callback) new PropertyValueFactory<Test, String>("TestCode"));
	}

	/**
	 * Initializes the table, columns, and timer.
	 */
	public void initialize() {
		setColumnsInTable();
		// This method is requesting data from the Server
		Test.clear();
		// Setting the Data to be displayed in the TableView
		table.setItems(Test);
		table.autosize();
		table.setEditable(true);
		ClientMissionHandler.GET_TESTS(Test, this.table);

	}

	/**
	 * Event handler for the "upload" button. Opens a file chooser dialog and allows
	 * the user to select a test file (Word document). If a file is selected, it
	 * calls the UPLOAD_TEST method of the ClientMissionHandler class.
	 *
	 * @param event The mouse event that triggered the upload action.
	 * @throws IOException If an I/O error occurs during file selection.
	 */
	@FXML
	void upload(MouseEvent event) throws IOException {
		System.out.println("upload");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Test File");

		// Set the initial directory for file selection
		String userHomeDirectory = System.getProperty("user.home");
		fileChooser.setInitialDirectory(new File(userHomeDirectory));

		// Add filters to specify the allowed file types (optional)
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Word Files", "*.doc", "*.docx"));

		// Show the file chooser dialog and get the selected file
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			ClientMissionHandler.UPLOAD_TEST(selectedFile);
		}
	}

	/**
	 * Event handler for the "DownloadTest" button. Retrieves the entered code from
	 * the code field. If the code field is not empty, it clears the field and
	 * retrieves the corresponding ID from the ClientMissionHandler class. If a
	 * request is available and the flag is set, it checks if the request is to
	 * change the test duration. If so, it updates the remaining time accordingly
	 * and resets the flag. It then downloads the tests using the entered code,
	 * calls the method of the ClientMissionHandler class, and starts the timer. If
	 * the status is "Paused", it cancels the timer and displays a dialog indicating
	 * that the test has been paused by the lecturer. If the remaining time is less
	 * than 300 seconds, it changes the timer label's text color to red. If the
	 * remaining time is less than 0, it cancels the timer, displays a dialog
	 * indicating that time is up, and navigates to the student menu screen.
	 *
	 * @param event The mouse event that triggered the download action.
	 * @throws IOException If an I/O error occurs during test download or navigation
	 *                     to the student menu screen.
	 */
	@FXML
	void DownloadTest(MouseEvent event) throws IOException {
		enteredCode = codeField.getText();
		codeField.clear();
		id = ClientMissionHandler.GET_ID_FROM_CODE(enteredCode);
		if (id != null) {
			test = ClientMissionHandler.GET_SINGLE_TEST2(enteredCode);

			// check if the code field is empty and then send message
			if (enteredCode.isEmpty()) {
				// codeField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
			}
			if (!enteredCode.isEmpty()) {
				// Reset the style to the default
				codeField.setStyle("");
				ClientMissionHandler.DOWNLOAD_TESTS(enteredCode);
				ClientMissionHandler.YAEL();
				////////
				Test mytest = ClientMissionHandler.GET_SINGLE_TEST2(enteredCode);
				timer(mytest.getDuration()); // Start the timer

			}
			System.out.println("end");
			LocalDateTime startDateTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formattedDateTime = startDateTime.format(formatter);

		} else {
			PopUpController popUpController = new PopUpController();
			popUpController.showAlert("Test not exist!");
		}
	}

	/**
	 * Starts the timer for the given number of seconds. Updates the timer label
	 * every second with the remaining time. If the status is "Paused", it cancels
	 * the timer and displays a dialog indicating that the test has been paused by
	 * the lecturer. If a request is available and the flag is set, it checks if the
	 * request is to change the test duration. If so, it updates the remaining time
	 * accordingly and resets the flag. If the remaining time is less than 300
	 * seconds, it changes the timer label's text color to red. If the remaining
	 * time is less than 0, it cancels the timer, displays a dialog indicating that
	 * time is up, and navigates to the student menu screen.
	 *
	 * @param seconds The total number of seconds for the timer.
	 */
	public void timer(int seconds) {
		int totalSeconds = seconds;

		timer = new Timer();
		TimerTask task = new TimerTask() {
			int remainingSeconds = totalSeconds;

			@Override
			public void run() {
				Platform.runLater(() -> {
					timerLabel.setText(getFormattedTime(remainingSeconds));
					remainingSeconds--;
					String status = ClientMissionHandler.GET_STATUS(enteredCode);
					String request = ClientMissionHandler.GET_REQUEST(id);
					if (status.equals("Paused")) {
						timer.cancel();
						Alert endTimeDialog = new Alert(Alert.AlertType.WARNING);
						endTimeDialog.setTitle("Test Paused By Lecturer!");
						endTimeDialog.setHeaderText(null);
						endTimeDialog.setContentText("Test Paused By Lecturer! ");
						endTimeDialog.showAndWait();
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/StudentMenuPage.fxml"));
						Parent root = null;
						try {
							root = loader.load();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Scene scene = new Scene(root);
						Stage studentMenuStage = new Stage();
						studentMenuStage.setTitle("Student Menu");
						studentMenuStage.setScene(scene);
						studentMenuStage.show();

						// Close the current screen
						Hidebutton.fire();

					}
					if (request != null && flag == 1) {

						String[] parts = request.split("_");
						if (parts.length == 2) {
							String part1 = parts[0];
							String part2 = parts[1];
							Alert endTimeDialog = new Alert(Alert.AlertType.WARNING);
							endTimeDialog.setTitle("Lecturer changed the test duration");
							endTimeDialog.setHeaderText(null);
							endTimeDialog.setContentText("Lecturer changed the test duration ");
							endTimeDialog.show();
							int totalSeconds1 = Integer.parseInt(part1);

							// Reset the remainingSeconds to the new totalSeconds
							remainingSeconds += totalSeconds1;

							// Update the timer label immediately with the new time
							timerLabel.setText(getFormattedTime(remainingSeconds));
							flag = 0;
						}
					}

					if (remainingSeconds < 300) {
						timerLabel.setTextFill(Color.RED);
					}
					if (remainingSeconds < 0) {
						timer.cancel();
						// notSubmitted++;
						Alert endTimeDialog = new Alert(Alert.AlertType.WARNING);
						endTimeDialog.setTitle("Time's up!");
						endTimeDialog.setHeaderText(null);
						endTimeDialog.setContentText("Time's up! ");
						endTimeDialog.showAndWait();

						ClientMissionHandler.ADD_PERFORMED_TEST(id, test.getSubject(), test.getCourse(),
								user.getUserID(), test.getAuthor(), 0, "0", "Manual");

						// Load the FXML file for the student menu screen
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/StudentMenuPage.fxml"));
						Parent root = null;
						try {
							root = loader.load();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Scene scene = new Scene(root);

						// Create a new stage for the student menu screen
						Stage studentMenuStage = new Stage();
						studentMenuStage.setTitle("Student Menu");
						studentMenuStage.setScene(scene);
						studentMenuStage.show();

						// Close the current screen
						Hidebutton.fire();
					}
				});

			}
		};
		timer.scheduleAtFixedRate(task, 0, 1000);
	}

	/**
	 * Formats the given number of seconds into HH:mm:ss format.
	 *
	 * @param seconds The number of seconds to format.
	 * @return The formatted time string in HH:mm:ss format.
	 */
	private String getFormattedTime(int seconds) {
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		int secs = seconds % 60;
		return String.format("%02d:%02d:%02d", hours, minutes, secs);
	}

	/**
	 * Event handler for the "submit" button. Displays a confirmation dialog to
	 * confirm the test submission. If confirmed, it cancels the timer, retrieves
	 * the current date and time, saves the performed test to the database, displays
	 * a submission alert, and navigates to the student menu screen.
	 *
	 * @param event The mouse event that triggered the submission action.
	 * @throws IOException If an I/O error occurs during navigation to the student
	 *                     menu screen.
	 */
	@FXML
	void submit(MouseEvent event) throws IOException {
		Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);

		confirmationDialog.setTitle("Confirmation");
		confirmationDialog.setHeaderText(null);
		confirmationDialog.setContentText("Are you sure you want to submit?");

		ButtonType result = confirmationDialog.showAndWait().orElse(ButtonType.CANCEL);
		if (result == ButtonType.OK) {
			timer.cancel();
			LocalDateTime endDateTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formattedDateTime = endDateTime.format(formatter);
			System.out.println("Submitted");
			// Send data of test

			// save the performed test to DB
			ClientMissionHandler.ADD_PERFORMED_TEST(id, test.getSubject(), test.getCourse(), user.getUserID(),
					test.getAuthor(), 0, "0", "Manual");

			Alert submissionAlert = new Alert(Alert.AlertType.INFORMATION);
			submissionAlert.setTitle("Test Submitted");
			submissionAlert.setHeaderText(null);
			submissionAlert.setContentText("You have successfully submitted the test!");
			submissionAlert.showAndWait();

			// Load the FXML file for the student menu screen
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/StudentMenuPage.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);

			// Create a new stage for the student menu screen
			Stage studentMenuStage = new Stage();
			studentMenuStage.setTitle("Student Menu");
			studentMenuStage.setScene(scene);
			studentMenuStage.show();

			// Close the current screen
			((Node) event.getSource()).getScene().getWindow().hide();
		} else {
			System.out.println("Cancelled");
			// Handle cancellation or do nothing
		}

	}

	@FXML
	void hide(ActionEvent event) throws IOException {
		// Get the current window
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		// Hide (close) the current window
		currentStage.hide();
	}

	@Override
	public void handle(WindowEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * Handles the action when the Back button is clicked.
	 * 
	 * @param event The mousr click.
	 * @throws Exception if an error occurs during the transition to the student
	 *                   menu.
	 */
	@FXML
	public void handleBackClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		studentMenuController clientController = new studentMenuController();
		clientController.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}
}
