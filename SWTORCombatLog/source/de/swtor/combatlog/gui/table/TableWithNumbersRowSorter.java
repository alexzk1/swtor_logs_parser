/**
 * 
 */
package de.swtor.combatlog.gui.table;

import java.util.Comparator;
import java.util.List;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/*
 * Copyright (c) 2012 Thomas Rath
 */

/**
 * @author rath
 *
 */
public class TableWithNumbersRowSorter<M extends TableModel> extends TableRowSorter<M> {
	
	public TableWithNumbersRowSorter() {
		super();	
	}

	public TableWithNumbersRowSorter(M model) {
		super(model);		
	}

	public void createComparatorsForNumberColumns(List<Integer> columnsWithNumberValues) {		
		// special comparator for number columns
		for (Integer numberColumn : columnsWithNumberValues) {			
			setComparator(numberColumn.intValue(), new Comparator<Integer>() {
	            public int compare(Integer int1, Integer int2) {
	                return int1.intValue() - int2.intValue();
	            }
	        });
		}
	}
}
