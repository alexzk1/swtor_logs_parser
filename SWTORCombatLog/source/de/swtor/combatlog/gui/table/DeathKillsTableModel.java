package de.swtor.combatlog.gui.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.swtor.combatlog.Constants;
import de.swtor.combatlog.Utilities;
import de.swtor.combatlog.data.Death;
import de.swtor.combatlog.localization.Localization;

/*
 * Copyright (c) 2012 Thomas Rath
 */

@SuppressWarnings("serial")
public class DeathKillsTableModel extends AbstractResultTableModel {

	private static int NUMBER_COLUMNS = 8;
	private List<Death> deathKills = null;

	public DeathKillsTableModel(List<Death> deathKills) {
		this.deathKills = deathKills;
		
		columnsWithNumberValues.add(Integer.valueOf(4));
		columnsWithNumberValues.add(Integer.valueOf(7));
	}

	@Override
	public int getRowCount() {
		return deathKills != null ? deathKills.size() : 0;
	}

	@Override
	public int getColumnCount() {
		return NUMBER_COLUMNS;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object result = "";
		Death death = deathKills.get(rowIndex);

		switch (columnIndex) {
		case 0:
			result = Constants.dateFormat.format(death.getDate());
			break;
		case 1:
			if (null != death.getFromCharacter()) {
				result = Utilities.displayName(death.getFromCharacter().getName());
			}
			break;
		case 2:
			if (null != death.getReason() && null != death.getReason().getToCharacter()) {
				result = Utilities.displayName(death.getReason().getToCharacter().getName());
			} else if (null != death.getToCharacter()) {
				result = Utilities.displayName(death.getToCharacter().getName());
			}
			break;
		case 3:
			if (null != death.getReason()) {
				result = Utilities.displayName(death.getReason().getName());
			}
			break;
		case 4:
			if (null != death.getReason()) {
				result = death.getReason().getValue();
			}
			break;
		case 5:
			if (null != death.getReason()) {
				result = death.getReason().getKind();
			}
			break;
		case 6:
			if (null != death.getReason()) {
				result = Utilities.displayValue(death.getReason().isCritical());
			}
			break;
		case 7:
			if (null != death.getReason()) {
				result = death.getReason().getAggro();
			}
			break;

		default:
			break;
		}

		return result;
	}

	@Override
	public String getColumnName(int column) {
		String result = "";

		switch (column) {
		case 0:
			result = Localization.getInstance().tr(Localization.COLUMN_TIME);
			break;
		case 1:
			result = Localization.getInstance().tr(Localization.COLUMN_DAMAGE_DEALER);
			break;
		case 2:
			result = Localization.getInstance().tr(Localization.COLUMN_TARGET);
			break;
		case 3:
			result = Localization.getInstance().tr(Localization.COLUMN_ABILITY_NAME);
			break;
		case 4:
			result = Localization.getInstance().tr(Localization.COLUMN_DAMAGE);
			break;
		case 5:
			result = Localization.getInstance().tr(Localization.COLUMN_DAMAGE_KIND);
			break;
		case 6:
			result = Localization.getInstance().tr(Localization.COLUMN_CRITICAL);
			break;
		case 7:
			result = Localization.getInstance().tr(Localization.COLUMN_THREAD);
			break;

		default:
			break;
		}

		return result;
	}
}
