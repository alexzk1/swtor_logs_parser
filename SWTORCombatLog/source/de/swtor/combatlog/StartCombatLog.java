package de.swtor.combatlog;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.UIManager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.StandardChartTheme;

import de.swtor.combatlog.configuration.Configuration;
import de.swtor.combatlog.gui.MainController;

/**
 * 
 * @author Thomas Rath
 */
public class StartCombatLog {
	
	private static Logger logger = Logger.getLogger("SWTOR Combat Analyser Logger");;
	
	/**
	 * @return the logger
	 */
	public static Logger getLogger() {
		return logger;
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SecurityException 
	 */
	public static void main(String[] args) throws SecurityException, IOException {       
		FileHandler fileHandler = new FileHandler("combatAnalyser.log", true);
		fileHandler.setFormatter(new SimpleFormatter());
		
		logger.addHandler(fileHandler);
		
		logger.setLevel(Level.ALL);
		
		logger.info("SWTOR Combat Analyser started!");
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception localException1) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				StartCombatLog.getLogger().log(Level.SEVERE, "Can't set look and feel class", e);
			}
		}
		
		ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow", true));
		
		try {
			Configuration.getInstance().loadProperties();
		} catch (Exception e) {
			StartCombatLog.getLogger().log(Level.SEVERE, "Can't load configuration", e);
		}
		
        MainController mainController = new MainController();
        
        mainController.startController();	
	}
}
