package de.swtor.combatlog.data;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import java.util.ArrayList;
import java.util.List;

import de.swtor.combatlog.Utilities;

public class AbstractCharacter {
	private String name;
		
	private List<Damage> damageReceived = new ArrayList<Damage>();
	
	private List<Damage> damageDealed = new ArrayList<Damage>();
	
	private List<AbsorbedDamage> damageAbsorbed = new ArrayList<AbsorbedDamage>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addDamageReceived(Damage damage) {
		damageReceived.add(damage);
	}
	
	public void addDamageDealed(Damage damage) {
		damageDealed.add(damage);
	}

	public void addDamageAbsorbed(AbsorbedDamage damage) {
		damageAbsorbed.add(damage);
	}
	
	public List<Damage> getDamageReceived() {
		return damageReceived;
	}

	public List<Damage> getDamageDealed() {
		return damageDealed;
	}

	public List<AbsorbedDamage> getDamageAbsorbed() {
		return damageAbsorbed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractCharacter other = (AbstractCharacter) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return Utilities.displayName(getName());
	}
}
