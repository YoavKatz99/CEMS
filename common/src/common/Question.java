package common;

import java.io.Serializable;

/**
 * 
 * The Question class represents a question object.
 * 
 * It implements the Serializable interface to support object serialization.
 */
@SuppressWarnings("serial")
public class Question implements Serializable {

	private String id;
	private String subject;
	private String CourseName;
	private String questionText;
	private String questionNumber;
	private String lecturer;
	private String op1;
	private String op2;
	private String op3;
	private String op4;
	private String Coreectop;
	String points;

	/**
	 * Constructs a Question object with the provided parameters.
	 *
	 * @param id             The ID of the question.
	 * @param subject        The subject of the question.
	 * @param courseName     The name of the course.
	 * @param questionText   The text of the question.
	 * @param questionNumber The number of the question.
	 * @param lecturer       The name of the lecturer.
	 * @param op1            The first option.
	 * @param op2            The second option.
	 * @param op3            The third option.
	 * @param op4            The fourth option.
	 * @param coreectop      The correct option.
	 * @param points         The points awarded for the question.
	 */
	public Question(String id, String subject, String courseName, String questionText, String questionNumber,
			String lecturer, String op1, String op2, String op3, String op4, String coreectop, String points) {
		super();
		this.id = id;
		this.subject = subject;
		CourseName = courseName;
		this.questionText = questionText;
		this.questionNumber = questionNumber;
		this.lecturer = lecturer;
		this.op1 = op1;
		this.op2 = op2;
		this.op3 = op3;
		this.op4 = op4;
		Coreectop = coreectop;
		this.points = points;
	}

	/**
	 * Constructs a Question object with the provided parameters.
	 *
	 * @param id             The ID of the question.
	 * @param subject        The subject of the question.
	 * @param courseName     The name of the course.
	 * @param questionText   The text of the question.
	 * @param questionNumber The number of the question.
	 * @param lecturer       The name of the lecturer.
	 * @param op1            The first option.
	 * @param op2            The second option.
	 * @param op3            The third option.
	 * @param op4            The fourth option.
	 * @param Coreectop      The correct option.
	 */
	public Question(String id, String subject, String courseName, String questionText, String questionNumber,
			String lecturer, String op1, String op2, String op3, String op4, String Coreectop) {
		super();
		this.id = id;
		this.subject = subject;
		this.CourseName = courseName;
		this.questionText = questionText;
		this.questionNumber = questionNumber;
		this.lecturer = lecturer;
		this.op1 = op1;
		this.op2 = op2;
		this.op3 = op3;
		this.op4 = op4;
		this.Coreectop = Coreectop;

	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	/**
	 * Retrieves the ID of the question.
	 *
	 * @return The ID of the question.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the ID of the question.
	 *
	 * @param id The ID of the question.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Retrieves the subject of the question.
	 *
	 * @return The subject of the question.
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the subject of the question.
	 *
	 * @param subject The subject of the question.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Retrieves the course name of the question.
	 *
	 * @return The course name of the question.
	 */
	public String getCourseName() {
		return CourseName;
	}

	/**
	 * Sets the course name of the question.
	 *
	 * @param courseName The course name of the question.
	 */
	public void setCourseName(String courseName) {
		CourseName = courseName;
	}

	/**
	 * Retrieves the text of the question.
	 *
	 * @return The text of the question.
	 */
	public String getText() {
		return questionText;
	}

	/**
	 * Sets the text of the question.
	 *
	 * @param text The text of the question.
	 */
	public void setText(String text) {
		this.questionText = text;
	}

	/**
	 * Retrieves the number of the question.
	 *
	 * @return The number of the question.
	 */
	public String getNumber() {
		return questionNumber;
	}

	/**
	 * Sets the number of the question.
	 *
	 * @param questionNumber The number of the question.
	 */
	public void setNumber(String questionNumber) {
		this.questionNumber = questionNumber;
	}

	/**
	 * Retrieves the lecturer of the question.
	 *
	 * @return The lecturer of the question.
	 */
	public String getLecturer() {
		return lecturer;
	}

	/**
	 * Sets the lecturer of the question.
	 *
	 * @param lecturer The lecturer of the question.
	 */
	public void setLecturer(String lecturer) {
		this.lecturer = lecturer;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionNumber;
	}

	public String getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(String questionNumber) {
		this.questionNumber = questionNumber;
	}

	/**
	 * Retrieves the first option of the question.
	 *
	 * @return The first option of the question.
	 */
	public String getOp1() {
		return op1;
	}

	/**
	 * Sets the first option of the question.
	 *
	 * @param op1 The first option of the question.
	 */
	public void setOp1(String op1) {
		this.op1 = op1;
	}

	/**
	 * Retrieves the second option of the question.
	 *
	 * @return The second option of the question.
	 */
	public String getOp2() {
		return op2;
	}

	/**
	 * Sets the second option of the question.
	 *
	 * @param op2 The second option of the question.
	 */
	public void setOp2(String op2) {
		this.op2 = op2;
	}

	/**
	 * Retrieves the third option of the question.
	 *
	 * @return The third option of the question.
	 */
	public String getOp3() {
		return op3;
	}

	/**
	 * Sets the third option of the question.
	 *
	 * @param op3 The third option of the question.
	 */
	public void setOp3(String op3) {
		this.op3 = op3;
	}

	/**
	 * Retrieves the fourth option of the question.
	 *
	 * @return The fourth option of the question.
	 */
	public String getOp4() {
		return op4;
	}

	/**
	 * Sets the fourth option of the question.
	 *
	 * @param op4 The fourth option of the question.
	 */
	public void setOp4(String op4) {
		this.op4 = op4;
	}

	/**
	 * Retrieves the correct option of the question.
	 *
	 * @return The correct option of the question.
	 */
	public String getCoreectop() {
		return Coreectop;
	}

	/**
	 * Sets the correct option of the question.
	 *
	 * @param coreectop The correct option of the question.
	 */
	public void setCoreectop(String coreectop) {
		Coreectop = coreectop;
	}

	public String toString() {
		return id + " " + subject + " " + CourseName + " " + questionText + " " + questionNumber + " " + lecturer + op1;
	}

}
