package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import client.ClientMissionHandler;
import common.ExtensionRequest;
import common.Test;
import common.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * The LecturerManageTestsController class is a controller that manages the user interface logic for managing tests
 * by a lecturer in the Computerized Exam Management System (CEMS) application.
 * It handles test status changes, time extensions, refreshing requests, and navigation.
 */
public class LecturerManageTestsController {
	@FXML
	private TableView<Test> testsTable;
	@FXML
	private TableColumn<Test, String> idCol;
	@FXML
	private TableColumn<Test, String> subjectCol;
	@FXML
	private TableColumn<Test, String> courseCol;
	@FXML
	private TableColumn<Test, String> authorCol;
	@FXML
	private TableColumn<Test, String> durationCol;
	@FXML
	private TableColumn<Test, String> codeCol;
	@FXML
	private TableColumn<Test, String> statusCol;
	@FXML
	private Button RequestBtn;
	@FXML
	private Button ApplyBtn;
	@FXML
	private Button BackBtn;
	@FXML
	private Label nameLbl = new Label();
	@FXML
	private ImageView info = new ImageView();

	private ObservableList<Test> testsList = FXCollections.observableArrayList();;
	private double xoffset;
	private double yoffset;
	private User user = LoginController.getLoggedInUser();
	String lecturerName = user.getName() + " " + user.getSurname();
	List<String> oldValues = new ArrayList<>();
	List<String> choices = new ArrayList<>();
	static List<ExtensionRequest> myRequests = new ArrayList<>();
	PopUpController popUpController = new PopUpController();
	private Timer refreshTimer = new Timer();
	private static final long REFRESH_INTERVAL = 100000;
	String timeChange = null;

    /**
     * Starts the lecturer manage tests page and initializes the controller.
     *
     * @param primaryStage The primary stage for the lecturer manage tests page.
     * @throws Exception if an error occurs while starting the page.
     */
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LecturerManageTestsPage.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
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
     * Initializes the controller and sets up the user interface components.
     */
	public void initialize() {
		nameLbl.setText(lecturerName);
		nameLbl.setAlignment(Pos.CENTER);
		info.setOnMouseClicked(event -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Valid status changes");
			alert.setContentText("Inactive --> Active\nActive --> Paused\n"
					+ "Active --> Done\nPasued -->Active\nPasued --> Done\n" + "Done --> Active");
			alert.showAndWait();
		});
		testsList = (ObservableList<Test>) FXCollections
				.observableArrayList(ClientMissionHandler.GET_MY_TESTS(lecturerName));
		// Initialize the choices list with initial values
		for (int i = 0; i < testsList.size(); i++) {
			choices.add(null);
			oldValues.add(null);
		}
		if (testsList.size() > 0) {
			// Set up the table columns
			idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
			subjectCol.setCellValueFactory(new PropertyValueFactory<>("Subject"));
			courseCol.setCellValueFactory(new PropertyValueFactory<>("Course"));
			authorCol.setCellValueFactory(new PropertyValueFactory<>("Author"));
			durationCol.setCellValueFactory(new PropertyValueFactory<>("Duration"));
			codeCol.setCellValueFactory(new PropertyValueFactory<>("TestCode"));
			statusCol.setCellFactory(
					column -> new ComboBoxTableCell<Test, String>("Active", "Inactive", "Paused", "Done") {
						@Override
						public void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							if (!empty) {
								int row = getIndex(); // Get the current row index
								ComboBox<String> comboBox = new ComboBox<>(getItems());
								String status = testsList.get(row).getStatus();
								comboBox.setValue(status);
								oldValues.set(row, status);
								setGraphic(comboBox);
								comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
									// Handle the ComboBox value change
									int row2 = getIndex();
									choices.set(row2, newValue); // Update the choices list
								});
							}
						}
					});
			testsTable.setItems(testsList);
		}
		startRefreshTimer();

	}

    /**
     * Handles the apply button click event.
     * Updates the selected test status changes and notifies the server.
     *
     * @param event The action event triggered by the apply button.
     * @throws Exception if an error occurs while handling the event.
     */
	@FXML
	public void handleApplyClick(ActionEvent event) throws Exception {
		List<String> updates = new ArrayList<>();
		String alertMsg = "Ilegal operation on test:\n";
		for (int i = 0; i < testsList.size(); i++) {
			String old = oldValues.get(i);
			String choice = choices.get(i);
			if (choice != null) {
				if (old.equals("Inactive") && choice.equals("Active")) {
					updates.add(testsList.get(i).getID() + " " + choice);
				} else if (old.equals("Active") && choice.equals("Paused")) {
					updates.add(testsList.get(i).getID() + " " + choice);
				} else if (old.equals("Active") && choice.equals("Done")) {
					updates.add(testsList.get(i).getID() + " " + choice);
				} else if (old.equals("Paused") && choice.equals("Active")) {
					updates.add(testsList.get(i).getID() + " " + choice);
				} else if (old.equals("Paused") && choice.equals("Done")) {
					updates.add(testsList.get(i).getID() + " " + choice);
				} else {
					updates.add(testsList.get(i).getID() + " " + old);
					alertMsg += testsList.get(i).getID() + "\n";
				}
			}
		}
		if (alertMsg.contains("0")) {
			popUpController.showAlert(alertMsg);
		}
		ClientMissionHandler.UPDATE_MY_TESTS(updates);
		popUpController.showAlert("Status updated!");
		initialize();
	}


    /**
     * Handles the extra time button click event.
     * Requests a time extension for a selected test from the user.
     *
     * @param event The action event triggered by the extra time button.
     * @throws Exception if an error occurs while handling the event.
     */
	@FXML
	public void handleExtraTimeClick(ActionEvent event) throws Exception {
		String id = null;
		String reason = null;

		Boolean isIdOk = false;
		// Create a TextInputDialog
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Time extension request");
		dialog.setHeaderText("Enter test ID: ");
		dialog.setContentText("input:");
		// Show the dialog and wait for user input
		Optional<String> result = dialog.showAndWait();
		// Check if the user entered a string
		if (result.isPresent()) {
			id = result.get();
			if (id != null) {
				for (Test test : testsList) {
					if (id.equals(test.getID()) && test.getStatus().equals("Active")) {
						isIdOk = true;
						break;
					}
				}
				if (isIdOk) {
					Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
					alert.setTitle("Confirmation Dialog");
					alert.setHeaderText("Select change type");
					alert.setContentText("Choose your option:");

					ButtonType buttonTypeIncrease = new ButtonType("Extra time");
					ButtonType buttonTypeDecrease = new ButtonType("Short time");

					alert.getButtonTypes().setAll(buttonTypeIncrease, buttonTypeDecrease);
					// Show the alert and wait for the user's response
					alert.showAndWait().ifPresent(response -> {
						if (response == buttonTypeIncrease) {
							timeChange = "+";
							System.out.println("Add time");
						} else if (response == buttonTypeDecrease) {
							System.out.println("Short time");
							timeChange = "-";
						}
					});
					TextInputDialog timedialog = new TextInputDialog();
					timedialog.setTitle("Time extension request");
					timedialog.setHeaderText("Enter amount of time: ");
					timedialog.setContentText("time input:");
					Optional<String> time = timedialog.showAndWait();
					if (time.isPresent()) {
						try {
							int parsedTime = Integer.parseInt(time.get());
							timeChange += time.get();
							TextInputDialog detailsdialog = new TextInputDialog();
							detailsdialog.setTitle("Time extension request");
							detailsdialog.setHeaderText("Enter reason for request: ");
							detailsdialog.setContentText("input:");
							Optional<String> result2 = detailsdialog.showAndWait();
							if (result2.isPresent()) {
								reason = timeChange + "_" + result2.get();
							}

							String applicant = user.getName() + " " + user.getSurname();
							ExtensionRequest request = new ExtensionRequest(applicant, id, reason, "pending");
							ClientMissionHandler.SEND_EXTENSION_REQUEST(request);
							popUpController.showAlert("Time extension request sent to department head");
							System.out.println(id + " " + reason);
						} catch (NumberFormatException e) {
							popUpController.showAlert("Invalid time input!");
						}
					}
				} else {
					popUpController.showAlert("Invalid test ID!");
				}

			}
		}

	}


    /**
     * Handles the refresh event.
     * Checks for approved extension requests and notifies the user.
     *
     * @throws Exception if an error occurs while handling the refresh.
     * @param event the start of the screen
     */
	@FXML
	public void handleRefresh(ActionEvent event) throws Exception {
		Platform.runLater(() -> {
			String applicant = user.getName() + " " + user.getSurname();
			List<ExtensionRequest> myRequests = ClientMissionHandler.CHECK_REQUEST_CHANGE(applicant);
			if (myRequests.size() > 0) {
				String approvedRequests = "SMS\n\nThe following extension request(s) were approved:\n";
				for (ExtensionRequest request : myRequests) {
					approvedRequests += request.getTestID() + "\n";
				}
				popUpController.showAlert(approvedRequests);
				ClientMissionHandler.CONCLUDE_REQUESTS(applicant);
			}
		});
	}


    /**
     * Starts the refresh timer to automatically check for approved extension requests.
     */
	public void startRefreshTimer() {
		refreshTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					handleRefresh(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, REFRESH_INTERVAL);
	}

	public void stopRefreshTimer() {
		if (refreshTimer != null) {
			refreshTimer.cancel();
			refreshTimer = null;
		}
	}

    /**
     * Handles the back button click event.
     * Returns to the lecturer main menu page.
     *
     * @param event The action event triggered by the back button.
     * @throws Exception if an error occurs while handling the event.
     */
	@FXML
	public void handleBackClick(ActionEvent event) throws Exception {
		stopRefreshTimer(); // Stop the timed function
		Stage primaryStage = new Stage();
		LecturerController clientController = new LecturerController();
		clientController.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

}
