package de.swtor.combatlog.data;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.swtor.combatlog.localization.Localization;

public class DataStore {

	public static String ownerOfTheCurrentLogFile="";
	
	public static String mainHumanPlayerName="";
	
	public static Fight activeFight = null;
	
	public static Date startDate;
	
	public static Date endDate;
	
	public static final List<Fight> fights = new ArrayList<Fight>();
	
	public static final List<HumanPlayerCharacter> humanPlayerCharacters = new ArrayList<HumanPlayerCharacter>();
	
	public static final List<NonPlayerCharacter> nonPlayerCharacters = new ArrayList<NonPlayerCharacter>();
	
	public static CalculatedResult calculatedResult = null;
	
	public static double totalDPS = 0;
	
	public static double totalHPS = 0;
	
	public static void resetData() {
		ownerOfTheCurrentLogFile = "";
		startDate = null;
		endDate = null;
		calculatedResult = null;
		totalDPS = 0;
		totalHPS = 0;
		
		for(Fight fight : fights) {
			
			for (HumanPlayerCharacter player : fight.getHumanPlayerCharacters()) {
				player.getBuffedCasted().clear();
				player.getBuffedReceived().clear();
				player.getDamageDealed().clear();
				player.getDamageReceived().clear();
				player.getHealDealed().clear();
				player.getHealReceived().clear();
				player.getDeads().clear();
			}
			
			for (NonPlayerCharacter player : fight.getNonPlayerCharacters()) {
				player.getDamageDealed().clear();
				player.getDamageReceived().clear();
			}
			
			fight.getHumanPlayerCharacters().clear();
			fight.getNonPlayerCharacters().clear();
			fight.setCalculatedResult(null);
		}
		
		fights.clear();
		
		for (HumanPlayerCharacter player : humanPlayerCharacters) {
			player.getBuffedCasted().clear();
			player.getBuffedReceived().clear();
			player.getDamageDealed().clear();
			player.getDamageReceived().clear();
			player.getHealDealed().clear();
			player.getHealReceived().clear();
			player.getDeads().clear();
		}
		
		humanPlayerCharacters.clear();

		for (NonPlayerCharacter player : nonPlayerCharacters) {
			player.getDamageDealed().clear();
			player.getDamageReceived().clear();
		}

		nonPlayerCharacters.clear();
		
		mainHumanPlayerName ="";
	}
	
	public static HumanPlayerCharacter findHumanPlayerCharacter(String name) {
		HumanPlayerCharacter result = new HumanPlayerCharacter(name);
		
		int index = humanPlayerCharacters.indexOf(result);
		if (index > -1) {
			result = humanPlayerCharacters.get(index);
		} else {
			result = null;
		}
		
		return result;
	}
	
	public static NonPlayerCharacter findNonPlayerCharacter(String name) {
		NonPlayerCharacter result = new NonPlayerCharacter(name);
		
		int index = nonPlayerCharacters.indexOf(result);
		if (index > -1) {
			result = nonPlayerCharacters.get(index);
		} else {
			result = null;
		}
		
		return result;
	}
	
	public static String nextFightName() {
		String result = Localization.getInstance().tr(Localization.FIGHT);
		
		result = result + " " + String.valueOf(fights.size()+1);
		
		return result;
	}
	
	private DataStore() {		
	}
}
