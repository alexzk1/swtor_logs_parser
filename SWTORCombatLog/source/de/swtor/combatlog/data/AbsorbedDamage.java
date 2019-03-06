package de.swtor.combatlog.data;

/*
 * Copyright (c) 2012 Thomas Rath
 */

public class AbsorbedDamage extends AbstractValuedData
{

    private int damageValue;

    public AbsorbedDamage(String name)
    {
        setName(name);
        setEvent(Event.ABSORBED_DAMAGE);
    }

    public int getDamageValue()
    {
        return damageValue;
    }

    public void setDamageValue(int damageValue)
    {
        this.damageValue = damageValue;
    }
}
