package de.swtor.combatlog.data;


/*
 * Copyright (c) 2012 Thomas Rath
 */

public class Death extends AbstractData {
	
	private Damage reason = null;

	public Death(String name) {
		setName(name);
		setEvent(Event.DEAD);
	}

	public Damage getReason() {
		return reason;
	}

	public void setReason(Damage reason) {
		this.reason = reason;
	}	
}
