package de.swtor.combatlog.data;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2012 Thomas Rath
 */

public class CalculatedResult
{

    private String[][] damageDealed;
    private String[][] damageReceived;
    private String[][] healedDealed;
    private String[][] healingReceived;
    private List<Death> deaths = new ArrayList<Death>();
    private List<AbsorbedDamage> absorbedDamages = new ArrayList<AbsorbedDamage>();
    public CalculatedResult()
    {
    }

    public String[][] getDamageDealed()
    {
        return damageDealed;
    }

    public void setDamageDealed(String[][] damageDealed)
    {
        this.damageDealed = damageDealed;
    }

    public String[][] getDamageReceived()
    {
        return damageReceived;
    }

    public void setDamageReceived(String[][] damageReceived)
    {
        this.damageReceived = damageReceived;
    }

    public String[][] getHealedDealed()
    {
        return healedDealed;
    }

    public void setHealedDealed(String[][] healedDealed)
    {
        this.healedDealed = healedDealed;
    }

    public String[][] getHealingReceived()
    {
        return healingReceived;
    }

    public void setHealingReceived(String[][] healingResult)
    {
        this.healingReceived = healingResult;
    }

    public List<Death> getDeaths()
    {
        return deaths;
    }

    public void setDeaths(List<Death> deaths)
    {
        this.deaths = deaths;
    }

    public List<AbsorbedDamage> getAbsorbedDamages()
    {
        return absorbedDamages;
    }

    public void setAbsorbedDamages(List<AbsorbedDamage> absorbedDamages)
    {
        this.absorbedDamages = absorbedDamages;
    }
}
