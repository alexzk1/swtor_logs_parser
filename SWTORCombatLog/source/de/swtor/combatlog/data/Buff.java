package de.swtor.combatlog.data;

/*
 * Copyright (c) 2012 Thomas Rath
 */

public class Buff extends AbstractData
{

    public Buff(String name)
    {
        setName(name);
        setEvent(Event.EVENT);
    }
}
