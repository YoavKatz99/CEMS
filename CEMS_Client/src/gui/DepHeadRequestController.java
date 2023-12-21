package gui;

import java.util.ArrayList;
import java.util.List;

import client.ClientMissionHandler;
import common.ExtensionRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * The controller class for the DepHeadRequestsPage.fxml GUI.
 * It handles the extension requests for a department head user.
 */
public class DepHeadRequestController {
	@FXML
	private TableView<ExtensionRequest> requestTable;
	@FXML
	private TableColumn<ExtensionRequest, String> applicantCol;
	@FXML
	private TableColumn<ExtensionRequest, String> testidCol;
	@FXML
	private TableColumn<ExtensionRequest, String> detailsCol;
	@FXML
	private TableColumn<ExtensionRequest, String> statusCol;
	@FXML
	private Button SaveBtn;
	@FXML
	private Button BackBtn;

	private ObservableList<ExtensionRequest> requestList = FXCollections.observableArrayList();;
	private double xoffset;
	private double yoffset;

	List<String> choices = new ArrayList<>();

    /**
     * Starts the DepHeadRequestsPage GUI.
     *
     * @param primaryStage The primary stage for the GUI.
     * @throws Exception if an error occurs during GUI loading.
     */
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DepHeadRequestsPage.fxml"));
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
     * Initializes the controller after the GUI is loaded.
     * Retrieves the extension requests from the server and sets up the table columns.
     */
	@SuppressWarnings("unchecked")
	public void initialize() {
		requestList = (ObservableList<ExtensionRequest>) FXCollections
				.observableArrayList(ClientMissionHandler.GET_REQUESTS());
		// Initialize the choices list with initial values
		for (int i = 0; i < requestList.size(); i++) {
			choices.add(null);
		}
		// Set up the table columns
		applicantCol.setCellValueFactory(new PropertyValueFactory<>("applicant"));
		testidCol.setCellValueFactory(new PropertyValueFactory<>("testID"));
		detailsCol.setCellValueFactory(new PropertyValueFactory<>("requestDetails"));
		statusCol.setCellFactory(column -> new ComboBoxTableCell<ExtensionRequest, String>("Approve", "Pending") {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty) {
					ComboBox<String> comboBox = new ComboBox<>(getItems());
					comboBox.setValue("Pending");
					setGraphic(comboBox);
					comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
						// Handle the ComboBox value change
						int row = getIndex();
						choices.set(row, newValue); // Update the choices list
					});
				}
			}
		});
		requestTable.setItems(requestList);
	}


    /**
     * Handles the action when the Save button is clicked.
     * Updates the selected extension requests with their status and saves the changes.
     *
     * @param event The action event.
     * @throws Exception if an error occurs during the update and save process.
     */
	@FXML
	public void handleSaveClick(ActionEvent event) throws Exception {
		// Iterate over the items in the requestTable
		List<String> approvedList = new ArrayList<>();
		for (int i = 0; i < choices.size(); i++) {
			String choice = choices.get(i);
			if ("Approve".equals(choice)) {
				ExtensionRequest request = requestList.get(i);
				String id = request.getTestID();
				approvedList.add(id);
			}
		}
		if (!approvedList.isEmpty()) {
			ClientMissionHandler.UPDATE_REQUESTS(approvedList);
			initialize();
		} else {
			System.out.println("empty");
		}
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

}
