import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.GroupLayout.Alignment;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class LogTracker_GUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3647632949741290766L;
	private LogTracker clientRef;
	private LogTracker_GUI autoRef;

	private JTextPane appLogTextPane;
	private JTextPane sysLogTextPane;

	private JLabel userLabel;
	private JLabel passLabel;
	private JLabel spreadsheetLabel;

	private JFrame loginFrame;
	private JFrame filtersFrame;

	private JPanel buttonsComponent;
	private JPanel loginComponent;

	private JPanel logStats;
	private JPanel centerPanel;
	private JPanel buttonsPanel;

	private JPanel filtersSelectionPanel;
	private JPanel filtersSummaryPanel;
	private JPanel filtersSelectionContainer;
	private JPanel filtersButtonsPanel;

	private JLabel logCountLabel;
	private JLabel logCountValue;

	private JTextField userField;
	private JPasswordField passField;
	private JComboBox spreadsheetField;

	private JButton loginButton;

	private JButton refreshButton;
	private JButton filtersButton;
	private JButton sysLogButton;

	private JButton addFilter;
	private JButton removeFilters;
	private JButton applyFilters;
	private JButton disposeFiltersFrame;

	private JButton resetField;
	private JButton resetOperation;

	private JDialog progressDialog;
	private JProgressBar progressBar;

	private JList logList;
	private DefaultListModel logListModel;

	private JFrame spreadsheetSelectionFrame;
	private JButton changeSpreadsheet;
	private JComboBox spreadsheetBox;
	private JButton doChangeSpreadsheet;

	private JTable filtersTable;
	private DefaultTableModel filtersTableModel;
	private Vector<Vector<String>> filtersAdded; 

	private AlwaysSelectableComboBox filtersFieldSelection;
	private AlwaysSelectableComboBox filtersOperationSelection;
	private AlwaysSelectableComboBox filtersValueSelection;

	private JScrollPane logListScrollPane;
	private JScrollPane logTextScrollPane;
	private JScrollPane sysLogScrollPane;
	private JScrollPane filtersTableScrollPane;

	private SimpleAttributeSet message;
	private SimpleAttributeSet error;
	private SimpleAttributeSet logKeyword;
	private SimpleAttributeSet stacktrace;

	public LogTracker_GUI(LogTracker client) {
		clientRef = client;
		autoRef = this;

		setCustomLookAndFeel();

		appLogTextPane = new JTextPane();
		appLogTextPane.setEditable(false);
		appLogTextPane.setOpaque(false);

		sysLogTextPane = new JTextPane();
		sysLogTextPane.setEditable(false);

		logListModel = new DefaultListModel();
		logListModel.add(0, "empty log list");
		logList = new JList(logListModel);
		logList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		logList.addListSelectionListener(new LogListSelectionListener());

		logListScrollPane = new JScrollPane(logList);
		logListScrollPane.setBorder(BorderFactory.createTitledBorder("Log list"));

		logTextScrollPane = new JScrollPane(appLogTextPane);
		logTextScrollPane.setBorder(BorderFactory.createTitledBorder("Log"));

		logStats = new JPanel();
		logStats.setBorder(BorderFactory.createTitledBorder("Stats"));
		logCountLabel = new JLabel("Log count");
		logCountValue = new JLabel(Integer.toString(clientRef.getLogsCount()));
		logCountValue.setForeground(Color.RED);
		logStats.add(logCountLabel);
		logStats.add(logCountValue);

		refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clientRef.setFiltersOn(false);
				fetchSpreadsheetContent();
			}
		});

		filtersButton = new JButton("Filters");
		filtersButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createFilterFrame();
			}
		});
		sysLogButton = new JButton("Log");

		changeSpreadsheet = new JButton("Change spreadsheet");
		changeSpreadsheet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createSpreadsheetSelectorFrame();
			}
		});

		buttonsPanel = new JPanel();
		buttonsPanel.add(refreshButton);
		buttonsPanel.add(filtersButton);
		buttonsPanel.add(sysLogButton);
		buttonsPanel.add(changeSpreadsheet);

		centerPanel = new JPanel();

		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(buttonsPanel,BorderLayout.NORTH);
		centerPanel.add(logTextScrollPane, BorderLayout.CENTER);
		centerPanel.add(logStats, BorderLayout.SOUTH);

		setLayout(new BorderLayout());

		add(logListScrollPane,BorderLayout.WEST);
		add(centerPanel,BorderLayout.CENTER);

		setTitle("LogTracker - Main window");

		setExtendedState(Frame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setMinimumSize(new Dimension(800, 480));
		setVisible(false);

		setupLogTextArea();
		createLoginFrame();
	}

	public void createSpreadsheetSelectorFrame() {
		doChangeSpreadsheet = new JButton("Change");
		doChangeSpreadsheet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = spreadsheetBox.getSelectedIndex();
				if(index != -1) {
					spreadsheetSelectionFrame.dispose();

					clientRef.setLogSpreadsheet(index);
					fetchSpreadsheetContent();
				}
			}
		});

		spreadsheetSelectionFrame = new JFrame("Change spreadsheet");
		spreadsheetSelectionFrame.getContentPane().setLayout(new BoxLayout(spreadsheetSelectionFrame.getContentPane(), BoxLayout.X_AXIS));

		spreadsheetBox = new JComboBox(clientRef.getSpreadsheetsTitles());
		spreadsheetSelectionFrame.add(spreadsheetBox);
		spreadsheetSelectionFrame.add(doChangeSpreadsheet);

		spreadsheetSelectionFrame.setAlwaysOnTop(true);
		spreadsheetSelectionFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		spreadsheetSelectionFrame.pack();
		spreadsheetSelectionFrame.setLocationRelativeTo(null);
		spreadsheetSelectionFrame.setVisible(true);
	}

	class LogListSelectionListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent evt) {
			int sel = ((JList) evt.getSource()).getSelectedIndex();

			int index = -1;
			Map<String, String> selectedLog = null;
			if(clientRef.isFilteringOn()) {
				index = clientRef.getFilteredLogsCount()-sel-1;
				selectedLog = clientRef.getFilteredLogsValues().get(Integer.valueOf(index));
			}
			else {
				index = clientRef.getLogsCount()-sel-1;
				selectedLog = clientRef.getLogValues().get(Integer.valueOf(index));
			}

			try {
				appLogTextPane.getStyledDocument().remove(0, appLogTextPane.getStyledDocument().getLength());
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			writeAppLog("K", "LOG_ID: ");
			writeAppLog("O", String.valueOf(index+1)+"\n");

			writeAppLog("K",clientRef.getTagsList().get(ColNamesAssoc.DATE_TIME)+": ");
			writeAppLog("O",selectedLog.get(clientRef.getTagsList().get(ColNamesAssoc.DATE_TIME))+"\n");

			writeAppLog("K",clientRef.getTagsList().get(ColNamesAssoc.APP_VERSION)+": ");
			writeAppLog("O",selectedLog.get(clientRef.getTagsList().get(ColNamesAssoc.APP_VERSION))+"\n");

			writeAppLog("K",clientRef.getTagsList().get(ColNamesAssoc.PHONE_MODEL)+": ");
			writeAppLog("O",selectedLog.get(clientRef.getTagsList().get(ColNamesAssoc.PHONE_MODEL))+"\n");

			writeAppLog("K",clientRef.getTagsList().get(ColNamesAssoc.ANDROID_VERSION)+": ");
			writeAppLog("O",selectedLog.get(clientRef.getTagsList().get(ColNamesAssoc.ANDROID_VERSION))+"\n");

			writeAppLog("K",clientRef.getTagsList().get(ColNamesAssoc.BOARD)+": ");
			writeAppLog("O",selectedLog.get(clientRef.getTagsList().get(ColNamesAssoc.BOARD))+"\n");

			writeAppLog("K",clientRef.getTagsList().get(ColNamesAssoc.PACKAGE_NAME)+": ");
			writeAppLog("O",selectedLog.get(clientRef.getTagsList().get(ColNamesAssoc.PACKAGE_NAME))+"\n");

			writeAppLog("K",clientRef.getTagsList().get(ColNamesAssoc.FINGERPRINT)+": ");
			writeAppLog("O",selectedLog.get(clientRef.getTagsList().get(ColNamesAssoc.FINGERPRINT))+"\n");

			writeAppLog("K",clientRef.getTagsList().get(ColNamesAssoc.STACK_TRACE)+": ");
			writeAppLog("S",selectedLog.get(clientRef.getTagsList().get(ColNamesAssoc.STACK_TRACE))+"\n");

			writeAppLog("K",clientRef.getTagsList().get(ColNamesAssoc.USER_COMMENTS)+": ");
			writeAppLog("O",selectedLog.get(clientRef.getTagsList().get(ColNamesAssoc.USER_COMMENTS))+"\n");

			writeAppLog("K",clientRef.getTagsList().get(ColNamesAssoc.USER_CRASH_DATE)+": ");
			writeAppLog("O",selectedLog.get(clientRef.getTagsList().get(ColNamesAssoc.USER_CRASH_DATE))+"\n");
		}
	}

	public void createLoginFrame() {
		autoRef.setEnabled(false);

		userLabel = new JLabel("Username");
		passLabel = new JLabel("Password");
		spreadsheetLabel = new JLabel("Log spreadsheet");

		userField = new JTextField("Insert username",30);
		userField.selectAll();

		passField = new JPasswordField("Insert password",30);
		passField.selectAll();

		spreadsheetField = new JComboBox();
		spreadsheetField.setModel(new DefaultComboBoxModel(new String[]{("")}));
		spreadsheetField.setEnabled(false);

		loginComponent = new JPanel();

		GroupLayout myDataLayout = new GroupLayout(loginComponent);
		loginComponent.setLayout(myDataLayout);
		myDataLayout.setAutoCreateGaps(true);

		GroupLayout.SequentialGroup hGroup = myDataLayout.createSequentialGroup();
		GroupLayout.ParallelGroup pGroup1 = myDataLayout.createParallelGroup();
		GroupLayout.ParallelGroup pGroup2 = myDataLayout.createParallelGroup();

		pGroup1.addComponent(userLabel).addComponent(passLabel)
		.addComponent(spreadsheetLabel);

		pGroup2.addComponent(userField).addComponent(passField)
		.addComponent(spreadsheetField);

		hGroup.addGroup(pGroup1);
		hGroup.addGroup(pGroup2);

		myDataLayout.setHorizontalGroup(hGroup);
		GroupLayout.SequentialGroup vGroup = myDataLayout.createSequentialGroup();
		vGroup.addGroup(myDataLayout.createParallelGroup(Alignment.BASELINE)
				.addComponent(userLabel).addComponent(userField));
		vGroup.addGroup(myDataLayout.createParallelGroup(Alignment.BASELINE)
				.addComponent(passLabel).addComponent(passField));
		vGroup.addGroup(myDataLayout.createParallelGroup(Alignment.BASELINE)
				.addComponent(spreadsheetLabel).addComponent(spreadsheetField));

		myDataLayout.setVerticalGroup(vGroup);

		loginButton = new JButton("Login");
		loginButton.addActionListener(new LoginListener());

		buttonsComponent = new JPanel();
		buttonsComponent.add(loginButton);

		loginFrame = new JFrame("LogTracker - Login");
		loginFrame.setLayout(new BorderLayout());
		loginFrame.setAlwaysOnTop(true);
		loginFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		loginFrame.add(loginComponent,BorderLayout.NORTH);
		loginFrame.add(buttonsComponent,BorderLayout.SOUTH);
		loginFrame.pack();
		loginFrame.setLocationRelativeTo(null);
		loginFrame.setVisible(true);
	}

	class LoginListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent evt) {
			if(userField.getText().equals("") || passField.getPassword().equals(""))
				JOptionPane.showMessageDialog(loginFrame, "You need to set both username and password fields");
			else {
				try {
					clientRef.login(userField.getText(),String.copyValueOf(passField.getPassword()));
				} catch (AuthenticationException e) {
					JOptionPane.showMessageDialog(loginFrame, "Invalid username or password. Please retry!");
					e.printStackTrace();
				}
				if(clientRef.isLoggedIn()) {
					clientRef.updateSpreadsheetsList();
					spreadsheetField.setModel(new DefaultComboBoxModel(clientRef.getSpreadsheetsTitles()));
					spreadsheetField.setEnabled(true);

					loginButton.setText("Confirm spreadsheet");
					loginButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							int index = spreadsheetField.getSelectedIndex();
							if(index != -1) {
								loginFrame.dispose();

								clientRef.setLogSpreadsheet(index);
								fetchSpreadsheetContent();
								autoRef.setDefaultCloseOperation(EXIT_ON_CLOSE);
								autoRef.setVisible(true);
							}
						}
					});
				} //if
			} //else
		}
	}

	public void fetchSpreadsheetContent() {
		Thread t = new Thread() {
			public void run() {
				try {
					clientRef.getSpreadsheetContent();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(autoRef, "An error has occurred with the selected document.\n Please, try again later.");
				}

				createProgressFrame("Loading worksheet contents..");

				synchronized (clientRef.lock) {
					while(clientRef.hasUpdatedLog() == false) {
						try {
							clientRef.lock.wait();
						} catch (InterruptedException e1) {
							writeSysLog("E", "Error while acquiring lock on client");
							e1.printStackTrace();
						}
					}

					updateLogList();
					progressBar.setIndeterminate(false);
					progressDialog.dispose();
					setEnabled(true);
				}
			}
		};
		t.start();
	}

	public void createProgressFrame(final String msg) {
		Thread t = new Thread() {
			public void run() {
				progressDialog = new JDialog(autoRef, msg, true);
				progressBar = new JProgressBar();
				progressBar.setIndeterminate(true);
				progressDialog.add(BorderLayout.CENTER, progressBar);
				progressDialog.add(BorderLayout.NORTH, new JLabel("LOADING ..."));
				progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
				progressDialog.setSize(300, 75);
				progressDialog.setLocationRelativeTo(autoRef);
				progressDialog.setVisible(true);
			}
		};
		t.start();
	}

	public void createFilterFrame() {
		setEnabled(false);

		filtersFieldSelection = new AlwaysSelectableComboBox(clientRef.getTagsList());
		filtersFieldSelection.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				filtersOperationSelection.setSelectedIndex(0);
			}
		});

		final Vector<String> ops = new Vector<String>();
		ops.add("greater than");
		ops.add("lesser than");
		ops.add("greater or equal");
		ops.add("lesser or equal");
		ops.add("equal");

		filtersOperationSelection = new AlwaysSelectableComboBox(ops);
		filtersOperationSelection.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				addFiltersValues();
			}
		});

		filtersValueSelection = new AlwaysSelectableComboBox();

		addFilter = new JButton("Add filter");
		addFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int field = filtersFieldSelection.getSelectedIndex();
				int operation = filtersOperationSelection.getSelectedIndex();
				String value = (String) filtersValueSelection.getSelectedItem();

				Vector<String> row = new Vector<String>();
				row.add(clientRef.getTagsList().get(field));
				row.add(ops.get(operation));
				row.add(value);

				Vector<String> rawRow = new Vector<String>();
				rawRow.add(String.valueOf(field));
				rawRow.add(String.valueOf(operation));
				rawRow.add(value);

				filtersAdded.add(rawRow);
				filtersTableModel.addRow(row);
				filtersTable.setModel(filtersTableModel);
			}
		});
		addFiltersValues();

		removeFilters = new JButton("Remove filters");
		removeFilters.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clientRef.setFiltersOn(false);
				filtersTable.setModel(new DefaultTableModel());
				updateLogList();
			}
		});
		applyFilters = new JButton("Apply filters");
		applyFilters.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(filtersAdded.size() == 0)
					JOptionPane.showMessageDialog(autoRef, "You must add at least a filter!");

				clientRef.filter(filtersAdded);
				updateLogList();
				filtersFrame.dispose();
				setEnabled(true);
			}
		});

		disposeFiltersFrame = new JButton("Cancel");
		disposeFiltersFrame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setEnabled(true);
				filtersFrame.dispose();
			}
		});

		filtersButtonsPanel = new JPanel();
		//filtersButtonsPanel.setLayout(new BoxLayout(filtersButtonsPanel, BoxLayout.X_AXIS));
		filtersButtonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		filtersButtonsPanel.add(addFilter);
		filtersButtonsPanel.add(removeFilters);
		filtersButtonsPanel.add(applyFilters);
		filtersButtonsPanel.add(disposeFiltersFrame);

		resetField = new JButton("Reset field");
		resetField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filtersOperationSelection.setEnabled(false);
				filtersValueSelection.setEnabled(false);
				filtersValueSelection.setModel(new DefaultComboBoxModel());
			}
		});
		resetOperation = new JButton("Reset operation");
		resetOperation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filtersValueSelection.setEnabled(false);
				addFilter.setEnabled(false);
				filtersValueSelection.setModel(new DefaultComboBoxModel());
			}
		});

		filtersSelectionPanel = new JPanel();
		filtersSelectionPanel.setLayout(new BoxLayout(filtersSelectionPanel, BoxLayout.Y_AXIS));
		filtersSelectionPanel.add(filtersFieldSelection);
		filtersSelectionPanel.add(filtersOperationSelection);
		filtersSelectionPanel.add(filtersValueSelection);

		filtersSelectionContainer = new JPanel();
		filtersSelectionContainer.setLayout(new BorderLayout());
		filtersSelectionContainer.add(filtersSelectionPanel,BorderLayout.NORTH);
		filtersSelectionContainer.add(filtersButtonsPanel,BorderLayout.CENTER);

		filtersTable = new JTable();
		filtersTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		filtersTable.setDragEnabled(false);
		filtersTable.setPreferredScrollableViewportSize(new Dimension(600, 150));
		filtersTable.setFillsViewportHeight(true);

		if (clientRef.isFilteringOn())
			filtersTable.setModel(filtersTableModel);

		filtersAdded = new Vector<Vector<String>>();
		filtersTableModel = new DefaultTableModel(1,4);

		filtersTableScrollPane = new JScrollPane(filtersTable);
		filtersTableScrollPane.setBorder(BorderFactory.createTitledBorder("Active filters"));

		filtersSummaryPanel = new JPanel();
		filtersSummaryPanel.add(filtersTableScrollPane);

		filtersFrame = new JFrame("LogTracker - Apply Filters");
		filtersFrame.setResizable(false);
		filtersFrame.setSize(new Dimension(750,400));

		filtersFrame.setLayout(new BorderLayout());
		filtersFrame.setLocationRelativeTo(null);
		//		filtersFrame.setAlwaysOnTop(true);

		filtersFrame.add(filtersSelectionContainer,BorderLayout.CENTER);
		filtersFrame.add(filtersSummaryPanel,BorderLayout.SOUTH);

		filtersFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		filtersFrame.setVisible(true);
	}

	public void addFiltersValues() {
		int sel = filtersFieldSelection.getSelectedIndex();
		Map<Integer,Map<String,String>> original = clientRef.getLogValues();
		Vector<String> tempFilterValues = new Vector<String>();
		for (int i=0; i<original.size(); i++) {
			Map<String,String> s = original.get(Integer.valueOf(i));
			String currentValue = s.get(clientRef.getTagsList().get(sel));
			if(!tempFilterValues.contains(currentValue))
				tempFilterValues.add(currentValue);

			filtersValueSelection.setModel(new DefaultComboBoxModel(tempFilterValues));
			filtersValueSelection.setEnabled(true);
			addFilter.setEnabled(true);
		}
	}

	public void setupLogTextArea() {
		message = new SimpleAttributeSet();
		StyleConstants.setForeground(message, Color.GREEN);
		StyleConstants.setBold(message, true);

		error = new SimpleAttributeSet();
		StyleConstants.setForeground(error, Color.DARK_GRAY);
		StyleConstants.setItalic(error, true);

		logKeyword = new SimpleAttributeSet();
		StyleConstants.setForeground(logKeyword, Color.BLUE);
		StyleConstants.setBold(logKeyword, true);

		stacktrace = new SimpleAttributeSet();
		StyleConstants.setForeground(stacktrace, Color.RED);
		StyleConstants.setItalic(stacktrace, true);
	}

	public void updateLogList() {
		logListModel = (DefaultListModel) logList.getModel();
		logListModel.clear();

		String[] listData;
		Map<Integer, Map<String, String>> logs;
		int logsCount;

		if(clientRef.isFilteringOn()) {
			logs = clientRef.getFilteredLogsValues();
			logsCount = clientRef.getFilteredLogsCount();
		}
		else {
			logs = clientRef.getLogValues();
			logsCount = clientRef.getLogsCount();
		}

		listData = new String[logsCount];

		for (int i=0; i<logsCount; i++) {
			Map<String,String> current = logs.get(Integer.valueOf(i));
			Vector<String> tagList = clientRef.getTagsList();
			listData[logsCount-i-1] = current.get(tagList.get(ColNamesAssoc.USER_CRASH_DATE));
		}

		logListModel = new DefaultListModel();

		for (int i=0; i<listData.length; i++)
			logListModel.addElement(listData[i]);

		logList.setModel(logListModel);
		logCountValue.setText(Integer.toString(logsCount));
	}

	/**
	 * Since Java standard LookAndFeel sucks bad, this method tries to set Nimbus
	 * as the default UIManager for the LogTracker application.
	 */
	public void setCustomLookAndFeel() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void writeSysLog(String type, String log) {
		StyledDocument doc = sysLogTextPane.getStyledDocument();
		try {
			if(type.equals("E"))
				doc.insertString(0, log, error);
			else
				doc.insertString(0, log, message);
		} catch(BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void writeAppLog(String type, String log) {
		StyledDocument doc = appLogTextPane.getStyledDocument();
		appLogTextPane.setOpaque(false);
		appLogTextPane.setBackground(new Color(0,0,0,0));

		try {
			if(type.equals("S"))
				doc.insertString(doc.getLength(), log, stacktrace);
			else if(type.equals("K"))
				doc.insertString(doc.getLength(), log.toUpperCase(), logKeyword);
			else
				doc.insertString(doc.getLength(), log, null);

		} catch(BadLocationException e) {
			e.printStackTrace();
		}
	}
}

//TODO spostare i metodi di business logic.. nella business logic -.-"



/*********************************************************************/
/**								OLD									**/
/*********************************************************************/

//private JButton selectField;
//private JButton selectOperation;
//private JButton selectValue;

//selectField = new JButton("Set field");
//selectField.addActionListener(new ActionListener() {
//	@Override
//	public void actionPerformed(ActionEvent arg0) {
//		filtersOperationSelection.setEnabled(true);
//	}
//});

//selectOperation = new JButton("Set operation");
//selectOperation.addActionListener(new ActionListener() {
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		int sel = filtersFieldSelection.getSelectedIndex();
//		Map<Integer,Map<String,String>> original = clientRef.getLogValues();
//		Vector<String> tempFilterValues = new Vector<String>();
//		for (int i=0; i<original.size(); i++) {
//			Map<String,String> s = original.get(Integer.valueOf(i));
//			String currentValue = s.get(clientRef.getTagsList().get(sel));
//			if(!tempFilterValues.contains(currentValue))
//				tempFilterValues.add(currentValue);
//			
//			filtersValueSelection.setModel(new DefaultComboBoxModel(tempFilterValues));
//			filtersValueSelection.setEnabled(true);
//		}
//	}
//});

//selectValue = new JButton("Set value");
//selectValue.addActionListener(new ActionListener() {
//	@Override
//	public void actionPerformed(ActionEvent arg0) {
//		addFilter.setEnabled(true);
//		removeFilter.setEnabled(false);
//	}
//});



// OLD FILTER LAYOUT

//filtersSelectionPanel = new JPanel();
//GroupLayout myFiltersSelectionLayout = new GroupLayout(filtersSelectionPanel);
//filtersSelectionPanel.setLayout(myFiltersSelectionLayout);
//myFiltersSelectionLayout.setAutoCreateGaps(true);
//
//GroupLayout.SequentialGroup hGroup = myFiltersSelectionLayout.createSequentialGroup();
//GroupLayout.ParallelGroup pGroup1 = myFiltersSelectionLayout.createParallelGroup();
//GroupLayout.ParallelGroup pGroup2 = myFiltersSelectionLayout.createParallelGroup();
//
//pGroup1.addComponent(filtersFieldSelection).addComponent(filtersOperationSelection)
//.addComponent(filtersValueSelection);
//
//pGroup2.addComponent(resetField).addComponent(resetOperation)
//.addComponent(addFilter);
//
//hGroup.addGroup(pGroup1);
//hGroup.addGroup(pGroup2);
//
//myFiltersSelectionLayout.setHorizontalGroup(hGroup);
//GroupLayout.SequentialGroup vGroup = myFiltersSelectionLayout.createSequentialGroup();
//vGroup.addGroup(myFiltersSelectionLayout.createParallelGroup(Alignment.BASELINE)
//		.addComponent(filtersFieldSelection).addComponent(resetField));
//vGroup.addGroup(myFiltersSelectionLayout.createParallelGroup(Alignment.BASELINE)
//		.addComponent(filtersOperationSelection).addComponent(resetOperation));
//vGroup.addGroup(myFiltersSelectionLayout.createParallelGroup(Alignment.BASELINE)
//		.addComponent(filtersValueSelection).addComponent(addFilter));
//
//myFiltersSelectionLayout.setVerticalGroup(vGroup);


