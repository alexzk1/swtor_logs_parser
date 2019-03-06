package de.swtor.combatlog.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Copyright (c) 2012 Thomas Rath
 */

public class Fight {
	
	private String name;
	
	private String targets;
	
	private Date startDate;
	
	private Date endDate;
	
	private double dps;
	
	private double hps;

	private List<HumanPlayerCharacter> humanPlayerCharacters = new ArrayList<HumanPlayerCharacter>();
	
	private List<NonPlayerCharacter> nonPlayerCharacters = new ArrayList<NonPlayerCharacter>();
	
	private CalculatedResult calculatedResult;

	public Fight(String name) {
		super();
		this.name = name;
	}
	
	public HumanPlayerCharacter findHumanPlayerCharacter(String name) {
		HumanPlayerCharacter result = new HumanPlayerCharacter(name);
		
		int index = humanPlayerCharacters.indexOf(result);
		if (index > -1) {
			result = humanPlayerCharacters.get(index);
		} else {
			result = null;
		}
		
		return result;
	}
	
	public NonPlayerCharacter findNonPlayerCharacter(String name) {
		NonPlayerCharacter result = new NonPlayerCharacter(name);
		
		int index = nonPlayerCharacters.indexOf(result);
		if (index > -1) {
			result = nonPlayerCharacters.get(index);
		} else {
			result = null;
		}
		
		return result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTargets() {
		return targets;
	}

	public void setTargets(String targets) {
		this.targets = targets;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		this.endDate = startDate; // sometimes no end fight is in the log file
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public double getDps() {
		return dps;
	}

	public void setDps(double dps) {
		this.dps = dps;
	}

	public double getHps() {
		return hps;
	}

	public void setHps(double hps) {
		this.hps = hps;
	}

	public List<HumanPlayerCharacter> getHumanPlayerCharacters() {
		return humanPlayerCharacters;
	}

	public void setHumanPlayerCharacters(
			List<HumanPlayerCharacter> humanPlayerCharacters) {
		this.humanPlayerCharacters = humanPlayerCharacters;
	}

	public List<NonPlayerCharacter> getNonPlayerCharacters() {
		return nonPlayerCharacters;
	}

	public void setNonPlayerCharacters(List<NonPlayerCharacter> nonPlayerCharacters) {
		this.nonPlayerCharacters = nonPlayerCharacters;
	}

	public CalculatedResult getCalculatedResult() {
		return calculatedResult;
	}

	public void setCalculatedResult(CalculatedResult calculatedResult) {
		this.calculatedResult = calculatedResult;
	}	
}
