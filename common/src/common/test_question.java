package common;

import java.io.Serializable;

/**
 *
 * Represents a test question.
 *
 * The test_question class implements the Serializable interface, allowing
 * objects of this class to be serialized and deserialized.
 *
 * It contains fields to store the test ID, question ID, and points associated
 * with the question.
 */
@SuppressWarnings("serial")
public class test_question implements Serializable {
	private String test_id;
	private String question_id;
	private String points;

	/**
	 * Constructs a new `test_question` object with the provided test ID, question
	 * ID, and points.
	 *
	 * @param test_id     The ID of the test associated with the question.
	 * @param question_id The ID of the question.
	 * @param points      The assigned points for the question in the test.
	 */
	public test_question(String test_id, String question_id, String points) {
		super();
		this.test_id = test_id;
		this.question_id = question_id;
		this.points = points;

	}

	/**
	 * Retrieves the ID of the test associated with the question.
	 *
	 * @return The test ID.
	 */
	public String getTest_id() {
		return test_id;
	}

	/**
	 * Sets the ID of the test associated with the question.
	 *
	 * @param test_id The test ID to be set.
	 */
	public void setTest_id(String test_id) {
		this.test_id = test_id;
	}

	/**
	 * Retrieves the ID of the question.
	 *
	 * @return The question ID.
	 */
	public String getQuestion_id() {
		return question_id;
	}

	/**
	 * Sets the ID of the question.
	 *
	 * @param question_id The question ID to be set.
	 */
	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}

	/**
	 * Retrieves the assigned points for the question in the test.
	 *
	 * @return The assigned points.
	 */
	public String getPoints() {
		return points;
	}

	/**
	 * Sets the assigned points for the question in the test.
	 *
	 * @param points The points to be assigned.
	 */
	public void setPoints(String points) {
		this.points = points;
	}

}
