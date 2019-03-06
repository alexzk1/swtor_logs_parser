package de.swtor.combatlog.data;

/*
 * Copyright (c) 2012 Thomas Rath
 */

public class Restore extends AbstractData {

	public Restore(String name) {
		setName(name);
		setEvent(Event.RESTORE);
	}
}
