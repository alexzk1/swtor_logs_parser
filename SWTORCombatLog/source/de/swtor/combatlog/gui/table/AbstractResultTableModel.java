package de.swtor.combatlog.gui.table;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class AbstractResultTableModel extends AbstractTableModel {

	protected List<Integer> columnsWithNumberValues = new ArrayList<Integer>();

	public AbstractResultTableModel() {
		super();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public List<Integer> getColumnsWithNumberValues() {
		return columnsWithNumberValues;
	}

}