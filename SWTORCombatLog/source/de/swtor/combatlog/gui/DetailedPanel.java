/*
 * Created by JFormDesigner on Sun Apr 29 17:35:26 CEST 2012
 */

package de.swtor.combatlog.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/*
 * Copyright (c) 2012 Thomas Rath
 */

/**
 * @author User #13
 */
public class DetailedPanel extends JPanel {
	public DetailedPanel() {
		initComponents();
	}

	public JTable getDetailTable() {
		return detailTable;
	}

	public JButton getCloseButton() {
		return closeButton;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		scrollPane1 = new JScrollPane();
		detailTable = new JTable();
		panel1 = new JPanel();
		closeButton = new JButton();

		//======== this ========
		setLayout(new BorderLayout());

		//======== scrollPane1 ========
		{
			scrollPane1.setViewportView(detailTable);
		}
		add(scrollPane1, BorderLayout.CENTER);

		//======== panel1 ========
		{
			panel1.setLayout(new GridBagLayout());
			((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
			((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 0};
			((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0, 1.0E-4};
			((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

			//---- closeButton ----
			closeButton.setText("text");
			panel1.add(closeButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		}
		add(panel1, BorderLayout.SOUTH);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JScrollPane scrollPane1;
	private JTable detailTable;
	private JPanel panel1;
	private JButton closeButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
