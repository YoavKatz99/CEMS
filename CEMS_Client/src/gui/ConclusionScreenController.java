package gui;

import javafx.scene.control.TextArea;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientMissionHandler;
import common.ExtensionRequest;
import common.Question;
import common.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import client.ClientMissionHandler;
import common.Question;
import common.test_question;
import common.Test;
import common.User;
import common.Test;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

/**
 * The ConclusionScreenController class is responsible for controlling the GUI and user interactions of the Conclusion Screen 
 * of creating a test.
 * This screen is used for creating a test points division and adding the test to the system.
 * It implements the Initializable interface to initialize the screen components and handle user interactions.
 */
@SuppressWarnings("unchecked")
public class ConclusionScreenController implements Initializable {
	@FXML
	private TableColumn<Question, String> IDColumn;

	@FXML
	private TextArea LecturerText;

	@FXML
	private Button AddTestButton;

	@FXML
	private TableColumn<Question, String> PointsColumn;

	@FXML
	private TextArea StudentText;

	@FXML
	private TableColumn<Question, String> CourseColumn;

	@FXML
	private ComboBox<String> TestCodeComboBox;

	@FXML
	private ComboBox<Integer> DurationTestComboBox;

	@FXML
	private TableColumn<Question, String> SubjectColumn;

	@FXML
	private TableColumn<Question, String> TextColumn;
	@FXML
	private TableView<common.Question> tableView;

	private TableView<test_question> tableView2;
	private List<Question> selectedQuestions;
	private double xoffset;
	private double yoffset;
	ObservableList<common.Question> Question = FXCollections.observableArrayList();

	@SuppressWarnings("unchecked")
	public void start(final Stage primaryStage) throws Exception {
		System.out.println("CreateTast");
		Parent root = FXMLLoader.load(this.getClass().getResource("/gui/createTestStep2.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS");
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

//		TestCodeComboBox.setItems(FXCollections.observableArrayList("1457", "1396", "1758","2963","5672"));
//		TestCodeComboBox.getSelectionModel().selectedItemProperty()
//				.addListener((observable, oldValue, newValue) -> addTastCode(newValue));

//		ComboBox<Integer> DurationTestComboBox = new ComboBox<>();
//		ObservableList<Integer> tableDuration = FXCollections.observableArrayList(60, 90, 120);
//		DurationTestComboBox.setItems(tableDuration);
//		DurationTestComboBox.getSelectionModel().selectedItemProperty().addListener(
//			    (observable, oldValue, newValue) -> addTastDuration(newValue));

//		ComboBox<String> TestCodeComboBox = new ComboBox<>();
//		TestCodeComboBox.setItems(FXCollections.observableArrayList("1457", "1396", "1758","2963","5672"));
//		TestCodeComboBox.getSelectionModel().selectedItemProperty()
//				.addListener((observable, oldValue, newValue) -> addTastCode(newValue));
//		
//		ComboBox<Integer> durationComboBox = new ComboBox<>();
//		durationComboBox.setItems(FXCollections.observableArrayList(60, 90, 120));
//		durationComboBox.getSelectionModel().selectedItemProperty().addListener(
//		    (observable, oldValue, newValue) -> addTastDuration(newValue));
//		

		primaryStage.setScene(scene);
		primaryStage.show();
		// primaryStage.setOnCloseRequest((EventHandler<WindowEvent>) this);

	}

	public void setQuestions(List<String> checked) {
		List<String> approvedList = checked;

		if (!approvedList.isEmpty()) {
			ClientMissionHandler.SELECTED_QUESTIONS(Question, this.tableView, approvedList);
			// initialize();
		} else {
			System.out.println("empty");
		}
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private void setColumnsInTable() {
		IDColumn.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("id"));
		SubjectColumn.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("subject"));
		CourseColumn.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("courseName"));
		TextColumn.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("questionText"));
		PointsColumn.setCellFactory(column -> {
			return new TableCell<Question, String>() {
				private final ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList("0", "10",
						"20", "30", "40", "50", "60", "70", "80", "90", "100"));

				{
					comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
						if (!isEmpty()) {
							int row = getIndex();
							((Question) getTableView().getItems().get(row)).setPoints(newValue);
						}
					});
				}

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						comboBox.setValue(item);
						setGraphic(comboBox);
					}
				}
			};
		});

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setColumnsInTable();
		// This method is requesting data from the Server
		Question.clear();
		// Setting the Data to be displayed in the TableView
		tableView.setItems(Question);
		tableView.autosize();
		tableView.setEditable(true);

		TestCodeComboBox.setItems(FXCollections.observableArrayList("1457", "1396", "1758", "2963", "5672"));
		TestCodeComboBox.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> addTastCode(observable, newValue, oldValue));

		DurationTestComboBox.setItems(FXCollections.observableArrayList(60, 90, 120));
		DurationTestComboBox.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> addTastDuration(observable, newValue, oldValue));

	}

	@FXML
	private void addTestButton(MouseEvent event) throws IOException {
		ObservableList<Question> selectedQuestions = tableView.getItems();
		ObservableList<test_question> test_question;
		String idQuestion = null;
		String questionText = null ;
		String subjectName = null;
		String courseName = null;
		String idTestCode = null ;
		int durationTest = 0;
		String maxNumTest = null;
		String TextLecturer = null ;
		String TextStudent = null;
		String IdTest = null;
		String lecturer = null;
		
		for (Question selectedQuestion : selectedQuestions) {
			idQuestion = selectedQuestion.getId();
			questionText = selectedQuestion.getQuestionText();
			String points = selectedQuestion.getPoints();
			subjectName = selectedQuestion.getSubject();
			courseName = selectedQuestion.getCourseName();
			idTestCode = TestCodeComboBox.getValue();
			durationTest = DurationTestComboBox.getValue();
			maxNumTest = getNextQuestionNumber();
			IdTest = getSubjectCode(subjectName) + getCorseCode(courseName) + maxNumTest;
			TextLecturer = LecturerText.getText();
			TextStudent = StudentText.getText();
			User user = LoginController.getLoggedInUser();
			lecturer = user.getName() +" " +user.getSurname();

			System.out.println("ID: " + idQuestion);
			System.out.println("Question Text: " + questionText);
			System.out.println("Points: " + points);
			System.out.println("TestCodeComboBox: " + idTestCode);
			System.out.println("DurationTestComboBox: " + durationTest);
			System.out.println("IdTest: " + IdTest);
			System.out.println("TextLecturer: " + TextLecturer);
			test_question = FXCollections.observableArrayList();
			ClientMissionHandler.ADD_POINTS(test_question, this.tableView, this.tableView2, IdTest, idQuestion, points);

		}
		Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
		confirmationDialog.setTitle("Confirmation");
		confirmationDialog.setHeaderText(null);
		confirmationDialog.setContentText("Are you sure you want to Add the test?");
		ButtonType result = confirmationDialog.showAndWait().orElse(ButtonType.CANCEL);

		if (result == ButtonType.OK) {
			// Send data of test
			ObservableList<Test> Test = FXCollections.observableArrayList();
			ClientMissionHandler.ADD_Test(Test , this.tableView, this.tableView2, IdTest, subjectName,
					courseName, lecturer, durationTest, idTestCode, TextLecturer, TextStudent, maxNumTest);
			ClientMissionHandler.CREATE_TEST_DATA(IdTest);
		} else {
			System.out.println("Cancelled");
			// Handle cancellation or do nothing
		}

		// Load the FXML file for the student menu screen
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LecturerMenuPage.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);

		// Create a new stage for the student menu screen
		Stage stage = new Stage();
		stage.setTitle("CreateQue");
		stage.setScene(scene);
		stage.show();

		// Close the current screen
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	@FXML
	private String getSubjectCode(String subjectName) {
		String subjectCode;
		System.out.println(subjectName + "266");
		switch (subjectName) {
		case "Math":
			subjectCode = "01";
			break;
		case "Programming languages":
			subjectCode = "02";
			break;
		case "Physics":
			subjectCode = "03";
			break;
		default:
			subjectCode = "0"; // Default value if courseName doesn't match any case
			break;
		}

		return subjectCode;
	}

	@FXML
	private String getCorseCode(String CourseName) {
		System.out.println(CourseName + "266");
		String subjectCode;
		switch (CourseName) {
		case "Algebra":
			CourseName = "01";
			break;
		case "Calculus":
			CourseName = "02";
			break;
		case "Logica":
			CourseName = "03";
			break;
		case "Python":
			CourseName = "01";
			break;
		case "Java":
			CourseName = "02";
			break;
		case "C":
			CourseName = "03";
			break;
		case "Mechanics":
			CourseName = "01";
			break;
		case "Electricity":
			CourseName = "02";
			break;
		case "Astronomy":
			CourseName = "03";
			break;
		default:
			CourseName = "0"; // Default value if courseName doesn't match any case
			break;
		}

		return CourseName;
	}

	@FXML
	private String getNextQuestionNumber() {
		Object obj;
		obj = ClientMissionHandler.MAX_NUM_TEST();
		int incrementedNumber = Integer.parseInt((String) obj) + 1;
		String formattedNumber = String.format("%02d", incrementedNumber);
		return formattedNumber;
	}

	private void addTastDuration(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
		// Your implementation here
	}

	private void addTastCode(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		// Your implementation here
	}

}