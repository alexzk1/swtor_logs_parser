package de.swtor.combatlog.data;

/*
 * Copyright (c) 2012 Thomas Rath
 */

public class Heal extends AbstractValuedData {

	public Heal(String name) {
		setName(name);
		setEvent(Event.HEAL);
	}
}
