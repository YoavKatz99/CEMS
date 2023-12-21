package common;

import java.io.Serializable;

/**
 * The MyFile class represents a file in the system. It stores information about
 * the file, including the file description, file name, size, and byte array
 * data.
 * 
 * This class provides methods to initialize the byte array, set and retrieve
 * the file name, size, byte array, and description.
 * 
 * The MyFile class is used to handle file-related operations within the system,
 * such as storing and retrieving files, manipulating file data, and managing
 * file information. It provides a convenient way to work with file data and
 * metadata.
 */
public class MyFile implements Serializable {

	private String Description = null;
	private String fileName = null;
	private int size = 0;
	public byte[] mybytearray;

	/**
	 * Initializes the byte array with the specified size.
	 * 
	 * @param size the size of the byte array
	 */
	public void initArray(int size) {
		mybytearray = new byte[size];
	}

	/**
	 * Constructs a MyFile object with the specified file name.
	 * 
	 * @param fileName the name of the file
	 */
	public MyFile(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Returns the file name.
	 * 
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the file name.
	 * 
	 * @param fileName the file name to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Returns the size of the file.
	 * 
	 * @return the size of the file
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets the size of the file.
	 * 
	 * @param size the size of the file to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Returns the byte array data of the file.
	 * 
	 * @return the byte array data of the file
	 */
	public byte[] getMybytearray() {
		return mybytearray;
	}

	/**
	 * Returns the byte at the specified index of the file's byte array.
	 * 
	 * @param i the index of the byte to retrieve
	 * @return the byte at the specified index
	 */
	public byte getMybytearray(int i) {
		return mybytearray[i];
	}

	/**
	 * Sets the byte array data of the file.
	 * 
	 * @param mybytearray the byte array data to set
	 */
	public void setMybytearray(byte[] mybytearray) {

		for (int i = 0; i < mybytearray.length; i++)
			this.mybytearray[i] = mybytearray[i];
	}

	/**
	 * Returns the description of the file.
	 * 
	 * @return the file description
	 */
	public String getDescription() {
		return Description;
	}

	/**
	 * Sets the description of the file.
	 * 
	 * @param description the file description to set
	 */
	public void setDescription(String description) {
		Description = description;
	}
}
