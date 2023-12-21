package common;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * The getFileUpload class provides a method to accept a file upload from a
 * client. It listens on a specific port and saves the received file to a
 * specified location.
 * 
 * This class provides the accept2() method to handle the file upload process.
 *
 * Listens for a file upload from a client on a specific port and saves the
 * received file.
 */

public class getFileUpload {
	/**
	 * @throws IOException if an I/O error occurs during the file upload process
	 */
	public static void accept2() throws IOException {
		ServerSocket serverSocket = null;

		try {

			serverSocket = new ServerSocket(4683);
		} catch (IOException ex) {
			System.out.println("Can't setup server on this port number. ");
		}

		Socket socket = null;
		InputStream in = null;
		OutputStream out = null;
		System.out.println("get_file_20");
		try {
			socket = serverSocket.accept();
		} catch (IOException ex) {
			System.out.println("Can't accept client connection. ");
		}

		try {
			in = socket.getInputStream();
		} catch (IOException ex) {
			System.out.println("Can't get socket input stream. ");
		}

		try {
			out = new FileOutputStream(
					"C:\\Users\\classroom\\Downloads\\G4_Server\\new_file_upload.docx", false);
		} catch (FileNotFoundException ex) {
			System.out.println("File not found. ");
		}

		byte[] bytes = new byte[16 * 1024];

		int count;
		while ((count = in.read(bytes)) > 0) {
			out.write(bytes, 0, count);
		}

		out.close();
		in.close();
		socket.close();
		serverSocket.close();
	}
}