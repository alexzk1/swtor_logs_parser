package de.swtor.combatlog.data;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import java.util.Date;

public abstract class AbstractData
{

    private String name;

    ;
    private Date date;
    private Event event;
    private AbstractCharacter fromCharacter;
    private AbstractCharacter toCharacter;
    private String sortString = null;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Event getEvent()
    {
        return event;
    }

    public void setEvent(Event event)
    {
        this.event = event;
    }

    public AbstractCharacter getFromCharacter()
    {
        return fromCharacter;
    }

    public void setFromCharacter(AbstractCharacter fromCharacter)
    {
        this.fromCharacter = fromCharacter;
    }

    public AbstractCharacter getToCharacter()
    {
        return toCharacter;
    }

    public void setToCharacter(AbstractCharacter toCharacter)
    {
        this.toCharacter = toCharacter;
    }

    public enum Event
    {
        EVENT, APPLY_EFFECT, REMOVE_EFFECT, DAMAGE, ABSORBED_DAMAGE, FALLING_DAMAGE, HEAL, RESTORE, CONSUME, DEAD, FIGHT_BEGIN, FIGHT_END
    }
}
