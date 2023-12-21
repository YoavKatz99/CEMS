package common;

import java.io.Serializable;
import java.util.List;

/**
 *
 * A serializable wrapper class for a List.
 *
 * The SerializableList class provides a way to serialize a List object and make
 * it serializable.
 *
 * It allows storing and transferring a List object between different components
 * or systems.
 *
 * @param <T> The type of elements in the List.
 */

@SuppressWarnings("serial")
public class SerializableList<T> implements Serializable {
	private List<T> list;

	/**
	 * Constructs a SerializableList object.
	 *
	 * @param list The list to be serialized.
	 */
	public SerializableList(List<T> list) {
		this.list = list;
	}

	/**
	 * Retrieves the serialized list.
	 *
	 * @return The serialized list.
	 */
	public List<T> getList() {
		return list;
	}
}
