package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientMissionHandler;
import common.ExtensionRequest;
import common.Question;
import common.User;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import client.ClientMissionHandler;
import common.Question;
import common.Test;
import common.User;

/**
 * Controller class for the Create Test Step 1 screen - choosing questions.
 * This class handles the user interactions and logic for creating a new test.
 */
public class CreateTest1Controller implements Initializable {

	@FXML
	private Button Back;

	@FXML
	private ComboBox<String> CourseComboBox;

	@FXML
	private TableColumn<?, ?> IfSelectedColumn;

	@FXML
	private Button NextButton;

	@FXML
	private TableView<common.Question> QuestionTable;

	@FXML
	private ComboBox<String> SubjectComboBox;

	@FXML
	private TableColumn<Question, String> correctopColumn;

	@FXML
	private TableColumn<Question, String> idcolumn;

	@FXML
	private TableColumn<Question, String> op1Column;

	@FXML
	private TableColumn<Question, String> op2Column;

	@FXML
	private TableColumn<Question, String> op3Column;

	@FXML
	private TableColumn<Question, String> op4Column;

	@FXML
	private TableColumn<Question, String> questionTextColumn;

	@FXML
	private TableColumn<Question, Boolean> selectColumn;
	
	List<Question> choices = new ArrayList<>();
	List<String> checked = new ArrayList<>();

	@FXML
	private Button ShowquestionButton;

	private final Button button = new Button();

	private double xoffset;
	private double yoffset;
	ObservableList<common.Question> Question = FXCollections.observableArrayList();

	/**
	 * Initializes and displays the Create Test Step 1 screen.
	 *
	 * @param primaryStage The primary stage for the application.
	 * @throws Exception if an error occurs during the loading of the FXML file.
	 */
	@SuppressWarnings("unchecked")
	public void start(final Stage primaryStage) throws Exception {
		System.out.println("CreateTast");
		Parent root = FXMLLoader.load(this.getClass().getResource("/gui/CreateTestStep1.fxml"));
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
		primaryStage.show();
		// primaryStage.setOnCloseRequest((EventHandler<WindowEvent>) this);

	}

	public void SelestQuestions() {
	}

	/*@FXML
	private void handleSelectButton(ActionEvent event) {
		// Get the selected question
		common.Question selectedQuestion = QuestionTable.getSelectionModel().getSelectedItem();
		if (selectedQuestion != null) {
			// Perform the desired action with the selected question
			System.out.println("Selected question: " + selectedQuestion);
		}
	}*/

	public void selectItems(ActionEvent event) throws IOException {
		
	    // Pass the selected questions to the conclusion screen
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateTestStep2.fxml"));
	    Parent conclusionScreen = loader.load();
	    ConclusionScreenController conclusionController = loader.getController();
	    conclusionController.setQuestions(checked);

	    // Load the conclusion screen
	    Scene conclusionScene = new Scene(conclusionScreen);
	    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    primaryStage.setScene(conclusionScene);
	    primaryStage.show();
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CourseComboBox.setItems(FXCollections.observableArrayList("Math", "Programming languages", "Physics"));
		CourseComboBox.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> updateSecondComboBox(newValue));
		ShowquestionButton.setOnAction(event -> {
			setTable(event);
		});

	}

	@FXML
	void getComboBoxInfo(ActionEvent event) {
		System.out.println(CourseComboBox.getValue());
	}

	private void updateSecondComboBox(String selection) {
		ObservableList<String> options = FXCollections.observableArrayList();

		if ("Math".equals(selection)) {
			options.addAll("Algebra", "Calculus", "Logic");
		} else if ("Programming languages".equals(selection)) {
			options.addAll("Python", "Java", "C");
		} else if ("Physics".equals(selection)) {
			options.addAll("Mechanics", "Electricity", "Astronomy");
		}

		SubjectComboBox.setItems(options);
	}

	@FXML
	private void setTable(ActionEvent event) {
		setColumnsInTable();
		// This method is requesting data from the Server
		Question.clear();
		// Setting the Data to be displayed in the TableView
		QuestionTable.setItems(Question);
		QuestionTable.autosize();
		QuestionTable.setEditable(true);
		String CourseName = (String) SubjectComboBox.getValue();
		System.out.println(CourseName);
		User user = LoginController.getLoggedInUser();
		String lecturer = user.getName() + " " + user.getSurname();
		System.out.println("hi");
		ClientMissionHandler.GET_QUESTION_TABLE_FOR_TEST(Question, this.QuestionTable, CourseName, lecturer);

	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setColumnsInTable() {
		idcolumn.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("id"));
		questionTextColumn.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("questionText"));
		op1Column.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("op1"));
		op2Column.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("op2"));
		op3Column.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("op3"));
		op4Column.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("op4"));
		correctopColumn.setCellValueFactory((Callback) new PropertyValueFactory<Question, String>("Coreectop"));
		// Create an ObservableList to store the selected items
		ObservableList<Question> selectedQuestions = FXCollections.observableArrayList();

		selectColumn.setCellValueFactory(param -> new SimpleBooleanProperty());

        selectColumn.setCellFactory(
                new Callback<TableColumn<Question, Boolean>, TableCell<Question, Boolean>>() {
                    @Override
                    public TableCell<Question, Boolean> call(TableColumn<Question, Boolean> column) {
                        return new CheckBoxTableCell<Question, Boolean>() {
                            @Override
                            public void updateItem(Boolean item, boolean empty) {
                                super.updateItem(item, empty);
                                if (!empty) {
                                    CheckBox checkBox = new CheckBox();
                                    checkBox.setSelected(item);
                                    checkBox.selectedProperty().addListener(
                                            (ObservableValue<? extends Boolean> observable, Boolean oldValue,
                                             Boolean newValue) -> {
                                               Question choice = getTableView().getItems().get(getIndex());
                                                if (newValue) {
                                                    System.out.println(choice.getId() + " is checked.");
                                                    checked.add((choice.getId()));
                                              
                                                } else {
                                                    System.out.println(choice + " is unchecked.");
                                                }
                                              
                                            });
                                    

                                    setGraphic(checkBox);
                                    setText(null);
                                }
                            }
                        };
                    }
        });

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
		LecturerTestsMenuController clientController = new LecturerTestsMenuController();
		clientController.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}


}