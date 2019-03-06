package de.swtor.combatlog.gui;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import de.swtor.combatlog.configuration.Configuration;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
    public final static int MIN_WIDTH = 800;
    public final static int MIN_HEIGHT = 600;
    
    public final static int PREF_WIDTH = 1000;
    public final static int PREF_HEIGHT = 800;

    public MainWindow() throws HeadlessException {
        initialize();
    }

    public void setTitle(String title) {
    	String completeTitle = Configuration.APP_NAME;
    	
    	if (null != title && title.length() > 0) {
    		completeTitle = completeTitle + " - " + title;
    	}
    	
    	super.setTitle(completeTitle);
    }    
    
    private void initialize() {
        setTitle(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setSize(PREF_WIDTH, PREF_HEIGHT);
        setPreferredSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));
        
        addWindowListener(new WindowAdapter() {					
			@Override
			public void windowClosing(WindowEvent e) {
				Configuration.getInstance().saveProperties();				
			}
			
		});
        
        pack();
        setLocationRelativeTo(null);
    }      
}
