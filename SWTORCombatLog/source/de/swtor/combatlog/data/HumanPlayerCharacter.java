package de.swtor.combatlog.data;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import de.swtor.combatlog.Utilities;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class HumanPlayerCharacter extends AbstractCharacter
{

    private List<Heal> healReceived = new ArrayList<Heal>();

    private List<Heal> healDealed = new ArrayList<Heal>();

    private List<Buff> buffedReceived = new ArrayList<Buff>();

    private List<Buff> buffedCasted = new ArrayList<Buff>();

    private List<Death> deaths = new ArrayList<Death>();

    private Hashtable<Long, Integer> damageCondensed = null;

    private Hashtable<Long, Integer> healCondensed = null;

    public HumanPlayerCharacter(String name)
    {
        setName(name);
    }

    public void addHealReceived(Heal heal)
    {
        healReceived.add(heal);
    }

    public void addHealDealed(Heal heal)
    {
        healDealed.add(heal);
    }

    public void addBuffedReceived(Buff buff)
    {
        buffedReceived.add(buff);
    }

    public void addBuffedCasted(Buff buff)
    {
        buffedCasted.add(buff);
    }

    public List<Heal> getHealReceived()
    {
        return healReceived;
    }

    public List<Heal> getHealDealed()
    {
        return healDealed;
    }

    public List<Buff> getBuffedReceived()
    {
        return buffedReceived;
    }

    public List<Buff> getBuffedCasted()
    {
        return buffedCasted;
    }

    public List<Death> getDeads()
    {
        return deaths;
    }

    public Hashtable<Long, Integer> getDamageCondensed()
    {
        return damageCondensed;
    }

    public void setDamageCondensed(Hashtable<Long, Integer> damageCondensed)
    {
        this.damageCondensed = damageCondensed;
    }

    public Hashtable<Long, Integer> getHealCondensed()
    {
        return healCondensed;
    }

    public void setHealCondensed(Hashtable<Long, Integer> healCondensed)
    {
        this.healCondensed = healCondensed;
    }

    @Override
    public String toString()
    {
        return Utilities.displayName(getName()) + " [DD Count (" + getDamageDealed().size() + ")" + "; Healed Count(" + getHealDealed().size() + ")]";
    }
}
