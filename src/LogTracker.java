import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import com.google.gdata.client.ClientLoginAccountType;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class LogTracker {
	
	/**
	 * This object represents a lock between the Business Logic and the Graphics.
	 * It is used in order to perform updates in the UI just after the data has been updated. 
	 * TODO removable?
	 */
	public Object lock;

	private LogTracker_GUI guiRef;

	private SpreadsheetService client;
	private SpreadsheetEntry[] spreadsheetsList;
	private SpreadsheetEntry logSpreadsheet;
	private String[] spreadsheetsTitles;

	private Map<Integer,Map<String,String>> logsValues;
	private Vector<String> tagsList;
	private int logsCount;

	private WorksheetEntry logsWorksheet;
	private URL listFeed;
	private ListFeed lists;

	private LogEntityFilter filters;

	private boolean loggedIn;
	private boolean logUpdated;

	private boolean filtersOn;
	private int filteredLogsCount;
	private Map<Integer,Map<String,String>> filteredLogsValues;

	private void init() {
		lock = new Object();

		logsValues = new HashMap<Integer, Map<String, String>>();
		tagsList = new Vector<String>();

		loggedIn = false;
		logUpdated = false;
		filtersOn = false;
		filteredLogsCount = -1;
		filteredLogsValues = new HashMap<Integer, Map<String,String>>();

		guiRef = new LogTracker_GUI(this);
	}

	/**
	 * The login method, which uses Google authentication APIs in order to perform the operation.
	 * @param usr the username parameter
	 * @param pwd the password paramenter
	 * @throws AuthenticationException
	 */
	public void login(String usr, String pwd) throws AuthenticationException {
		client = new SpreadsheetService("acra-logtracker-megadevs");
		client.setUserCredentials(usr, pwd, ClientLoginAccountType.GOOGLE);
		guiRef.writeSysLog("E","Error in logging in with your Google credentials: please retry!");
		guiRef.writeSysLog("L","Login successful. Please select the log spreadsheet from the given list.");
		loggedIn = true;
	}

	public void updateSpreadsheetsList() {
		URL metafeedUrl = null;
		try {
			metafeedUrl = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
		} catch (MalformedURLException e) {
			guiRef.writeSysLog("E","The Google spreadsheets URL seems invalid. Nothing that you can do about it.");
			e.printStackTrace();
		}

		SpreadsheetFeed feed;
		try {
			if(metafeedUrl != null) {
				feed = client.getFeed(metafeedUrl, SpreadsheetFeed.class);

				LinkedList<SpreadsheetEntry> spreadsheets = (LinkedList<SpreadsheetEntry>) feed.getEntries();
				spreadsheetsList = new SpreadsheetEntry[spreadsheets.size()];
				spreadsheetsList = spreadsheets.toArray(spreadsheetsList);
			}
		} catch (ServiceException e) {
			guiRef.writeSysLog("E","Error in retrieving your spreadsheets list. Please try again!");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		spreadsheetsTitles = new String[spreadsheetsList.length];
		int i=0;
		for(SpreadsheetEntry s : spreadsheetsList) {
			spreadsheetsTitles[i] = s.getTitle().getPlainText();
			i++;
		}
	}

	/**
	 * The 'filter' method handles the filtering operations from the LogTracker instance. It receives
	 * a collection of filters and for each one of them it creates the corrisponding FilterCriteria
	 * implementation object. When all FilterCriteria objects have been processed, they are applied to the
	 * whole collection of logs.
	 * @param filterParams a collection of "raw" filters, which must be processed and then applied
	 */
	public void filter(Vector<Vector<String>> filterParams) {
		filters = new LogEntityFilter(this);		

		for (int i=0; i<filterParams.size(); i++) {
			int field = Integer.valueOf(filterParams.get(i).get(0));
			int operation = Integer.valueOf(filterParams.get(i).get(1));
			String value = filterParams.get(i).get(2);	

			switch (field) {
				case (ColNamesAssoc.APP_VERSION) : {
					AppVersionFilterCriteria filter = new AppVersionFilterCriteria(value, operation);
					filters.addFilterCriteria(filter);
				} break;
				case (ColNamesAssoc.BOARD) : {
					BoardFilterCriteria filter = new BoardFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.BRAND) : {
					BrandFilterCriteria filter = new BrandFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.CRASH_CONFIGURATION) : {
					CrashConfigurationFilterCriteria filter = new CrashConfigurationFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.CUSTOM) : {
					CustomFilterCriteria filter = new CustomFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.DEVICE) : {
					DeviceFilterCriteria filter = new DeviceFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.DISPLAY) : {
					DisplayFilterCriteria filter = new DisplayFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.FILE_PATH) : {
					FilePathFilterCriteria filter = new FilePathFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.FINGERPRINT) : {
					FingerprintFilterCriteria filter = new FingerprintFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.HOST) : {
					HostFilterCriteria filter = new HostFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.ID) : {
					IDFilterCriteria filter = new IDFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.INITIAL_CONFIGURATION) : {
					InitialConfigurationFilterCriteria filter = new InitialConfigurationFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.MODEL) : {
					ModelFilterCriteria filter = new ModelFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.PACKAGE_NAME) : {
					PackageNameFilterCriteria filter = new PackageNameFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.PHONE_MODEL) : {
					PhoneModelFilterCriteria filter = new PhoneModelFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.PRODUCT) : {
					ProductFilterCriteria filter = new ProductFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.STACK_TRACE) : {
					StacktraceFilterCriteria filter = new StacktraceFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.TAGS) : {
					TagsFilterCriteria filter = new TagsFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.TYPE) : {
					TypeFilterCriteria filter = new TypeFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.USER) : {
					UserFilterCriteria filter = new UserFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.USER_COMMENTS) : {
					UserCommentsFilterCriteria filter = new UserCommentsFilterCriteria(value);
					filters.addFilterCriteria(filter);
				}; break;
				case (ColNamesAssoc.USER_CRASH_DATE) : {
					UserCrashDateFilterCriteria filter = new UserCrashDateFilterCriteria(value);
					filters.addFilterCriteria(filter);
				} break;
			}
		}

		filters.filter();
		filtersOn = true;
	}

	public void getSpreadsheetContent() throws Exception {
		Thread t = new Thread() {
			public void run() {
				logUpdated = false;
				logsCount = 0;
				try {
					logsWorksheet = logSpreadsheet.getDefaultWorksheet();
					listFeed = logsWorksheet.getListFeedUrl();
					lists = client.getFeed(listFeed, ListFeed.class);

					int i=0;
					for (ListEntry entry : lists.getEntries()) {
						String current = "";
						Map<String,String> valueMap = new HashMap<String, String>();

						for (String tag : entry.getCustomElements().getTags()) {
							current = entry.getCustomElements().getValue(tag);
							valueMap.put(tag, current);
							//System.out.println("  <gsx:" + tag + ">" + entry.getCustomElements().getValue(tag) + "</gsx:" + tag + ">");
						}

						logsValues.put(Integer.valueOf(i), valueMap);
						i++;
					}

					ListEntry tagEntry = lists.getEntries().get(0);

					/// tags list has to be saved separately, to avoid multiple entries in
					/// the Vector<String> tagsList field
					for (String tag : tagEntry.getCustomElements().getTags())
						tagsList.add(tag);

					logsCount = logsValues.size();
				}
				catch(Exception e) {
					guiRef.writeSysLog("E","Error in retrieving spreadsheet data.");
					e.printStackTrace();
					logsCount = -1;
					//TODO gestisci meglio l'eccezione: dialog di errore e POI uscita forzata
				}
				
				synchronized(lock) {
					logUpdated = true;
					lock.notifyAll();
				}
			}
		};

		t.start();
	}

	public static void main(String[] args) {
		LogTracker logInstance = new LogTracker();
		logInstance.init();
	}

	public int getLogsCount() {
		return logsCount;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLogSpreadsheet(int index) {
		this.logSpreadsheet = spreadsheetsList[index];
	}

	public String[] getSpreadsheetsTitles() {
		return spreadsheetsTitles;
	}

	public Map<Integer, Map<String,String>> getLogValues() {
		return logsValues;
	}

	public boolean hasUpdatedLog() {
		return logUpdated;
	}

	public Vector<String> getTagsList() {
		return tagsList;
	}

	public void setFiltersOn(boolean filtersOn) {
		this.filtersOn = filtersOn;
	}

	public void setFilteredLogsCount(int filteredLogsCount) {
		this.filteredLogsCount = filteredLogsCount;
	}

	public void setFilteredLogsValues(
			Map<Integer, Map<String, String>> filteredLogsValues) {
		this.filteredLogsValues = filteredLogsValues;
	}

	public boolean isFilteringOn() {
		return filtersOn;
	}

	public int getFilteredLogsCount() {
		return filteredLogsCount;
	}

	public Map<Integer, Map<String, String>> getFilteredLogsValues() {
		return filteredLogsValues;
	}

}