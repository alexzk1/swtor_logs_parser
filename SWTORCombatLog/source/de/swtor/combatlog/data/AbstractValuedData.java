package de.swtor.combatlog.data;

/*
 * Copyright (c) 2012 Thomas Rath
 */

public abstract class AbstractValuedData extends AbstractData
{

    private boolean critical = false;
    private int value;
    private int aggro;
    private String kind = "";

    public boolean isCritical()
    {
        return critical;
    }

    public void setCritical(boolean critical)
    {
        this.critical = critical;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public int getAggro()
    {
        return aggro;
    }

    public void setAggro(int aggro)
    {
        this.aggro = aggro;
    }

    public String getKind()
    {
        return kind;
    }

    public void setKind(String kind)
    {
        this.kind = kind;
    }
}
