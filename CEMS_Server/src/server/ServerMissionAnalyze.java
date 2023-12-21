package server;

import java.sql.SQLException;

import common.ClientStatus;
import common.Mission;
import common.MissionPack;
import common.Response;
import entities.DatabaseConnector;
import ocsf.server.ConnectionToClient;

/**
 * Redirects the clientMissionControler to the correct method in the query
 * executor
 */
public class ServerMissionAnalyze {

	public static void MissionsAnalyze(MissionPack obj, ConnectionToClient client) throws SQLException {
		switch (obj.getMission()) {
		case GET_QUESTIONS: {
			QueryExecutor.getQuestionsData(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_QUESTION_TABLE: {
			QueryExecutor.getQuestionTable(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case UP_TEST: {
			QueryExecutor.uploadtest(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_TEST: {
			QueryExecutor.getTestsInfo(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_MY_TESTS: {
			QueryExecutor.getLecturerTests(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}

		case GET_QUESTION_TABLE_FOR_TEST: {
			QueryExecutor.getQuestionTableTest(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}

		case SEND_CONNECTION_DETAILS: {
			QueryExecutor.updateClientList(obj, client, ClientStatus.CONNECTED);
			break;
		}

		case SEND_DISCONNECTION_DETAILS: {
			QueryExecutor.updateClientList(obj, client, ClientStatus.DISCONNECTED);
			break;
		}
		case LOGIN: {
			QueryExecutor.getLoginInfo(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_GRADES: {
			QueryExecutor.getStudentGrades(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case LOGOUT: {
			QueryExecutor.logout(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_COURSES: {
			QueryExecutor.getCoursesList(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_TESTS_OF_COURSE: {
			QueryExecutor.getTestsOfCourseList(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;

		}
		case GENERATE_LECTURER_REPORT: {
			QueryExecutor.getDataofTest(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_DB_TABLE: {
			QueryExecutor.getDataBaseTable(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case ADD_QUESTIONS_DATA: {
			QueryExecutor.addquestiontodb(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_QUESTIONS_FROM_TEST: {
			QueryExecutor.getQuestionsFromTest(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_LISTS: {
			QueryExecutor.getReportsRelatedInfo(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GENERATE_DEP_HEAD_REPORT: {
			QueryExecutor.getDepHeadReportData(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_REQUESTS: {
			QueryExecutor.getExtensionRequests(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case UPDATE_REQUESTS: {
			QueryExecutor.updateExtensionRequests(obj,
					DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_ALL_TESTS_STATS: {
			QueryExecutor.getAllTestsStats(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case ADD_PERFORMED_TEST: {
			QueryExecutor.addPerformedTesttoDB(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_MAX_CODE_QUESTION: {
			QueryExecutor.getMaxCodeQuestion(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_YAEL: {
			QueryExecutor.downloadTest(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case UPDATE_MY_TESTS: {
			QueryExecutor.updateTests(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case SEND_EXTENSION_REQUEST: {
			QueryExecutor.sendExtensionRequest(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case CHECK_REQUEST_CHANGE: {
			QueryExecutor.getApprovedRequests(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_PERFORMED_TESTS: {
			QueryExecutor.getPerformedTests(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_SINGLE_TEST: {
			QueryExecutor.getSingleTest(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_SINGLE_TEST2: {
			QueryExecutor.getSingleTest2(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}

		case ADD_TEST_DATA: {
			QueryExecutor.addTestData(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_ID_FROM_CODE: {
			QueryExecutor.getIdFromCode(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case CONCLUDE_REQUESTS: {
			QueryExecutor.concludeRequests(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_STATUS: {
			QueryExecutor.getStatusTest(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_STATUS_TEST: {
			QueryExecutor.getStatusTestmanuall(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_REQUEST_TEST: {
			QueryExecutor.getrequest(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_TEST_BY_CHOICE: {
			QueryExecutor.getTestIdList(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case GET_GRADES_BY_LECTURER: {
			QueryExecutor.getGradesByLecturer(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case UPDATE_PERFORMED_TESTS: {
			QueryExecutor.updatePerformedTests(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case SELECTED_QUESTIONS: {
			QueryExecutor.updateSelectQuestion(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case MAX_NUM_TEST_ID: {
			QueryExecutor.getMaxNumTest(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case ADD_TEST_QUESTION: {
			QueryExecutor.updateTestQuestion(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case ADD_TEST: {
			QueryExecutor.updateTest(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		case CREATE_TEST_DATA: {
			QueryExecutor.createTestData(obj, DatabaseConnector.getDatabaseConnectorInstance().getConnection());
			break;
		}
		}
	}

}
