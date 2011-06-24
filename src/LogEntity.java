import java.util.Vector;

/**
 * This class represents a log entry in the collection. A log features
 * an unique ID and a list of values.
 * @author dextor
 *
 */
public class LogEntity {
	private int ID;
	private Vector<String> logEntries;

	public LogEntity(int id, Vector<String> data) {
		ID = id;
		logEntries = data;
	}

	/**
	 * @return the ID
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @return the logEntries
	 */
	public Vector<String> getLogEntries() {
		return logEntries;
	}
}