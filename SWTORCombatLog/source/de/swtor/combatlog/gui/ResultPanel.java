/*
 * Created by JFormDesigner on Sat Apr 14 14:56:13 CEST 2012
 */

package de.swtor.combatlog.gui;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 * @author User #13
 */
public class ResultPanel extends JPanel {
	public ResultPanel() {
		initComponents();
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public JTable getDamageTable() {
		return damageTable;
	}

	public JTable getDamageReceivedTable() {
		return damageReceivedTable;
	}

	public JTable getHealedTable() {
		return healedTable;
	}

	public JTable getHealingReceivedTable() {
		return healingReceivedTable;
	}

	public JTable getKillsDeathsTable() {
		return killsDeathsTable;
	}

	public JTable getDamageAbsorbedTable() {
		return damageAbsorbedTable;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		tabbedPane = new JTabbedPane();
		damagePane = new JPanel();
		scrollPane1 = new JScrollPane();
		damageTable = new JTable();
		damageReceivedPane = new JPanel();
		scrollPane2 = new JScrollPane();
		damageReceivedTable = new JTable();
		damageAbsorbed = new JPanel();
		scrollPane6 = new JScrollPane();
		damageAbsorbedTable = new JTable();
		healPane = new JPanel();
		scrollPane3 = new JScrollPane();
		healedTable = new JTable();
		healReceivedPane = new JPanel();
		scrollPane4 = new JScrollPane();
		healingReceivedTable = new JTable();
		killsDeaths = new JPanel();
		scrollPane5 = new JScrollPane();
		killsDeathsTable = new JTable();

		//======== this ========
		setLayout(new BorderLayout());

		//======== tabbedPane ========
		{

			//======== damagePane ========
			{
				damagePane.setLayout(new BorderLayout());

				//======== scrollPane1 ========
				{

					//---- damageTable ----
					damageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					damageTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					scrollPane1.setViewportView(damageTable);
				}
				damagePane.add(scrollPane1, BorderLayout.CENTER);
			}
			tabbedPane.addTab("Damage dealed", damagePane);


			//======== damageReceivedPane ========
			{
				damageReceivedPane.setLayout(new BorderLayout());

				//======== scrollPane2 ========
				{

					//---- damageReceivedTable ----
					damageReceivedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					damageReceivedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					scrollPane2.setViewportView(damageReceivedTable);
				}
				damageReceivedPane.add(scrollPane2, BorderLayout.CENTER);
			}
			tabbedPane.addTab("Damage received", damageReceivedPane);


			//======== damageAbsorbed ========
			{
				damageAbsorbed.setLayout(new BorderLayout());

				//======== scrollPane6 ========
				{
					scrollPane6.setViewportView(damageAbsorbedTable);
				}
				damageAbsorbed.add(scrollPane6, BorderLayout.CENTER);
			}
			tabbedPane.addTab("Damage absorbed", damageAbsorbed);


			//======== healPane ========
			{
				healPane.setLayout(new BorderLayout());

				//======== scrollPane3 ========
				{

					//---- healedTable ----
					healedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					healedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					scrollPane3.setViewportView(healedTable);
				}
				healPane.add(scrollPane3, BorderLayout.CENTER);
			}
			tabbedPane.addTab("Healed", healPane);


			//======== healReceivedPane ========
			{
				healReceivedPane.setLayout(new BorderLayout());

				//======== scrollPane4 ========
				{

					//---- healingReceivedTable ----
					healingReceivedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					healingReceivedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					scrollPane4.setViewportView(healingReceivedTable);
				}
				healReceivedPane.add(scrollPane4, BorderLayout.CENTER);
			}
			tabbedPane.addTab("Healing received", healReceivedPane);


			//======== killsDeaths ========
			{
				killsDeaths.setLayout(new BorderLayout());

				//======== scrollPane5 ========
				{
					scrollPane5.setViewportView(killsDeathsTable);
				}
				killsDeaths.add(scrollPane5, BorderLayout.CENTER);
			}
			tabbedPane.addTab("Kills/Deaths", killsDeaths);

		}
		add(tabbedPane, BorderLayout.CENTER);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JTabbedPane tabbedPane;
	private JPanel damagePane;
	private JScrollPane scrollPane1;
	private JTable damageTable;
	private JPanel damageReceivedPane;
	private JScrollPane scrollPane2;
	private JTable damageReceivedTable;
	private JPanel damageAbsorbed;
	private JScrollPane scrollPane6;
	private JTable damageAbsorbedTable;
	private JPanel healPane;
	private JScrollPane scrollPane3;
	private JTable healedTable;
	private JPanel healReceivedPane;
	private JScrollPane scrollPane4;
	private JTable healingReceivedTable;
	private JPanel killsDeaths;
	private JScrollPane scrollPane5;
	private JTable killsDeathsTable;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
