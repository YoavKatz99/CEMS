package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.net.URL;
import java.util.ResourceBundle;
import client.ClientMissionHandler;
import common.Question;
import common.User;

/**
 * The QuestionTableController class is a controller for the Question Table GUI.
 * It handles the functionality and behavior of the GUI, including displaying a
 * table of questions.
 */
public class QuestionTableController implements Initializable {

	@FXML
	private Button BackBtn;

	@FXML
	private TableView<Question> questionTable;

	@FXML
	private TableColumn<Question, String> correctopColumn;

	@FXML
	private TableColumn<Question, String> courseNameCloumn;

	@FXML
	private TableColumn<Question, String> idColumn;

	@FXML
	private TableColumn<Question, String> op1Column;

	@FXML
	private TableColumn<Question, String> op2Column;

	@FXML
	private TableColumn<Question, String> op3Column;

	@FXML
	private TableColumn<Question, String> op4Column;

	@FXML
	private TableColumn<Question, String> questionNumberColumn;

	@FXML
	private TableColumn<Question, String> questionTextColumn;

	@FXML
	private TableColumn<Question, String> subjectColumn;

	@FXML
	private TableColumn<Question, String> lecturerColumn;

	ObservableList<common.Question> Question = FXCollections.observableArrayList();

	// @SuppressWarnings("unchecked")
	public void start(final Stage primaryStage) throws Exception {
		// System.out.println("Question Table");
		Parent root = FXMLLoader.load(this.getClass().getResource("/gui/questionTable.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS");
		primaryStage.setScene(scene);
		primaryStage.show();
		// primaryStage.setOnCloseRequest((EventHandler<WindowEvent>) this);
	}

	/**
	 * Initializes the controller and the GUI. This method is called automatically
	 * after loading the FXML file.
	 *
	 * @param arg0 The URL to the FXML file.
	 * @param arg1 The ResourceBundle for localization.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setColumnsInTable();
		// This method is requesting data from the Server
		Question.clear();
		// Setting the Data to be displayed in the TableView
		questionTable.setItems(Question);
		questionTable.autosize();
		questionTable.setEditable(true);
		User user = LoginController.getLoggedInUser();
		String lecturer = user.getName() + " " + user.getSurname();
		ClientMissionHandler.GET_QUESTION_TABLE(Question, this.questionTable, lecturer);
	}

	/**
	 * Sets up the table columns in the TableView.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setColumnsInTable() {
		idColumn.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("id"));
		subjectColumn.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("subject"));
		courseNameCloumn.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("CourseName"));
		questionTextColumn.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("questionText"));
		questionNumberColumn
				.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("questionNumber"));
		lecturerColumn.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("lecturer"));
		op1Column.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("op1"));
		op2Column.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("op2"));
		op3Column.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("op3"));
		op4Column.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("op4"));
		correctopColumn.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("Coreectop"));
	}

	/**
	 * Handles the event when the Back button is clicked.
	 *
	 * @param event The ActionEvent triggered by clicking the Back button.
	 * @throws Exception if an exception occurs during the handling of the event.
	 */
	@FXML
	private void handleBackBtnClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		CreateQuestionController controller = new CreateQuestionController();
		controller.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

}
