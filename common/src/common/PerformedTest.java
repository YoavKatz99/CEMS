package common;

import java.io.Serializable;

/**
 * The PerformedTest class represents a performed test in the system. It stores
 * information about the test, including the test ID, subject, course, user ID,
 * author, grade, and copy check status.
 * 
 * This class provides constructors to create a PerformedTest object with
 * different combinations of these attributes.
 * 
 * The PerformedTest class is used to track and manage performed tests within
 * the system. It allows storing and retrieving information about the tests,
 * such as the test ID, subject, course, user ID, author, grade, and copy check
 * status. This class is designed to facilitate the processing and analysis of
 * performed tests.
 */
@SuppressWarnings("serial")
public class PerformedTest implements Serializable {

	private String testID;
	private String subject;
	private String Course;
	private String userId;
	private String author;
	private int grade;
	private String copyCheck;
	private String type;
	private String notes;
	
	/**
	 * Constructs a PerformedTest object with the specified test ID, course, and
	 * grade.
	 * 
	 * @param id     the ID of the performed test
	 * @param course the course associated with the performed test
	 * @param grade  the grade obtained in the performed test
	 */
	public PerformedTest(String id, String course, int grade) {
		super();
		this.testID = id;
		this.Course = course;
		this.grade = grade;
	}

	/**
	 * Constructs a PerformedTest object with the specified test ID, subject,
	 * course, user ID, author, and grade.
	 * 
	 * @param id      the ID of the performed test
	 * @param subject the subject of the performed test
	 * @param course  the course associated with the performed test
	 * @param userId  the ID of the user who performed the test
	 * @param author  the author of the test
	 * @param grade   the grade obtained in the performed test
	 */
	public PerformedTest(String id, String subject, String course, String userId, String author, int grade) {
		super();
		this.testID = id;
		this.subject = subject;
		this.Course = course;
		this.userId = userId;
		this.author = author;
		this.grade = grade;
	}

	/**
	 * Constructs a PerformedTest object with the specified test ID, subject,
	 * course, user ID, author, grade, and copy check status.
	 * 
	 * @param id        the ID of the performed test
	 * @param subject   the subject of the performed test
	 * @param course    the course associated with the performed test
	 * @param userId    the ID of the user who performed the test
	 * @param author    the author of the test
	 * @param grade     the grade obtained in the performed test
	 * @param copyCheck the status of the copy check for the performed test
	 * @param type the type of the performed test
	 */

	public PerformedTest(String id, String subject, String course, String userId, String author, int grade, String copyCheck, String type) {
		super();
		this.testID = id;
		this.subject = subject;
		this.Course = course;
		this.userId = userId;
		this.author = author;
		this.grade = grade;
		this.copyCheck = copyCheck;
		this.type = type;
	}

	
	public PerformedTest(String id, String course, String userId,  int grade, String notes) {
		super();
		this.testID = id;
		this.Course = course;
		this.grade = grade;
		this.userId = userId;
		this.notes = notes;
	}
	
	/**
	 * Returns the test ID of the performed test.
	 * 
	 * @return the test ID
	 */
	public String getTestID() {
		return testID;
	}

	/**
	 * Sets the test ID of the performed test.
	 * 
	 * @param id the test ID to set
	 */
	public void setId(String id) {
		this.testID = id;
	}

	/**
	 * Returns the subject of the performed test.
	 * 
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the subject of the performed test.
	 * 
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Returns the course associated with the performed test.
	 * 
	 * @return the course
	 */
	public String getCourse() {
		return Course;
	}

	/**
	 * Sets the course associated with the performed test.
	 * 
	 * @param course the course to set
	 */
	public void setCourse(String course) {
		Course = course;
	}

	/**
	 * Returns the user ID of the user who performed the test.
	 * 
	 * @return the user ID
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user ID of the user who performed the test.
	 * 
	 * @param userId the user ID to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Returns the author of the test.
	 * 
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Sets the author of the test.
	 * 
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Returns the grade obtained in the performed test.
	 * 
	 * @return the grade
	 */
	public int getGrade() {
		return grade;
	}

	/**
	 * Sets the grade obtained in the performed test.
	 * 
	 * @param grade the grade to set
	 */
	public void setGrade(int grade) {
		this.grade = grade;
	}

	/**
	 * Returns the copy check status of the performed test.
	 * 
	 * @return the copy check status
	 */
	public String getCopyCheck() {
		return copyCheck;
	}

	/**
	 * Sets the copy check status of the performed test.
	 * 
	 * @param copyCheck the copy check status to set
	 */
	public void setCopyCheck(String copyCheck) {
		this.copyCheck = copyCheck;
	}
	
	public String getNotes() {
		return notes;
	}


	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}

}
