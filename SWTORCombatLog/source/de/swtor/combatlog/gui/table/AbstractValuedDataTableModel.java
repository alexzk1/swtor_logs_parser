package de.swtor.combatlog.gui.table;

import de.swtor.combatlog.Constants;
import de.swtor.combatlog.Utilities;
import de.swtor.combatlog.data.AbstractValuedData;
import de.swtor.combatlog.localization.Localization;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2012 Thomas Rath
 */

public class AbstractValuedDataTableModel extends AbstractResultTableModel
{

    private static int NUMBER_COLUMNS = 8;

    private List<AbstractValuedData> model;
    private boolean damageTable = true;

    public AbstractValuedDataTableModel(List<AbstractValuedData> data)
    {
        model = new ArrayList<AbstractValuedData>(data);
    }

    public AbstractValuedDataTableModel(List<AbstractValuedData> data, boolean damageTable)
    {
        this(data);

        this.damageTable = damageTable;

        if (this.damageTable)
        {
            columnsWithNumberValues.add(Integer.valueOf(4));
            columnsWithNumberValues.add(Integer.valueOf(7));
        } else
        {
            columnsWithNumberValues.add(Integer.valueOf(4));
            columnsWithNumberValues.add(Integer.valueOf(6));
        }
    }

    @Override
    public int getRowCount()
    {
        return model.size();
    }

    @Override
    public int getColumnCount()
    {
        if (!damageTable)
        {
            return NUMBER_COLUMNS - 1;
        }

        return NUMBER_COLUMNS;
    }

    @Override
    public Object getValueAt(int row, int column)
    {
        Object result = "";
        AbstractValuedData rowData = model.get(row);

        if (damageTable)
        {
            switch (column)
            {
                case 0:
                    result = Constants.dateFormat.format(rowData.getDate());
                    break;
                case 1:
                    if (null != rowData.getFromCharacter())
                    {
                        result = Utilities.displayName(rowData.getFromCharacter().getName());
                    }
                    break;
                case 2:
                    if (null != rowData.getToCharacter())
                    {
                        result = Utilities.displayName(rowData.getToCharacter().getName());
                    }
                    break;
                case 3:
                    result = Utilities.displayName(rowData.getName());
                    break;
                case 4:
                    result = rowData.getValue();
                    break;
                case 5:
                    result = rowData.getKind();
                    break;
                case 6:
                    result = Utilities.displayValue(rowData.isCritical());
                    break;
                case 7:
                    result = rowData.getAggro();
                    break;

                default:
                    break;
            }
        } else
        {
            switch (column)
            {
                case 0:
                    result = Constants.dateFormat.format(rowData.getDate());
                    break;
                case 1:
                    if (null != rowData.getFromCharacter())
                    {
                        result = Utilities.displayName(rowData.getFromCharacter().getName());
                    }
                    break;
                case 2:
                    if (null != rowData.getToCharacter())
                    {
                        result = Utilities.displayName(rowData.getToCharacter().getName());
                    }
                    break;
                case 3:
                    result = Utilities.displayName(rowData.getName());
                    break;
                case 4:
                    result = rowData.getValue();
                    break;
                case 5:
                    result = Utilities.displayValue(rowData.isCritical());
                    break;
                case 6:
                    result = rowData.getAggro();
                    break;

                default:
                    break;
            }
        }

        return result;
    }

    @Override
    public String getColumnName(int column)
    {
        String result = "";

        if (damageTable)
        {
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
        } else
        {
            switch (column)
            {
                case 0:
                    result = Localization.getInstance().tr(Localization.COLUMN_TIME);
                    break;
                case 1:
                    result = Localization.getInstance().tr(Localization.COLUMN_HEALER);
                    break;
                case 2:
                    result = Localization.getInstance().tr(Localization.COLUMN_TARGET);
                    break;
                case 3:
                    result = Localization.getInstance().tr(Localization.COLUMN_ABILITY_NAME);
                    break;
                case 4:
                    result = Localization.getInstance().tr(Localization.COLUMN_HEAL);
                    break;
                case 5:
                    result = Localization.getInstance().tr(Localization.COLUMN_CRITICAL);
                    break;
                case 6:
                    result = Localization.getInstance().tr(Localization.COLUMN_THREAD);
                    break;

                default:
                    break;
            }
        }

        return result;
    }
}
