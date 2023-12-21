package common;

import java.io.Serializable;

/**
 * The ExtensionRequest class represents a request for an extension made by an
 * applicant for a specific test.
 *
 * The request includes information about the applicant, the test ID, the
 * details of the extension request, and the current status of the request.
 */
@SuppressWarnings("serial")
public class ExtensionRequest implements Serializable {

	private String applicant; // The applicant who made the request
	private String testID; // The ID of the test
	private String requestDetails; // The details of the extension request
	private String status; // The current status of the request

	/**
	 * Constructs an ExtensionRequest object with the specified applicant, test ID,
	 * request details, and status.
	 *
	 * @param applicant      The applicant who made the request
	 * @param testID         The ID of the test
	 * @param requestDetails The details of the extension request
	 * @param status         The current status of the request
	 */
	public ExtensionRequest(String applicant, String testID, String requestDetails, String status) {
		super();
		this.applicant = applicant;
		this.testID = testID;
		this.requestDetails = requestDetails;
		this.status = status;
	}

	/**
	 * Returns the applicant who made the extension request.
	 *
	 * @return The applicant who made the request
	 */
	public String getApplicant() {
		return applicant;
	}

	/**
	 * Sets the applicant who made the extension request.
	 *
	 * @param applicant The applicant who made the request
	 */
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	/**
	 * Returns the ID of the test for which the extension is requested.
	 *
	 * @return The ID of the test
	 */
	public String getTestID() {
		return testID;
	}

	/**
	 * Sets the ID of the test for which the extension is requested.
	 *
	 * @param testID The ID of the test
	 */
	public void setTestID(String testID) {
		this.testID = testID;
	}

	/**
	 * Returns the details of the extension request.
	 *
	 * @return The details of the extension request
	 */
	public String getRequestDetails() {
		return requestDetails;
	}

	/**
	 * Sets the details of the extension request.
	 *
	 * @param requestDetails The details of the extension request
	 */
	public void setRequestDetails(String requestDetails) {
		this.requestDetails = requestDetails;
	}

	/**
	 * Returns the current status of the extension request.
	 *
	 * @return The current status of the request
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the current status of the extension request.
	 *
	 * @param status The current status of the request
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
