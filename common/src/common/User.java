package common;

import java.io.Serializable;

/**
 * The User class represents a user in the system.
 * It contains information such as the username, password, user type, name, surname, and user ID.
 * The class implements the Serializable interface to support object serialization.
 */
@SuppressWarnings("serial")
public class User implements Serializable{
	private String userName;
	private String Password;
	private String UserType;
	private String name;
	private String surname;
	private String userID;

    /**
     * Returns the user's ID.
     *
     * @return the user's ID
     */
	public String getUserID() {
		return userID;
	}

    /**
     * Sets the user's ID.
     *
     * @param userID the user's ID
     */
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	/**
     * Constructs a User object with the specified username, password, user type, name, surname, and user ID.
     *
     * @param userName the username of the user
     * @param password the password of the user
     * @param userType the type of the user (e.g., lecturer, student)
     * @param name     the name of the user
     * @param surname  the surname of the user
     * @param UserID   the ID of the user
     */
	public User(String userName, String password, String userType, String name, String surname, String UserID) {
		super();
		this.userName = userName;
		Password = password;
		UserType = userType;
		this.name = name;
		this.surname = surname;
		this.userID = UserID;
	}

    /**
     * Returns the user's name.
     *
     * @return the user's name
     */
	public String getName() {
		return name;
	}

    /**
     * Sets the user's name.
     *
     * @param name the user's name
     */
	public void setName(String name) {
		this.name = name;
	}

    /**
     * Returns the user's surname.
     *
     * @return the user's surname
     */
	public String getSurname() {
		return surname;
	}

    /**
     * Sets the user's surname.
     *
     * @param surname the user's surname
     */
	public void setSurname(String surname) {
		this.surname = surname;
	}

    /**
     * Returns the user's type.
     *
     * @return the user's type
     */
	public String getUserType() {
		return UserType;
	}

    /**
     * Sets the user's type.
     *
     * @param userType the user's type
     */
	public void setUserType(String userType) {
		UserType = userType;
	}

	public User(String userName, String password, String userType) {
		super();
		this.userName = userName;
		Password = password;
		UserType = userType;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

    /**
     * Returns the username of the user.
     *
     * @return the username of the user
     */
	public String getUserName() {
		return userName;
	}


    /**
     * Sets the username of the user.
     *
     * @param userName the username of the user
     */
	public void setUserName(String userName) {
		this.userName = userName;
	}

    /**
     * Returns the password of the user.
     *
     * @return the password of the user
     */
	public String getPassword() {
		return Password;
	}


    /**
     * Sets the password of the user.
     *
     * @param password the password of the user
     */
	public void setPassword(String password) {
		Password = password;
	}

}
