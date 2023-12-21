package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientMissionHandler;
import common.PerformedTest;
import common.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

/**
 * Controller class for the Lecturer Grades Check GUI.
 */
public class LecturerGradesCheck implements Initializable {
	@FXML
	private Button BackBtn;

	@FXML
	private ToggleButton editButton;
	@FXML
	private Circle light;

	@FXML
	private Label idLbl = new Label();
	@FXML
	private TableView<PerformedTest> gradesTable;
	@FXML
	private TableColumn<PerformedTest, String> testIDCol;
	@FXML
	private TableColumn<PerformedTest, String> courseCol;
	@FXML
	private TableColumn<PerformedTest, Integer> gradeCol;

	@FXML
	private TableColumn<PerformedTest, String> notesCol;

	@FXML
	private TableColumn<PerformedTest, String> studentIDCol;

	ObservableList<PerformedTest> PerformedTests = FXCollections.observableArrayList();
	List<PerformedTest> ptToUpdate = new ArrayList<>();
	private User user = LoginController.getLoggedInUser();

	private double xoffset;
	private double yoffset;
	private boolean update = false;

	/**
	 * Starts the GradesViewPage GUI.
	 * 
	 * @param primaryStage The primary stage for the GUI.
	 * @throws Exception if an error occurs during GUI loading.
	 */

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/LecturerGradesCheck.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS Grades");
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
	 * Loads the performed tests and displays them in the table.
	 * 
	 * @throws Exception if an error occurs during the loading of tests.
	 */
	public void loadTests() throws Exception {
		ClientMissionHandler.GET_GRADES_BY_LECTURER(user, this.PerformedTests, this.gradesTable);
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
		if (!ptToUpdate.isEmpty())
			ClientMissionHandler.UPDATE_PERFORMED_TESTS(user, ptToUpdate);

		Stage primaryStage = new Stage();
		LecturerController clientController = new LecturerController();
		clientController.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	/**
	 * Handles the action when the Edit button is clicked.
	 *
	 * @param event The mouse click.
	 */
	@FXML
	void handleEditClick(ActionEvent event) {
		if (editButton.isSelected()) {
			light.setFill(Color.GREEN);
			gradeCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
			notesCol.setCellFactory(TextFieldTableCell.forTableColumn());
			gradeCol.setEditable(true);
			notesCol.setEditable(true);
			gradesTable.setEditable(true);
		} else {
			light.setFill(Color.RED);
			gradeCol.setCellFactory(centeredCellFactoryInt()); // Use the original cell factory
			notesCol.setCellFactory(centeredCellFactory()); // Use the original cell factory
			gradeCol.setEditable(false);
			notesCol.setEditable(false);
			gradesTable.setEditable(false);
		}
	}

	/**
	 * Handles the commit event when the grade column is edited.
	 *
	 * @param event The cell edit event.
	 */
	private void onGradeEditCommit(TableColumn.CellEditEvent<PerformedTest, Integer> event) {
		PerformedTest test = event.getRowValue();
		int newGrade = event.getNewValue();
		test.setGrade(newGrade);
		ptToUpdate.add(test);
		update = true;
		// Save the changes to the database or perform any necessary actions
	}

	/**
	 * Handles the commit event when the notes column is edited.
	 *
	 * @param event The cell edit event.
	 */
	private void onNotesEditCommit(TableColumn.CellEditEvent<PerformedTest, String> event) {
		PerformedTest test = event.getRowValue();
		String newNotes = event.getNewValue();
		test.setNotes(newNotes);
		ptToUpdate.add(test);
		update = true;

		// Save the changes to the database or perform any necessary actions
	}

	/**
	 * Initialize table properties and load the students grade
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Initialize table columns
		testIDCol.setCellValueFactory((Callback) new PropertyValueFactory<PerformedTest, String>("testID"));
		courseCol.setCellValueFactory((Callback) new PropertyValueFactory<PerformedTest, String>("course"));
		gradeCol.setCellValueFactory((Callback) new PropertyValueFactory<PerformedTest, Integer>("grade"));
		studentIDCol.setCellValueFactory((Callback) new PropertyValueFactory<PerformedTest, String>("userId"));
		notesCol.setCellValueFactory((Callback) new PropertyValueFactory<PerformedTest, String>("notes"));

		gradeCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		notesCol.setCellFactory(TextFieldTableCell.forTableColumn());
		gradeCol.setOnEditCommit(this::onGradeEditCommit);
		notesCol.setOnEditCommit(this::onNotesEditCommit);
		gradesTable.setEditable(false);

		idLbl.setText(user.getName() + " " + user.getSurname());
		idLbl.setAlignment(Pos.CENTER);
		// Set cell factory for each column
		testIDCol.setCellFactory(centeredCellFactory());
		courseCol.setCellFactory(centeredCellFactory());
		studentIDCol.setCellFactory(centeredCellFactory());
		notesCol.setCellFactory(centeredCellFactory());
		gradeCol.setCellFactory(centeredCellFactoryInt());
		// Set styles for the table
		gradesTable.setItems(PerformedTests);
		gradesTable.autosize();
		try {
			loadTests();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a cell factory that centers the content in a table cell for string
	 * values.
	 * 
	 * @return The centered cell factory.
	 */
	private <T> Callback<TableColumn<T, String>, TableCell<T, String>> centeredCellFactory() {
		return column -> new TableCell<>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(null);
					setAlignment(Pos.CENTER);
				} else {
					setText(item);
					setAlignment(Pos.CENTER);
				}
			}
		};
	}

	/**
	 * Creates a cell factory that centers the content in a table cell for integer
	 * values.
	 * 
	 * @return The centered cell factory for integers.
	 */
	private <T> Callback<TableColumn<T, Integer>, TableCell<T, Integer>> centeredCellFactoryInt() {
		return column -> new TableCell<>() {
			@Override
			protected void updateItem(Integer item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(null);
					setAlignment(Pos.CENTER);
				} else {
					setText(item.toString());
					setAlignment(Pos.CENTER);
				}
			}
		};
	}

}
