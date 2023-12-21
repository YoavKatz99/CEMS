package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import common.*;
import entities.ConnectedClient;
import gui.ServerController;
import javafx.collections.ObservableList;
import ocsf.server.ConnectionToClient;

/**
 * The QueryExecutor class provides methods for executing database queries
 * related to questions.
 */
public class QueryExecutor {
	 
	/**
	 * Retrieves questions data from the database and populates a list of Question
	 * objects.
	 *
	 * @param obj The MissionPack object containing information about the operation.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getQuestionsData(MissionPack obj, Connection con) {
		List<Question> questions = new ArrayList<Question>();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM cems.question");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				questions.add(new Question(rs.getString("id"), rs.getString("subject"), rs.getString("courseName"),
						rs.getString("questionText"), rs.getString("questionNumber"), rs.getString("lecturer"),
						rs.getString("op1"), rs.getString("op2"), rs.getString("op3"), rs.getString("op4"),
						rs.getString("Coreectop")));
			}
			System.out.println("Retrieved " + questions.size() + " questions from the database.");
			rs.close();
			if (questions.size() > 0) {
				obj.setResponse(Response.FOUND_QUESTIONS);
				obj.setInformation(questions);
				System.out.println(questions.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Importing orders from cems.question failed!");
			obj.setResponse(Response.DIDNT_FOUND_QUESTIONS);
		}
	}

	/**
	 * Updates a question in the database based on the provided MissionPack object.
	 *
	 * @param obj The MissionPack object containing the question information to be
	 *            updated.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void updateClientInDatabase(MissionPack obj, Connection con) {
		Question question = (Question) obj.getInformation();
		PreparedStatement ps = null;
		boolean idExists = false;
		try {
			ps = con.prepareStatement("SELECT * FROM cems.question");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString("id").equals(question.getId())) {
					idExists = true;
					break;
				}
			}

		} catch (SQLException sqlException) {
			System.out.println("Statement failure");
		}
		if ((idExists == false)) {
			obj.setResponse(Response.EDIT_QUESTIONS_FAILD);
			return;
		} else {
			try {
				ps = con.prepareStatement(
						"UPDATE cems.question SET id = ?, questionText = ?, questionNumber = ? WHERE id = ?");
			} catch (SQLException sqlException) {
				System.out.println("Statement failure");
			}

			System.out
					.println(question.getQuestionText() + " " + question.getQuestionNumber() + " " + question.getId());
			try {
				String subject = question.getId().substring(0, 2);
				ps.setString(1, subject + question.getQuestionNumber());
				ps.setString(2, question.getQuestionText());
				ps.setString(3, question.getQuestionNumber());
				ps.setString(4, question.getId());
				ps.executeUpdate();
				obj.setInformation(question);
				obj.setResponse(Response.EDIT_QUESTIONS_SUCCESS);

			} catch (Exception exception) {
				System.out.println("Executing statement-Updating question text and question number has failed");
				obj.setResponse(Response.EDIT_QUESTIONS_FAILD);

				exception.printStackTrace();

			}
		}
	}

	/**
	 * Updates the client list based on the provided MissionPack object and client
	 * information.
	 *
	 * @param obj              The MissionPack object containing information about
	 *                         the operation.
	 * @param client           The ConnectionToClient object representing the client
	 *                         connection.
	 * @param connectionStatus The ClientStatus representing the status of the
	 *                         client connection.
	 */
	public static void updateClientList(MissionPack obj, ConnectionToClient client, ClientStatus connectionStatus) {
		ObservableList<ConnectedClient> clientList = ServerConfiguration.getClientList();

		for (int i = 0; i < clientList.size(); i++) {
			/* Comparing clients by IP addresses */
			if (clientList.get(i).getIp().equals(client.getInetAddress().getHostAddress()))
				clientList.remove(i);
		}

		/*
		 * In both cases of Connect and Disconnected we will need to add Client into the
		 * list so this function covers both of them simultaneously
		 */

		boolean hasAdd = clientList.add(new ConnectedClient(client.getInetAddress().getHostAddress(),
				client.getInetAddress().getHostName(), connectionStatus));
		if (hasAdd)
			obj.setResponse(Response.UPDATE_CONNECTION_SUCCESS);
		else
			obj.setResponse(Response.UPDATE_CONNECTION_FAILD);
		ServerConfiguration.setClientList(clientList);

	}

	/**
	 * Retrieves login information from the database and validates user credentials.
	 *
	 * @param obj The MissionPack object containing the user information.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getLoginInfo(MissionPack obj, Connection con) {
		// Extract user credentials from the MissionPack object
		User user = (User) obj.getInformation();
		String username = user.getUserName();
		String password = user.getPassword();
		PreparedStatement ps = null;
		// Execute the SQL query to search for user credentials
		try {
			String query = "SELECT * FROM user WHERE userName = ? AND password = ?";
			ps = con.prepareStatement(query);
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet resultSet = ps.executeQuery();
			// Check if the user credentials are found in the database
			if (resultSet.next() && resultSet.getString(8).equals("LoggedOut")) {
				// User credentials found, send a success response to the client
				user.setUserType(resultSet.getString(3));
				user.setName(resultSet.getString(4));
				user.setSurname(resultSet.getString(5)); // get user info
				user.setUserID(resultSet.getString(7));
				obj.setInformation(user);
				obj.setResponse(Response.LOGIN_SUCCESS);
				// User credentials found, update the isLoggedOut column to loggedIn
				String updateQuery = "UPDATE user SET connectionStatus = ? WHERE userName = ?";
				PreparedStatement updatePs = con.prepareStatement(updateQuery);
				updatePs.setString(1, "LoggedIn"); // Set status to loggedIn
				updatePs.setString(2, username);
				updatePs.executeUpdate();
			} else {
				if (resultSet.getString(8).equals("LoggedIn"))
					obj.setResponse(Response.LOGIN_FAILURE_loggedIn);
				else {
					// User credentials not found, send a failure response to the client
					obj.setResponse(Response.LOGIN_FAILURE_notInDB);
				}
			}
		} catch (SQLException e) {
			// Handle any exceptions that occur during the database operation
			e.printStackTrace();
			// Send an error response to the client
			obj.setResponse(Response.LOGIN_FAILURE_notInDB);
		}
	}

	/**
	 * Logs out a user by updating the connection status in the database.
	 *
	 * @param obj The MissionPack object containing the user information.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void logout(MissionPack obj, Connection con) {
		User user = (User) obj.getInformation();
		PreparedStatement ps = null;
		try {
			String updateQuery = "UPDATE user SET connectionStatus = ? WHERE userID = ?";
			ps = con.prepareStatement(updateQuery);
			ps.setString(1, "LoggedOut");
			ps.setString(2, user.getUserID());
			ps.executeUpdate();
			obj.setResponse(Response.LOGOUT_SUCCESS);
		} catch (SQLException e) {
			e.printStackTrace();
			obj.setResponse(Response.LOGOUT_FAILURE);
		}
	}

	/**
	 * Retrieves the grades of a student from the database.
	 *
	 * @param obj The MissionPack object containing the user information.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getStudentGrades(MissionPack obj, Connection con) {
		List<PerformedTest> myTests = new ArrayList<PerformedTest>();
		User user = (User) obj.getInformation();
		String studentID = user.getUserID();

		PreparedStatement statement = null;
		try {
			// Create the SQL query
			String query = "SELECT id,course,   grade " + "FROM performedtest " + "WHERE userID = ?";
			// Prepare the statement
			statement = con.prepareStatement(query);
			// Set the student ID parameter
			statement.setString(1, studentID);
			// Execute the query
			ResultSet resultSet = statement.executeQuery();
			// Process the results
			while (resultSet.next()) {
				String testID = resultSet.getString("id");
				String course = resultSet.getString("course");
				int grade = resultSet.getInt("grade");
				// Create a new PerformedTest object
				PerformedTest performedTest = new PerformedTest(testID, course, grade);
				myTests.add(performedTest);
			}
			resultSet.close();
			if (myTests.size() > 0) {
				obj.setResponse(Response.GET_GRADES_SUCCESS);
				obj.setInformation(myTests);
				System.out.println(Response.GET_GRADES_SUCCESS);
			} else {
				obj.setResponse(Response.GET_GRADES_FAILURE);
				System.out.println("NO TESTS FOUND FOR THIS STUDENT");
			}

		} catch (SQLException e) {
			System.out.println(Response.GET_GRADES_FAILURE);
			e.printStackTrace();
		}

	}

	/**
	 * Retrieves the list of courses associated with a specific author from the
	 * database.
	 *
	 * @param obj The MissionPack object containing the user information.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getCoursesList(MissionPack obj, Connection con) {
		List<String> courses = new ArrayList<String>();
		User user = (User) obj.getInformation();
		String author = user.getName() + " " + user.getSurname();
		String query = "SELECT DISTINCT course FROM performedtest WHERE author = ?";
		try {
			PreparedStatement statement = con.prepareStatement(query);
			statement.setString(1, author);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String course = resultSet.getString("course");
				courses.add(course);
			}
			obj.setInformation(courses);
			obj.setResponse(Response.GET_COURSES_SUCCESS);
		} catch (SQLException e) {
			System.out.println(Response.GET_COURSES_FAILURE);
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the list of tests for a specific course from the database.
	 *
	 * @param obj The MissionPack object containing the course information.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getTestsOfCourseList(MissionPack obj, Connection con) {
		List<String> tests = new ArrayList<String>();
		PreparedStatement ps = null;
		String courseName = (String) obj.getInformation();
		try {
			String query = "SELECT DISTINCT id FROM performedtest WHERE course = ?";
			ps = con.prepareStatement(query);
			ps.setString(1, courseName);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				String test = resultSet.getString("id");
				tests.add(test);
			}
			obj.setInformation(tests);
			obj.setResponse(Response.GET_TESTS_OF_COURSE_SUCCESS);
		} catch (SQLException e) {
			System.out.println(Response.GET_TESTS_OF_COURSE_FAILURE);
			e.printStackTrace();
		}
	}

	/**
	 * Adds a question to the database.
	 *
	 * @param obj The MissionPack object containing the question information.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void addquestiontodb(MissionPack obj, Connection con) {
		Question question = (Question) obj.getInformation();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(
					"INSERT INTO cems.question (id,subject, courseName, questionText,questionNumber,lecturer, op1, op2, op3, op4,correctop) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, question.getId());
			ps.setString(3, question.getCourseName());
			ps.setString(2, question.getSubject());
			ps.setString(4, question.getQuestionText());
			ps.setString(5, question.getQuestionNumber());
			ps.setString(6, question.getLecturer());
			ps.setString(7, question.getOp1());
			ps.setString(8, question.getOp2());
			ps.setString(9, question.getOp3());
			ps.setString(10, question.getOp4());
			ps.setString(11, question.getCoreectop());
			ps.executeUpdate();
			obj.setInformation(question);
			obj.setResponse(Response.ADD_QUESTION_SUCCSESS);
		} catch (SQLException e) {
			//
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the maximum question number from the database.
	 *
	 * @param obj The MissionPack object containing the information.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getMaxCodeQuestion(MissionPack obj, Connection con) {
		try {
			String query = "SELECT MAX(questionNumber) AS max_code FROM question";
			System.out.println(query);
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				String maxCode = rs.getString("max_code");
				System.out.println("Max Code Question Number: " + maxCode);
				obj.setResponse(Response.FOUND_MAX_CODE_QUESTION);
				obj.setInformation(maxCode);
				System.out.println(obj.toString());
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Importing max code question from cems.question failed!");
			obj.setResponse(Response.DIDNT_FOUND_max_code_question);
		}
	}

	/**
	 * Retrieves the data of a specific test from the database.
	 *
	 * @param obj The MissionPack object containing the test ID.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getDataofTest(MissionPack obj, Connection con) {
		String testID = (String) obj.getInformation();
		TestData testData = new TestData(testID, null, null, null, 0, 0, 0, 0, 0, null, null);
		PreparedStatement ps = null;
		int distribution[] = new int[10];
		try {
			String query = "SELECT * FROM test_stats WHERE test_id = ?";
			ps = con.prepareStatement(query);
			ps.setString(1, testID);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				testData.setAverage(resultSet.getDouble("average"));
				testData.setMedain(resultSet.getInt("median"));
				testData.setAuthor(resultSet.getString("author"));
				testData.setDate(resultSet.getDate("date"));
				testData.setTime_allocated(resultSet.getString("time_allocated"));
				testData.setStudents_started(resultSet.getInt("students_started"));
				testData.setSubmitted(resultSet.getInt("submitted"));
				testData.setNotSubmitted(testData.getStudents_started() - testData.getSubmitted());
				for (int i = 0; i < 10; i++) {
					distribution[i] = resultSet.getInt("decile" + (i + 1));
				}
				testData.setDistribution(distribution);
				System.out.println(testData.getMedain());
				obj.setInformation(testData);
				obj.setResponse(Response.GENERATE_LECTURER_REPORT_SUCCESS);
			}

		} catch (SQLException e) {
			System.out.println(Response.GENERATE_LECTURER_REPORT_FAILURE);
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the data from a specific database table.
	 *
	 * @param obj The MissionPack object containing the table name.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getDataBaseTable(MissionPack obj, Connection con) {
		List<Map<String, Object>> tableData = new ArrayList<>();

		String tableName = (String) obj.getInformation();
		try (Statement statement = con.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName)) {
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (resultSet.next()) {
				Map<String, Object> row = new HashMap<>();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metaData.getColumnName(i);
					System.out.println(columnName);
					Object columnValue = resultSet.getObject(i);
					row.put(columnName, columnValue);
				}
				tableData.add(row);
				obj.setInformation(tableData);
				obj.setResponse(Response.GET_DB_TABLE_SUCCESS);

			}
		} catch (SQLException e) {
			obj.setResponse(Response.GET_DB_TABLE_FAILURE);
			e.printStackTrace();
		}

	}

	/**
	 * Retrieves the ID associated with a test.
	 *
	 * @param obj The MissionPack object containing the test code.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getId(MissionPack obj, Connection con) {
		PreparedStatement ps = null;
		String code = (String) obj.getInformation();
		try {
			ps = con.prepareStatement("SELECT ID FROM tests WHERE Testcode=? ");
			ps.setString(1, code);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				String testId = resultSet.getNString("ID");
				obj.setInformation(testId);
				obj.setResponse(Response.GET_TEST_ID_SUCCESS);
			} else {
				// User credentials not found, send a failure response to the client
				obj.setResponse(Response.GET_TEST_ID_FAILURE);
			}
		} catch (SQLException sqlException) {
			System.out.println("SQL FAILURE");
		}
	}

	/**
	 * Downloads a test file.
	 *
	 * @param obj The MissionPack object containing the test code.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void downloadTest(MissionPack obj, Connection con) {
		File newFile=null;
		PreparedStatement ps = null;
		String code = (String) obj.getMission().toString();
		System.out.println(code);
		try {
			Socket socket = null;
			String host = ServerController.getIp();
			socket = new Socket(host, 4682);
			System.out.println("file server");
			
			switch (code) {
			case "1457" :
				newFile = new File(
						"C:\\Users\\classroom\\Downloads\\G4_Server\\1457.docx");
				break;
			case "1396" :
				newFile = new File(
						"C:\\Users\\classroom\\Downloads\\G4_Server\\1396.docx");
				break;
				
			case "1758" :
				newFile = new File(
						"C:\\Users\\classroom\\Downloads\\G4_Server\\1758.docx");
				break;
			case "2963" :
				newFile = new File(
						"C:\\Users\\classroom\\Downloads\\G4_Server\\2963.docx");
				break;
			case "5672" :
				newFile = new File(
						"C:\\Users\\classroom\\Downloads\\G4_Server\\5672.docx");
				break;
			}
			System.out.println("file server2");
			// Get the size of the file
			long length = newFile.length();
			byte[] bytes = new byte[16 * 1024];
			InputStream in = new FileInputStream(newFile);
			OutputStream out = socket.getOutputStream();

			int count;
			while ((count = in.read(bytes)) > 0) {
				out.write(bytes, 0, count);
			}
			out.close();
			in.close();
			socket.close();

			// Prepares the file to be sent back to the client
			// MyFile fileToSendBack = fromFile(newFile);

			// Sends the file back to the client
			// client.sendToClient(fileToSendBack);
		} catch (Exception e) {
			System.out.println("Error in creating a file");
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves data for all tests from the database.
	 *
	 * @param obj The MissionPack object.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getTestsData(MissionPack obj, Connection con) {
		System.out.println("getTestsData");
		List<Test> tests = new ArrayList<Test>();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM cems.tests");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				tests.add(new Test(rs.getString("Select"), rs.getString("ID"), rs.getString("Subject"),
						rs.getString("Course"), rs.getString("Author"), rs.getString("Status"), rs.getInt("Duration"),
						rs.getString("TestCode")));
			}
			System.out.println("Retrieved " + tests.size() + " tests from the database.");
			rs.close();
			if (tests.size() > 0) {
				obj.setResponse(Response.FOUND_TESTS);
				obj.setInformation(tests);
				System.out.println(tests.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Importing orders from cems.Test failed!");
			obj.setResponse(Response.DIDNT_FOUND_TESTS);
		}
	}

	/**
	 * Retrieves information related to reports.
	 *
	 * @param obj The MissionPack object containing the department.
	 * @param con The Connection object for connecting to the database.
	 */
	@SuppressWarnings("unchecked")
	public static void getReportsRelatedInfo(MissionPack obj, Connection con) {
		String dep = (String) obj.getInformation();
		List<String> courses = new ArrayList<>();
		List<String> lecturers = new ArrayList<>();
		List<String> students = new ArrayList<>();
		try {
			String courseQuery = "SELECT DISTINCT course FROM performedtest WHERE subject = ?";
			String lecturerQuery = "SELECT DISTINCT author FROM performedtest WHERE subject = ?";
			String studentQuery = "SELECT DISTINCT userID FROM performedtest WHERE subject = ?";
			PreparedStatement ps1 = con.prepareStatement(courseQuery);
			PreparedStatement ps2 = con.prepareStatement(lecturerQuery);
			PreparedStatement ps3 = con.prepareStatement(studentQuery);
			ps1.setString(1, dep);
			ps2.setString(1, dep);
			ps3.setString(1, dep);
			ResultSet rs1 = ps1.executeQuery();
			ResultSet rs2 = ps2.executeQuery();
			ResultSet rs3 = ps3.executeQuery();
			while (rs1.next())
				courses.add(rs1.getString("course"));
			while (rs2.next())
				lecturers.add(rs2.getString("author"));
			while (rs3.next())
				students.add(rs3.getString("userID"));

			SerializableList<String> serializableCourses = new SerializableList<>(courses);
			SerializableList<String> serializableLecturers = new SerializableList<>(lecturers);
			SerializableList<String> serializableStudents = new SerializableList<>(students);

			SerializableList<String>[] allLists = new SerializableList[] { serializableCourses, serializableLecturers,
					serializableStudents };

			obj.setInformation(allLists);
			obj.setResponse(Response.GET_LISTS_SUCCESS);
		} catch (SQLException e) {
			e.printStackTrace();
			obj.setResponse(Response.GET_LISTS_FAILURE);
		}
	}

	/**
	 * Uploads a test file from the client to the server.
	 *
	 * @param msg The MissionPack object.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void uploadtest(MissionPack msg, Connection con) {
		System.out.println("uploadtest- query");
		TimerTask task = new TimerTask() {
			public void run() {
				try {
					getFileUpload.accept2();
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
	}

	/**
	 * Retrieves data for department head reports based on the given query and
	 * choice.
	 *
	 * @param obj The MissionPack object containing the query and choice.
	 * @param con The Connection object for connecting to the database.
	 */
	@SuppressWarnings("unchecked")
	public static void getDepHeadReportData(MissionPack obj, Connection con) {
		List<String> queryInfo = (List<String>) obj.getInformation();
		String query = queryInfo.get(0);
		String choice = queryInfo.get(1);
		PreparedStatement ps = null;
		List<TestData> testDataList = new ArrayList<>();
		List<PerformedTest> performedTestList = new ArrayList<>();
		try {
			ps = con.prepareStatement(query);
			ps.setString(1, choice);
			ResultSet resultSet = ps.executeQuery();
			if (!(query.contains("userID"))) {
				while (resultSet.next()) {
					TestData testData = new TestData(null, null, null, null, 0, 0, 0, 0, 0, null, null);
					int distribution[] = new int[10];
					testData.setTest_id(resultSet.getString("test_id"));
					testData.setAverage(resultSet.getDouble("average"));
					testData.setMedain(resultSet.getInt("median"));
					testData.setAuthor(resultSet.getString("author"));
					testData.setDate(resultSet.getDate("date"));
					testData.setTime_allocated(resultSet.getString("time_allocated"));
					testData.setStudents_started(resultSet.getInt("students_started"));
					testData.setSubmitted(resultSet.getInt("submitted"));
					testData.setNotSubmitted(testData.getStudents_started() - testData.getSubmitted());
					for (int i = 0; i < 10; i++) {
						distribution[i] = resultSet.getInt("decile" + (i + 1));
					}
					testData.setDistribution(distribution);
					testDataList.add(testData);

				}
				obj.setInformation(testDataList);
			} else {
				while (resultSet.next()) {
					PerformedTest performedTest = new PerformedTest(null, null, null, null, null, 0);
					performedTest.setId(resultSet.getString("id"));
					performedTest.setCourse(resultSet.getString("course"));
					performedTest.setUserId(resultSet.getString("userID"));
					performedTest.setAuthor(resultSet.getString("author"));
					performedTest.setGrade(resultSet.getInt("grade"));
					performedTestList.add(performedTest);
				}
				obj.setInformation(performedTestList);
			}

			obj.setResponse(Response.GENERATE_DEP_HEAD_REPORT_SUCCESS);
		} catch (SQLException e) {
			e.printStackTrace();
			obj.setResponse(Response.GENERATE_DEP_HEAD_REPORT_FAILURE);
		}
	}

	/**
	 * Retrieves extension requests from the database with a specified status.
	 *
	 * @param obj The MissionPack object containing the status of the requests to
	 *            retrieve.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getExtensionRequests(MissionPack obj, Connection con) {
		List<ExtensionRequest> requestList = new ArrayList<>();
		PreparedStatement ps = null;
		String query = "SELECT * FROM request WHERE status = ?";
		try {
			ps = con.prepareStatement(query);
			ps.setString(1, "pending");
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				ExtensionRequest request = new ExtensionRequest(null, null, null, null);
				request.setApplicant(resultSet.getString("applicant"));
				request.setTestID(resultSet.getString("testID"));
				request.setRequestDetails(resultSet.getString("details"));
				request.setStatus(resultSet.getString("status"));
				requestList.add(request);
			}
			obj.setInformation(requestList);
			obj.setResponse(Response.GET_REQUESTS_SUCCESS);
		} catch (SQLException e) {
			e.printStackTrace();
			obj.setResponse(Response.GET_REQUESTS_FAILURE);
		}
	}

	/**
	 * Updates the status of extension requests in the database based on the
	 * approved list.
	 *
	 * @param obj The MissionPack object containing the approved list of extension
	 *            requests.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void updateExtensionRequests(MissionPack obj, Connection con) {
		@SuppressWarnings("unchecked")
		List<String> approvedList = (List<String>) obj.getInformation();
		PreparedStatement ps = null;
		String query = "UPDATE request SET status = ? WHERE testID = ?";
		try {
			for (String testID : approvedList) {
				// Remove square brackets from the test ID
				String cleanedTestID = testID.replace("[", "").replace("]", "");
				System.out.println(cleanedTestID);
				ps = con.prepareStatement(query);
				ps.setString(1, "approved");
				ps.setString(2, testID);
				ps.executeUpdate();
			}
			obj.setResponse(Response.UPDATE_REQUESTS_SUCCESS);
		} catch (SQLException e) {
			e.printStackTrace();
			obj.setResponse(Response.UPDATE_REQUESTS_FAILURE);
		}

	}

	/**
	 * Retrieves all tests statistics from the database.
	 *
	 * @param obj The MissionPack object.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getAllTestsStats(MissionPack obj, Connection con) {
		List<TestData> testDataList = new ArrayList<TestData>();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM test_stats");
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				TestData testData = new TestData(null, null, null, null, 0, 0, 0, 0, 0, null, null);
				int distribution[] = new int[10];
				testData.setTest_id(resultSet.getString("test_id"));
				testData.setAverage(resultSet.getDouble("average"));
				testData.setMedain(resultSet.getInt("median"));
				testData.setAuthor(resultSet.getString("author"));
				testData.setDate(resultSet.getDate("date"));
				testData.setTime_allocated(resultSet.getString("time_allocated"));
				testData.setStudents_started(resultSet.getInt("students_started"));
				testData.setSubmitted(resultSet.getInt("submitted"));
				testData.setNotSubmitted(testData.getStudents_started() - testData.getSubmitted());
				for (int i = 0; i < 10; i++) {
					distribution[i] = resultSet.getInt("decile" + (i + 1));
				}
				testData.setDistribution(distribution);
				testDataList.add(testData);

			}
			resultSet.close();
			if (testDataList.size() > 0) {
				obj.setResponse(Response.FOUND_TESTS);
				obj.setInformation(testDataList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Importing orders from cems.Test failed!");
			obj.setResponse(Response.DIDNT_FOUND_TESTS);
		}
	}

	/**
	 * Retrieves the question table from the database for a specific lecturer.
	 *
	 * @param obj The MissionPack object containing the lecturer's name.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getQuestionTable(MissionPack obj, Connection con) {
		List<Question> questions = new ArrayList<Question>();
		try {
			System.out.println((String) obj.getInformation() + "474");
			String lecturer = (String) obj.getInformation();
			PreparedStatement ps = con.prepareStatement(
					"SELECT id,subject,courseName,questionText,questionNumber,lecturer,op1,op2,op3,op4,correctop FROM question WHERE lecturer = ?");
			ps.setString(1, lecturer);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				questions.add(new Question(rs.getString("id"), rs.getString("subject"), rs.getString("courseName"),
						rs.getString("questionText"), rs.getString("questionNumber"), rs.getString("lecturer"),
						rs.getString("op1"), rs.getString("op2"), rs.getString("op3"), rs.getString("op4"),
						rs.getString("correctop")));
			}
			System.out.println("Retrieved " + questions.size() + " questions from the database.");
			rs.close();
			if (questions.size() > 0) {
				obj.setResponse(Response.GET_QUESTION_TABLE_SUCCESS);
				obj.setInformation(questions);
				System.out.println(questions.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Importing orders from cems.question failed!");
			obj.setResponse(Response.GET_QUESTION_TABLE_FAILURE);
		}

	}

	/**
	 * Retrieves the question table from the database based on the course name and
	 * lecturer.
	 *
	 * @param obj The MissionPack object containing the course name and lecturer.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getQuestionTableTest(MissionPack obj, Connection con) {
		List<Question> questions = new ArrayList<Question>();
		Question que = (Question) obj.getInformation();
		String courseName = que.getCourseName();
		String lecturer = que.getLecturer();
		try {

			PreparedStatement ps = con.prepareStatement(
					"SELECT id,questionText,op1,op2,op3,op4,correctop FROM question WHERE courseName = ? AND lecturer = ?");
			ps.setString(1, courseName);
			ps.setString(2, lecturer);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				questions.add(new Question(rs.getString("id"), null, null, rs.getString("questionText"), null, null,
						rs.getString("op1"), rs.getString("op2"), rs.getString("op3"), rs.getString("op4"),
						rs.getString("correctop")));
			}
			System.out.println("Retrieved " + questions.size() + " questions from the database.");
			rs.close();
			if (questions.size() > 0) {
				obj.setResponse(Response.GET_QUESTION_TABLE_SUCCESS);
				obj.setInformation(questions);
				System.out.println(questions.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Importing orders from cems.question failed!");
			obj.setResponse(Response.GET_QUESTION_TABLE_FAILURE);
		}

	}

	/**
	 * Retrieves information about all tests from the database.
	 *
	 * @param obj The MissionPack object.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getTestsInfo(MissionPack obj, Connection con) {
		System.out.println("getTestsData");
		List<Test> tests = new ArrayList<Test>();
		try {
			PreparedStatement ps = con
					.prepareStatement("SELECT ID,Subject,Course,Author,Duration,TestCode FROM cems.tests");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				tests.add(new Test(null, rs.getString("ID"), rs.getString("Subject"), rs.getString("Course"),
						rs.getString("Author"), null, rs.getInt("Duration"), rs.getString("TestCode")));
			}
			System.out.println("Retrieved " + tests.size() + " tests from the database.");
			rs.close();
			if (tests.size() > 0) {
				obj.setResponse(Response.FOUND_TESTS);
				obj.setInformation(tests);
				System.out.println(tests.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Importing orders from cems.Test failed!");
			obj.setResponse(Response.DIDNT_FOUND_TESTS);
		}
	}

	/**
	 * Retrieves tests created by a specific lecturer from the database.
	 *
	 * @param obj The MissionPack object containing the lecturer's name.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getLecturerTests(MissionPack obj, Connection con) {
		String lecturerName = (String) obj.getInformation();
		List<Test> tests = new ArrayList<Test>();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM cems.tests WHERE Author = ?");
			ps.setString(1, lecturerName);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				tests.add(new Test(rs.getString("Select"), rs.getString("ID"), rs.getString("Subject"),
						rs.getString("Course"), rs.getString("Author"), rs.getString("Status"), rs.getInt("Duration"),
						rs.getString("TestCode")));
			}
			rs.close();
			if (tests.size() > 0) {
				obj.setResponse(Response.FOUND_TESTS);
				obj.setInformation(tests);
				System.out.println(tests.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Importing orders from cems.Test failed!");
			obj.setResponse(Response.DIDNT_FOUND_TESTS);
		}
	}

	/**
	 * Updates the status of multiple tests in the database.
	 *
	 * @param obj The MissionPack object containing the list of tests and their
	 *            updated status.
	 * @param con The Connection object for connecting to the database.
	 */
	@SuppressWarnings("unchecked")
	public static void updateTests(MissionPack obj, Connection con) {
		List<String> testsList = (List<String>) obj.getInformation();
		String updateQuery = "UPDATE tests SET Status = ? WHERE ID = ?";
		for (String test : testsList) {
			try {
				String[] parts = test.split(" "); // Split the string at the space character
				String id = parts[0]; // The first part is the ID
				String status = parts[1]; // The second part is the status
				PreparedStatement ps = con.prepareStatement(updateQuery);
				ps.setString(1, status);
				ps.setString(2, id);
				int rowsAffected = ps.executeUpdate();
				System.out.println("Row affected: " + rowsAffected);
				obj.setResponse(Response.UPDATE_SUCCESS);
				;
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.UPDATE_FAILURE);
			}

		}

	}

	/**
	 * Sends an extension request to the database.
	 *
	 * @param obj The MissionPack object containing the extension request
	 *            information.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void sendExtensionRequest(MissionPack obj, Connection con) {
		ExtensionRequest extensionRequest = (ExtensionRequest) obj.getInformation();
		// SQL query to insert the ExtensionRequest into the "request" table
		String query = "INSERT INTO request (applicant, testID, details, status) VALUES (?, ?, ?, ?)";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			// Set the values for the placeholders using the ExtensionRequest object
			ps.setString(1, extensionRequest.getApplicant());
			ps.setString(2, extensionRequest.getTestID());
			ps.setString(3, extensionRequest.getRequestDetails());
			ps.setString(4, extensionRequest.getStatus());
			ps.executeUpdate();
			obj.setResponse(Response.UPDATE_REQUESTS_SUCCESS);
		} catch (SQLException e) {
			e.printStackTrace();
			obj.setResponse(Response.UPDATE_REQUESTS_FAILURE);
		}
	}

	/**
	 * Retrieves a list of approved extension requests for a specific applicant from
	 * the database.
	 *
	 * @param obj The MissionPack object containing the applicant's name.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getApprovedRequests(MissionPack obj, Connection con) {
		List<ExtensionRequest> requestList = new ArrayList<>();
		String applicant = (String) obj.getInformation();
		PreparedStatement ps = null;
		String query = "SELECT * FROM request WHERE status = ? AND applicant = ?";
		try {
			ps = con.prepareStatement(query);
			ps.setString(1, "approved");
			ps.setString(2, applicant);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				ExtensionRequest request = new ExtensionRequest(null, null, null, null);
				request.setApplicant(resultSet.getString("applicant"));
				request.setTestID(resultSet.getString("testID"));
				request.setRequestDetails(resultSet.getString("details"));
				request.setStatus(resultSet.getString("status"));
				requestList.add(request);
			}
			obj.setInformation(requestList);
			obj.setResponse(Response.GET_REQUESTS_SUCCESS);
		} catch (SQLException e) {
			e.printStackTrace();
			obj.setResponse(Response.GET_REQUESTS_FAILURE);
		}

	}

	/**
	 * Retrieves the test ID from the database based on the test code.
	 *
	 * @param obj The MissionPack object containing the test code.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getIdFromCode(MissionPack obj, Connection con) {
		PreparedStatement ps = null;
		String code = (String) obj.getInformation();
		try {
			ps = con.prepareStatement("SELECT ID FROM cems.tests WHERE TestCode = ?");
			ps.setString(1, code);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				String testId = resultSet.getString("ID");
				obj.setInformation(testId);
				obj.setResponse(Response.GET_TEST_ID_SUCCESS);
			} else {
				// User credentials not found, send a failure response to the client
				obj.setResponse(Response.GET_TEST_ID_FAILURE);
			}
		} catch (SQLException sqlException) {
			System.out.println("SQL FAILURE");
		}
	}

	/**
	 * Retrieves a single test from the database based on the test ID.
	 *
	 * @param obj The MissionPack object containing the test ID.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getSingleTest(MissionPack obj, Connection con) {
		System.out.println("getTest");
		Test test = null;
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM cems.tests WHERE ID = ?");
			ps.setString(1, (String) obj.getInformation());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				test = new Test(rs.getString("Select"), rs.getString("ID"), rs.getString("Subject"),
						rs.getString("Course"), rs.getString("Author"), rs.getString("Status"), rs.getInt("Duration"),
						rs.getString("TestCode"));
			}
			rs.close();
			if (!test.equals(null)) {
				System.out.println("Retrieved test from the database.");
				obj.setResponse(Response.FOUND_TESTS);
				obj.setInformation(test);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Importing order from cems.Test failed!");
			obj.setResponse(Response.DIDNT_FOUND_TESTS);
		}
	}

	/**
	 * Retrieves a single test from the database based on the test code.
	 *
	 * @param obj The MissionPack object containing the test code.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getSingleTest2(MissionPack obj, Connection con) {
		System.out.println("getTest");
		Test test = null;
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM cems.tests WHERE TestCode = ?");
			ps.setString(1, (String) obj.getInformation());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				test = new Test(rs.getString("Select"), rs.getString("ID"), rs.getString("Subject"),
						rs.getString("Course"), rs.getString("Author"), rs.getString("Status"), rs.getInt("Duration"),
						rs.getString("TestCode"));
			}
			System.out.println("Retrieved test from the database.");
			rs.close();
			if (!test.equals(null)) {
				obj.setResponse(Response.FOUND_TESTS);
				obj.setInformation(test);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Importing order from cems.Test failed!");
			obj.setResponse(Response.DIDNT_FOUND_TESTS);
		}
	}

	/**
	 * Adds a performed test to the database.
	 *
	 * @param obj The MissionPack object containing the performed test information.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void addPerformedTesttoDB(MissionPack obj, Connection con) {
		PerformedTest pTest = (PerformedTest) obj.getInformation();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(
					"INSERT INTO cems.performedtest (id, subject, course, userID, author, grade, answers, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, pTest.getTestID());
			ps.setString(2, pTest.getSubject());
			ps.setString(3, pTest.getCourse());
			ps.setString(4, pTest.getUserId());
			ps.setString(5, pTest.getAuthor());
			ps.setString(6, String.valueOf(pTest.getGrade()));
			ps.setString(7, pTest.getCopyCheck());
			ps.setString(8, pTest.getType());
			ps.executeUpdate();
			obj.setInformation(pTest);
			obj.setResponse(Response.UPLOAD_SUCCESS);
		} catch (SQLException e) {
			//
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves performed tests from the database based on the test ID and type.
	 *
	 * @param obj The MissionPack object containing the test ID.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getPerformedTests(MissionPack obj, Connection con) {
		List<PerformedTest> testDataList = new ArrayList<PerformedTest>();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM performedtest WHERE id = ? AND type = ?");
			ps.setString(1, (String) obj.getInformation());
			ps.setString(2, "Online");
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				PerformedTest pt = new PerformedTest(null, null, null, null, null, 0, null, null);
				pt.setAuthor(resultSet.getString(5));
				pt.setCourse(resultSet.getString(3));
				pt.setGrade(Integer.valueOf(resultSet.getString(6)));
				pt.setId(resultSet.getString(1));
				pt.setSubject(resultSet.getString(2));
				pt.setUserId(resultSet.getString(4));
				pt.setCopyCheck(resultSet.getString(7));
				pt.setType(resultSet.getString(8));
				pt.setNotes(resultSet.getString(9));
				testDataList.add(pt);

			}
			resultSet.close();
			if (testDataList.size() > 0) {
				obj.setResponse(Response.FOUND_TESTS);
				obj.setInformation(testDataList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Importing orders from cems.performedtest failed!");
			obj.setResponse(Response.DIDNT_FOUND_TESTS);
		}
	}

	/**
	 * Adds test data to the database.
	 *
	 * @param obj        The MissionPack object containing the test data.
	 * @param connection The Connection object for connecting to the database.
	 */
	public static void addTestData(MissionPack obj, Connection connection) {
		TestData testData = (TestData) obj.getInformation();
		PreparedStatement ps = null;
		try {
			PreparedStatement requestPs = connection
					.prepareStatement("SELECT details FROM request WHERE (testID = ?) AND (status = 'approved')");
			requestPs.setString(1, testData.getTest_id());
			ResultSet rs = requestPs.executeQuery();
			String addedtime = "0";
			if (rs.next() && !rs.equals(null)) {
				addedtime = rs.getString(1);
				addedtime = addedtime.split("_")[0];
			}
			ps = connection.prepareStatement(
					"UPDATE cems.test_stats SET average = ?, median = ?, decile1 = ?, decile2 = ?, decile3 = ?, decile4 = ?, decile5 = ?, decile6 = ?, decile7 = ?, decile8 = ?, decile9 = ?, decile10 = ?, course = ?, date = ?, time_allocated = ?, students_started = ?, submitted = ?, author = ? WHERE test_id = ?");
			ps.setDouble(1, testData.getAverage());
			ps.setInt(2, testData.getMedain());
			ps.setInt(3, testData.getDistribution()[0]);
			ps.setInt(4, testData.getDistribution()[1]);
			ps.setInt(5, testData.getDistribution()[2]);
			ps.setInt(6, testData.getDistribution()[3]);
			ps.setInt(7, testData.getDistribution()[4]);
			ps.setInt(8, testData.getDistribution()[5]);
			ps.setInt(9, testData.getDistribution()[6]);
			ps.setInt(10, testData.getDistribution()[7]);
			ps.setInt(11, testData.getDistribution()[8]);
			ps.setInt(12, testData.getDistribution()[9]);
			ps.setString(13, testData.getCourse());
			java.util.Date utilDate = new java.util.Date();
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			ps.setDate(14, sqlDate);
			ps.setString(15,
					String.valueOf(Integer.parseInt(testData.getTime_allocated()) + Integer.parseInt(addedtime)));
			ps.setInt(16, Integer.valueOf(testData.getStudents_started()));
			ps.setInt(17, Integer.valueOf(testData.getSubmitted()));
			ps.setString(18, testData.getAuthor());
			ps.setString(19, testData.getTest_id());
			ps.executeUpdate();
			obj.setResponse(Response.UPLOAD_SUCCESS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves questions from a specific test in the database.
	 *
	 * @param obj The MissionPack object containing the test ID.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getQuestionsFromTest(MissionPack obj, Connection con) {
		List<Question> questions = new ArrayList<Question>();
		try {
			String query = "SELECT q.id, q.subject, q.courseName, q.questionText, q.questionNumber, q.lecturer, q.op1, q.op2, q.op3, q.op4, q.correctop, tq.points "
					+ "FROM tests t " + "JOIN test_question tq ON t.ID = tq.test_id "
					+ "JOIN question q ON tq.question_id = q.id " + "WHERE t.ID = ?";
			System.out.println(query);
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, obj.getInformation().toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				questions.add(new Question(rs.getString("id"), rs.getString("subject"), rs.getString("courseName"),
						rs.getString("questionText"), rs.getString("questionNumber"), rs.getString("lecturer"),
						rs.getString("op1"), rs.getString("op2"), rs.getString("op3"), rs.getString("op4"),
						rs.getString("correctop"), rs.getString("points")));
			}
			System.out.println("Retrieved " + questions.size() + " questions from the database.");
			rs.close();
			if (questions.size() > 0) {
				obj.setResponse(Response.FOUND_QUESTIONS);
				obj.setInformation(questions);
				System.out.println(questions.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Importing orders from cems.question failed!");
			obj.setResponse(Response.DIDNT_FOUND_QUESTIONS);
		}
	}

	/**
	 * Concludes requests by updating their status in the database.
	 *
	 * @param obj The MissionPack object containing the applicant information.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void concludeRequests(MissionPack obj, Connection con) {
		String applicant = (String) obj.getInformation();
		PreparedStatement ps = null;
		String query = "UPDATE request SET status = ? WHERE applicant = ?";
		try {
			ps = con.prepareStatement(query);
			ps.setString(1, "done");
			ps.setString(2, applicant);
			ps.executeUpdate();
			obj.setResponse(Response.GET_REQUESTS_SUCCESS);
		} catch (SQLException e) {
			e.printStackTrace();
			obj.setResponse(Response.GET_REQUESTS_FAILURE);
		}

	}

	/**
	 * Retrieves the status of a test from the database based on the test code.
	 *
	 * @param obj The MissionPack object containing the test code.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getStatusTest(MissionPack obj, Connection con) {
		PreparedStatement ps = null;
		String code = (String) obj.getInformation();
		try {
			ps = con.prepareStatement("SELECT Status FROM tests WHERE Testcode=? AND Status=? ");
			ps.setString(1, code);
			ps.setString(2, "Active");
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				String status = resultSet.getNString("Status");
				obj.setInformation(status);
				obj.setResponse(Response.GET_STATUS_TEST_SUCCESS);
			} else {
				// User credentials not found, send a failure response to the client
				obj.setResponse(Response.GET_STATUS_TEST_FAILD);
			}
		} catch (SQLException sqlException) {
			System.out.println("SQL FAILURE");
		}
	}

	/**
	 * Retrieves a request from the database based on the test ID and status.
	 *
	 * @param obj The MissionPack object containing the test ID.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getrequest(MissionPack obj, Connection con) {
		PreparedStatement ps = null;
		String id = (String) obj.getInformation();
		try {
			ps = con.prepareStatement("SELECT details FROM request WHERE testID=? AND status=?  ");
			ps.setString(1, id);
			ps.setString(2, "done");
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				String details = resultSet.getNString("details");
				obj.setInformation(details);
				obj.setResponse(Response.GET_REQUEST_TEST_SUCCESS);
			} else {
				// User credentials not found, send a failure response to the client
				obj.setResponse(Response.GET_REQUEST_TEST_FAILD);
			}
		} catch (SQLException sqlException) {
			System.out.println("SQL FAILURE");
		}

	}

	/**
	 * Retrieves the status of a test from the database based on the test code.
	 *
	 * @param obj The MissionPack object containing the test code.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getStatusTestmanuall(MissionPack obj, Connection con) {
		PreparedStatement ps = null;
		String code = (String) obj.getInformation();
		try {
			ps = con.prepareStatement("SELECT Status FROM tests WHERE Testcode=?  ");
			ps.setString(1, code);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				String status = resultSet.getNString("Status");
				obj.setInformation(status);
				obj.setResponse(Response.GET_STATUS_TEST_SUCCESS);
			} else {
				// User credentials not found, send a failure response to the client
				obj.setResponse(Response.GET_STATUS_TEST_FAILD);
			}
		} catch (SQLException sqlException) {
			System.out.println("SQL FAILURE");
			obj.setResponse(Response.GET_STATUS_TEST_FAILD);
		}

	}

	/**
	 * Retrieves a list of test IDs based on a specific filter (course, lecturer, or
	 * student).
	 *
	 * @param obj The MissionPack object containing the filter information.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getTestIdList(MissionPack obj, Connection con) {
		String choice = (String) obj.getInformation();
		String[] arr = choice.split(" ");
		String filter = arr[0]; // course/lecturer/student
		String specific = arr[1];
		System.out.println(filter + specific);
		String query = null;
		query = "SELECT DISTINCT id FROM performedtest WHERE " + filter + "= ?";
		PreparedStatement ps = null;
		List<String> idList = new ArrayList<>();
		try {
			ps = con.prepareStatement(query);
			ps.setString(1, specific);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				idList.add(resultSet.getString("id"));
			}
			if (idList.size() > 0) {
				System.out.println(idList);
				obj.setResponse(Response.GET_TEST_ID_SUCCESS);
				obj.setInformation(idList);
			} else {
				obj.setResponse(Response.GET_TEST_ID_FAILURE);
			}
		} catch (SQLException sqlException) {
			System.out.println("SQL FAILURE");
			obj.setResponse(Response.GET_TEST_ID_FAILURE);
		}
	}

	/**
	 * Retrieves grades for tests conducted by a specific lecturer.
	 *
	 * @param obj The MissionPack object containing the lecturer information.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getGradesByLecturer(MissionPack obj, Connection con) {
		List<PerformedTest> myTests = new ArrayList<PerformedTest>();
		User user = (User) obj.getInformation();
		String author = user.getName() + " " + user.getSurname();

		PreparedStatement statement = null;
		try {
			// Create the SQL query
			String query = "SELECT id,subject,course,userID,grade,notes " + "FROM performedtest " + "WHERE author = ?";
			// Prepare the statement
			statement = con.prepareStatement(query);
			// Set the student ID parameter
			statement.setString(1, author);
			// Execute the query
			ResultSet resultSet = statement.executeQuery();
			// Process the results
			while (resultSet.next()) {
				String testID = resultSet.getString("id");
				String course = resultSet.getString("course");
				int grade = resultSet.getInt("grade");
				String userID = resultSet.getString("userID");
				String notes = resultSet.getString("notes");

				// Create a new PerformedTest object
				PerformedTest performedTest = new PerformedTest(testID, course, userID, grade, notes);
				myTests.add(performedTest);
			}
			resultSet.close();
			if (myTests.size() > 0) {
				obj.setResponse(Response.GET_GRADES_SUCCESS);
				obj.setInformation(myTests);
				System.out.println(Response.GET_GRADES_SUCCESS);
			} else {
				obj.setResponse(Response.GET_GRADES_FAILURE);
				System.out.println("NO TESTS FOUND FOR THIS STUDENT");
			}

		} catch (SQLException e) {
			System.out.println(Response.GET_GRADES_FAILURE);
			e.printStackTrace();
		}

	}

	/**
	 * Updates the grades and notes for performed tests in the database.
	 *
	 * @param obj        The MissionPack object containing the performed test
	 *                   information.
	 * @param connection The Connection object for connecting to the database.
	 */
	public static void updatePerformedTests(MissionPack obj, Connection connection) {
		@SuppressWarnings("unchecked")
		List<PerformedTest> performedTests = (List<PerformedTest>) obj.getInformation();
		PreparedStatement ps = null;
		try {
			ps = connection
					.prepareStatement("UPDATE cems.performedtest SET grade = ?, notes = ? WHERE id = ? AND userID = ?");
			for (PerformedTest performedTest : performedTests) {
				ps.setInt(1, performedTest.getGrade());
				ps.setString(2, performedTest.getNotes());
				ps.setString(3, performedTest.getTestID());
				ps.setString(4, performedTest.getUserId());
				ps.addBatch();
			}
			ps.executeBatch();

			obj.setResponse(Response.UPLOAD_SUCCESS);
		} catch (

		SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the list of approved questions in the database based on the selected
	 * question IDs.
	 *
	 * @param obj The MissionPack object containing the selected question IDs.
	 * @param con The Connection object for connecting to the database.
	 * @throws SQLException if a database access error occurs.
	 */
	@SuppressWarnings("unchecked")
	public static void updateSelectQuestion(MissionPack obj, Connection con) throws SQLException {
		@SuppressWarnings("unchecked")
		List<Question> approvedList = new ArrayList<Question>();
		List<String> Select = (List<String>) obj.getInformation();
		System.out.println("updateSelectQuestion");
		try {
			for (int i = 0; i < Select.size(); i++) {
				String id = Select.get(i);
				System.out.println(id);

				PreparedStatement ps = con
						.prepareStatement("SELECT id,subject,courseName,questionText FROM cems.question WHERE id= ?");
				ps.setString(1, id);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {

					approvedList
							.add(new Question(rs.getString("id"), rs.getString("subject"), rs.getString("courseName"),
									rs.getString("questionText"), null, null, null, null, null, null, null, null));
				}
				System.out.println("Retrieved " + approvedList.size() + " questions from the database.");
				rs.close();
			}
			if (approvedList.size() > 0) {
				obj.setResponse(Response.SELECTED_QUESTIONS_SUCCESS);
				obj.setInformation(approvedList);
				System.out.println(approvedList.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Importing orders from cems.question failed!");
			obj.setResponse(Response.SELECTED_QUESTIONS_FAILURE);
		}

	}

	/**
	 * Updates the test-question relationship in the database.
	 *
	 * @param obj The MissionPack object containing the test-question information.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void updateTestQuestion(MissionPack obj, Connection con) {
		test_question testQue = (test_question) obj.getInformation();
		String points = testQue.getPoints();
		String idQuestion = testQue.getQuestion_id();
		String idTest = testQue.getTest_id();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("INSERT INTO cems.test_question (test_id, question_id, points) VALUES (?, ?, ?)");
			ps.setString(1, idTest);
			ps.setString(2, idQuestion);
			ps.setString(3, points);
			int rowsAffected = ps.executeUpdate(); // Check the number of rows affected
			if (rowsAffected > 0) {
				obj.setInformation(testQue);
				obj.setResponse(Response.ADD_TEST_POINTS_SUCCESS);
				System.out.println(obj.getResponse());
			} else {
				System.out.println("No rows were affected. The insertion failed.");
			}
		} catch (SQLException sqlException) {
			System.out.println("Statement failure: " + sqlException.getMessage());
			sqlException.printStackTrace(); // Print the stack trace for debugging
		} finally {
			// Close the PreparedStatement
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(points + " " + idQuestion + " " + idTest);
	}

	/**
	 * Inserts a new test into the database.
	 *
	 * @param obj The MissionPack object containing the test information.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void updateTest(MissionPack obj, Connection con) {
		Test testQue = (Test) obj.getInformation();
		String ID = testQue.getID();
		String Subject = testQue.getSubject();
		String Course = testQue.getCourse();
		String Author = testQue.getAuthor();
		Integer Duration = testQue.getDuration();
		String TestCode = testQue.getTestCode();
		String LecturerText = testQue.getLecturerText();
		String StudentText = testQue.getStudentText();
		String NumTest = testQue.getNumTest();

		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(
					"INSERT INTO cems.tests (ID, Subject, Course,Author,Status,Duration,TestCode,LecturerText,StudentText,NumTest) VALUES (?, ?, ? ,? ,? ,? ,? ,? ,?, ?)");
			ps.setString(1, ID);
			ps.setString(2, Subject);
			ps.setString(3, Course);
			ps.setString(4, Author);
			ps.setString(5, "Inactive");
			ps.setInt(6, Duration);
			ps.setString(7, TestCode);
			ps.setString(8, LecturerText);
			ps.setString(9, StudentText);
			ps.setString(10, NumTest);
			int rowsAffected = ps.executeUpdate(); // Check the number of rows affected
			if (rowsAffected > 0) {
				obj.setInformation(testQue);
				obj.setResponse(Response.ADD_TEST_SUCCESS);
				System.out.println(obj.getResponse());
			} else {
				System.out.println("No rows were affected. The insertion failed.");
			}
		} catch (SQLException sqlException) {
			System.out.println("Statement failure: " + sqlException.getMessage());
			sqlException.printStackTrace(); // Print the stack trace for debugging
		} finally {
			// Close the PreparedStatement
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Retrieves the maximum test number from the database.
	 *
	 * @param obj The MissionPack object.
	 * @param con The Connection object for connecting to the database.
	 */
	public static void getMaxNumTest(MissionPack obj, Connection con) {
		try {
			String query = "SELECT MAX(NumTest) AS max_num FROM tests";
			System.out.println(query);
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				String max_num = rs.getString("max_num");
				System.out.println("Max Code Question Number: " + max_num);
				obj.setResponse(Response.FOUND_MAX_NUM_TEST);
				obj.setInformation(max_num);
				System.out.println(obj.toString());
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Importing max code question from cems.question failed!");
			obj.setResponse(Response.DIDNT_FOUND_max_NUM_TEST);
		}

	}

	/**
	 * Creates test data in the database.
	 *
	 * @param obj        The MissionPack object containing the test ID.
	 * @param connection The Connection object for connecting to the database.
	 */
	public static void createTestData(MissionPack obj, Connection connection) {
		String testID = (String) obj.getInformation();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(
					"INSERT INTO test_stats (test_id, average, median, decile1, decile2, decile3, decile4, decile5, decile6, decile7, decile8, decile9, decile10, course, date, time_allocated, students_started, submitted, author) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, testID);
			ps.setDouble(2, 0);
			ps.setInt(3, 0);
			ps.setInt(4, 0);
			ps.setInt(5, 0);
			ps.setInt(6, 0);
			ps.setInt(7, 0);
			ps.setInt(8, 0);
			ps.setInt(9, 0);
			ps.setInt(10, 0);
			ps.setInt(11, 0);
			ps.setInt(12, 0);
			ps.setInt(13, 0);
			ps.setString(14, "0");
			java.util.Date utilDate = new java.util.Date();
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			ps.setDate(15, sqlDate);
			ps.setString(16, "0");
			ps.setInt(17, 0);
			ps.setInt(18, 0);
			ps.setString(19, " ");
			ps.executeUpdate();
			obj.setResponse(Response.UPLOAD_SUCCESS);
		} catch (SQLException e) {
			obj.setResponse(Response.UPDATE_FAILURE);
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
