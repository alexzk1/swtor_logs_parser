package de.swtor.combatlog.gui;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import de.swtor.combatlog.StartCombatLog;
import de.swtor.combatlog.analyzer.MasterAnalyzer;
import de.swtor.combatlog.configuration.Configuration;
import de.swtor.combatlog.data.AbstractValuedData;
import de.swtor.combatlog.data.DataStore;
import de.swtor.combatlog.data.HumanPlayerCharacter;
import de.swtor.combatlog.gui.DetailedController.DataType;
import de.swtor.combatlog.icons.Icons;
import de.swtor.combatlog.localization.Localization;
import de.swtor.combatlog.parser.CombatLogParser;

public class MainController extends AbstractGuiController {

	private JFrame mainWindow;
	private InfoPanel infoPanel = new InfoPanel();
	private JProgressBar progressBar;
	private GlassPane glassPane;
	private OverviewController activeController;
	private CombatLogParser combatLogParser = new CombatLogParser();
	private JMenu recentFilesMenu;
	private JPopupMenu recentFilesPopMenu = new JPopupMenu();
	
	private MasterAnalyzer masterAnalyzer = new MasterAnalyzer();

	private static MainController singleton;

	public MainController() {
		if (singleton != null) {
			throw new IllegalStateException("Es kann nur einen geben!");
		}

		singleton = this;
	}

	/**
	 * @return the singleton
	 */
	public static MainController getSingleton() {
		return singleton;
	}

	@Override
	public void updateActions() {
		Vector<String> items = new Vector<String>();

		items.add(MAIN_CONTROLLER_OPEN_FILE);
		items.add(MAIN_CONTROLLER_RECENTFILE_BUTTON);
		items.add(MAIN_CONTROLLER_INFO);
		items.add(MAIN_CONTROLLER_EXIT);

		// Recent file menu
		for (int i = 0; i < Configuration.NUMBER_RECENT_FILES; i++) {
			items.add(MAIN_CONTROLLER_RECENTFILE + i);
		}

		if (null != activeController && activeController.isSelected()) {
			HumanPlayerCharacter player = activeController.getSelectedPlayer();
			
			if (null != player) {
				if (null != player.getDamageDealed() && player.getDamageDealed().size() > 0) {
					items.add(OVERVIEW_CONTROLLER_SHOW_DAMAGE_DONE);
				}
				if (null != player.getDamageReceived() && player.getDamageReceived().size() > 0) {
					items.add(OVERVIEW_CONTROLLER_SHOW_DAMAGE_RECEIVED);
				}
				if (null != player.getHealDealed() && player.getHealDealed().size() > 0) {
					items.add(OVERVIEW_CONTROLLER_SHOW_HEALING_DONE);
				}
				if (null != player.getHealReceived() && player.getHealReceived().size() > 0) {
					items.add(OVERVIEW_CONTROLLER_SHOW_HEALING_RECEIVED);
				}
			}
		}
		
		updateWidgets(items, true);
	}

	@Override
	public void startController() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mainWindow = new MainWindow();

				JPanel mainPanel = new JPanel(new BorderLayout());
				mainPanel.add(createToolbar(), BorderLayout.NORTH);
				mainPanel.add(createStatusBar(), BorderLayout.SOUTH);
				setView(mainPanel);

				mainWindow.getContentPane().add(mainPanel);
				mainWindow.setJMenuBar(createMenuBar());

				glassPane = new GlassPane(mainWindow.getJMenuBar());
				glassPane.setOpaque(false);
				mainWindow.setGlassPane(glassPane);

				mainWindow.setIconImage(Icons.WINDOW_ICON.getImage());

				updateActions();

				mainWindow.pack();

				mainWindow.setVisible(true);

				new SplashScreen(mainWindow);
			}
		});
	}

	@Override
	public void performAction(String actionCommand) {
		if (actionCommand.equalsIgnoreCase(MAIN_CONTROLLER_OPEN_FILE)) {
			startProgress();

			File combatLogFile = openCombatLog();

			openCombatlogFile(combatLogFile);

			stopProgress();
		} else if (actionCommand.equalsIgnoreCase(MAIN_CONTROLLER_INFO)) {
			startProgress();
			JOptionPane.showMessageDialog(KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
					infoPanel, Localization.getInstance().tr(Localization.MESSAGE_INFO_TITLE),
					JOptionPane.PLAIN_MESSAGE);
			stopProgress();
		} else if (actionCommand.equalsIgnoreCase(MAIN_CONTROLLER_EXIT)) {
			Configuration.getInstance().saveProperties();

			System.exit(0);
		} else if (actionCommand.equalsIgnoreCase(MAIN_CONTROLLER_RECENTFILE_BUTTON)) {
			PointerInfo info = MouseInfo.getPointerInfo();;		    
		    Point location = info.getLocation();
		    Point windowLocation = getView().getLocationOnScreen();
		    recentFilesPopMenu.show(getView(), location.x - windowLocation.x, location.y - windowLocation.y);
		} else if (actionCommand.startsWith(MAIN_CONTROLLER_RECENTFILE)) {
			int recentFileNumber = Integer.valueOf(actionCommand.substring(actionCommand.length() - 1));

			String fileName = Configuration.getInstance().getRecentFiles().get(recentFileNumber);

			startProgress();

			File combatLogFile = new File(fileName);

			openCombatlogFile(combatLogFile);

			stopProgress();
		}  else if (actionCommand.equalsIgnoreCase(OVERVIEW_CONTROLLER_SHOW_DAMAGE_DONE)) {
			List<AbstractValuedData> detailedData = activeController.getSelectedDamageDone();
			String dataName = activeController.getSelectedDataName();
			
			if (null != detailedData) {
				DetailedController controller = new DetailedController(detailedData, DataType.DAMAGE_DONE, dataName);
				controller.startController();
			}
		}  else if (actionCommand.equalsIgnoreCase(OVERVIEW_CONTROLLER_SHOW_DAMAGE_RECEIVED)) {
			List<AbstractValuedData> detailedData = activeController.getSelectedDamageReceived();
			String dataName = activeController.getSelectedDataName();
			
			if (null != detailedData) {
				DetailedController controller = new DetailedController(detailedData, DataType.DAMAGE_RECEIVED, dataName);
				controller.startController();
			}
		}  else if (actionCommand.equalsIgnoreCase(OVERVIEW_CONTROLLER_SHOW_HEALING_DONE)) {
			List<AbstractValuedData> detailedData = activeController.getSelectedHealingDone();
			String dataName = activeController.getSelectedDataName();
			
			if (null != detailedData) {
				DetailedController controller = new DetailedController(detailedData, DataType.HEAL_DONE, dataName);
				controller.startController();
			}
		}  else if (actionCommand.equalsIgnoreCase(OVERVIEW_CONTROLLER_SHOW_HEALING_RECEIVED)) {
			List<AbstractValuedData> detailedData = activeController.getSelectedHealingReceived();
			String dataName = activeController.getSelectedDataName();
			
			if (null != detailedData) {
				DetailedController controller = new DetailedController(detailedData, DataType.HEAL_RECEIVED, dataName);
				controller.startController();
			}
		} 
	}

	private void openCombatlogFile(File combatLogFile) {
		if (null != combatLogFile) {
			if (null != activeController) {
				activeController.stopController();
				activeController = null;
				mainWindow.setTitle("");
			}

			try {
				DataStore.resetData();

				StartCombatLog.getLogger().info("Parse combat log: " + combatLogFile.getName());

				combatLogParser.parse(combatLogFile);

				Configuration.getInstance().addRecentFiles(combatLogFile.getPath());
				updateRecentFilesMenu();

				String playerName = selectPlayerName();

				if (null != playerName) {
					DataStore.mainHumanPlayerName = playerName;

					StartCombatLog.getLogger().info("Choose player name: " + playerName);

					mainWindow.setTitle(playerName + " ("+ combatLogFile.getName() +")");
					
					masterAnalyzer.calculateAllResults(playerName);
					
					activeController = new OverviewController(this);
					activeController.startController();
				} else {
					StartCombatLog.getLogger().info("No data to analyze found in file " + combatLogFile.getName());
				}
			} catch (Exception e) {
				Configuration.getInstance().removeRecentFiles(combatLogFile.getPath());
				updateRecentFilesMenu();
				StartCombatLog.getLogger().log(Level.SEVERE, "Error during parsing", e);
				JOptionPane.showMessageDialog(mainWindow, e.getMessage(),
						Localization.getInstance().tr(Localization.MESSAGE_ERROR_PARSING), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private String selectPlayerName() {
		String selectedUser = null;

		while (null == selectedUser) {
			String mainPlayerName = null;
			List<String> playerNameList = new ArrayList<String>();

			for (int i = 0; i < DataStore.humanPlayerCharacters.size(); i++) {
				if ((DataStore.humanPlayerCharacters.get(i).getDamageDealed().size() > 0)
						|| (DataStore.humanPlayerCharacters.get(i).getHealDealed().size() > 0)) {
					String playerName = DataStore.humanPlayerCharacters.get(i).toString();
					
					playerNameList.add(playerName);
					
					// Select the player name and not a Companion
					if (playerName.startsWith(DataStore.ownerOfTheCurrentLogFile) &&
					    !playerName.startsWith(DataStore.ownerOfTheCurrentLogFile+":")) {
						mainPlayerName = playerName;
					}
				}
			}

			if (playerNameList.size() > 1) {
				Collections.sort(playerNameList);

				final JList list = new JList(playerNameList.toArray());
				list.setSelectedValue(mainPlayerName, true);
				final JLabel label = new JLabel(Localization.getInstance().tr(Localization.MESSAGE_SELECT_PLAYER));
				final String option = Localization.getInstance().tr(Localization.SELECT);

				final JOptionPane optionPane = new JOptionPane(new Object[] { label, new JScrollPane(list) },
						JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_OPTION, null, new Object[] {option}) {

					@Override
					public void selectInitialValue() {
						super.selectInitialValue();
						list.ensureIndexIsVisible(list.getSelectedIndex());
						list.requestFocus();
					}
					
					
				};
				final JDialog dialog = optionPane.createDialog(null,
						Localization.getInstance().tr(Localization.MESSAGE_SELECT_PLAYER_TITLE));
				dialog.setVisible(true);
				if (option ==  optionPane.getValue()) {
					selectedUser = (String) list.getSelectedValue();
				}
			} else if (playerNameList.size() == 1) {
				selectedUser = playerNameList.get(0);
			} else {
				JOptionPane.showMessageDialog(mainWindow, Localization.getInstance().tr(Localization.MESSAGE_NO_DATA),
						Localization.getInstance().tr(Localization.MESSAGE_NO_DATA_TITLE),
						JOptionPane.INFORMATION_MESSAGE);
				return null;
			}
		}

		int index = selectedUser.indexOf(" [");
		
		return selectedUser.substring(0, index);
	}

	public void startProgress() {
		mainWindow.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		progressBar.setString(Localization.getInstance().tr(Localization.MESSAGE_WORKING));
		progressBar.setIndeterminate(true);
		glassPane.setVisible(true);
	}

	public void stopProgress() {
		glassPane.setVisible(false);
		mainWindow.setCursor(Cursor.getDefaultCursor());
		progressBar.setString(Localization.getInstance().tr(Localization.MESSAGE_READY));
		progressBar.setIndeterminate(false);
		mainWindow.validate();
	}

	/**
	 * @return the mainWindow
	 */
	public JFrame getMainWindow() {
		return mainWindow;
	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu mainMenu = new JMenu(Localization.getInstance().tr(Localization.MENU_FILE));	
		mainMenu.setMnemonic(Localization.getInstance().tr(Localization.MENU_FILE).substring(0, 1).toCharArray()[0]);

		generateJMenuItem(mainMenu, Localization.getInstance().tr(Localization.MENU_OPEN), Icons.OPEN_FOLDER, 'O',
				MAIN_CONTROLLER_OPEN_FILE);

		recentFilesMenu = new JMenu(Localization.getInstance().tr(Localization.MENU_RECENT_FILES));
		recentFilesMenu.setIcon(Icons.BOOKMARK);
		mainMenu.add(recentFilesMenu);		
		updateRecentFilesMenu();

		generateJMenuItem(mainMenu, Localization.getInstance().tr(Localization.MENU_EXIT), Icons.COMPUTER_OFF, 'E',
				MAIN_CONTROLLER_EXIT);

		menuBar.add(mainMenu);		
		
		JMenu detailsMenu = new JMenu(Localization.getInstance().tr(Localization.MENU_DETAILS));		
		detailsMenu.setMnemonic('e');
		generateJMenuItem(detailsMenu, Localization.getInstance().tr(Localization.MENU_DAMAGE_DONE), Icons.DAMAGE_DONE, 'D',
				OVERVIEW_CONTROLLER_SHOW_DAMAGE_DONE);
		generateJMenuItem(detailsMenu, Localization.getInstance().tr(Localization.MENU_DAMAGE_RECEIVED), Icons.DAMAGE_RECEIVED, 'R',
				OVERVIEW_CONTROLLER_SHOW_DAMAGE_RECEIVED);
		generateJMenuItem(detailsMenu, Localization.getInstance().tr(Localization.MENU_HEALING_DONE), Icons.HEALING_DONE, 'H',
				OVERVIEW_CONTROLLER_SHOW_HEALING_DONE);
		generateJMenuItem(detailsMenu, Localization.getInstance().tr(Localization.MENU_HEALING_RECEIVED), Icons.HEALING_RECEIVED, 'E',
				OVERVIEW_CONTROLLER_SHOW_HEALING_RECEIVED);
		
		menuBar.add(detailsMenu);

		JMenu infoMenu = new JMenu("?");
		infoMenu.setMnemonic('?');
		generateJMenuItem(infoMenu, Localization.getInstance().tr(Localization.MENU_INFO), Icons.INFO, 'A',
				MAIN_CONTROLLER_INFO);

		menuBar.add(infoMenu);

		return menuBar;
	}

	/*
	 * create a menu item for a component (menubar or popup menu), put it in the
	 * corresponding hashtable for this component under the key for this
	 * item/action
	 */
	private void generateJMenuItem(JComponent parent, String label, ImageIcon icon, char mnemonic, String key) {
		JMenuItem item = new JMenuItem(label, icon);
		item.setMnemonic(mnemonic);

		setActionCommand(key, item);

		parent.add(item);
	}

	private JToolBar createToolbar() {
		JToolBar toolBar = new JToolBar();
		toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
		toolBar.add(createToolbarButton(MAIN_CONTROLLER_OPEN_FILE, Icons.OPEN_FOLDER,
				Localization.getInstance().tr(Localization.MENU_OPEN)));
		toolBar.add(createToolbarButton(MAIN_CONTROLLER_RECENTFILE_BUTTON, Icons.BOOKMARK,
				Localization.getInstance().tr(Localization.MENU_RECENT_FILES)));
		toolBar.addSeparator();
		toolBar.add(createToolbarButton(OVERVIEW_CONTROLLER_SHOW_DAMAGE_DONE, Icons.DAMAGE_DONE,
				Localization.getInstance().tr(Localization.MENU_DAMAGE_DONE)));
		toolBar.add(createToolbarButton(OVERVIEW_CONTROLLER_SHOW_DAMAGE_RECEIVED, Icons.DAMAGE_RECEIVED,
				Localization.getInstance().tr(Localization.MENU_DAMAGE_RECEIVED)));
		toolBar.add(createToolbarButton(OVERVIEW_CONTROLLER_SHOW_HEALING_DONE, Icons.HEALING_DONE,
				Localization.getInstance().tr(Localization.MENU_HEALING_DONE)));
		toolBar.add(createToolbarButton(OVERVIEW_CONTROLLER_SHOW_HEALING_RECEIVED, Icons.HEALING_RECEIVED,
				Localization.getInstance().tr(Localization.MENU_HEALING_RECEIVED)));
		
		toolBar.add(Box.createHorizontalGlue());
		toolBar.add(createToolbarButton(MAIN_CONTROLLER_INFO, Icons.INFO,
				Localization.getInstance().tr(Localization.MENU_INFO)));		
		toolBar.add(createToolbarButton(MAIN_CONTROLLER_EXIT, Icons.COMPUTER_OFF,
				Localization.getInstance().tr(Localization.MENU_EXIT)));

		return toolBar;
	}

	private JButton createToolbarButton(String actionCommand, ImageIcon icon, String tooltip) {
		JButton button = new JButton(icon);
		button.setRolloverEnabled(true);
		button.setFocusable(false);
		button.setToolTipText(tooltip);
		setActionCommand(actionCommand, button);

		return button;
	}

	private Component createStatusBar() {
		JPanel statusBar = new JPanel(new BorderLayout());
		progressBar = new JProgressBar();
		Dimension newDim = new Dimension(200, 20);
		progressBar.setPreferredSize(newDim);
		progressBar.setString(Localization.getInstance().tr(Localization.MESSAGE_READY));
		progressBar.setStringPainted(true);
		progressBar.setIndeterminate(false);
		statusBar.add(progressBar, BorderLayout.EAST);
		return statusBar;
	}

	private File openCombatLog() {
		File result = null;

		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setCurrentDirectory(new File(Configuration.getInstance().getLastDir()));

		int returnVal = chooser.showDialog(null, Localization.getInstance().tr(Localization.MESSAGE_CHOOSE_FILE));

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			result = chooser.getSelectedFile();

			if (null != result) {
				String fileName = result.getName();
				String pathName = result.getPath();
				int index = pathName.lastIndexOf(fileName);
				String pathWithoutName = pathName.substring(0, index - 1);
				Configuration.getInstance().setLastDir(pathWithoutName);
			}
		}

		return result;
	}

	private void updateRecentFilesMenu() {
		List<String> recentFilesList = Configuration.getInstance().getRecentFiles();

		recentFilesMenu.removeAll();
		recentFilesPopMenu.removeAll();

		for (int i = 0; i < recentFilesList.size(); i++) {
			JMenuItem newitem = recentFilesMenu.add(recentFilesList.get(i));
			setActionCommand(MAIN_CONTROLLER_RECENTFILE + i, newitem);
			
		    newitem = recentFilesPopMenu.add(recentFilesList.get(i));
			setActionCommand(MAIN_CONTROLLER_RECENTFILE + i, newitem);
		}
	}
}
