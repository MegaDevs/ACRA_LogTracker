import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * This is the base interface for each filtering class. Every filter class
 * must implement this interface in order to specialize the 'passes' method.
 * @author dextor
 *
 */
interface FilterCriteria {
	/**
	 * This method defines the criterias for which the passed object 'o'
	 * is considered valid.
	 * @param o the passed object to analyze
	 * @param tags names of the fields
	 * @return true if the passed object satisfies the conditions, false otherwise
	 */
	public boolean passes(Object o, Vector<String> tags);
}



/**
 * This class filters the objects collection based on the 'application version' field.
 * @author dextor
 *
 */
class AppVersionFilterCriteria implements FilterCriteria {
	String appVersion;
	int operation;

	public AppVersionFilterCriteria(String appv, int op) {
		appVersion = appv;
		operation = op;
	}

	@SuppressWarnings("unchecked")
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



/**
 * This class filters the objects collection based on the 'board' field.
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



/**
 * This class filters the objects collection based on the 'brand' field.
 * @author dextor
 *
 */
class BrandFilterCriteria implements FilterCriteria {
	private String brand;

	public BrandFilterCriteria(String b) {
		brand = b;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.BRAND));
		if(value == null)
			return false;
		else
			return (value.equals(brand));
	}

}



/**
 * This class filters the objects collection based on the 'crash configuration' field.
 * @author dextor
 *
 */
class CrashConfigurationFilterCriteria implements FilterCriteria {
	private String crashConfiguration;

	public CrashConfigurationFilterCriteria(String b) {
		crashConfiguration = b;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.CRASH_CONFIGURATION));
		if(value == null)
			return false;
		else
			return (value.equals(crashConfiguration));
	}

}



/**
 * This class filters the objects collection based on the 'custom' field.
 * @author dextor
 *
 */
class CustomFilterCriteria implements FilterCriteria {
	private String custom;

	public CustomFilterCriteria(String b) {
		custom = b;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.CUSTOM));
		if(value == null)
			return false;
		else
			return (value.equals(custom));
	}

}



/**
 * This class filters the objects collection based on the 'date time' field.
 * @author dextor
 *
 */
class DateTimeFilterCriteria implements FilterCriteria {
	public DateTimeFilterCriteria(String d) {}
	
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		// TODO Auto-generated method stub
		return true;
	}
	
}



/**
 * This class filters the objects collection based on the 'device' field.
 * @author dextor
 *
 */
class DeviceFilterCriteria implements FilterCriteria {
	private String device;
	
	public DeviceFilterCriteria(String p) {
		device = p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.DEVICE));
		if(value == null)
			return false;
		else
			return (value.equals(device));
	}
}



/**
 * This class filters the objects collection based on the 'user comments' field.
 * @author dextor
 *
 */
class DisplayFilterCriteria implements FilterCriteria {
	private String display;

	public DisplayFilterCriteria(String b) {
		display = b;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.DISPLAY));
		if(value == null)
			return false;
		else
			return (value.equals(display));
	}

}



/**
 * This class filters the objects collection based on the 'file path' field.
 * @author dextor
 *
 */
class FilePathFilterCriteria implements FilterCriteria {
	private String file_path;

	public FilePathFilterCriteria(String p) {
		file_path = p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.FILE_PATH));
		if(value == null)
			return false;
		else
			return (value.equals(file_path));
	}
}



/**
 * This class filters the objects collection based on the 'fingerprint' field.
 * @author dextor
 *
 */
class FingerprintFilterCriteria implements FilterCriteria {
	private String fingerprint;
	
	public FingerprintFilterCriteria(String p) {
		fingerprint = p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.FINGERPRINT));
		if(value == null)
			return false;
		else
			return (value.equals(fingerprint));
	}
}



/**
 * This class filters the objects collection based on the 'host' field.
 * @author dextor
 *
 */
class HostFilterCriteria implements FilterCriteria {
	private String host;
	
	public HostFilterCriteria(String p) {
		host = p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.HOST));
		if(value == null)
			return false;
		else
			return (value.equals(host));
	}
}



/**
 * This class filters the objects collection based on the 'id' field.
 * @author dextor
 *
 */
class IDFilterCriteria implements FilterCriteria {
	private String id;
	
	public IDFilterCriteria(String p) {
		id = p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.ID));
		if(value == null)
			return false;
		else
			return (value.equals(id));
	}
}



/**
 * This class filters the objects collection based on the 'initial configuration' field.
 * @author dextor
 *
 */
class InitialConfigurationFilterCriteria implements FilterCriteria {
	private String initialConfiguration;

	public InitialConfigurationFilterCriteria(String b) {
		initialConfiguration = b;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.INITIAL_CONFIGURATION));
		if(value == null)
			return false;
		else
			return (value.equals(initialConfiguration));
	}

}



/**
 * This class filters the objects collection based on the 'package name' field.
 * @author dextor
 *
 */
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
 * This class filters the objects collection based on the 'model' field.
 * @author dextor
 *
 */
class ModelFilterCriteria implements FilterCriteria {
	private String model;

	public ModelFilterCriteria(String p) {
		model = p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.MODEL));
		if(value == null)
			return false;
		else
			return (value.equals(model));
	}
}



/**
 * This class filters the objects collection based on the 'phone model' field.
 * @author dextor
 *
 */
class PhoneModelFilterCriteria implements FilterCriteria {
	private String phone_model;

	public PhoneModelFilterCriteria(String p) {
		phone_model = p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.PHONE_MODEL));
		if(value == null)
			return false;
		else
			return (value.equals(phone_model));
	}
}



/**
 * This class filters the objects collection based on the 'product' field.
 * @author dextor
 *
 */
class ProductFilterCriteria implements FilterCriteria {
	private String product;

	public ProductFilterCriteria(String p) {
		product = p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.PRODUCT));
		if(value == null)
			return false;
		else
			return (value.equals(product));
	}
}



/**
 * This class filters the objects collection based on the 'custom' field.
 * @author dextor
 *
 */
class StacktraceFilterCriteria implements FilterCriteria {
	private String stacktrace;

	public StacktraceFilterCriteria(String b) {
		stacktrace = b;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.STACK_TRACE));
		if(value == null)
			return false;
		else
			return (value.equals(stacktrace));
	}

}



/**
 * This class filters the objects collection based on the 'tags' field.
 * @author dextor
 *
 */
class TagsFilterCriteria implements FilterCriteria {
	private String tags;

	public TagsFilterCriteria(String p) {
		tags = p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.TAGS));
		if(value == null)
			return false;
		else
			return (value.equals(this.tags));
	}
}



/**
 * This class filters the objects collection based on the 'type' field.
 * @author dextor
 *
 */
class TypeFilterCriteria implements FilterCriteria {
	private String type;

	public TypeFilterCriteria(String p) {
		type = p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.TYPE));
		if(value == null)
			return false;
		else
			return (value.equals(type));
	}
}



/**
 * This class filters the objects collection based on the 'user' field.
 * @author dextor
 *
 */
class UserFilterCriteria implements FilterCriteria {
	private String user;

	public UserFilterCriteria(String p) {
		user = p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.USER));
		if(value == null)
			return false;
		else
			return (value.equals(user));
	}
}



/**
 * This class filters the objects collection based on the 'user comments' field.
 * @author dextor
 *
 */
class UserCommentsFilterCriteria implements FilterCriteria {
	private String userComments;

	public UserCommentsFilterCriteria(String b) {
		userComments = b;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.USER_COMMENTS));
		if(value == null)
			return false;
		else
			return (value.equals(userComments));
	}

}



/**
 * This class filters the objects collection based on the 'user comments' field.
 * @author dextor
 *
 */
class UserCrashDateFilterCriteria implements FilterCriteria {
	private String userCrashDate;

	public UserCrashDateFilterCriteria(String b) {
		userCrashDate = b;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean passes(Object o, Vector<String> tags) {
		Map<String,String> obj = (Map<String, String>) o;
		String value = obj.get(tags.get(ColNamesAssoc.USER_CRASH_DATE));
		if(value == null)
			return false;
		else
			return (value.equals(userCrashDate));
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
	/**
	 * Private data fields: the original objects collection, the filters collection
	 * and a reference to the LogTracker object.
	 */
	private Vector<FilterCriteria> filters;
	private Map<Integer,Map<String,String>> originalCollection;
	private LogTracker clientRef;

	/**
	 * Default constructor for the LogEntityFilter object.
	 * @param ref a reference to the LogTracker instance
	 */
	public LogEntityFilter(LogTracker ref) {
		clientRef = ref;
		filters = new Vector<FilterCriteria>();
		originalCollection = clientRef.getLogValues();
	}

	/**
	 * This method applies all the filters to the original collection of log entries and sets
	 * the new collection, which contains all the filtered entries, to the LogTracker object.
	 */
	public void filter() {
		Map<Integer, Map<String,String>> newCollection = new HashMap<Integer, Map<String,String>>();
		int newID = 0;

		for (int i=0; i<originalCollection.size(); i++) {
			Map<String,String> currentLogEntry = originalCollection.get(Integer.valueOf(i));
			boolean good = true;

			for (FilterCriteria f : filters)
				/// testing all of the filters with the 'passes' method
				good = good && f.passes(currentLogEntry,clientRef.getTagsList());

			if(good) {
				/// the object satisfies all the filters' conditions, so it must be added to the
				/// new collection
				newCollection.put(Integer.valueOf(newID), currentLogEntry);
				newID++;
			}
		}

		/// updating the LogTracker with the filtered collection of logs
		clientRef.setFilteredLogsValues(newCollection);
		clientRef.setFilteredLogsCount(newCollection.size());
	}
	
	/**
	 * Method that adds a new filter to the filters collection. 
	 * @param newFilter new filter to add
	 */
	public void addFilterCriteria(FilterCriteria newFilter) {
		filters.add(newFilter);
	}

	/**
	 * Get/Set methods for the LogEntityFilter class.
	 */
	
	///
	public Vector<FilterCriteria> getFilters() {
		return filters;
	}
}
