import java.util.Vector;

public class LogEntity {
	private int ID;
	private Vector<String> logEntries;

	public LogEntity(int id, Vector<String> data) {
		ID = id;
		logEntries = data;
	}

	/**
	 * @return the iD
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