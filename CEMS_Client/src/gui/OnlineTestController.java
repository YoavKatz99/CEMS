package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.ClientMissionHandler;
import common.PerformedTest;
import common.Question;
import common.Test;
import common.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The OnlineTestController class controls the functionality of the online test GUI.
 * It handles displaying questions, recording answers, and managing the test timer.
 */
public class OnlineTestController extends Application {

	@FXML
	private RadioButton a1RadioButton;

	@FXML
	private TextField a1Text;

	@FXML
	private RadioButton a2RadioButton;

	@FXML
	private TextField a2Text;

	@FXML
	private RadioButton a3RadioButton;

	@FXML
	private TextField a3Text;

	@FXML
	private RadioButton a4RadioButton;

	@FXML
	private TextField a4Text;

	@FXML
	private Button nextQButton;

	@FXML
	private Label positionLabel;

	@FXML
	private Button prevQButton;

	@FXML
	private TextArea questionText;

	@FXML
	private ProgressBar progressBar;

	@FXML
	private Label timeLabel;
	
    @FXML
    private Button hideButton;
  
    @FXML
    private Button submitBtn;
	private String testCode = "1457";
	private String testId = OnlineTestEnterController.getTestId();
	private List<Question> Qlist = new ArrayList<>();
	private RadioButton[] radioButtonArray;
	private int currQuestion = 0;
	private int[] answersArray;
	private TextField[] answersFieldArray;
	private User user = LoginController.getLoggedInUser();
	private String copyCheck = "";
	private int notSubmitted = 0;
	private boolean isLocked = false;
	private Test test = OnlineTestEnterController.getTest();
	private Timer timer;
	private String request = null;
	private boolean requestDone = true;
	private String[] answersText;

	 /**
     * Initializes the OnlineTestController class.
     */
	@FXML
	void initialize() {
		Qlist.clear();
		ClientMissionHandler.GET_QUESTIONS_FROM_TEST(Qlist, test.getID());
		radioButtonArray = new RadioButton[] { a1RadioButton, a2RadioButton, a3RadioButton, a4RadioButton };
		answersFieldArray = new TextField[] {a1Text, a2Text, a3Text, a4Text };
		answersText = new String[Qlist.size()];
		questionText.setText(Qlist.get(0).getQuestionText());
		a1Text.setText(Qlist.get(0).getOp1());
		a2Text.setText(Qlist.get(0).getOp2());
		a3Text.setText(Qlist.get(0).getOp3());
		a4Text.setText(Qlist.get(0).getOp4());
		answersArray = new int[Qlist.size()];
		for(int i = 0; i < Qlist.size(); i++) {
			answersText[i] = "";
		}
		for (int j = 0; j < Qlist.size(); j++)
			answersArray[j] = -1;
		timer(test.getDuration());

	}

	public static void main(String[] args) {
		launch(args);
	}


    /**
     * Starts the JavaFX application by loading the FXML file and displaying the GUI.
     * @param primaryStage The primary stage for this application.
     * @throws Exception if an error occurs while loading the FXML file.
     */
	public void start(final Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(this.getClass().getResource("/gui/OnlineTestController.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Online Test");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

    /**
     * Starts the timer for the online test with the specified duration.
     * @param seconds The duration of the online test in seconds.
     */
	public void timer(int seconds) {
		int totalSeconds = seconds;

		timer = new Timer();
		TimerTask task = new TimerTask() {
			int remainingSeconds = totalSeconds;

			@Override
			public void run() {
				Platform.runLater(() -> {
					timeLabel.setText(getFormattedTime(remainingSeconds));
					remainingSeconds--;
					String status= ClientMissionHandler.GET_STATUS(testCode);
					String request= ClientMissionHandler.GET_REQUEST(testId);
					if(status.equals("Paused"))
					{
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
						hideButton.fire();

					} 
					if(request!=null && requestDone) {
						
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
					        timeLabel.setText(getFormattedTime(remainingSeconds));
					        requestDone = false;
					}
					}
					
					
					
					if (remainingSeconds < 300) {
						timeLabel.setTextFill(Color.RED);
					}
					if (remainingSeconds < 0) {
						timer.cancel();
						//notSubmitted++;
						Alert endTimeDialog = new Alert(Alert.AlertType.WARNING);
						endTimeDialog.setTitle("Time's up!");
						endTimeDialog.setHeaderText(null);
						endTimeDialog.setContentText("Time's up! ");
						endTimeDialog.showAndWait();
						
						int grade = checkTest();
						ClientMissionHandler.ADD_PERFORMED_TEST(testId, test.getSubject(), test.getCourse(),
								user.getUserID(), user.getName() + " " + user.getSurname(), grade, copyCheck, "Online");

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
						hideButton.fire();
					}
					});
			
			}
		};
		timer.scheduleAtFixedRate(task,0,1000);
	}

    /**
     * Formats the given time in seconds into a formatted string of hours, minutes, and seconds.
     * @param seconds The time in seconds.
     * @return A formatted string representation of the time.
     */
	private String getFormattedTime(int seconds) {
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		int secs = seconds % 60;
		return String.format("%02d:%02d:%02d", hours, minutes, secs);
	}

    /**
     * Handles the selection of an answer for a question.
     *
     * @param event the mouse event triggered by the selection
     */
	@FXML
	void markQuestion(MouseEvent event) {
		for (int i = 0; i < 4; i++) {
			if (radioButtonArray[i] != (RadioButton) event.getSource())
				radioButtonArray[i].setSelected(false);
			else {
				radioButtonArray[i].setSelected(true);
				answersArray[currQuestion] = i;
				answersText[currQuestion] = answersFieldArray[i].getText();
			}

		}

	}

	void resetSelect(int k) {
		for (int i = 0; i < 4; i++) {
			if (i != k)
				radioButtonArray[i].setSelected(false);
			else {
				if (k != -1)
					radioButtonArray[i].setSelected(true);
			}

		}
	}

    /**
     * Displays the previous question.
     *
     * @param event the mouse event triggered by the button click
     */
	@FXML
	void PreviousQuestion(MouseEvent event) {
		if (currQuestion > 0) {
			currQuestion--;
			questionText.setText(Qlist.get(currQuestion).getQuestionText());
			a1Text.setText(Qlist.get(currQuestion).getOp1());
			a2Text.setText(Qlist.get(currQuestion).getOp2());
			a3Text.setText(Qlist.get(currQuestion).getOp3());
			a4Text.setText(Qlist.get(currQuestion).getOp4());
			progressBar.setProgress((currQuestion + 1) / Qlist.size());
			positionLabel.setText("Test01:Q" + (currQuestion + 1));
			resetSelect(answersArray[currQuestion]);
		}

	}

    /**
     * Displays the next question.
     *
     * @param event the mouse event triggered by the button click
     */
	@FXML
	void nextQuestion(MouseEvent event) {
		if (currQuestion < Qlist.size() - 1) {
			currQuestion++;
			questionText.setText(Qlist.get(currQuestion).getQuestionText());
			a1Text.setText(Qlist.get(currQuestion).getOp1());
			a2Text.setText(Qlist.get(currQuestion).getOp2());
			a3Text.setText(Qlist.get(currQuestion).getOp3());
			a4Text.setText(Qlist.get(currQuestion).getOp4());
			progressBar.setProgress((currQuestion + 1) / Qlist.size());
			positionLabel.setText("Test01:Q" + (currQuestion + 1));
			resetSelect(answersArray[currQuestion]);

		}

	}

    /**
     * Submits the test.
     *
     * @param event the mouse event triggered by the button click
     * @throws IOException if an error occurs during submission
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
			System.out.println("Submitted");
			// Send data of test
			int grade = checkTest();
			ClientMissionHandler.ADD_PERFORMED_TEST(test.getID(), test.getSubject(), test.getCourse(), user.getUserID(),
					test.getAuthor(), grade, copyCheck, "Online");

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

	int checkTest() {
		int grade = 0;
		for (int i = 0; i < Qlist.size(); i++) {
			if (answersText[i].equals(Qlist.get(i).getCoreectop())) {
				grade += Integer.parseInt(Qlist.get(i).getPoints());
				copyCheck += "0";
			} else
				copyCheck += String.valueOf(answersArray[i] + 1);

		}
		return grade;

	}
	


}
