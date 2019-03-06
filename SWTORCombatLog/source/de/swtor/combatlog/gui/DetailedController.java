package de.swtor.combatlog.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import de.swtor.combatlog.data.AbstractValuedData;
import de.swtor.combatlog.gui.table.AbstractValuedDataTableModel;
import de.swtor.combatlog.gui.table.TableRenderer;
import de.swtor.combatlog.gui.table.TableWithNumbersRowSorter;
import de.swtor.combatlog.icons.Icons;
import de.swtor.combatlog.localization.Localization;

/*
 * Copyright (c) 2012 Thomas Rath
 */

public class DetailedController extends AbstractGuiController {
	
	public enum DataType {DAMAGE_DONE, DAMAGE_RECEIVED, HEAL_DONE, HEAL_RECEIVED};
	
	private List<AbstractValuedData> model;
	private DataType dataType;
	private String dataName;
	
	public DetailedController(List<AbstractValuedData> model, DataType dataType, String dataName) {
		this.model = model;
		this.dataType = dataType;
		this.dataName = dataName;
	}

	@Override
	public void updateActions() {
		
	}

	@Override
	public void startController() {
		setView(new DetailedPanel());

		AbstractValuedDataTableModel tableModel = new AbstractValuedDataTableModel(model, isDamage());
		getView().getDetailTable().setModel(tableModel);
		getView().getDetailTable().setDefaultRenderer(Object.class, new TableRenderer(false));	
		
		// Make table sortable
		TableWithNumbersRowSorter<TableModel> sorter = new TableWithNumbersRowSorter<TableModel>();
		getView().getDetailTable().setRowSorter(sorter);
		sorter.setModel(tableModel);	
		// special comparator for number columns
		sorter.createComparatorsForNumberColumns(tableModel.getColumnsWithNumberValues());
		
		final JDialog detailsWindow = new JDialog((Frame)null, determineTitle(), false);
		
		detailsWindow.getContentPane().add(getView(), BorderLayout.CENTER);
		detailsWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		detailsWindow.setIconImage(determineImage());
		detailsWindow.setPreferredSize(new Dimension(1000, detailsWindow.getPreferredSize().height));
		
		getView().getCloseButton().setText(Localization.getInstance().tr(Localization.CLOSE));
		
		getView().getCloseButton().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				detailsWindow.setVisible(false);
			}
		});
		
		detailsWindow.pack();
		detailsWindow.setLocationRelativeTo(null);
		detailsWindow.setVisible(true);	
	}

	@Override
	public DetailedPanel getView() {
		return (DetailedPanel)super.getView();
	}

	@Override
	public void stopController() {
		// TODO Auto-generated method stub
	}

	@Override
	public void performAction(String actionCommand) {
		// TODO Auto-generated method stub

	}

	private boolean isDamage() {
		return dataType == DataType.DAMAGE_DONE || dataType == DataType.DAMAGE_RECEIVED;
	}
	
	private String determineTitle() {
		String title = "";
		
		switch (dataType) {
		case DAMAGE_DONE:
			title = Localization.getInstance().tr(Localization.TABBED_PANE_DAMAGE_DEALED);
			break;
		case DAMAGE_RECEIVED:
			title = Localization.getInstance().tr(Localization.TABBED_PANE_DAMAGE_RECEIVED);
			break;
		case HEAL_DONE:
			title = Localization.getInstance().tr(Localization.TABBED_PANE_HEALED);
			break;
		case HEAL_RECEIVED:
			title = Localization.getInstance().tr(Localization.TABBED_PANE_HEALING_RECEIVED);
			break;			

		default:
			break;
		}
		
		return title + " - "+ dataName;
	}
	
	private Image determineImage() {
		Image image = null;
		
		switch (dataType) {
		case DAMAGE_DONE:
			image = Icons.DAMAGE_DONE_16x16.getImage();
			break;
		case DAMAGE_RECEIVED:
			image = Icons.DAMAGE_RECEIVED_16x16.getImage();
			break;
		case HEAL_DONE:
			image = Icons.HEALING_DONE_16x16.getImage();
			break;
		case HEAL_RECEIVED:
			image = Icons.HEALING_RECEIVED_16x16.getImage();
			break;			

		default:
			break;
		}
		
		return image;
	}
}
