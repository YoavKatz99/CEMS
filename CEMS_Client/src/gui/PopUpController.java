package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Controller class for displaying popup alerts. This class provides methods to
 * show different types of popup alerts.
 */
public class PopUpController {

	/**
	 * Displays an information alert popup with the given message.
	 *
	 * @param msg The message to be displayed in the popup.
	 */
	public void showAlert(String msg) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Popup Message");
		alert.setHeaderText(null);
		alert.setContentText(msg);

		// Customize the button text
		ButtonType okButton = new ButtonType("OK");
		alert.getButtonTypes().setAll(okButton);

		// Show the alert
		alert.showAndWait();

	}
}
