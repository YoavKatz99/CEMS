package gui;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientMissionHandler;
import common.PerformedTest;
import common.Test;
import common.TestData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * The controller class for the department head reports page. This class handles
 * the generation and display of various reports based on user selections. It
 * interacts with the client and server to retrieve the necessary data for
 * generating reports.
 */
public class DepHeadReportsController implements Initializable {
	@FXML
	private ComboBox<String> reportTypeComboBox;
	@FXML
	private ComboBox<String> specComboBox;
	@FXML
	private Button generateReportBtn;
	@FXML
	private Button backBtn;
	@FXML
	private AreaChart<String, Number> distributionChart;
	@FXML
	private LineChart<String, Number> gradesChart;
	@FXML
	private BarChart<String, Number> medianChart;
	@FXML
	private Label reportLabel = new Label();
	@FXML
	private Label instructionLabel = new Label();
	@FXML
	private Label secondSelectionLabel = new Label();

	private List<?> result;
	private List<?> allTestsData;
	private ObservableList<String> Types = FXCollections.observableArrayList();
	private ObservableList<String> Courses = FXCollections.observableArrayList();
	private ObservableList<String> Lecturers = FXCollections.observableArrayList();
	private ObservableList<String> Students = FXCollections.observableArrayList();
	private ObservableList<TestData> testData = FXCollections.observableArrayList();
	private ObservableList<TestData> allData = FXCollections.observableArrayList();
	private ObservableList<PerformedTest> performedTest = FXCollections.observableArrayList();
	private List<String> copyIds = new ArrayList<>();
	private List<PerformedTest> performedTests;
	private double xoffset;
	private double yoffset;

	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("depHeadReportPage.fxml"));
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
	 * Initializes the controller and sets up the initial state of the UI
	 * components.
	 *
	 * @param location  The location used to resolve relative paths for the root
	 *                  object.
	 * @param resources The resources used to localize the root object.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		reportLabel.setVisible(false);
		secondSelectionLabel.setAlignment(Pos.CENTER);
		distributionChart.setVisible(false);
		medianChart.setVisible(false);
		gradesChart.setVisible(false);
		distributionChart.getXAxis().setAnimated(false);
		gradesChart.getXAxis().setAnimated(false);
		medianChart.getXAxis().setAnimated(false);
		distributionChart.getYAxis().setAnimated(false);
		gradesChart.getYAxis().setAnimated(false);
		medianChart.getYAxis().setAnimated(false);
		Types.addAll("Course", "Lecturer", "Student");
		reportTypeComboBox.setItems(Types);
		getInfoToLists();
		reportTypeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				updateSpecComboBox(newValue);
			}
		});
	}

	/**
	 * Retrieves the necessary data from the server and populates the lists used in
	 * the UI.
	 */
	public void getInfoToLists() {
		ClientMissionHandler.GET_LISTS(Courses, Lecturers, Students, DepHeadMenuController.getDepartment());
	}

	/**
	 * Updates the specified combo box based on the selected report type.
	 *
	 * @param selectedType The selected report type.
	 */
	public void updateSpecComboBox(String selectedType) {
		getInfoToLists();
		specComboBox.getItems().clear(); // Clear the current items in specComboBox
		if (selectedType != null) {
			if (selectedType.equals("Course")) {
				secondSelectionLabel.setText("Select course");
				specComboBox.setItems(Courses);
			} else if (selectedType.equals("Lecturer")) {
				secondSelectionLabel.setText("Select lecturer");
				specComboBox.setItems(Lecturers);
			} else if (selectedType.equals("Student")) {
				secondSelectionLabel.setText("Select student ID");
				specComboBox.setItems(Students);
			}
		}
	}

	/**
	 * Handles the "Generate Report" button click event. Generates the selected
	 * report based on user selections and displays the results in the UI.
	 *
	 * @param event The action event triggered by clicking the "Generate Report"
	 *              button.
	 * @throws Exception if an error occurs during report generation.
	 */
	@SuppressWarnings("unchecked")
	@FXML
	public void handleGenerateReportClick(ActionEvent event) throws Exception {
		reportLabel.setVisible(true);
		clearChartsData();
		String reportType = reportTypeComboBox.getValue();
		String specChoise = specComboBox.getValue();
		String query;
		String choice = null;
		PopUpController popUpController = new PopUpController();
		if (reportType == null && specChoise == null) {
			popUpController.showAlert("Select report Type and Then Select specified report!");
		} else if (specChoise == null) {
			popUpController.showAlert("Must Select specified report!");
			/**** Course Report ****/
		} else if (reportType.equals("Course")) {
			choice = "course " + specChoise;
			generateStatsByID(choice);
			query = "SELECT * FROM test_stats WHERE course = ?";
			result = (List<TestData>) ClientMissionHandler.GENERATE_DEP_HEAD_REPORT(query, specChoise);
			reportLabel.setText("> " + specChoise + " course tests report");
			populateCharts_TestData();
			/**** Lecturer Report ****/
		} else if (reportType.equals("Lecturer")) {
			// choice = "author " + specChoise;
			// System.out.println(choice);
			// generateStatsByID(choice);
			query = "SELECT * FROM test_stats WHERE author = ?";
			result = (List<TestData>) ClientMissionHandler.GENERATE_DEP_HEAD_REPORT(query, specChoise);
			reportLabel.setText("> Lecturer " + specChoise + " tests report");
			populateCharts_TestData();
			/**** Student Report ****/
		} else if (reportType == "Student") {
			choice = "userID " + specChoise;
			generateStatsByID(choice);
			query = "SELECT * FROM performedtest WHERE userID = ?";
			result = (List<PerformedTest>) ClientMissionHandler.GENERATE_DEP_HEAD_REPORT(query, specChoise);
			allTestsData = (List<TestData>) ClientMissionHandler.GET_ALL_TESTS_STATS();
			allData = FXCollections.observableArrayList((List<TestData>) allTestsData);
			reportLabel.setText("> Student ID #" + specChoise + " tests report");
			populateCharts_PerformedTest();
		}
	}

	/**
	 * Sets the UI settings specific to the student report.
	 */
	public void setStudentReportSettings() {
		instructionLabel.setVisible(false);
		medianChart.setVisible(true);
		medianChart.setTitle("Grades");
		medianChart.setPrefSize(800, 300);
		medianChart.setLegendVisible(true);
		medianChart.setLayoutX(10);
		distributionChart.setTitle("Distribution");
		distributionChart.setVisible(true);
		distributionChart.setPrefSize(800, 300);
		distributionChart.setLayoutY(330);
		gradesChart.setVisible(false);
	}

	/**
	 * Populates the charts with performed test data for generating the student
	 * report.
	 */
	@SuppressWarnings("unchecked")
	private void populateCharts_PerformedTest() {
		if (result != null && !result.isEmpty() && result.get(0) instanceof PerformedTest) {
			performedTest = FXCollections.observableArrayList((List<PerformedTest>) result);
		}
		setStudentReportSettings();
		XYChart.Series<String, Number> algebraSeries = new XYChart.Series<>();
		XYChart.Series<String, Number> calculusSeries = new XYChart.Series<>();
		XYChart.Series<String, Number> logicSeries = new XYChart.Series<>();
		XYChart.Series<String, Number> cSeries = new XYChart.Series<>();
		XYChart.Series<String, Number> javaSeries = new XYChart.Series<>();
		XYChart.Series<String, Number> pythonSeries = new XYChart.Series<>();
		XYChart.Series<String, Number> mechanicsSeries = new XYChart.Series<>();
		XYChart.Series<String, Number> electricitySeries = new XYChart.Series<>();
		XYChart.Series<String, Number> astronomySeries = new XYChart.Series<>();
		algebraSeries.setName("Algebra");
		calculusSeries.setName("Calculus");
		mechanicsSeries.setName("Mechanics");
		logicSeries.setName("Logic");
		cSeries.setName("C");
		javaSeries.setName("Java");
		pythonSeries.setName("Python");
		electricitySeries.setName("Electricity");
		astronomySeries.setName("Astronomy");
		for (PerformedTest test : performedTest) {
			int grade = test.getGrade();
			String course = test.getCourse();
			String testID = test.getTestID();
			double avg = getAvg(testID);
			int median = getMed(testID);
			if (course.equals("Calculus")) {
				XYChart.Data<String, Number> data = new XYChart.Data<>(testID, grade);
				data.setNode(new CustomDataNode(avg, median));
				calculusSeries.getData().add(data);
			} else if (course.equals("Algebra")) {
				XYChart.Data<String, Number> data = new XYChart.Data<>(testID, grade);
				data.setNode(new CustomDataNode(avg, median));
				algebraSeries.getData().add(data);
			} else if (course.equals("Logic")) {
				XYChart.Data<String, Number> data = new XYChart.Data<>(testID, grade);
				data.setNode(new CustomDataNode(avg, median));
				logicSeries.getData().add(data);
			} else if (course.equals("C")) {
				XYChart.Data<String, Number> data = new XYChart.Data<>(testID, grade);
				data.setNode(new CustomDataNode(avg, median));
				cSeries.getData().add(data);
			} else if (course.equals("Java")) {
				XYChart.Data<String, Number> data = new XYChart.Data<>(testID, grade);
				data.setNode(new CustomDataNode(avg, median));
				javaSeries.getData().add(data);
			} else if (course.equals("Python")) {
				XYChart.Data<String, Number> data = new XYChart.Data<>(testID, grade);
				data.setNode(new CustomDataNode(avg, median));
				pythonSeries.getData().add(data);
			} else if (course.equals("Mechanics")) {
				XYChart.Data<String, Number> data = new XYChart.Data<>(testID, grade);
				data.setNode(new CustomDataNode(avg, median));
				mechanicsSeries.getData().add(data);
			} else if (course.equals("Electricity")) {
				XYChart.Data<String, Number> data = new XYChart.Data<>(testID, grade);
				data.setNode(new CustomDataNode(avg, median));
				electricitySeries.getData().add(data);
			} else if (course.equals("Astronomy")) {
				XYChart.Data<String, Number> data = new XYChart.Data<>(testID, grade);
				data.setNode(new CustomDataNode(avg, median));
				astronomySeries.getData().add(data);
			}

			for (TestData data : allData) {
				if (data.getTest_id().equals(test.getTestID())) {
					int[] distribution = data.getDistribution();
					// Create a single series for the distribution data
					XYChart.Series<String, Number> decileSeries = new XYChart.Series<>();
					decileSeries.setName(testID);
					// Add the data points to the area chart
					decileSeries.getData().add(new XYChart.Data<>("", 0));
					int i;
					for (i = 0; i < distribution.length - 1; i++) {
						int startRange = i * 10;
						int endRange = startRange + 9;
						String barLabel = startRange + " - " + endRange;
						decileSeries.getData().add(new XYChart.Data<>(barLabel, distribution[i]));
					}
					decileSeries.getData().add(new XYChart.Data<>("90-100", distribution[i]));
					decileSeries.getData().add(new XYChart.Data<>(" ", 0));
					distributionChart.getData().add(decileSeries);
				}
			}

		}
		if (calculusSeries.getData().size() > 0)
			medianChart.getData().add(calculusSeries);
		if (algebraSeries.getData().size() > 0)
			medianChart.getData().add(algebraSeries);
		if (logicSeries.getData().size() > 0)
			medianChart.getData().add(logicSeries);
		if (cSeries.getData().size() > 0)
			medianChart.getData().add(cSeries);
		if (javaSeries.getData().size() > 0)
			medianChart.getData().add(javaSeries);
		if (pythonSeries.getData().size() > 0)
			medianChart.getData().add(pythonSeries);
		if (astronomySeries.getData().size() > 0)
			medianChart.getData().add(astronomySeries);
		if (electricitySeries.getData().size() > 0)
			medianChart.getData().add(electricitySeries);
		if (mechanicsSeries.getData().size() > 0)
			medianChart.getData().add(mechanicsSeries);

	}

	/**
	 * Clears the data and charts in the UI.
	 */
	public void clearChartsData() {
		// Clear existing data series
		gradesChart.getData().clear();
		medianChart.getData().clear();
		distributionChart.getData().clear();
		if (result != null)
			result.clear();
	}

	/**
	 * Populates the charts with test data for generating reports other than the
	 * student report.
	 */
	@SuppressWarnings("unchecked")
	public void populateCharts_TestData() {
		instructionLabel.setVisible(false);
		gradesChart.setTitle("Average");
		gradesChart.setVisible(true);
		gradesChart.setLegendVisible(false);
		medianChart.setVisible(true);
		medianChart.setPrefSize(403, 260);
		medianChart.setLayoutX(405);
		medianChart.setTitle("Median");
		distributionChart.setVisible(true);
		distributionChart.setTitle("Distribution");
		if (result != null && !result.isEmpty() && result.get(0) instanceof TestData) {
			testData = FXCollections.observableArrayList((List<TestData>) result);
		}
		XYChart.Series<String, Number> averageSeries = new XYChart.Series<>();
		for (TestData data : testData) {
			double average = data.getAverage();
			double median = data.getMedain();
			int[] distribution = data.getDistribution();
			String testID = data.getTest_id();
			averageSeries.getData().add(new XYChart.Data<>(testID, average));
			XYChart.Series<String, Number> medianSeries = new XYChart.Series<>();
			medianSeries.setName(testID);
			medianSeries.getData().add(new XYChart.Data<>("", median));
			medianChart.getData().add(medianSeries);
			medianChart.applyCss();
			// Create a single series for the distribution data
			XYChart.Series<String, Number> decileSeries = new XYChart.Series<>();
			decileSeries.setName(testID);
			// Add the data points to the area chart
			decileSeries.getData().add(new XYChart.Data<>("", 0));
			int i;
			for (i = 0; i < distribution.length - 1; i++) {
				int startRange = i * 10;
				int endRange = startRange + 9;
				String barLabel = startRange + " - " + endRange;
				decileSeries.getData().add(new XYChart.Data<>(barLabel, distribution[i]));
			}
			decileSeries.getData().add(new XYChart.Data<>("90-100", distribution[i]));
			decileSeries.getData().add(new XYChart.Data<>(" ", 0));
			distributionChart.getData().add(decileSeries);
		}
		gradesChart.getData().add(averageSeries);
	}

	/**
	 * Handles the "Back" button click event. Returns to the department head menu
	 * page.
	 *
	 * @param event The action event triggered by clicking the "Back" button.
	 * @throws Exception if an error occurs during page navigation.
	 */
	@FXML
	public void handleBackClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		DepHeadMenuController clientController = new DepHeadMenuController();
		clientController.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	/**
	 * Retrieves the average value for a test with the specified ID.
	 *
	 * @param testID The ID of the test.
	 * @return The average value for the test.
	 */
	private double getAvg(String testID) {
		double avg = 0;
		for (TestData test : allData) {
			if (test.getTest_id().equals(testID))
				avg = test.getAverage();
		}
		return avg;
	}

	/**
	 * Retrieves the median value for a test with the specified ID.
	 *
	 * @param testID The ID of the test.
	 * @return The median value for the test.
	 */
	private int getMed(String testID) {
		int med = 0;
		for (TestData test : allData) {
			if (test.getTest_id().equals(testID))
				med = test.getMedain();
		}
		return med;
	}

	/**
	 * Custom class for displaying data nodes in the charts. Extends the StackPane
	 * class.
	 */
	public class CustomDataNode extends StackPane {
		private final Text text;

		public CustomDataNode(double testAvg, int median) {
			Rectangle rectangle = new Rectangle(62, 35);
			rectangle.setFill(Color.TRANSPARENT);
			text = new Text("Avg: " + testAvg + "\nMedian: " + median);
			text.setStyle("-fx-font-size: 9pt;");
			text.setWrappingWidth(rectangle.getWidth());
			text.setTextAlignment(TextAlignment.CENTER);
			text.setFill(Color.WHITE);
			Rectangle background = new Rectangle(rectangle.getWidth(), rectangle.getHeight());
			background.setFill(Color.TRANSPARENT);
			setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					text.setVisible(true);
					background.setFill(Color.BLACK);
					background.setStroke(Color.WHITE);
				}
			});

			setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					background.setFill(Color.TRANSPARENT);
					background.setStroke(Color.TRANSPARENT);
					text.setVisible(false);
				}
			});
			text.setVisible(false);
			setAlignment(Pos.TOP_CENTER); // Adjust the alignment of the label text as needed
			getChildren().addAll(background, rectangle, text);

		}
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
		for (int j = 0; j < performedTests.size(); j++) {
			grades[j] = performedTests.get(j).getGrade();
			average += performedTests.get(j).getGrade();
			if (grades[j] != 100)
				dist[(int) (performedTests.get(j).getGrade() / 10)]++;
			else
				dist[(int) (performedTests.get(j).getGrade() / 10) - 1]++;

		}
		average = average / performedTests.size();
		// Create a DecimalFormat object with the desired format pattern
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		// Format the double value as a string
		String formattedNumberString = decimalFormat.format(average);
		// Parse the formatted number string back to a double value
		average = Double.parseDouble(formattedNumberString);
		median = grades[(int) Math.floor(performedTests.size() / 2)];
		TestData testDataSend = new TestData(id, course, date, time, performedTests.size(), 18, 5, average, median,
				dist, author);
		ClientMissionHandler.ADD_TEST_DATA(testDataSend);

	}

	// updates all the test relevant
	public void generateStatsByID(String choice) {
		List<String> testIDlist = ClientMissionHandler.GET_TEST_BY_CHOICE(choice);
		for (int i = 0; i < testIDlist.size(); i++) {
			makeData(testIDlist.get(i));
		}
	}

}
