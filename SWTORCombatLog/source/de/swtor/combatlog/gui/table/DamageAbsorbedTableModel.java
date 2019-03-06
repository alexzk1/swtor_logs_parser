package de.swtor.combatlog.gui.table;

import de.swtor.combatlog.Constants;
import de.swtor.combatlog.Utilities;
import de.swtor.combatlog.data.AbsorbedDamage;
import de.swtor.combatlog.localization.Localization;

import java.util.List;

/*
 * Copyright (c) 2012 Thomas Rath
 */

@SuppressWarnings("serial")
public class DamageAbsorbedTableModel extends AbstractResultTableModel
{

    private static int NUMBER_COLUMNS = 7;
    private List<AbsorbedDamage> absorbedDamages = null;

    public DamageAbsorbedTableModel(List<AbsorbedDamage> absorbedDamages)
    {
        this.absorbedDamages = absorbedDamages;

        columnsWithNumberValues.add(Integer.valueOf(4));
        columnsWithNumberValues.add(Integer.valueOf(5));
    }

    @Override
    public int getRowCount()
    {
        return absorbedDamages != null ? absorbedDamages.size() : 0;
    }

    @Override
    public int getColumnCount()
    {
        return NUMBER_COLUMNS;
    }

    @Override
    public boolean isCellEditable(int row, int column)
    {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Object result = "";
        AbsorbedDamage absorbedDamage = absorbedDamages.get(rowIndex);

        switch (columnIndex)
        {
            case 0:
                result = Constants.dateFormat.format(absorbedDamage.getDate());
                break;
            case 1:
                if (null != absorbedDamage.getFromCharacter())
                {
                    result = Utilities.displayName(absorbedDamage.getFromCharacter().getName());
                }
                break;
            case 2:
                if (null != absorbedDamage.getToCharacter())
                {
                    result = Utilities.displayName(absorbedDamage.getToCharacter().getName());
                }
                break;
            case 3:
                result = Utilities.displayName(absorbedDamage.getName());
                break;
            case 4:
                result = absorbedDamage.getValue();
                break;
            case 5:
                result = absorbedDamage.getDamageValue();
                break;
            case 6:
                result = absorbedDamage.getKind();
                break;

            default:
                break;
        }

        return result;
    }

    @Override
    public String getColumnName(int column)
    {
        String result = "";

        switch (column)
        {
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
                result = Localization.getInstance().tr(Localization.COLUMN_DAMAGE_ABSORBED);
                break;
            case 5:
                result = Localization.getInstance().tr(Localization.COLUMN_DAMAGE);
                break;
            case 6:
                result = Localization.getInstance().tr(Localization.COLUMN_DAMAGE_KIND);
                break;

            default:
                break;
        }

        return result;
    }
}
