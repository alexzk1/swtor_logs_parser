package de.swtor.combatlog;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import java.text.SimpleDateFormat;

public class Constants
{

    // Format like [23:29:04.202]
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");


    public static final String EVENT = "836045448945472";
    public static final String CONSUME = "836045448945473";
    public static final String APPLY_EFFECT = "836045448945477";
    public static final String REMOVE_EFFECT = "836045448945478";
    public static final String RESTORE = "836045448945476";
    public static final String DEAD = "836045448945493";
    public static final String DAMAGE = "836045448945501";
    public static final String HEAL = "836045448945500";
    public static final String ABSORBED = "836045448945511";
    public static final String FIGHT_BEGIN = "836045448945489";
    public static final String FIGHT_END = "836045448945490";
    public static final String FALLING_DAMAGE = "836045448945484";

    private Constants()
    {
    }
}
