package de.swtor.combatlog.gui;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.swtor.combatlog.configuration.Configuration;
import de.swtor.combatlog.icons.Icons;

@SuppressWarnings("serial")
public class InfoPanel extends JPanel {
    public InfoPanel() {
        super();

        init();
    }

    private void init() {
        setLayout(new GridBagLayout());

        JLabel picture = new JLabel(Icons.SPLASH_SCREEN);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.insets = new Insets(5,0,0,0);
        gridBagConstraints.anchor = GridBagConstraints.CENTER;

        add(new JLabel(Configuration.APP_NAME + " - Version "+Configuration.VERSION), gridBagConstraints);

        gridBagConstraints.gridy = 1;

        add(new JLabel("Copyright: Thomas Rath, 2012"), gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;

        add(picture, gridBagConstraints);
    }
}

