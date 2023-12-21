package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientMissionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.stage.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import javafx.scene.control.TreeTableView;

import common.Question;
import common.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Node;
import javafx.util.Callback;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import client.ClientMissionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.event.ActionEvent;

/**
 * Controller class for the Create Question screen.
 * This class handles the user interactions and logic for creating a new question.
 */
public class CreateQuestionController implements Initializable {

	@FXML
	private ComboBox<String> CoursenameComboBox;

	@FXML
	private TextArea QuestionText;

	@FXML
	private ComboBox<String> SubjectComboBox;

	@FXML
	private TextArea ans1;

	@FXML
	private TextArea ans2;

	@FXML
	private TextArea ans3;

	@FXML
	private TextArea ans4;

	@FXML
	private Button Submit;

	@FXML
	private Button BackButton;

	@FXML
	private RadioButton option1;

	@FXML
	private RadioButton option2;

	@FXML
	private RadioButton option3;

	@FXML
	private RadioButton option4;

	@FXML
	private Button ViewQuestion;

	private RadioButton[] radioButtonArray;

	private TextArea[] answersArray;

	private String finalrightAnswer = "";

	/**
	 * Initializes and displays the Create Question screen.
	 *
	 * @param primaryStage The primary stage for the application.
	 * @throws Exception if an error occurs during the loading of the FXML file.
	 */
	@SuppressWarnings("unchecked")
	public void start(final Stage primaryStage) throws Exception {
		System.out.println("CreateQue");
		Parent root = FXMLLoader.load(this.getClass().getResource("/gui/CreateQue.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS");
		primaryStage.setScene(scene);
		primaryStage.show();
		// ((Node)event.getSource()).getScene().getWindow().hide();
	}

	public void handleQuestionsTableClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		QuestionTableController controller = new QuestionTableController();
		controller.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CoursenameComboBox.setItems(FXCollections.observableArrayList("Math", "Programming languages", "Physics"));
		CoursenameComboBox.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> updateSecondComboBox(newValue));
		Submit.setOnAction(event -> {
			try {
				addQuestionSubmit(event);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		// addInputToComboBox.setOnAction(event -> addInputToComboBox(event));
		radioButtonArray = new RadioButton[] { option1, option2, option3, option4 };
		answersArray = new TextArea[] { ans1, ans2, ans3, ans4 };
	}

	@FXML
	void getComboBoxInfo(ActionEvent event) {
		System.out.println(CoursenameComboBox.getValue());
	}

	private void updateSecondComboBox(String selection) {
		ObservableList<String> options = FXCollections.observableArrayList();

		if ("Math".equals(selection)) {
			options.addAll("Algebra", "Calculus", "Logica");
		} else if ("Programming languages".equals(selection)) {
			options.addAll("Python", "Java", "C");
		} else if ("Physics".equals(selection)) {
			options.addAll("Mechanics", "Electricity", "Astronomy");
		}

		SubjectComboBox.setItems(options);
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

	@FXML
	private void addQuestionSubmit(ActionEvent event) throws IOException {
		String questionNumber = getNextQuestionNumber();
		String subjectName = (String) CoursenameComboBox.getValue();
		String subjectCode = getSubjectCode(subjectName); // Retrieve the subject code based on the subject name
		String id = subjectCode + questionNumber;
		String courseName = (String) SubjectComboBox.getValue();
		User user = LoginController.getLoggedInUser();
		String lecturer = user.getName() + " " + user.getSurname();
		String questionText = QuestionText.getText();
		String answer1 = ans1.getText();
		String answer2 = ans2.getText();
		String answer3 = ans3.getText();
		String answer4 = ans4.getText();
		String rightAnswer = finalrightAnswer;

		Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
		confirmationDialog.setTitle("Confirmation");
		confirmationDialog.setHeaderText(null);
		confirmationDialog.setContentText("Are you sure you want to submit?");

		ButtonType result = confirmationDialog.showAndWait().orElse(ButtonType.CANCEL);

		if (result == ButtonType.OK) {
			System.out.println("Submitted");
			// Perform submission action here
		} else {
			System.out.println("Cancelled");
			// Handle cancellation or do nothing
		}
		if (result == ButtonType.OK) {
			// Send data of test
			ClientMissionHandler.ADD_QUESTIONS(id, questionNumber, lecturer, courseName, subjectName, questionText,
					answer1, answer2, answer3, answer4, rightAnswer);

			// Load the FXML file for the student menu screen
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/CreateQue.fxml"));
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
	}

	@FXML
	private String getSubjectCode(String subjectName) {
		String subjectCode;

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
	private String getNextQuestionNumber() {
		Object obj;
		obj = ClientMissionHandler.MAX_CODE_QUESTIONS();
		int number = Integer.parseInt((String) obj); // Convert the string to an integer
		number++; // Increment the integer value by one
		String max_code = Integer.toString(number);
		return max_code;
	}

	@FXML
	private void markQuestion(MouseEvent event) {
		for (int i = 0; i < 4; i++) {
			if (radioButtonArray[i] != (RadioButton) event.getSource())
				radioButtonArray[i].setSelected(false);
			else {
				radioButtonArray[i].setSelected(true);
				finalrightAnswer = answersArray[i].getText();
			}
		}
	}

}