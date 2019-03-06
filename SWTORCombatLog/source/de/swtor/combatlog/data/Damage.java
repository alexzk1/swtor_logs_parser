package de.swtor.combatlog.data;

/*
 * Copyright (c) 2012 Thomas Rath
 */

public class Damage extends AbstractValuedData
{

    public Damage(String name)
    {
        setName(name);
        setEvent(Event.DAMAGE);
    }
}
