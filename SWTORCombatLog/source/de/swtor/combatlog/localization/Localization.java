package de.swtor.combatlog.localization;

import de.swtor.combatlog.StartCombatLog;

import java.util.ResourceBundle;
import java.util.logging.Level;

/*
 * Copyright (c) 2012 Thomas Rath
 */

public class Localization
{

    // General
    public static final String FIGHT = "fight";
    public static final String TARGETS = "targets";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String DPS = "dps";
    public static final String HPS = "hps";
    public static final String SECONDS = "seconds";
    public static final String SELECT = "select";
    public static final String CLOSE = "close";
    public static final String YES = "yes";
    public static final String NO = "no";
    public static final String FALLING_DAMAGE = "fallingDamage";

    // Menu
    public static final String MENU_FILE = "menuFile";
    public static final String MENU_OPEN = "menuOpen";
    public static final String MENU_INFO = "menuInfo";
    public static final String MENU_EXIT = "menuExit";
    public static final String MENU_DETAILS = "menuDetails";
    public static final String MENU_RECENT_FILES = "menuRecentFiles";
    public static final String MENU_DAMAGE_DONE = "menuDamageDone";
    public static final String MENU_DAMAGE_RECEIVED = "menuDamageReceived";
    public static final String MENU_HEALING_DONE = "menuHealingDone";
    public static final String MENU_HEALING_RECEIVED = "menuHealingReceived";

    // Messages
    public static final String MESSAGE_CHOOSE_FILE = "messagChooseFile";
    public static final String MESSAGE_READY = "messageReady";
    public static final String MESSAGE_WORKING = "messageWorking";
    public static final String MESSAGE_SELECT_PLAYER_TITLE = "messageSelectPlayerTitle";
    public static final String MESSAGE_SELECT_PLAYER = "messageSelectPlayer";
    public static final String MESSAGE_NO_DATA_TITLE = "messageNoDataTitle";
    public static final String MESSAGE_NO_DATA = "messageNoData";
    public static final String MESSAGE_INFO_TITLE = "messageInfoTitel";
    public static final String MESSAGE_ERROR_PARSING = "messageErrorParsing";
    public static final String MESSAGE_ERROR_DISPLAYING = "messageErrorDisplayingValues";

    // TabbedPane labels
    public static final String TABBED_PANE_DAMAGE_DEALED = "tabPaneDamageDealed";
    public static final String TABBED_PANE_DAMAGE_RECEIVED = "tabPaneDamageReceived";
    public static final String TABBED_PANE_HEALED = "tabPaneHealed";
    public static final String TABBED_PANE_HEALING_RECEIVED = "tabPaneHealingReceived";
    public static final String TABBED_PANE_KILLS_DEATHS = "tabPaneKillsDeath";
    public static final String TABBED_PANE_DAMAGE_ABSORBED = "tabPaneDamageAbsorbed";

    // Table Column names
    public static final String COLUMN_NAME = "columnName";
    public static final String COLUMN_PLAYER_NAME = "columnPlayerName";
    public static final String COLUMN_NUMBER = "columnNumber";
    public static final String COLUMN_NUMBER_PERCENT = "columnNumberPercent";
    public static final String COLUMN_DAMAGE = "columnDamage";
    public static final String COLUMN_DAMAGE_PERCENT = "columnDamagePercent";
    public static final String COLUMN_CRITS = "columnCrits";
    public static final String COLUMN_CRITS_PERCENT = "columnCritsPercent";
    public static final String COLUMN_MIN = "columnMinimum";
    public static final String COLUMN_MAX = "columnMaximum";
    public static final String COLUMN_AVG = "columnAverage";
    public static final String COLUMN_HEAL = "columnHeal";
    public static final String COLUMN_HEAL_PERCENT = "columnHealPercent";
    public static final String COLUMN_HEALER = "columnHealer";
    public static final String COLUMN_TIME = "columnTime";
    public static final String COLUMN_OPPONENT = "columnOpponent";
    public static final String COLUMN_DAMAGE_KIND = "columnDamageKind";
    public static final String COLUMN_CRITICAL = "columnCritical";
    public static final String COLUMN_THREAD = "columnThreat";
    public static final String COLUMN_ABILITY_NAME = "columnAbilityName";
    public static final String COLUMN_TARGET = "columnTarget";
    public static final String COLUMN_DAMAGE_DEALER = "columnDamageDealer";
    public static final String COLUMN_DAMAGE_ABSORBED = "columnDamageAbsorbed";

    // Analyzer text
    public static final String ANALYZER_TOTAL = "analyzerTotal";
    public static final String ANALYZER_SUM = "analyzerSum";

    private static final String bundleBaseName = "localization";

    private static Localization singleton;

    private ResourceBundle bundle;

    private Localization()
    {
        try
        {
            bundle = ResourceBundle.getBundle(bundleBaseName);
        } catch (Exception e)
        {
            StartCombatLog.getLogger().log(Level.SEVERE, "Error during loading resource bundle", e);
        }
    }

    public static Localization getInstance()
    {
        if (null == singleton)
        {
            singleton = new Localization();
        }

        return singleton;
    }

    public String tr(String key)
    {
        return bundle.getString(key);
    }
}
