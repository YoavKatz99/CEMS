package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import client.ClientMissionHandler;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;;

/**
 * The controller class for the DepHeadSysemDataPage.fxml GUI.
 * It handles the display of system data for a department head user.
 */
public class DepHeadSysemDataController implements Initializable {

	@FXML
	private Button BackBtn;
	@FXML
	private Button ViewTableBtn;
	@FXML
	private ComboBox<String> tableComboBox;
	@FXML
	private TableView<Object> tableView;

	// private User user = LoginController.getLoggedInUser();
	private double xoffset;
	private double yoffset;
	private List<Map<String, Object>> tableData = new ArrayList<>();

    /**
     * Starts the DepHeadSysemDataPage GUI.
     *
     * @param primaryStage The primary stage for the GUI.
     * @throws Exception if an error occurs during GUI loading.
     */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/DataBasePage.fxml"));
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
     * Handles the action when a table is selected from the ComboBox.
     * Retrieves the data for the selected table from the server and updates the TableView.
     *
     * @param event The action event.
     * @throws Exception if an error occurs during the data retrieval or table update process.
     */
	@FXML
	public void handleTableSelection(ActionEvent event) throws Exception {
		String Selection = (String) tableComboBox.getValue();
		if (Selection != null) {
			switch (Selection) {

			case "Users": {
				tableData = ClientMissionHandler.GET_DB_TABLE("user");
				break;
			}
			case "Preformed tests": {
				tableData = ClientMissionHandler.GET_DB_TABLE("performedtest");
				break;
			}
			case "Test statistics": {
				tableData = ClientMissionHandler.GET_DB_TABLE("test_stats");
				break;
			}
			case "Question": {
				tableData = ClientMissionHandler.GET_DB_TABLE("question");
				break;
			}
			case "Tests": {
				tableData = ClientMissionHandler.GET_DB_TABLE("tests");
				break;
			}
			}
			updateTableView();
		} else {
			PopUpController popUpController = new PopUpController();
			popUpController.showAlert("Must choose table before proceeding!");
		}
	}

	
    /**
     * Updates the TableView with the retrieved table data.
     */
	public void updateTableView() {
		tableView.getColumns().clear(); // Clear existing columns
		if (tableData.isEmpty()) {
			return; // No data to display
		}
		Map<String, Object> firstRow = tableData.get(0);
		for (String columnName : firstRow.keySet()) {
			TableColumn<Object, Object> column = new TableColumn<>(columnName);
			column.setCellValueFactory(
					new Callback<TableColumn.CellDataFeatures<Object, Object>, ObservableValue<Object>>() {
						@Override
						public ObservableValue<Object> call(TableColumn.CellDataFeatures<Object, Object> param) {
							@SuppressWarnings("unchecked")
							Map<String, Object> rowData = (Map<String, Object>) param.getValue();
							return new SimpleObjectProperty<>(rowData.get(columnName));
						}
					});
			tableView.getColumns().add(column);
		}
		ObservableList<Object> rowData = FXCollections.observableArrayList(tableData);
		tableView.setItems(rowData);
	}

    /**
     * Handles the action when the Back button is clicked.
     * Returns to the department head menu.
     *
     * @param event The action event.
     * @throws Exception if an error occurs during the menu loading.
     */
	@FXML
	public void handleBackClick(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		DepHeadMenuController clientController = new DepHeadMenuController();
		clientController.start(primaryStage);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<String> tableNames = FXCollections.observableArrayList("Users", "Question", "Tests",
				"Preformed tests", "Test statistics");
		tableComboBox.setItems(tableNames);
	}
}
