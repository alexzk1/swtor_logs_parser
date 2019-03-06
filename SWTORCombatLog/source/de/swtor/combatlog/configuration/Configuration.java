package de.swtor.combatlog.configuration;

import de.swtor.combatlog.StartCombatLog;

import java.io.*;
import java.text.DateFormat;
import java.util.*;
import java.util.logging.Level;

/*
 * Copyright (c) 2012 Thomas Rath
 */

public class Configuration
{
    public final static String VERSION = "1.2";

    public final static String APP_NAME = "SWTOR Combat Log Analyzer";

    public final static int NUMBER_RECENT_FILES = 9;

    private final static String LAST_DIR = "last.dir";
    private final static String RECENT_FILE = "recent.file";

    private final static String CONFIG_FILE_NAME = "combatlog.xml";

    private static Configuration sInstance = null;

    private List<String> recentFiles = new ArrayList<String>();
    private Properties properties = new Properties();

    public static Configuration getInstance()
    {
        if (null == sInstance)
        {
            sInstance = new Configuration();
        }

        return sInstance;
    }

    public void saveProperties()
    {
        FileOutputStream propOutFile = null;
        try
        {
            String comments = DateFormat.getInstance().format(new Date());

            storeRecentFilesToPreferences();

            String propertyFile = getFullPropertiesFileName();
            propOutFile = new FileOutputStream(propertyFile);

            properties.storeToXML(propOutFile, comments);
        } catch (FileNotFoundException e)
        {
            StartCombatLog.getLogger().log(Level.SEVERE, "Can't find " + CONFIG_FILE_NAME, e);
        } catch (IOException e)
        {
            StartCombatLog.getLogger().log(Level.SEVERE, "Can't save properties to " + CONFIG_FILE_NAME, e);
        } finally
        {
            if (null != propOutFile)
            {
                try
                {
                    propOutFile.close();
                } catch (IOException e)
                {
                    StartCombatLog.getLogger().log(Level.SEVERE, "Can't close config file " + CONFIG_FILE_NAME, e);
                }
            }
        }
    }

    public void loadProperties() throws Exception
    {
        FileInputStream propInFile = null;
        try
        {
            String propertyFile = getFullPropertiesFileName();
            propInFile = new FileInputStream(propertyFile);

            properties.loadFromXML(propInFile);

            fillRecentFileStack();
        } catch (FileNotFoundException e)
        {
            //throw new Exception("Cannot find " + CONFIG_FILE_NAME, e);
        } catch (InvalidPropertiesFormatException e)
        {
            throw new Exception("The property format is wrong!", e);
        } catch (IOException e)
        {
            throw new Exception("Cannot load properties from " + CONFIG_FILE_NAME, e);
        } finally
        {
            if (null != propInFile)
            {
                propInFile.close();
            }
        }
    }

    public String getLastDir()
    {
        return (String) properties.getProperty(LAST_DIR, System.getProperty("user.home"));
    }

    public void setLastDir(String lastDir)
    {
        properties.setProperty(LAST_DIR, lastDir);
    }

    public List<String> getRecentFiles()
    {
        return recentFiles;
    }

    public void addRecentFiles(String recentFileName)
    {
        recentFiles.remove(recentFileName);
        recentFiles.add(0, recentFileName);

        if (recentFiles.size() > NUMBER_RECENT_FILES)
        {
            recentFiles.remove(recentFiles.size() - 1);
        }
    }

    public void removeRecentFiles(String recentFileName)
    {
        recentFiles.remove(recentFileName);
    }

    private void fillRecentFileStack()
    {
        for (int i = 0; i < NUMBER_RECENT_FILES; i++)
        {
            String fileName = (String) properties.getProperty(RECENT_FILE + "." + i, null);

            if (null == fileName)
            {
                break;
            }

            recentFiles.add(fileName);
        }
    }

    private void storeRecentFilesToPreferences()
    {
        if (null != recentFiles)
        {
            for (int i = 0; i < NUMBER_RECENT_FILES; i++)
            {
                if (i < recentFiles.size())
                {
                    properties.put(RECENT_FILE + "." + i, (String) recentFiles.get(i));
                } else
                {
                    properties.remove(RECENT_FILE + "." + i);
                }
            }
        }
    }

    private String getFullPropertiesFileName()
    {
        String home = System.getProperty("user.home");
        return home + File.separator + CONFIG_FILE_NAME;
    }
}
