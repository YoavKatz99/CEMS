package client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import common.*;
import gui.ClientUI;
import gui.LecturerReportController;
import gui.LoginController;
import gui.PopUpController;
import gui.ServerController;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * The ClientMissionHandler class acts as a controller for the client-side of
 * the application. It handles the communication between the client and the
 * server.
 */
public class ClientMissionHandler {
	/**
	 * Connects to the server with the provided IP address and port. Sends the
	 * connection details to the server. If the connection is successful, hides the
	 * current window and opens the login window.
	 *
	 * @param event The mouse event that triggered the connection attempt.
	 * @param ip    The IP address of the server.
	 * @param port  The port number of the server.
	 * @throws Exception If an exception occurs during the connection process.
	 */
	public static void CONNECT_TO_SERVER(final MouseEvent event, final String ip, final String port) throws Exception {
		ClientUI.chat = new ClientController(ip, Integer.parseInt(port));
		MissionPack obj = new MissionPack(Mission.SEND_CONNECTION_DETAILS, null, null);
		final ArrayList<String> details = new ArrayList<String>();
		details.add(InetAddress.getLocalHost().getHostAddress());
		details.add(InetAddress.getLocalHost().getHostName());
		obj.setInformation(details);
		System.out.println("in client");
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse().equals(Response.UPDATE_CONNECTION_SUCCESS)) {
			((Node) event.getSource()).getScene().getWindow().hide();
			Stage primaryStage = new Stage();
			LoginController clientController = new LoginController();
			clientController.start(primaryStage);
		} else {
			System.out.println("did not connect to server");
		}
	}

	/**
	 * Retrieves the list of questions from the server and populates the provided
	 * ListView and TableView.
	 *
	 * @param listView    The ObservableList used by the ListView to display
	 *                    questions.
	 * @param table       The TableView used to display questions.
	 * @param statusLabel The label used to display the upload status.
	 */
	public static void GET_QUESTIONS(final ObservableList<Question> listView, final TableView<Question> table,
			final Label statusLabel) {
		MissionPack obj = new MissionPack(Mission.GET_QUESTIONS, null, null);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.FOUND_QUESTIONS) {
			listView.clear();
			List<?> list = (List<?>) obj.getInformation();
			for (int i = 0; i < list.size(); ++i) {
				listView.add((Question) list.get(i));
				System.out.println(list.get(i));
			}
			table.setItems(listView);
			statusLabel.setText("Upload Success");
		} else {
			statusLabel.setText("Upload Failed");
		}
	}

	/**
	 * Disconnects from the server. Sends a disconnection request to the server with
	 * the client's connection details.
	 */
	public static void DISCONNECT_FROM_SERVER() {
		// Create a MissionPack object with the disconnection mission
		final MissionPack obj = new MissionPack(Mission.SEND_DISCONNECTION_DETAILS, null, null);
		final ArrayList<String> details = new ArrayList<String>();
		try {
			// Add the client's host address and host name to the details list
			details.add(InetAddress.getLocalHost().getHostAddress());
			details.add(InetAddress.getLocalHost().getHostName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Set the details as the information in the MissionPack object
		obj.setInformation(details);
		ClientUI.chat.accept(obj);
	}

	/**
	 * Attempts to log in a user with the provided username and password.
	 *
	 * @param username        The username of the user to log in.
	 * @param password        The password of the user to log in.
	 * @param loginController The LoginController instance associated with the login
	 *                        process.
	 * @return true if the login is successful, false otherwise.
	 * @throws Exception If an error occurs during the login process.
	 */
	public static boolean LOGIN(String username, String password, LoginController loginController) throws Exception {
		// Create a User object with the provided username and password
		User user = new User(username, password, null, null, null, null);
		// Create a MissionPack object with the login mission and the User object as
		// information
		MissionPack obj = new MissionPack(Mission.LOGIN, null, (Object) user);
		ClientUI.chat.accept(obj);// Send the login request to the server
		obj = ClientUI.chat.getResponseFromServer(); // Receive the response from the server
		// Process the server response
		if (obj.getResponse() == Response.LOGIN_SUCCESS) {
			// Login successful
			user = (User) obj.getInformation();
			loginController.setPage(user);
			return true;
			// if the user not exsist in the DB
		} else if (obj.getResponse() == Response.LOGIN_FAILURE_notInDB) {
			loginController.userNotExistsError();
			// if the user already loggedin
		} else if (obj.getResponse() == Response.LOGIN_FAILURE_loggedIn) {
			loginController.userAlreadyLoggenInError();
		}
		return false;
	}

	/**
	 * Logs out the specified user.
	 *
	 * @param user The user to log out.
	 * @return true if the logout is successful, false otherwise.
	 */
	public static boolean LOGOUT(User user) {
		// Create a MissionPack object with the logout mission and the User object as
		// information
		MissionPack obj = new MissionPack(Mission.LOGOUT, null, (Object) user);
		// Send the logout request to the server
		ClientUI.chat.accept(obj);
		// Receive the response from the server
		obj = ClientUI.chat.getResponseFromServer();
		// Process the server response
		if (obj.getResponse() == Response.LOGOUT_SUCCESS)
			// Logout successful
			return true;
		else
			// Logout failed
			return false;
	}

	/**
	 * Retrieves the grades for the specified user and populates the provided
	 * ListView and TableView with the retrieved data.
	 *
	 * @param user1    The user for whom to retrieve the grades.
	 * @param listView The ObservableList representing the ListView to populate with
	 *                 the retrieved grades.
	 * @param table    The TableView to populate with the retrieved grades.
	 * @throws Exception if an error occurs during the retrieval process.
	 */

	public static void GET_GRADES(User user1, ObservableList<PerformedTest> listView, TableView<PerformedTest> table)
			throws Exception {
		User user = user1;
		// Create a MissionPack object with the get grades mission and the User object
		// as information
		MissionPack obj = new MissionPack(Mission.GET_GRADES, null, (Object) user);
		ClientUI.chat.accept(obj);// Send the request to the server
		obj = ClientUI.chat.getResponseFromServer(); // Receive the response from the server
		if (obj.getResponse() == Response.GET_GRADES_SUCCESS) {
			// Clear the existing items in the ListView
			listView.clear();
			// Retrieve the list of performed tests from the server response
			List<?> list = (List<?>) obj.getInformation();
			// Iterate over the list and populate the simplified performed test objects in
			// the ListView
			for (int i = 0; i < list.size(); ++i) {
				PerformedTest performedTest = (PerformedTest) list.get(i);
				PerformedTest simplifiedPerformedTest = new PerformedTest(performedTest.getTestID(),
						performedTest.getCourse(), performedTest.getGrade());
				listView.add(simplifiedPerformedTest);
			}
			table.setItems(listView);

		} else {
			// No tests available
			System.out.println("NO TESTS AVIALABLE");

		}
	}

	/**
	 * Retrieves the courses for the specified user and populates the provided
	 * ObservableList and ComboBox with the retrieved data.
	 *
	 * @param Courses        The ObservableList representing the courses to populate
	 *                       with the retrieved data.
	 * @param courseComboBox The ComboBox to populate with the retrieved courses.
	 * @param user           The user for whom to retrieve the courses.
	 */
	public static void GET_COURSES(ObservableList<String> Courses, ComboBox<String> courseComboBox, User user) {
		// Create a MissionPack object with the get courses mission and the User object
		// as information
		MissionPack obj = new MissionPack(Mission.GET_COURSES, null, (Object) user);
		// Send the request to the server
		ClientUI.chat.accept(obj);
		// Receive the response from the server
		obj = ClientUI.chat.getResponseFromServer();
		// Process the server response
		if (obj.getResponse() == Response.GET_COURSES_SUCCESS) {

			Courses.clear();

			List<?> list = (List<?>) obj.getInformation();

			for (int i = 0; i < list.size(); ++i) {
				String course = (String) list.get(i);
				Courses.add(course);
			}
			// Set the items in the ComboBox with the populated ObservableList
			courseComboBox.setItems(Courses);
		} else {
			// Courses load error
			System.out.println("COURSES LOAD ERROR");
		}
	}

	/**
	 * Retrieves the tests associated with the specified course and populates the
	 * provided ObservableList and ComboBox with the retrieved data.
	 *
	 * @param courseName   The name of the course for which to retrieve the tests.
	 * @param tests        The ObservableList representing the tests to populate
	 *                     with the retrieved data.
	 * @param testComboBox The ComboBox to populate with the retrieved tests.
	 */
	public static void GET_TESTS_OF_COURSE(String courseName, ObservableList<String> tests,
			ComboBox<String> testComboBox) {
		// Create a MissionPack object with the get tests of course mission and the
		// course name as information
		MissionPack obj = new MissionPack(Mission.GET_TESTS_OF_COURSE, null, (Object) courseName);
		ClientUI.chat.accept(obj);// Send the request to the server
		obj = ClientUI.chat.getResponseFromServer(); // Receive the response from the server
		if (obj.getResponse() == Response.GET_TESTS_OF_COURSE_SUCCESS) {
			// Clear the existing items in the ObservableList
			tests.clear();
			// Retrieve the list of tests from the server response
			List<?> list = (List<?>) obj.getInformation();
			// Iterate over the list and add the tests to the ObservableList
			for (int i = 0; i < list.size(); ++i) {
				String test = (String) list.get(i);
				tests.add(test);
			}
			// Set the items in the ComboBox with the populated ObservableList
			testComboBox.setItems(tests);
		} else {
			// Tests load error
			System.out.println("TESTS LOAD ERROR");
		}
	}

	/**
	 * Generates a lecturer report for the specified test and updates the UI
	 * components with the retrieved data.
	 *
	 * @param testID        The ID of the test for which to generate the lecturer
	 *                      report.
	 * @param averageLbl    The Label to display the average value in the UI.
	 * @param medianLbl     The Label to display the median value in the UI.
	 * @param info          The ObservableList representing the test information to
	 *                      populate in the UI.
	 * @param testInfoTable The TableView to display the test information in the UI.
	 * @param controller    The LecturerReportController instance for updating the
	 *                      UI components.
	 */
	public static void GENERATE_LECTURER_REPORT(String testID, Label averageLbl, Label medianLbl,
			ObservableList<TestData> info, TableView<TestData> testInfoTable, LecturerReportController controller) {
		// Create a MissionPack object with the generate lecturer report mission and the
		// test ID as information
		MissionPack obj = new MissionPack(Mission.GENERATE_LECTURER_REPORT, null, (Object) testID);
		ClientUI.chat.accept(obj);// Send the request to the server
		obj = ClientUI.chat.getResponseFromServer(); // Receive the response from the server
		// Process the server response
		if (obj != null && obj.getResponse() == Response.GENERATE_LECTURER_REPORT_SUCCESS) {
			// Retrieve the TestData object from the server response
			TestData testData = (TestData) obj.getInformation();
			// Update the UI components with the retrieved data
			controller.testIdLbl.setText("Test Id " + testData.getTest_id() + " Report");
			DecimalFormat decimalFormat = new DecimalFormat("0.00");// define the format
			String formattedNumber = decimalFormat.format(testData.getAverage());
			averageLbl.setText("Average: " + formattedNumber);
			medianLbl.setText("Median: " + testData.getMedain());
			TestData infoForTable = new TestData(testData.getDate(), testData.getTime_allocated(),
					testData.getStudents_started(), testData.getSubmitted(), testData.getNotSubmitted());
			info.clear();// clear the info
			info.add(infoForTable);
			testInfoTable.setItems(info);

			XYChart.Series<String, Number> series = controller.distributionChart.getData().get(0);
			series.getData().clear();
			controller.distributionChart.getXAxis().setLabel("Test " + testData.getTest_id() + " Distribution");
			controller.distributionChart.getYAxis().setLabel("Count");
			int[] distribution = testData.getDistribution();
			int i;
			for (i = 0; i < 9; i++) {
				int startRange = i * 10;
				int endRange = startRange + 9;
				String barLabel = startRange + "-" + endRange;
				series.getData().add(new XYChart.Data<>(barLabel, distribution[i]));
			}
			series.getData().add(new XYChart.Data<>("90-100", distribution[i]));

		} else {
			System.out.println("REPORT FAILURE");
		}

	}

	/**
	 * Retrieves the data from a database table specified by the table name.
	 *
	 * @param tableName The name of the database table to retrieve data from.
	 * @return A List of Maps representing the table data, where each Map contains
	 *         column names as keys and corresponding column values as values.
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> GET_DB_TABLE(String tableName) {
		List<Map<String, Object>> tableData = new ArrayList<>();
		MissionPack obj = new MissionPack(Mission.GET_DB_TABLE, null, (Object) tableName);
		ClientUI.chat.accept(obj);// Send the request to the server
		obj = ClientUI.chat.getResponseFromServer(); // Receive the response from the server
		// Retrieve the table data from the server response
		if (obj.getResponse() == Response.GET_DB_TABLE_SUCCESS) {
			tableData = (List<Map<String, Object>>) obj.getInformation();
		}
		// cannot Retrieve the table data from the server response
		if (obj.getResponse() == Response.GET_DB_TABLE_FAILURE) {
			System.out.println("Failed to retrieve the table");
		}
		return tableData;
	}

	/**
	 * Adds a question to the database with the provided question details.
	 *
	 * @param id             The ID of the question.
	 * @param questionNumber The question number.
	 * @param lecturer       The name of the lecturer.
	 * @param courseName     The name of the course.
	 * @param subjectName    The name of the subject.
	 * @param questionText   The text of the question.
	 * @param answer1        The first answer option.
	 * @param answer2        The second answer option.
	 * @param answer3        The third answer option.
	 * @param answer4        The fourth answer option.
	 * @param rightAnswer    The correct answer.
	 * @return The result of the question addition operation.
	 */
	public static Object ADD_QUESTIONS(String id, String questionNumber, String lecturer, String courseName,
			String subjectName, String questionText, String answer1, String answer2, String answer3, String answer4,
			String rightAnswer) {
		final Question Question = new Question(null, null, null, null, null, null, null, null, null, null, null);
		// Set the question details
		Question.setCourseName(courseName);
		Question.setId(id);
		Question.setQuestionNumber(questionNumber);
		Question.setLecturer(lecturer);
		Question.setCoreectop(rightAnswer);
		Question.setSubject(subjectName);
		Question.setText(questionText);
		Question.setOp1(answer1);
		Question.setOp2(answer2);
		Question.setOp3(answer3);
		Question.setOp4(answer4);
		// Create a new Question object with the provided details
		Question question = new Question(id, subjectName, courseName, questionText, questionNumber, lecturer, answer1,
				answer2, answer3, answer4, rightAnswer);
		// Create a MissionPack object with the add questions data mission and the
		// Question object as information
		MissionPack obj = new MissionPack(Mission.ADD_QUESTIONS_DATA, null, question);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();

		if (obj.getResponse() == Response.ADD_QUESTION_SUCCSESS) {
			System.out.println("Added question to DB");
			return obj.getInformation();
		} else {
			System.out.println("fail");
		}
		return null;

	}

	/**
	 * Retrieves the maximum code questions from the server.
	 *
	 * @return The maximum code questions information if found, or null if not
	 *         found.
	 */
	public static Object MAX_CODE_QUESTIONS() {
		// Create a MissionPack object with the get max code question mission
		MissionPack obj = new MissionPack(Mission.GET_MAX_CODE_QUESTION, null, null);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.FOUND_MAX_CODE_QUESTION) {
			// if the max code found
			System.out.println("Max code was found");
			return obj.getInformation();
		} else {
			// the max code wasnt found
			System.out.println("fail");
			return null;
		}

	}

	/**
	 * Retrieves the test data from the database and displays it on the screen.
	 *
	 * @param listView The ObservableList to store the retrieved test data.
	 * @param table    The TableView to display the test data.
	 */
	// get the test data from the DB and shoe it in the screen
	public static void GET_TESTS(ObservableList<Test> listView, TableView<Test> table) {
		// create new mission
		MissionPack obj = new MissionPack(Mission.GET_TEST, null, null);
		// Receive the response from the server
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.FOUND_TESTS) {
			listView.clear();
			List<?> list = (List<?>) obj.getInformation();
			for (int i = 0; i < list.size(); ++i) {
				listView.add((Test) list.get(i));
				System.out.println(list.get(i));
			}
			table.setItems(listView);

		} else {
			System.out.println("No tests found.");
		}
	}

	/**
	 * Initiates the process of downloading tests from the server to the client.
	 *
	 * @param msg The message containing the necessary information to initiate the
	 *            download.
	 * @throws IOException If an I/O error occurs while downloading the tests.
	 */
	// the server send the file to the client
	public static void DOWNLOAD_TESTS(Object msg) throws IOException {
		System.out.println("here");
		// Get the test ID from the code
		MissionPack obj = new MissionPack(Mission.GET_ID_FROM_CODE, null, msg);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		// Check if getting the test ID was successful
		if (obj.getResponse() == Response.GET_TEST_ID_SUCCESS) {
			// Get the status of the test
			MissionPack obj2 = new MissionPack(Mission.GET_STATUS, null, msg);
			ClientUI.chat.accept(obj2);
			obj2 = ClientUI.chat.getResponseFromServer();
			// Check if getting the test status was successful
			if (obj2.getResponse() == Response.GET_STATUS_TEST_SUCCESS) {
				// Create a TimerTask to schedule the file download
				TimerTask task = new TimerTask() {
					public void run() {
						try {
							getFileDownload.accept2();
						} catch (IOException e) {

							e.printStackTrace();
						}
						;
					}
				};
				Timer timer = new Timer("Timer");
				long delay = 0;
				timer.schedule(task, delay);

				System.out.println(300);
				MissionPack obj1 = new MissionPack(Mission.GET_YAEL, null, msg);
			}
		}
	}

	/**
	 * Retrieves the questions from a specific test in the database and displays
	 * them on the screen.
	 *
	 * @param Qlist   The list to store the retrieved questions.
	 * @param test_id The ID of the test to retrieve the questions from.
	 */
	// get the test data from the DB and shoe it in the screen
	public static void GET_QUESTIONS_FROM_TEST(List<Question> Qlist, String test_id) {
		MissionPack obj = new MissionPack(Mission.GET_QUESTIONS_FROM_TEST, null, (Object) test_id);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		// Check if questions were found for the specified test
		if (obj.getResponse() == Response.FOUND_QUESTIONS) {
			Qlist.clear();
			List<?> list = (List<?>) obj.getInformation();
			// Iterate through the retrieved questions and add them to the list
			for (int i = 0; i < list.size(); ++i) {
				Qlist.add((Question) list.get(i));
				System.out.println(list.get(i).toString());
			}

		} else {
			// if the tests not found
			System.out.println("No tests found.");
		}
	}

	/**
	 * Retrieves lists of courses, lecturers, and students based on a specified
	 * department and populates the corresponding ObservableLists.
	 *
	 * @param courses    The ObservableList to store the retrieved list of courses.
	 * @param lecturers  The ObservableList to store the retrieved list of
	 *                   lecturers.
	 * @param students   The ObservableList to store the retrieved list of students.
	 * @param department The department for which the lists are to be retrieved.
	 */
	@SuppressWarnings("unchecked")
	public static void GET_LISTS(ObservableList<String> courses, ObservableList<String> lecturers,
			ObservableList<String> students, String department) {
		MissionPack obj = new MissionPack(Mission.GET_LISTS, null, department);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		// Check if the lists were retrieved successfully
		if (obj.getResponse() == Response.GET_LISTS_SUCCESS) {
			SerializableList<String>[] allLists = (SerializableList<String>[]) obj.getInformation();
			// Clear the existing lists and populate them with the retrieved data
			courses.clear();
			courses.addAll(allLists[0].getList());
			lecturers.clear();
			lecturers.addAll(allLists[1].getList());
			students.clear();
			students.addAll(allLists[2].getList());

		} else if (obj.getResponse() == Response.GET_LISTS_FAILURE) {
			System.out.println("Lists extraction failure");
		}
	}

	/**
	 * Generates a department head report based on the provided query and choice.
	 *
	 * @param query  The query string used to generate the report.
	 * @param choice The choice parameter for generating the report.
	 * @return A List containing the generated report data.
	 */
	public static List<?> GENERATE_DEP_HEAD_REPORT(String query, String choice) {
		List<String> queryInfo = Arrays.asList(query, choice);
		MissionPack obj = new MissionPack(Mission.GENERATE_DEP_HEAD_REPORT, null, (Object) queryInfo);
		// accept the information from the server
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.GENERATE_DEP_HEAD_REPORT_SUCCESS) {
			List<?> list = (List<?>) obj.getInformation();
			return list;
		} else {
			System.out.println("Lists extraction failure");
		}
		return null;
	}

	/**
	 * Retrieves a list of requests from the server.
	 *
	 * @return A List containing the requested data.
	 */
	public static List<?> GET_REQUESTS() {
		// create new mission
		MissionPack obj = new MissionPack(Mission.GET_REQUESTS, null, null);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.GET_REQUESTS_SUCCESS) {
			List<?> list = (List<?>) obj.getInformation();
			return list;
		} else {
			System.out.println("Lists extraction failure");
		}
		return null;
	}

	/**
	 * Updates the status of requests based on the approved list.
	 *
	 * @param approvedList The list of approved requests.
	 * @throws Exception if an error occurs during the update process.
	 */
	public static void UPDATE_REQUESTS(List<String> approvedList) throws Exception {
		// create new mission
		MissionPack obj = new MissionPack(Mission.UPDATE_REQUESTS, null, (Object) approvedList);
		// accept the information from the server
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.UPDATE_REQUESTS_SUCCESS) {
			PopUpController popUpController = new PopUpController();
			popUpController.showAlert(approvedList.toString() + " Approved!");
		} else {
			System.out.println(Response.UPDATE_REQUESTS_FAILURE);
		}
	}

	/**
	 * Retrieves statistics for all tests.
	 *
	 * @return A list containing the statistics for all tests.
	 */
	public static List<?> GET_ALL_TESTS_STATS() {
		// create new mission
		MissionPack obj = new MissionPack(Mission.GET_ALL_TESTS_STATS, null, null);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.FOUND_TESTS) {
			// if the tests found
			List<?> list = (List<?>) obj.getInformation();
			return list;
		} else {
			System.out.println("Lists extraction failure");
		}
		return null;
	}

	/**
	 * Adds a performed test to the database.
	 *
	 * @param id        The ID of the performed test.
	 * @param subject   The subject of the performed test.
	 * @param course    The course of the performed test.
	 * @param userId    The ID of the user who performed the test.
	 * @param author    The author of the test.
	 * @param grade     The grade received for the performed test.
	 * @param copycheck The copycheck status of the performed test.
	 * @param type      The type of the test.
	 */
	public static void ADD_PERFORMED_TEST(String id, String subject, String course, String userId, String author,
			int grade, String copycheck, String type) {
		PerformedTest pTest1 = new PerformedTest(id, subject, course, userId, author, grade, copycheck, type);
		MissionPack obj = new MissionPack(Mission.ADD_PERFORMED_TEST, null, pTest1);
		ClientUI.chat.accept(obj);
		// recive the information from the server
		obj = ClientUI.chat.getResponseFromServer();
		// Check if the test is successfully uploaded to the database
		if (obj.getResponse() == Response.UPLOAD_SUCCESS) {
			System.out.println("Test Added to DB");
		} else {
			System.out.println("fail");
		}

	}

	/**
	 * Retrieves the IP address of the local host.
	 *
	 * @return The IP address of the local host, or "Unknown" if the address cannot
	 *         be determined.
	 */
	public static String GET_IP() {
		try {
			// get the ip from the computer
			InetAddress localHost = Inet4Address.getLocalHost();
			// return the ip
			return localHost.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "Unknown";
		}
	}

	/**
	 * Performs the YAEL operation.
	 *
	 * This method sends a YAEL request to the server and waits for a response.
	 */
	public static void YAEL() {
		MissionPack obj = new MissionPack(Mission.GET_YAEL, null, null);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();

	}

	/**
	 * Retrieves the question table data for a specific lecturer and displays it in
	 * the provided list view and table.
	 *
	 * This method sends a request to the server to fetch the question table data
	 * for the specified lecturer. It populates the provided list view and table
	 * with the retrieved data if the request is successful.
	 *
	 * @param listView The list view to populate with the question data.
	 * @param table    The table to populate with the question data.
	 * @param lecturer The name of the lecturer for whom to retrieve the question
	 *                 table data.
	 */
	public static void GET_QUESTION_TABLE(ObservableList<Question> listView, TableView<Question> table,
			String lecturer) {
		MissionPack obj = new MissionPack(Mission.GET_QUESTION_TABLE, null, lecturer);
		ClientUI.chat.accept(obj);
		System.out.println(lecturer);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.GET_QUESTION_TABLE_SUCCESS) {
			listView.clear();
			List<?> list = (List<?>) obj.getInformation();
			for (int i = 0; i < list.size(); ++i) {
				listView.add((Question) list.get(i));
				System.out.println(list.get(i));
			}
			table.setItems(listView);
			// statusLabel.setText("Upload Success");
		} else {
			System.out.println("No questions found.");
			// statusLabel.setText("Upload Failed");
		}
	}

	/**
	 * Uploads a test file to the server.
	 *
	 * This method allows the client to behave like a server by uploading a test
	 * file to the server. It establishes a socket connection with the server and
	 * sends the file's contents over the network.
	 *
	 * @param selectedFile The test file to be uploaded.
	 * @throws IOException If an I/O error occurs while uploading the file.
	 */
	// the client behave like a server
	public static void UPLOAD_TEST(File selectedFile) throws IOException {
		System.out.println("client mission handler upload activated");
		PreparedStatement ps = null;
//			 File newFile = new File(selectedFile.getAbsolutePath());
		try {
			// Creates a new file with the received filename in the specified directory
			System.out.println("client mission handler upload activated in try");

			// newFile.getParentFile().mkdirs();
			MissionPack obj = new MissionPack(Mission.UP_TEST, null, null);
			ClientUI.chat.accept(obj);
			obj = ClientUI.chat.getResponseFromServer();
			System.out.println("client mission handler upload activated after obj");
			// Establish a socket connection to the file server
			Socket socket = null;
			String host = ServerController.getIp();
			socket = new Socket(host, 4683);
			System.out.println("file server");
			// Get the size of the file
			long length = selectedFile.length();
			byte[] bytes = new byte[16 * 1024];
			InputStream in = new FileInputStream(selectedFile);
			OutputStream out = socket.getOutputStream();

			int count;
			while ((count = in.read(bytes)) > 0) {
				// Write the file content to the socket's output stream
				out.write(bytes, 0, count);
			}
			out.close();
			in.close();
			socket.close();
		} catch (Exception e) {
			System.out.println("Error in creating a file");
			e.printStackTrace();
		}
		// The uploaded file has been sent to the server
	}

	/**
	 * Retrieves the question table data for a specific lecturer and CourseName ,and
	 * displays it in the provided list view and table.
	 *
	 * @param question      The ObservableList to store the retrieved questions.
	 * @param questionTable The TableView to display the question table.
	 * @param CourseName    The name of the course associated with the test.
	 * @param lecturer      The name of the lecturer responsible for the test.
	 */
	public static void GET_QUESTION_TABLE_FOR_TEST(ObservableList<Question> question, TableView<Question> questionTable,
			String CourseName, String lecturer) {
		Question que = new Question(null, null, CourseName, null, null, lecturer, null, null, null, null, null);
		MissionPack obj = new MissionPack(Mission.GET_QUESTION_TABLE_FOR_TEST, null, (Object) que);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.GET_QUESTION_TABLE_SUCCESS) {
			question.clear();
			List<?> list = (List<?>) obj.getInformation();
			for (int i = 0; i < list.size(); ++i) {
				question.add((Question) list.get(i));
				System.out.println(list.get(i));
			}
			questionTable.setItems(question);
			// statusLabel.setText("Upload Success");
		} else {
			System.out.println("No questions found.");
			// statusLabel.setText("Upload Failed");
		}
	}

	/**
	 * Retrieves a list of tests associated with a specific lecturer.
	 *
	 * @param lecturer The name of the lecturer to retrieve tests for.
	 * @return A list of Test objects representing the tests associated with the
	 *         specified lecturer. Returns null if no tests are found.
	 */
	@SuppressWarnings("unchecked")
	public static List<Test> GET_MY_TESTS(String lecturer) {
		// Create a mission to retrieve tests associated with the specified lecturer
		MissionPack obj = new MissionPack(Mission.GET_MY_TESTS, null, lecturer);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.FOUND_TESTS) {
			// Retrieve the list of tests from the server's response
			return (List<Test>) obj.getInformation();

		} else {
			System.out.println("No tests found.");
		}
		return null;
	}

	/**
	 * Updates the specified tests with the provided updates.
	 *
	 * @param updatesList The list of updates to be applied to the tests.
	 */
	public static void UPDATE_MY_TESTS(List<String> updatesList) {
		// Create a mission pack object with the appropriate mission type and
		// information
		MissionPack obj = new MissionPack(Mission.UPDATE_MY_TESTS, null, updatesList);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.UPDATE_SUCCESS) {
			System.out.println("Updated successfuly");
		} else {
			System.out.println("No tests found.");
		}
	}

	/**
	 * 
	 * Updates the Departhead request table.
	 * 
	 * @param request The updates to be applied to the tests.
	 */
	public static void SEND_EXTENSION_REQUEST(ExtensionRequest request) {
		MissionPack obj = new MissionPack(Mission.SEND_EXTENSION_REQUEST, null, request);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.UPDATE_REQUESTS_SUCCESS) {
			System.out.println("Updated successfuly");
		} else {
			System.out.println("update not successful");
		}

	}

	/**
	 * 
	 * Updates the specified tests with the provided updates.
	 * 
	 * @param applicant The updates to be applied to the tests.
	 * @return list of extension requests
	 */
	@SuppressWarnings("unchecked")
	public static List<ExtensionRequest> CHECK_REQUEST_CHANGE(String applicant) {
		MissionPack obj = new MissionPack(Mission.CHECK_REQUEST_CHANGE, null, applicant);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.GET_REQUESTS_SUCCESS) {
			return (List<ExtensionRequest>) obj.getInformation();
		} else {
			System.out.println("not successful");
		}

		return null;
	}

	/**
	 * Retrieves a single test based on the given testId.
	 *
	 * @param testId The ID of the test to retrieve.
	 * @return The retrieved Test object, or null if no test was found.
	 */
	public static Test GET_SINGLE_TEST(String testId) {
		MissionPack obj = new MissionPack(Mission.GET_SINGLE_TEST, null, testId);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.FOUND_TESTS) {
			return (Test) obj.getInformation();

		} else {
			System.out.println("No tests found.");
		}
		return null;
	}

	/**
	 * Retrieves a list of performed tests based on the given testId.
	 *
	 * @param testid The ID of the test to retrieve performed tests for.
	 * @return A List of performed tests, or null if no tests were found.
	 */
	public static List<?> GET_PERFORMED_TESTS(String testid) {
		MissionPack obj = new MissionPack(Mission.GET_PERFORMED_TESTS, null, testid);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.FOUND_TESTS) {
			List<?> list = (List<?>) obj.getInformation();
			return list;
		} else {
			System.out.println("Lists extraction failure");
		}
		return null;
	}

	/**
	 * Adds test data.
	 *
	 * @param testData The TestData object to add.
	 */
	public static void ADD_TEST_DATA(TestData testData) {
		MissionPack obj = new MissionPack(Mission.ADD_TEST_DATA, null, testData);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.UPLOAD_SUCCESS) {
		} else {
			System.out.println("fail");
		}

	}

	/**
	 * Retrieves the test ID based on the given testCode.
	 *
	 * @param testCode The code of the test to retrieve the ID for.
	 * @return The ID of the test, or null if the retrieval failed.
	 */
	public static String GET_ID_FROM_CODE(String testCode) {
		MissionPack obj = new MissionPack(Mission.GET_ID_FROM_CODE, null, testCode);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.GET_TEST_ID_SUCCESS) {
			String id = (String) obj.getInformation();
			return id;
		} else {
			System.out.println("failure");
		}
		return null;
	}

	/**
	 * Retrieves a single test based on the entered code.
	 *
	 * @param enteredCode The entered code for the test.
	 * @return The retrieved Test object, or null if no test was found.
	 */
	public static Test GET_SINGLE_TEST2(String enteredCode) {
		MissionPack obj = new MissionPack(Mission.GET_SINGLE_TEST2, null, enteredCode);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.FOUND_TESTS) {
			return (Test) obj.getInformation();
		} else {
			System.out.println("No tests found.");
		}
		return null;
	}

	/**
	 * Concludes requests for the given applicant.
	 *
	 * @param applicant The applicant for whom to conclude requests.
	 */
	public static void CONCLUDE_REQUESTS(String applicant) {
		MissionPack obj = new MissionPack(Mission.CONCLUDE_REQUESTS, null, applicant);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.GET_REQUESTS_SUCCESS) {
			System.out.println("Status changed");
		} else {
			System.out.println("not successful");
		}

	}

	/**
	 * Retrieves the request information for the given request ID.
	 *
	 * @param id The ID of the request to retrieve.
	 * @return The request information as a String, or null if no request was found.
	 */
	public static String GET_REQUEST(String id) {
		MissionPack obj2 = new MissionPack(Mission.GET_REQUEST_TEST, null, id);
		ClientUI.chat.accept(obj2);
		obj2 = ClientUI.chat.getResponseFromServer();
		if (obj2.getResponse() == Response.GET_REQUEST_TEST_SUCCESS) {
			return (String) obj2.getInformation();
		} else {
			System.out.println("No status found.");
		}
		return null;
	}

	/**
	 * Retrieves the status for the test with the entered code.
	 *
	 * @param enteredCode The entered code for the test.
	 * @return The status of the test as a String, or null if no status was found.
	 */
	public static String GET_STATUS(String enteredCode) {
		MissionPack obj2 = new MissionPack(Mission.GET_STATUS_TEST, null, enteredCode);
		ClientUI.chat.accept(obj2);
		obj2 = ClientUI.chat.getResponseFromServer();
		if (obj2.getResponse() == Response.GET_STATUS_TEST_SUCCESS) {
			return (String) obj2.getInformation();
		} else {
			System.out.println("No status found.");
		}
		return null;
	}

	/**
	 * Retrieves a list of tests based on the given choice.
	 *
	 * @param choice The choice to filter tests by.
	 * @return A List of test IDs as Strings, or null if no tests were found.
	 */
	@SuppressWarnings("unchecked")
	public static List<String> GET_TEST_BY_CHOICE(String choice) {
		MissionPack obj = new MissionPack(Mission.GET_TEST_BY_CHOICE, null, choice);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.GET_TEST_ID_SUCCESS) {
			return (List<String>) obj.getInformation();
		} else {
			System.out.println("No tests found.");
		}
		return null;
	}

	/**
	 * Retrieves grades for tests performed by a lecturer and populates the provided
	 * ListView and TableView.
	 *
	 * @param user1    The User object representing the lecturer.
	 * @param listView The ObservableList to populate with PerformedTest objects.
	 * @param table    The TableView to set with the populated list.
	 * @throws Exception If an error occurs during the retrieval process.
	 */
	public static void GET_GRADES_BY_LECTURER(User user1, ObservableList<PerformedTest> listView,
			TableView<PerformedTest> table) throws Exception {
		User user = user1;
		MissionPack obj = new MissionPack(Mission.GET_GRADES_BY_LECTURER, null, (Object) user);
		ClientUI.chat.accept(obj); // Send the request to the server
		obj = ClientUI.chat.getResponseFromServer(); // Receive the response from the server
		if (obj.getResponse() == Response.GET_GRADES_SUCCESS) {
			listView.clear();
			List<?> list = (List<?>) obj.getInformation();
			for (int i = 0; i < list.size(); ++i) {
				PerformedTest performedTest = (PerformedTest) list.get(i);
				PerformedTest simplifiedPerformedTest = new PerformedTest(performedTest.getTestID(),
						performedTest.getCourse(), performedTest.getUserId(), performedTest.getGrade(),
						performedTest.getNotes());
				listView.add(simplifiedPerformedTest);
			}
			table.setItems(listView);
		} else {
			System.out.println("NO TESTS AVAILABLE");
		}
	}

	/**
	 * Updates the performed tests for a specific user.
	 *
	 * @param user       The User object representing the user.
	 * @param ptToUpdate The List of PerformedTest objects to update.
	 */
	public static void UPDATE_PERFORMED_TESTS(User user, List<PerformedTest> ptToUpdate) {
		MissionPack obj = new MissionPack(Mission.UPDATE_PERFORMED_TESTS, null, (Object) ptToUpdate);
		ClientUI.chat.accept(obj); // Send the request to the server
		obj = ClientUI.chat.getResponseFromServer(); // Receive the response from the server
		if (obj.getResponse() == Response.UPLOAD_SUCCESS) {
			System.out
					.println("performed tests by " + user.getName() + " " + user.getSurname() + "updated successfuly");
		} else {
			System.out.println("fail");
		}
	}

	/**
	 * Retrieves selected questions based on the approved list and populates the
	 * provided TableView.
	 *
	 * @param question      The ObservableList to populate with Question objects.
	 * @param questionTable The TableView to set with the populated list.
	 * @param approvedList  The List of approved questions.
	 */
	public static void SELECTED_QUESTIONS(ObservableList<Question> question, TableView<Question> questionTable,
			List<String> approvedList) {
		MissionPack obj = new MissionPack(Mission.SELECTED_QUESTIONS, null, (Object) approvedList);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.SELECTED_QUESTIONS_SUCCESS) {
			question.clear();
			List<?> list = (List<?>) obj.getInformation();
			for (int i = 0; i < list.size(); ++i) {
				question.add((Question) list.get(i));
				System.out.println(list.get(i));
			}
			questionTable.setItems(question);
		} else {
			System.out.println(Response.SELECTED_QUESTIONS_FAILURE);
		}

	}

	/**
	 * Adds points to a test question.
	 *
	 * @param test_question The ObservableList of test_question objects.
	 * @param tableView     The TableView of Question objects.
	 * @param tableView2    The TableView of test_question objects.
	 * @param points        The points to add.
	 * @param idQuestion    The ID of the question.
	 * @param idTest        The ID of the test.
	 */
	public static void ADD_POINTS(ObservableList<test_question> test_question, TableView<Question> tableView,
			TableView<test_question> tableView2, String points, String idQuestion, String idTest) {
		test_question que = new test_question(points, idQuestion, idTest);
		MissionPack obj = new MissionPack(Mission.ADD_TEST_QUESTION, null, que);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.ADD_TEST_POINTS_SUCCESS) {
			System.out.println("add");
		} else {
			System.out.println(Response.ADD_TEST_POINTS_FAILURE);
		}

	}

	/**
	 * Adds a new Test.
	 *
	 * @param test_question The ObservableList of Test objects.
	 * @param tableView     The TableView of Question objects.
	 * @param tableView2    The TableView of test_question objects.
	 * @param idTest        The ID of the test.
	 * @param subjectName   The name of the subject.
	 * @param courseName    The name of the course.
	 * @param lecturer      The name of the lecturer.
	 * @param durationTest  The duration of the test.
	 * @param idTestCode    The code of the test.
	 * @param textLecturer  The lecturer's text.
	 * @param textStudent   The student's text.
	 * @param maxNumTest    The maximum number of tests.
	 */
	public static void ADD_Test(ObservableList<Test> test_question, TableView<Question> tableView,
			TableView<test_question> tableView2, String idTest, String subjectName, String courseName, String lecturer,
			Integer durationTest, String idTestCode, String textLecturer, String textStudent, String maxNumTest) {
		Test que = new Test(idTest, subjectName, courseName, lecturer, durationTest, idTestCode, textLecturer,
				textStudent, maxNumTest);
		MissionPack obj = new MissionPack(Mission.ADD_TEST, null, que);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.ADD_TEST_SUCCESS) {
			System.out.println("add2");
		} else {
			System.out.println(Response.ADD_TEST_FAILURE);
		}
	}

	/**
	 * Retrieves the maximum number of tests.
	 *
	 * @return The maximum number of tests, or null if not found.
	 */
	public static Object MAX_NUM_TEST() {
		MissionPack obj = new MissionPack(Mission.MAX_NUM_TEST_ID, null, null);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.FOUND_MAX_NUM_TEST) {
			System.out.println("Max code was found");
			return obj.getInformation();
		} else {
			System.out.println("fail");
			return null;
		}
	}

	/**
	 * Creates test data for the given test ID.
	 *
	 * @param idTest The ID of the test to create data for.
	 */
	public static void CREATE_TEST_DATA(String idTest) {
		MissionPack obj = new MissionPack(Mission.CREATE_TEST_DATA, null, idTest);
		ClientUI.chat.accept(obj);
		obj = ClientUI.chat.getResponseFromServer();
		if (obj.getResponse() == Response.UPLOAD_SUCCESS) {
		} else {
			System.out.println("fail");
		}

	}

}
