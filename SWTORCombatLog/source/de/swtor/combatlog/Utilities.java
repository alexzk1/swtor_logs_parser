package de.swtor.combatlog;

import de.swtor.combatlog.localization.Localization;

/*
 * Copyright (c) 2012 Thomas Rath
 */

public class Utilities
{

    private Utilities()
    {
    }

    public static String displayName(String name)
    {
        String displayName = null;

        int index = name.indexOf(" {");
        if (index > -1)
        {
            displayName = name.substring(0, index);
        } else
        {
            displayName = name;
        }

        return displayName;
    }

    public static String displayValue(boolean value)
    {
        String displayValue = null;

        if (value)
        {
            displayValue = Localization.getInstance().tr(Localization.YES);
        } else
        {
            displayValue = Localization.getInstance().tr(Localization.NO);
        }

        return displayValue;
    }

}
