package de.swtor.combatlog.gui;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JWindow;

import de.swtor.combatlog.icons.Icons;

public class SplashScreen extends JWindow {

	public SplashScreen(Frame frame) {
		super(frame);
		
		JLabel l = new JLabel(Icons.SPLASH_SCREEN);
        getContentPane().add(l, BorderLayout.CENTER);
        pack();
        Dimension screenSize =
          Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = l.getPreferredSize();
        setLocation(screenSize.width/2 - (labelSize.width/2),
                    screenSize.height/2 - (labelSize.height/2));
        addMouseListener(new MouseAdapter()
            {
                public void mousePressed(MouseEvent e)
                {
                    setVisible(false);
                    dispose();
                }
            });
        setVisible(true);
        
        Timer timer = new Timer(); 
        
        // nach drei Sekunden wieder schliessen 
        timer.schedule( new TimerTask() {
			public void run() {
				SplashScreen.this.setVisible(false);
			}        	
        }, 3000 ); 

    }
}

