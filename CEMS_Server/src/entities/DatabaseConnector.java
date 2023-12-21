package entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import common.MissionPack;
import ocsf.server.ConnectionToClient;
import server.ServerMissionAnalyze;

/**
 * The DatabaseConnector class is responsible for connecting to the database,
 * parsing data, and managing the connection.
 */
public class DatabaseConnector {

	private static DatabaseConnector dataBaseConnector = null;
	private Connection connection = null;
	private boolean isConnected = false;
	private List<String> connectionDetailsList = new ArrayList<>();

	/**
	 * Private constructor to prevent direct instantiation of the DatabaseConnector
	 * class.
	 */
	private DatabaseConnector() {
	}

	/**
	 * Returns the singleton instance of the DatabaseConnector class.
	 *
	 * @return The DatabaseConnector instance
	 */
	public static DatabaseConnector getDatabaseConnectorInstance() {
		if (dataBaseConnector == null) {
			dataBaseConnector = new DatabaseConnector();
		}
		return dataBaseConnector;
	}

	/**
	 * Returns the database connection. If the connection is not established, it
	 * configures the driver and connects to the database.
	 *
	 * @return The database connection
	 */
	public Connection getConnection() {
		if (connection == null) {
			configDriver();
			connect();
		}
		return connection;
	}

	/**
	 * Parses the given MissionPack object and invokes the server's mission analysis
	 * method.
	 *
	 * @param obj    The MissionPack object to parse
	 * @param client The ConnectionToClient object representing the client
	 */
	public static void parsingToData(MissionPack obj, ConnectionToClient client) {
		try {
			ServerMissionAnalyze.MissionsAnalyze(obj, client);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * connecting to the database with the correct information that given from the
	 * server screen. if the information is incorrect, it will throw SQLException.
	 * 
	 * @return string depending on the information
	 */
	public String connect() {
		StringBuffer buff = new StringBuffer();
		buff.append(configDriver());
		if (buff.toString().equals("\nDriver definition failed\n"))
			return buff.toString();
		// add check if length smaller than 4
		if (connectionDetailsList.size() == 4) {
			String ip = connectionDetailsList.get(0);
			String dbName = connectionDetailsList.get(1);
			String dbUsername = connectionDetailsList.get(2);
			String dbPassword = connectionDetailsList.get(3);
			connectToDatebase(buff, ip, dbName, dbUsername, dbPassword);
		} else {
			buff.append("missing arguments");
		}
		return buff.toString();
	}

	/**
	 * Sets the connection details list.
	 *
	 * @param detailsList The list of connection details
	 */
	public void setConnectionDetailsList(List<String> detailsList) {
		for (String currentDetail : detailsList) {
			connectionDetailsList.add(currentDetail);
		}
	}

	/**
	 * Disconnects from the database.
	 *
	 * @return True if the disconnection is successful, false otherwise
	 */
	public boolean disconnect() {
		if (isConnected) {
			if (connection != null) {
				try {
					connection.close();
					connection = null;
				} catch (SQLException e) {
					return false;
				}
			}
		}
		isConnected = false;
		return true;
	}

	/**
	 * Establishes a connection to the database using the provided connection
	 * details. Updates the connection status and the StringBuffer object with the
	 * connection result.
	 *
	 * @param buff       The StringBuffer object to store the connection status.
	 * @param ip         The IP address of the database server.
	 * @param dbName     The name of the database.
	 * @param dbUsername The username for the database connection.
	 * @param dbPassword The password for the database connection.
	 */
	private void connectToDatebase(StringBuffer buff, String ip, String dbName, String dbUsername, String dbPassword) {
		try {
			connection = DriverManager.getConnection(dbName, dbUsername, dbPassword); // URL, Username, Password+changed
																						// url with message
																						// "&useSSL=false"
			buff.append("\nDatabase connection succeeded!\n");
			isConnected = true;

		} catch (SQLException e) {
			buff.append("\nDatabase connection failed!\n");
			isConnected = false;
		}
	}

	/**
	 * configures the driver for the JDBC API
	 * 
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	private String configDriver() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			return "\nDriver definition succeed\n";
		} catch (Exception ex) {
			/* handle the error */
			return "\nDriver definition failed\n";
		}
	}
}
