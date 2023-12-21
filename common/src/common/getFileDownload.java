package common;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * The getFileDownload class provides a method to accept a file download request
 * from a client. It listens on a specific port and sends the requested file to
 * the client.
 * 
 * This class provides the accept2() method to handle the file download process.
 * Listens for a file download request from a client on a specific port and
 * sends the requested file.
 * 
 */
public class getFileDownload {
	/**
	 * @throws IOException if an I/O error occurs during the file download process
	 */
	public static void accept2() throws IOException {
		ServerSocket serverSocket = null;

		try {

			serverSocket = new ServerSocket(4682);
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
					"C:\\Users\\classroom\\Downloads\\G4_Server\\new_file_download.docx", false);
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