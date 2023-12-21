package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import common.ClientStatus;
import common.MissionPack;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;

import entities.ConnectedClient;
import entities.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import server.ServerConfiguration;

/**
 * The ServerController class is a controller for the server GUI application. It
 * handles the initialization of the GUI components and provides event handlers
 * for button clicks and other user interactions.
 */
public class ServerController implements Initializable {

	@FXML
	private TextField DBNameField;

	@FXML
	private PasswordField DBPasswordTextField;

	@FXML
	private TextField DBUserTextField;

	@FXML
	private TextField IPTextField;

	@FXML
	private Button connectButton;

	@FXML
	private TableView<ConnectedClient> connectionTable;

	@FXML
	private TextArea consoleField;

	@FXML
	private Button disconnectButton;

	@FXML
	private TableColumn<ConnectedClient, String> hostCol;

	@FXML
	private TableColumn<ConnectedClient, String> ipCol;

	@FXML
	private TextField portTextField;

	private static String ip = null;

	@FXML
	private TableColumn<ConnectedClient, String> statusCol;
	PrintStream replaceConsole;
	static ObservableList<ConnectedClient> listView = FXCollections.observableArrayList();
	private static boolean ifFirstConnector;

	public static ObservableList<ConnectedClient> getListView() {
		return listView;
	}

	public static void setListView(ObservableList<ConnectedClient> listView) {
		ServerController.listView = listView;
	}

	public void start(final Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerScreen.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
	}

	/**
	 * Handles the button click event for the connectButton. Connects to the server
	 * using the provided details.
	 *
	 * @param event The MouseEvent representing the button click event.
	 */
	@FXML
	void clickOnConnect(MouseEvent event) {
		List<String> detailsList = new ArrayList<>();
		ServerUI.runServer(portTextField.getText());
		String ip = IPTextField.getText();
		String DBName = DBNameField.getText();
		String DBUserName = DBUserTextField.getText();
		String password = DBPasswordTextField.getText();
		if ((ip != null) && (DBName != null) && (DBUserName != null) && (password != null)) {
			detailsList.add(IPTextField.getText());
			detailsList.add(DBNameField.getText());
			detailsList.add(DBUserTextField.getText());
			detailsList.add(DBPasswordTextField.getText());
			DatabaseConnector.getDatabaseConnectorInstance().setConnectionDetailsList(detailsList);
		}
		System.out.println(DatabaseConnector.getDatabaseConnectorInstance().connect());
		connectButton.setDisable(true);
		disconnectButton.setDisable(false);
		disableDataInput(true);
		importUtility importUtility = new importUtility();
		// importUtility.importDB();
	}

	/**
	 * Handles the button click event for the disconnectButton. Disconnects from the
	 * server.
	 *
	 * @param event The MouseEvent representing the button click event.
	 */
	@FXML
	void clickOnDisconnect(MouseEvent event) {
		ServerUI.disconnect();
		disconnectButton.setDisable(true);
		connectButton.setDisable(false);
		disableDataInput(false);
		connectionTable.getItems().clear();
	}

	/**
	 * Disables or enables the input fields based on the specified condition.
	 *
	 * @param condition The condition indicating whether to disable or enable the
	 *                  input fields.
	 */
	void disableDataInput(boolean Condition) {
		portTextField.setDisable(Condition);
		IPTextField.setDisable(Condition);
		DBNameField.setDisable(Condition);
		DBUserTextField.setDisable(Condition);
		DBPasswordTextField.setDisable(Condition);
	}

	public static String getIp() {
		return ip;
	}

	public String getLocalIp() {
		try {
			ip = Inet4Address.getLocalHost().getHostAddress();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return ip;
	}

	private void setTableColumns() {
		ipCol.setCellValueFactory(new PropertyValueFactory<ConnectedClient, String>("ip"));
		hostCol.setCellValueFactory(new PropertyValueFactory<ConnectedClient, String>("host"));
		statusCol.setCellValueFactory(new PropertyValueFactory<ConnectedClient, String>("status"));
	}

	void consoleStreamIntoGUI() {
		replaceConsole = new PrintStream(new gui.Console(consoleField));
		System.setOut(replaceConsole);
		System.setErr(replaceConsole);
	}

	public static void SetObser(MissionPack obj) {
		@SuppressWarnings("unchecked")
		final ArrayList<String> list = (ArrayList<String>) obj.getInformation();
		final ConnectedClient client = new ConnectedClient(list.get(0), list.get(1), ClientStatus.CONNECTED);
		listView.add(client);
		if (ifFirstConnector) {
			listView.remove(0);
			ifFirstConnector = false;
		}
	}

	/**
	 * Initializes the controller after the FXML file is loaded. This method is
	 * called automatically by the JavaFX framework.
	 *
	 * @param location  The location used to resolve relative paths for the root
	 *                  object.
	 * @param resources The resources used to localize the root object.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Initialize the table columns about the connected clients detail
		connectionTable.setItems(ServerConfiguration.getClientList());
		// Setting up our TableView columns
		setTableColumns();
		// Change output stream into the ServerGUI Console Area
		consoleStreamIntoGUI();
		try {
			ip = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		IPTextField.setText(ip);
		portTextField.setText("5555");
		DBNameField.setText("jdbc:mysql://localhost/cems?serverTimezone=IST");
		DBUserTextField.setText("root");
		DBPasswordTextField.setText("roi270495");
		disconnectButton.setDisable(true);
	}
}
