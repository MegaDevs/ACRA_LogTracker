import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

interface FilterCriteria {
	public boolean passes(Object o, Vector<String> tags);
}

class AppVersionFilterCriteria implements FilterCriteria {
	String appVersion;
	int operation;

	public AppVersionFilterCriteria(String appv, int op) {
		appVersion = appv;
		operation = op;
	}

	@Override
	public boolean passes(Object o, Vector<String> tags) {
		//TODO implementare maggiore/minore e varianti
		
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.APP_VERSION));
		if(value == null)
			return false;
		else
			return (value.equals(appVersion));
	}
}

class DateTimeFilterCriteria implements FilterCriteria {
	private Date date;


	public DateTimeFilterCriteria(String d) {
		int spaceIndex = d.indexOf(' ');
		String dateString = d.substring(0, spaceIndex);
		String timeString = d.substring(spaceIndex);

		//TODO finire
	}

	@Override
	public boolean passes(Object o, Vector<String> tags) {
		// TODO Auto-generated method stub
		return false;
	}

}


/**
 * Filter class for "board" field.
 * @author dextor
 *
 */
class BoardFilterCriteria implements FilterCriteria {
	private String board;

	public BoardFilterCriteria(String b) {
		board = b;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.BOARD));
		if(value == null)
			return false;
		else
			return (value.equals(board));
	}

}


class PackageNameFilterCriteria implements FilterCriteria {
	private String package_name;

	public PackageNameFilterCriteria(String p) {
		package_name = p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.PACKAGE_NAME));
		if(value == null)
			return false;
		else
			return (value.equals(package_name));
	}
}


/**
 * Main filtering class. In order for a LogTracker object to filter, a LogEntityFilter instance
 * must be created. Then, any number of FilterCriteria objects may be added to that instance. Finally,
 * by calling the filter() method, all of the added FilterCriteria will be applied to the original
 * (i.e., not filtered) logs collection.
 * @author dextor
 *
 */
public class LogEntityFilter {
	private Vector<FilterCriteria> filters;
	private Map<Integer,Map<String,String>> originalCollection;

	private LogTracker clientRef;

	public LogEntityFilter(LogTracker ref) {
		clientRef = ref;
		filters = new Vector<FilterCriteria>();
		originalCollection = clientRef.getLogValues();
	}

	public void filter() {
		Map<Integer, Map<String,String>> newCollection = new HashMap<Integer, Map<String,String>>();
		int newID = 0;

		for (int i=0; i<originalCollection.size(); i++) {
			Map<String,String> currentLogEntry = originalCollection.get(Integer.valueOf(i));
			boolean good = true;

			for (FilterCriteria f : filters)
				good = good && f.passes(currentLogEntry,clientRef.getTagsList());

			if(good) {
				newCollection.put(Integer.valueOf(newID), currentLogEntry);
				newID++;
			}
		}

		clientRef.setFilteredLogsValues(newCollection);
		clientRef.setFilteredLogsCount(newCollection.size());
	}

	public void addFilterCriteria(FilterCriteria newFilter) {
		filters.add(newFilter);
	}

	public Vector<FilterCriteria> getFilters() {
		return filters;
	}
}
