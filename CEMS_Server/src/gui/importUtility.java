package gui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entities.DatabaseConnector;

/**
 * Utility class for importing data from a text file into a database table.
 */
public class importUtility {
	/**
	 * Imports data from a text file into a database table. The text file should be
	 * tab-separated and have the following columns: userName, password, userType,
	 * name, surname, email, userID, connectionStatus.
	 */
	public void importDB() {
		String txtFilePath = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\user.txt";
		String tableName = "user";
		String query = "INSERT INTO " + tableName
				+ " (userName, password, userType, name, surname, email, userID, connectionStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection con = DatabaseConnector.getDatabaseConnectorInstance().getConnection();
				BufferedReader br = new BufferedReader(new FileReader(txtFilePath));
				PreparedStatement ps = con.prepareStatement(query)) {

			String line;
			while ((line = br.readLine()) != null) {
				String[] data = line.split("\t");
				if (data.length == 8) {
					for (int i = 0; i < data.length; i++) {
						ps.setString(i + 1, data[i]);
					}
					ps.executeUpdate();
				}
			}
			// Execute a SELECT query to refresh the table
			String selectQuery = "SELECT * FROM " + tableName;
			try (PreparedStatement selectStatement = con.prepareStatement(selectQuery);
					ResultSet resultSet = selectStatement.executeQuery()) {
			} catch (SQLException e) {
				System.out.println("Failed to refresh table: " + e.getMessage());
			}

			System.out.println("Data imported successfully to CEMS database");
		} catch (IOException | SQLException e) {
			System.out.println("Failed to import TXT data: " + e.getMessage());
		}
	}
}
