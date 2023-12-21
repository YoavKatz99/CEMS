package common;

import java.io.Serializable;

/**
 * The MissionPack class represents a package containing a mission, its
 * response, and additional information. It is used to encapsulate related data
 * and pass it between components or systems in the application.
 * 
 * This class provides methods to set and retrieve the mission, response, and
 * information within the mission pack. It also overrides the toString() method
 * to provide a customized string representation of the mission pack.
 */
@SuppressWarnings("serial")
public class MissionPack implements Serializable {
	private Mission mission;
	private Response response;
	private Object information;

	/**
	 * Constructs a MissionPack object with the specified mission, response, and
	 * information.
	 * 
	 * @param mission     the mission object
	 * @param response    the response object
	 * @param information the additional information object
	 */
	public MissionPack(Mission mission, Response response, Object information) {
		this.mission = mission;
		this.response = response;
		this.information = information;
	}

	/**
	 * Returns the mission object within the mission pack.
	 * 
	 * @return the mission object
	 */
	public Mission getMission() {
		return mission;
	}

	/**
	 * Sets the mission object within the mission pack.
	 * 
	 * @param mission the mission object to set
	 */
	public void setMission(Mission mission) {
		this.mission = mission;
	}

	/**
	 * Returns the response object within the mission pack.
	 * 
	 * @return the response object
	 */
	public Response getResponse() {
		return response;
	}

	/**
	 * Sets the response object within the mission pack.
	 * 
	 * @param response the response object to set
	 */
	public void setResponse(Response response) {
		this.response = response;
	}

	/**
	 * Returns the additional information object within the mission pack.
	 * 
	 * @return the additional information object
	 */
	public Object getInformation() {
		return information;
	}

	/**
	 * Sets the additional information object within the mission pack.
	 * 
	 * @param information the additional information object to set
	 */
	public void setInformation(Object information) {
		this.information = information;
	}

	/**
	 * Returns a string representation of the mission pack. The string includes the
	 * mission and response details.
	 * 
	 * @return a string representation of the mission pack
	 */
	@Override
	public String toString() {
		return "You try to : " + getMission() + "  " + "The response is : " + getResponse();
	}
}