package gui;

import java.io.IOException;
import java.io.OutputStream;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * The Console class replaces the output stream with a TextArea within the GUI.
 * It allows redirecting output to the TextArea in the FXML file.
 */
public class Console extends OutputStream {
	private TextArea console;

	/**
	 * Constructs a Console object with the specified TextArea.
	 *
	 * @param console The TextArea used as the console output
	 */
	public Console(TextArea console) {
		this.console = console;
	}

	/**
	 * Appends the specified text to the console TextArea. This method is run on the
	 * JavaFX application thread to ensure thread safety.
	 *
	 * @param valueOf The text to append
	 */

	public void appendText(String valueOf) {
		Platform.runLater(() -> console.appendText(valueOf));
	}

	/**
	 * Writes a byte to the output stream. This method is called when writing output
	 * to the console. It converts the byte to a character and appends it to the
	 * console TextArea.
	 *
	 * @param b The byte to write
	 * @throws IOException If an I/O error occurs
	 */
	@Override
	public void write(int b) throws IOException {
		appendText(String.valueOf((char) b));
	}
}
