package de.swtor.combatlog.gui.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/*
 * Copyright (c) 2012 Thomas Rath
 */

@SuppressWarnings("serial")
public class TableRenderer extends DefaultTableCellRenderer
{
    private static final Color LIGHT_BLUE = new Color(237, 243, 254);
    private static final Color DARK_BLUE = new Color(0, 0, 102);

    private boolean drawFirstRowAsTotalRow = true;

    public TableRenderer()
    {
    }

    public TableRenderer(boolean drawFirstRowAsTotalRow)
    {
        super();
        this.drawFirstRowAsTotalRow = drawFirstRowAsTotalRow;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column)
    {

        Component c = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        Color lFgColor = table.getForeground();
        Color lBgColor = table.getBackground();
        Font font = table.getFont();

        if (row == 0 && drawFirstRowAsTotalRow)
        {
            font = font.deriveFont(Font.BOLD);
        }

        if (isSelected)
        { //color remains the same while selected
            lFgColor = table.getSelectionForeground();
            lBgColor = table.getSelectionBackground();
        } else
        {
            if (row == 0 && drawFirstRowAsTotalRow)
            {
                lFgColor = DARK_BLUE;
            } else
            {
                lFgColor = table.getForeground();
            }
            if (row % 2 != 0)
            {//once out of two rows, change color
                lBgColor = table.getBackground();
            } else
            {
                lBgColor = LIGHT_BLUE;
            }
        }

        c.setForeground(lFgColor);
        c.setBackground(lBgColor);
        c.setFont(font);

        // tooltip handling for long strings
        if (table.getColumnModel().getColumn(column).getWidth() < getPreferredSize().width)
        {
            setToolTipText(value.toString());
        } else
        {
            setToolTipText(null);
        }

        return c;
    }

}
