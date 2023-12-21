package common;

import java.io.Serializable;

/**
 *
 * Represents a Test object. The Test class implements the Serializable
 * interface, allowing objects of this class to be serialized and deserialized.
 * It contains fields to store various properties of a test, such as select, ID,
 * subject text, course name, author text, status, duration, and test code.
 */
@SuppressWarnings("serial")
public class Test implements Serializable {

	private String select;
	private String ID;
	private String subjectText;
	private String Course;
	private String authorText;
	private String status;
	private Integer Duration;
	private String TestCode;
	private String LecturerText;
	private String StudentText;
	private String NumTest;

	/**
	 * Default constructor for the Test class.
	 */
	public Test() {
	}

	/**
	 * Constructor for the Test class.
	 *
	 * @param select      The select value.
	 * @param ID          The ID value.
	 * @param subjectText The subject text value.
	 * @param courseName  The course name value.
	 * @param authorText  The author text value.
	 * @param status      The status value.
	 * @param Duration    The duration value.
	 * @param TestCode    The test code value.
	 */
	public Test(String select, String ID, String subjectText, String courseName, String authorText, String status,
			Integer Duration, String TestCode) {
		super();
		this.select = select;
		this.ID = ID;
		this.subjectText = subjectText;
		this.Course = courseName;
		this.authorText = authorText;
		this.status = status;
		this.Duration = Duration;
		this.TestCode = TestCode;
	}

	/**
	 * Constructor for the Test class.
	 *
	 * @param id           The ID value.
	 * @param subjectText  The subject text value.
	 * @param courseName   The course name value.
	 * @param authorText   The author text value.
	 * @param Duration     The duration value.
	 * @param TestCode     The test code value.
	 * @param LecturerText The lecturer text value.
	 * @param StudentText  The student text value.
	 * @param NumTest      The number of test value.
	 */
	public Test(String id, String subjectText, String courseName, String authorText, Integer Duration, String TestCode,
			String LecturerText, String StudentText, String NumTest) {
		this.ID = id;
		this.subjectText = subjectText;
		this.Course = courseName;
		this.authorText = authorText;
		this.Duration = Duration;
		this.TestCode = TestCode;
		this.LecturerText = LecturerText;
		this.StudentText = StudentText;
		this.NumTest = NumTest;

	}

	/**
	 * Retrieves the lecturer text value.
	 * 
	 * @return The lecturer text.
	 */
	public String getLecturerText() {
		return LecturerText;
	}

	/**
	 * Sets the lecturer text value.
	 * 
	 * @param lecturerText The lecturer text to set.
	 */
	public void setLecturerText(String lecturerText) {
		LecturerText = lecturerText;
	}

	/**
	 * Retrieves the subject text value.
	 * 
	 * @return The subject text.
	 */
	public String getSubjectText() {
		return subjectText;
	}

	/**
	 * Sets the subject text value.
	 * 
	 * @param subjectText The subject text to set.
	 */
	public void setSubjectText(String subjectText) {
		this.subjectText = subjectText;
	}

	/**
	 * Retrieves the student text value.
	 * 
	 * @return The student text.
	 */
	public String getStudentText() {
		return StudentText;
	}

	/**
	 * Sets the student text value.
	 * 
	 * @param studentText The student text to set.
	 */
	public void setStudentText(String studentText) {
		StudentText = studentText;
	}

	/**
	 * Retrieves the number of test value.
	 * 
	 * @return The number of test.
	 */
	public String getNumTest() {
		return NumTest;
	}

	/**
	 * Sets the number of test value.
	 * 
	 * @param numTest The number of test to set.
	 */
	public void setNumTest(String numTest) {
		NumTest = numTest;
	}

	/**
	 * Retrieves the status of the test.
	 *
	 * @return The status of the test.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status of the test.
	 *
	 * @param status The status of the test.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Retrieves the select value.
	 *
	 * @return The select value.
	 */
	public String select() {
		return select;
	}

	/**
	 * Sets the select value.
	 *
	 * @param select The select value.
	 */
	public void setselect(String select) {
		this.select = select;
	}

	/**
	 * Retrieves the ID value.
	 *
	 * @return The ID value.
	 */
	public String getID() {
		return ID;
	}

	/**
	 * Sets the ID value.
	 *
	 * @param ID The ID value.
	 */
	public void setID(String ID) {
		this.ID = ID;
	}

	/**
	 * Retrieves the subject text value.
	 *
	 * @return The subject text value.
	 */
	public String getSubject() {
		return subjectText;
	}

	/**
	 * Sets the subject text value.
	 *
	 * @param subject The subject text value.
	 */
	public void setSubject(String subject) {
		this.subjectText = subjectText;
	}

	/**
	 * Retrieves the course name value.
	 *
	 * @return The course name value.
	 */
	public String getCourse() {
		return Course;
	}

	/**
	 * Sets the course name value.
	 *
	 * @param courseName The course name value.
	 */
	public void setCourse(String courseName) {
		Course = courseName;
	}

	/**
	 * Retrieves the author text value.
	 *
	 * @return The author text value.
	 */
	public String getAuthor() {
		return authorText;
	}

	/**
	 * Sets the author text value.
	 *
	 * @param authorText The author text value.
	 */
	public void setAuthor(String authorText) {
		this.authorText = authorText;
	}

	/**
	 * Retrieves the status value.
	 *
	 * @return The status value.
	 */
	public String getstatus() {
		return status;
	}

	/**
	 * Sets the status value.
	 *
	 * @param status The status value.
	 */
	public void setstatus(String status) {
		this.status = status;
	}

	/**
	 * Retrieves the duration value.
	 *
	 * @return The duration value.
	 */
	public Integer getDuration() {
		return Duration;
	}

	/**
	 * Sets the duration value.
	 *
	 * @param Duration The duration value.
	 */
	public void setDturation(Integer Duration) {
		this.Duration = Duration;
	}

	/**
	 * Retrieves the test code value.
	 *
	 * @return The test code value.
	 */
	public String getTestCode() {
		return TestCode;
	}

	/**
	 * Sets the test code value.
	 *
	 * @param TestCode The test code value.
	 */
	public void setTestCode(String TestCode) {
		this.TestCode = TestCode;
	}

	/**
	 * Returns a string representation of the Test object.
	 *
	 * @return A string representation of the Test object.
	 */
	public String toString() {
		return ID + " " + subjectText + " " + Course + " " + authorText + " " + status + " " + Duration + " " + TestCode
				+ " ";
	}

}
