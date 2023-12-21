package gui;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientMissionHandler;
import common.PerformedTest;
import common.Test;
import common.TestData;
import common.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * The controller class for the LecturerReportPage.fxml GUI. It handles the
 * generation and display of reports for a specific lecturer.
 */
public class LecturerReportController implements Initializable {

	@FXML
	public BarChart<String, Number> distributionChart;
	@FXML
	private ComboBox<String> courseComboBox;
	@FXML
	private ComboBox<String> testComboBox;
	@FXML
	private Label lecturerNameLbl = new Label();
	@FXML
	public Label testIdLbl = new Label();
	@FXML
	public Label averageLbl = new Label();
	@FXML
	public Label medianLbl = new Label();
	@FXML
	private Button genertaeReportBtn;
	@FXML
	private Button backBtn;
	@FXML
	private TableView<TestData> testInfoTable;
	@FXML
	private TableColumn<TestData, Date> dateCol;
	@FXML
	private TableColumn<TestData, String> timeCol;
	@FXML
	private TableColumn<TestData, Integer> stdStartedCol;
	@FXML
	private TableColumn<TestData, Integer> submittedCol;
	@FXML
	private TableColumn<TestData, Integer> notSubmittedCol;

	private User user = LoginController.getLoggedInUser();
	private double xoffset;
	private double yoffset;

	ObservableList<String> Courses = FXCollections.observableArrayList();
	ObservableList<String> Tests = FXCollections.observableArrayList();
	ObservableList<TestData> testData = FXCollections.observableArrayList();
	private List<PerformedTest> performedTests;
	private List<String> copyIds = new ArrayList<>();

	/**
	 * Starts the LecturerReportPage GUI.
	 *
	 * @param primaryStage The primary stage for the GUI.
	 * @throws Exception if an error occurs during GUI loading.
	 */
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LecturerReportPage.fxml"));
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		testInfoTable.setVisible(false);
		distributionChart.setVisible(false);
		distributionChart.getXAxis().setAnimated(false);
		distributionChart.getYAxis().setAnimated(false);
		averageLbl.setVisible(false);
		medianLbl.setVisible(false);
		testIdLbl.setVisible(false);
		testInfoTable.autosize();
		averageLbl.setWrapText(true);
		medianLbl.setWrapText(true);
		lecturerNameLbl.setText(user.getName() + " " + user.getSurname() + "'s");
		// Initialize the BarChart with initial data
		setCourses();
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		distributionChart.getData().add(series);
		for (int i = 0; i < 9; i++) {
			int startRange = i * 10;
			int endRange = startRange + 9;
			String barLabel = startRange + " - " + endRange;
			series.getData().add(new XYChart.Data<>(barLabel, 0));
		}
		series.getData().add(new XYChart.Data<>("90 - 100", 0)); // Add the last bar

		// Initialize the table
		initializeTestInfoTable();
		// Add a listener to courseComboBox selection changes
		courseComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				setTests(newValue);
			}
		});
	}

	/**
	 * Initializes the table columns for the test information table.
	 */
	@SuppressWarnings("unchecked")
	public void initializeTestInfoTable() {
		// Clear any existing columns
		testInfoTable.getColumns().clear();
		// Set up the columns and their respective data types
		TableColumn<TestData, Date> dateCol = new TableColumn<>("Date");
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		TableColumn<TestData, String> timeCol = new TableColumn<>("Time");
		timeCol.setCellValueFactory(new PropertyValueFactory<>("time_allocated"));
		TableColumn<TestData, Integer> stdStartedCol = new TableColumn<>("Students Started");
		stdStartedCol.setCellValueFactory(new PropertyValueFactory<>("students_started"));
		TableColumn<TestData, Integer> submittedCol = new TableColumn<>("Submitted");
		submittedCol.setCellValueFactory(new PropertyValueFactory<>("submitted"));
		TableColumn<TestData, Integer> notSubmittedCol = new TableColumn<>("Not Submitted");
		notSubmittedCol.setCellValueFactory(new PropertyValueFactory<>("notSubmitted"));
		testInfoTable.getColumns().addAll(dateCol, timeCol, stdStartedCol, submittedCol, notSubmittedCol);
		testInfoTable.autosize();
	}

	/**
	 * Initializes the ComboBox with the available courses.
	 */
	public void setCourses() {
		ClientMissionHandler.GET_COURSES(Courses, courseComboBox, user);
	}

	/**
	 * Sets the available tests for the selected course in the testComboBox.
	 *
	 * @param courseName The selected course name.
	 */
	public void setTests(String courseName) {
		ClientMissionHandler.GET_TESTS_OF_COURSE(courseName, Tests, testComboBox);
	}

	/**
	 * Handles the action when the Generate Report button is clicked.
	 *
	 * @param event The action event.
	 * @throws Exception if an error occurs during report generation.
	 */
	@FXML
	public void handleGenerateClick(ActionEvent event) throws Exception {

		String testID = testComboBox.getValue();
		makeData(testID);
		if (testID != null && courseComboBox.getValue() != null) {
			testInfoTable.setVisible(true);
			distributionChart.setVisible(true);
			averageLbl.setVisible(true);
			medianLbl.setVisible(true);
			testIdLbl.setVisible(true);
			ClientMissionHandler.GENERATE_LECTURER_REPORT(testID, averageLbl, medianLbl, testData, testInfoTable, this);
		} else {
			PopUpController popUpController = new PopUpController();
			popUpController.showAlert("Must choose test Id!");
		}
	}

	/**
	 * Handles the action when the Back button is clicked.
	 *
	 * @param event The action event.
	 * @throws Exception if an error occurs during the transition to the lecturer
	 *                   menu.
	 */
	@FXML
	public void handleBackClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		LecturerController clientController = new LecturerController();
		clientController.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	/**
	 * Calculates and saves test data based on the provided test ID.
	 *
	 * @param testID The ID of the test.
	 */
	@SuppressWarnings("unchecked")
	public void makeData(String testID) {
		performedTests = (List<PerformedTest>) ClientMissionHandler.GET_PERFORMED_TESTS(testID);
		Test test = ClientMissionHandler.GET_SINGLE_TEST(testID);
		double average = 0;
		int median = 0;
		int[] dist = new int[10];
		int[] grades = new int[performedTests.size()];
		String id = test.getID(), course = test.getCourse(), time = String.valueOf(test.getDuration()),
				author = test.getAuthor();
		Date date = Calendar.getInstance().getTime();
		copyIds.clear();
		for (int j = 0; j < performedTests.size(); j++) {
			grades[j] = performedTests.get(j).getGrade();
			average += performedTests.get(j).getGrade();
			if (grades[j] >= 100)
				dist[9]++;
			else {
				dist[(int) (performedTests.get(j).getGrade() / 10)]++;
			}
			String copyCheckA = performedTests.get(j).getCopyCheck();
			for (int k = j + 1; k < performedTests.size(); k++) {
				boolean isCopy = false;
				String copyCheckB = performedTests.get(k).getCopyCheck();
				String template = "";
				if (Integer.valueOf(copyCheckA) != 0 && Integer.valueOf(copyCheckB) != 0) {
					for (int c = 0; c < copyCheckA.length(); c++) {
						if (copyCheckA.charAt(c) != copyCheckB.charAt(c)) {
							template += String.valueOf(Math.max(Character.getNumericValue(copyCheckA.charAt(c)),
									Character.getNumericValue(copyCheckB.charAt(c))));
						} else {
							template += "0";
						}}

						if (Integer.valueOf(template) == Math
								.abs(Integer.valueOf(copyCheckA) - Integer.valueOf(copyCheckB))) {
							isCopy = true;
						}
					}
					if (isCopy) {
						copyIds.add("Students: " + performedTests.get(j).getUserId() + " and "
								+ performedTests.get(k).getUserId());

					}

				}
			
		}

		average = average / performedTests.size();

		// Create a DecimalFormat object with the desired format pattern
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		// Format the double value as a string
		String formattedNumberString = decimalFormat.format(average);
		// Parse the formatted number string back to a double value
		average = Double.parseDouble(formattedNumberString);
		Arrays.sort(grades);
		median = grades[(int) Math.floor(performedTests.size() / 2)];
		TestData testDataSend = new TestData(id, course, date, time, performedTests.size(), performedTests.size() - 1,
				1, average, median, dist, author);
		ClientMissionHandler.ADD_TEST_DATA(testDataSend);
		if (!copyIds.isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText(null);
			String message = "Copies Detected:\n";
			for (String s : copyIds) {
				message += s;
				message += "\n";
			}
			alert.setContentText(message);

			// Display the alert dialog
			alert.show();
		}

	}

}
