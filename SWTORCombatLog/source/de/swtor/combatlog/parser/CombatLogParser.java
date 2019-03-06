package de.swtor.combatlog.parser;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import de.swtor.combatlog.Constants;
import de.swtor.combatlog.StartCombatLog;
import de.swtor.combatlog.data.AbsorbedDamage;
import de.swtor.combatlog.data.AbstractCharacter;
import de.swtor.combatlog.data.AbstractData.Event;
import de.swtor.combatlog.data.Damage;
import de.swtor.combatlog.data.DataStore;
import de.swtor.combatlog.data.Death;
import de.swtor.combatlog.data.Fight;
import de.swtor.combatlog.data.Heal;
import de.swtor.combatlog.data.HumanPlayerCharacter;
import de.swtor.combatlog.data.NonPlayerCharacter;
import de.swtor.combatlog.localization.Localization;

public class CombatLogParser {

	private Fight activeFight = null;
	private int day;
	private long oldDay = 0;
	private Calendar calender = null;

	public CombatLogParser() {
	}

	public void parse(File combatLogFile) throws Exception {
		List<String> combatLines = readLines(combatLogFile);

		activeFight = null;
		day = 1;
		oldDay = 0;
		calender = Calendar.getInstance();
		analyseCombatLog(combatLines);
	}

	public List<String> readLines(File combatLogFile) throws Exception {
		String line;
		List<String> combatLines = new ArrayList<String>();

		FileReader fread = null;
		BufferedReader in = null;
		try {
			fread = new FileReader(combatLogFile);
			in = new BufferedReader(fread);

			while ((line = in.readLine()) != null) {
				combatLines.add(line);
			}
		} catch (IOException e) {
			throw new Exception("Can't load the file: " + combatLogFile.getPath(), e);
		} finally {
			try {
				if (null != in) {
					in.close();
				}
				if (null != fread) {
					fread.close();
				}
			} catch (IOException e) {
				throw new Exception("Can't close the file!", e);
			}
		}

		return combatLines;
	}

	public void analyseCombatLog(List<String> combatLog) throws Exception {
		for (String combatLogLine : combatLog) {
			parseOneLine(combatLogLine);
		}
	}

	/**
	 * Parse something like [23:29:04.202] [@Lyra] [@Knocker] [Heilungstrance
	 * {812964294688768}] [Effekt anwenden {836045448945477}: Heilung
	 * {836045448945500}] (1689*) <718>
	 * 
	 * @param combatLogLine
	 */
	private void parseOneLine(String combatLogLine) {
		try {
			if (null == combatLogLine || combatLogLine.length() == 0) {
				return;
			}

			int currentIndexFrom = 0;
			int currentIndexTo = 0;

			// DateTime [23:29:04.202]
			currentIndexFrom = 1; // skip the first [
			currentIndexTo = combatLogLine.indexOf("]", currentIndexFrom);
			String dateString = combatLogLine.substring(currentIndexFrom, currentIndexTo);
			Date date = calculateDate(dateString);

			// From name [@Knocker]
			currentIndexFrom = currentIndexTo + 3; // skip ] [
			currentIndexTo = combatLogLine.indexOf("]", currentIndexFrom);
			String fromName = combatLogLine.substring(currentIndexFrom, currentIndexTo);

			// To name [@Knocker]
			currentIndexFrom = currentIndexTo + 3; // skip ] [
			currentIndexTo = combatLogLine.indexOf("]", currentIndexFrom);
			String toName = combatLogLine.substring(currentIndexFrom, currentIndexTo);

			// Cast or Buff Name like [Heilungstrance {812964294688768}]
			currentIndexFrom = currentIndexTo + 3; // skip ] [
			currentIndexTo = combatLogLine.indexOf("]", currentIndexFrom);
			String castName = combatLogLine.substring(currentIndexFrom, currentIndexTo);

			// Event like [Effekt anwenden {836045448945477}: Heilung
			// {836045448945500}]
			currentIndexFrom = currentIndexTo + 3; // skip ] [
			currentIndexTo = combatLogLine.indexOf("]", currentIndexFrom);
			String event = combatLogLine.substring(currentIndexFrom, currentIndexTo);

			// Cast Value (e.g. damage, heal or gain resource like (1689*)
			// The * means critical
			currentIndexFrom = currentIndexTo + 3; // skip ] (
			currentIndexTo = combatLogLine.indexOf(")", currentIndexFrom);
			String castValue = combatLogLine.substring(currentIndexFrom, currentIndexTo);

			// Was the Damage absorbed? like (1258 Elementar {836045448940875}
			// (1258 absorbiert {836045448945511}))
			String absorbedValue = null;
			if (combatLogLine.contains(Constants.ABSORBED)) {
				currentIndexTo = combatLogLine.indexOf(")", currentIndexFrom);
				currentIndexTo++;
				
				// first ( in damage string
				int absorbedIndex = combatLogLine.indexOf("(", currentIndexFrom);;
				absorbedValue = combatLogLine.substring(absorbedIndex, currentIndexTo);
			}

			// Aggro Value  like <1689>
			// This tag is optional, so check the string size
			currentIndexFrom = currentIndexTo + 3; // skip ) <

			String aggroValue = null;
			if (currentIndexFrom < combatLogLine.length()) {
				currentIndexTo = combatLogLine.indexOf(">", currentIndexFrom);
				aggroValue = combatLogLine.substring(currentIndexFrom, currentIndexTo);
			}

			createAndAddData(date, fromName, toName, castName, event, castValue, aggroValue, absorbedValue);
		} catch (Exception e) {
			// Skip this line, may be crap
			StartCombatLog.getLogger().log(Level.SEVERE, "Can't parse this line: " + combatLogLine, e);
		}
	}

	private void createAndAddData(Date date, String fromName, String toName, String castName, String eventString,
			String castValue, String aggroValue, String absorbedValue) {
		// search and determine from Character
		AbstractCharacter fromGlobalChar = determineGlobalCharacter(fromName);
		// search and determine to Character
		AbstractCharacter toGlobalChar = determineGlobalCharacter(toName);

		Event event = determineEvent(eventString);

		if (event == Event.FIGHT_BEGIN) {
			createNextFight(date);
			
			if (null == DataStore.ownerOfTheCurrentLogFile || DataStore.ownerOfTheCurrentLogFile.length() == 0) {
				DataStore.ownerOfTheCurrentLogFile = fromGlobalChar.getName();
			}
		} else if (event == Event.FIGHT_END) {
			closeFight(date);
		} else if (event == Event.DAMAGE) {
			// Sometimes a fight begin is missing in the log
			// but i presume that damage has something to do with a fight
			if (null == activeFight) {
				createNextFight(date);
			}

			// global
			parseAndSaveDamage(date, castName, castValue, aggroValue, absorbedValue, fromGlobalChar, toGlobalChar);

			// search and determine from Character
			AbstractCharacter fromChar = determineFightCharacter(fromName);
			// search and determine to Character
			AbstractCharacter toChar = determineFightCharacter(toName);

			parseAndSaveDamage(date, castName, castValue, aggroValue, absorbedValue, fromChar, toChar);
		} else if (event == Event.HEAL) {
			// Heal could happen outside a fight, so ignore it at first.
			if (null != activeFight) {
				// global
				parseAndSaveHeal(date, castName, castValue, aggroValue, fromGlobalChar, toGlobalChar);
	
				// search and determine from Character
				AbstractCharacter fromChar = determineFightCharacter(fromName);
				// search and determine to Character
				AbstractCharacter toChar = determineFightCharacter(toName);
	
				parseAndSaveHeal(date, castName, castValue, aggroValue, fromChar, toChar);
			}
		} else if (event == Event.DEAD) {
			// Dead could happen outside a fight, so ignore it at first.
			if (null != activeFight) {			
				createDead(date, eventString, fromGlobalChar, toGlobalChar);
				
				// search and determine from Character
				AbstractCharacter fromChar = determineFightCharacter(fromName);
				// search and determine to Character
				AbstractCharacter toChar = determineFightCharacter(toName);
				
				createDead(date, eventString, fromChar, toChar);
			}
		} else if (event == Event.FALLING_DAMAGE) {
			// Falling damage could happen outside a fight, so ignore it at first.
			if (null != activeFight) {
				// global
				parseAndSaveFallingDamage(date, castValue, fromGlobalChar);
	
				// search and determine to Character
				AbstractCharacter fromChar = determineFightCharacter(fromName);
	
				parseAndSaveFallingDamage(date, castValue, fromChar);
			}
		}
		
		setEndDate(date);
	}

	private void createDead(Date date, String eventString, AbstractCharacter fromChar, AbstractCharacter toChar) {
		Death dead = new Death(eventString);
		dead.setFromCharacter(fromChar);
		dead.setToCharacter(toChar);
		dead.setDate(date);
		
		if (fromChar instanceof HumanPlayerCharacter) {
			((HumanPlayerCharacter)fromChar).getDeads().add(dead);
			
			if (fromChar.getDamageDealed().size() > 0) {
				dead.setReason(fromChar.getDamageDealed().get(fromChar.getDamageDealed().size() - 1));
			}
		}
					
		if (toChar instanceof HumanPlayerCharacter) {
			// if death by accident: [23:57:34.501] [@Knocker] [@Knocker]
			if (!toChar.equals(fromChar)) {
				((HumanPlayerCharacter)toChar).getDeads().add(dead);
			}
			
			if (toChar.getDamageReceived().size() > 0) {
				dead.setReason(toChar.getDamageReceived().get(toChar.getDamageReceived().size() - 1));
			}
		}
	}

	private void setEndDate(Date date) {
		// for the case the end fight is missing in the log file
		if (null != activeFight) {
			activeFight.setEndDate(date);
	    	DataStore.endDate = date;
		}
	}

	private void closeFight(Date date) {
		setEndDate(date);
		activeFight = null;
	}
	
	private void createNextFight(Date date) {
		if (null != activeFight) {
			closeFight(date);
		}

		if (null == DataStore.startDate) {
			DataStore.startDate = date;
		}
		activeFight = new Fight(DataStore.nextFightName());
		activeFight.setStartDate(date);
		DataStore.fights.add(activeFight);
	}

	private void parseAndSaveHeal(Date date, String castName, String castValue, String aggroValue,
			AbstractCharacter fromChar, AbstractCharacter toChar) {
		Heal heal = new Heal(castName);
		heal.setDate(date);

		// determine cast value
		int to = castValue.indexOf(" ");
		String castValueInt = null;
		if (to > -1) {
			castValueInt = castValue.substring(0, to);
		} else {
			castValueInt = castValue;
		}

		// remove a possible *
		int sternIndex = castValueInt.indexOf("*");
		if (sternIndex > -1) {
			castValueInt = castValueInt.substring(0, sternIndex);
		}

		if (null != castValueInt) {
			heal.setValue(Integer.valueOf(castValueInt));
		}

		if (null != aggroValue) {
			heal.setAggro(Integer.valueOf(aggroValue));
		}

		heal.setName(castName);

		if (castValue.contains("*")) {
			heal.setCritical(true);
		}

		if (fromChar instanceof HumanPlayerCharacter) {
			heal.setFromCharacter(fromChar);
			((HumanPlayerCharacter) fromChar).addHealDealed(heal);
		}
		if (toChar instanceof HumanPlayerCharacter) {
			heal.setToCharacter(toChar);
			((HumanPlayerCharacter) toChar).addHealReceived(heal);
		}
	}

	private void parseAndSaveDamage(Date date, String castName, String castValue, String aggroValue, String absorbedValue,
			AbstractCharacter fromChar, AbstractCharacter toChar) {
		Damage damage = new Damage(castName);
		damage.setDate(date);
		// determine cast value
		int to = castValue.indexOf(" ");
		String castValueInt = null;
		if (to > -1) {
			castValueInt = castValue.substring(0, to);
		} else {
			castValueInt = castValue;
		}

		// remove a possible *
		int sternIndex = castValueInt.indexOf("*");
		if (sternIndex > -1) {
			castValueInt = castValueInt.substring(0, sternIndex);
		}

		if (null != castValueInt) {
			damage.setValue(Integer.valueOf(castValueInt));
		}
		if (null != aggroValue) {
			damage.setAggro(Integer.valueOf(aggroValue));
		}

		// determine damage kind
		int from = castValue.indexOf(" ");
		from++;
		to = castValue.indexOf(" ", from);
		if (to > -1) {
			damage.setKind(castValue.substring(from, to));
		}

		damage.setName(castName);

		if (castValue.contains("*")) {
			damage.setCritical(true);
		}

		damage.setFromCharacter(fromChar);
		damage.setToCharacter(toChar);
		
		if (null != fromChar) {
			fromChar.addDamageDealed(damage);
		}
		
		if (null != toChar) {
			toChar.addDamageReceived(damage);
		}
		
		// Was any damage absorbed?
		// The string looks like this: (1410 absorbiert {836045448945511})
		if (null != absorbedValue) {
			from = absorbedValue.indexOf("(");
			from++; // remove the (
		    to = absorbedValue.indexOf(" ");
			
			AbsorbedDamage absorbedDamage = new AbsorbedDamage(castName);
			absorbedDamage.setValue(Integer.valueOf(absorbedValue.substring(from, to)));
			
			absorbedDamage.setDate(damage.getDate());
			absorbedDamage.setDamageValue(damage.getValue());
			absorbedDamage.setKind(damage.getKind());
			absorbedDamage.setAggro(damage.getAggro());
			
			absorbedDamage.setFromCharacter(fromChar);
			absorbedDamage.setToCharacter(toChar);
			
			if (null != fromChar) {
				fromChar.addDamageAbsorbed(absorbedDamage);
			}
			
			if (null != toChar) {
				toChar.addDamageAbsorbed(absorbedDamage);
			}
		}
	}
	
	/**
	 * Parse String like
	 * 
	 * [23:57:34.500] [@Knocker] [] [] [Ereignis {836045448945472}: Fallschaden {836045448945484}] (18764)
	 * 
	 */
	private void parseAndSaveFallingDamage(Date date, String castValue, AbstractCharacter fromChar) {

		Damage damage = new Damage(Localization.getInstance().tr(Localization.FALLING_DAMAGE));
		damage.setDate(date);
		damage.setEvent(Event.FALLING_DAMAGE);
		
		// cast name is a empty string, do nothing here
		int to = castValue.indexOf(" ");
		String castValueInt = null;
		if (to > -1) {
			castValueInt = castValue.substring(0, to);
		} else {
			castValueInt = castValue;
		}

		// no * for crit
		
		if (null != castValueInt) {
			damage.setValue(Integer.valueOf(castValueInt));
		}

		// no damage kind

		damage.setFromCharacter(fromChar);
		fromChar.addDamageReceived(damage);
		damage.setToCharacter(fromChar);
	}	

	private Event determineEvent(String eventString) {
		Event result = null;

		// Ereignis
		// Effekt anwenden (Schaden, Heilunf, Buff)
		// Effekt entfernen
		// Wiederherstellen
		// Verbrauchen
		// Tod (ein Ereignis)
		// Kampf beginnen (ein Ereignis)
		// Kampf verlassen (ein Ereignis)

		if (eventString.contains(Constants.EVENT)) {
			result = Event.EVENT;

			if (eventString.contains(Constants.DEAD)) {
				result = Event.DEAD;
			} else if (eventString.contains(Constants.FIGHT_BEGIN)) {
				result = Event.FIGHT_BEGIN;
			} else if (eventString.contains(Constants.FIGHT_END)) {
				result = Event.FIGHT_END;
			} else if (eventString.contains(Constants.FALLING_DAMAGE)) {
				result = Event.FALLING_DAMAGE;
			}
		} else if (eventString.contains(Constants.APPLY_EFFECT)) {
			result = Event.APPLY_EFFECT;

			if (eventString.contains(Constants.DAMAGE)) {
				result = Event.DAMAGE;
			} else if (eventString.contains(Constants.HEAL)) {
				result = Event.HEAL;
			} else if (eventString.contains(Constants.ABSORBED)) {
				result = Event.ABSORBED_DAMAGE;
			}
		} else if (eventString.contains(Constants.REMOVE_EFFECT)) {
			result = Event.REMOVE_EFFECT;
		} else if (eventString.contains(Constants.CONSUME)) {
			result = Event.CONSUME;
		} else if (eventString.contains(Constants.RESTORE)) {
			result = Event.RESTORE;
		} 

		return result;
	}

	private AbstractCharacter determineFightCharacter(String name) {
		return determineCharacter(name, activeFight.getHumanPlayerCharacters(), activeFight.getNonPlayerCharacters());
	}

	private AbstractCharacter determineGlobalCharacter(String name) {
		return determineCharacter(name, DataStore.humanPlayerCharacters, DataStore.nonPlayerCharacters);
	}

	private AbstractCharacter determineCharacter(String name, List<HumanPlayerCharacter> humanPlayerCharacters,
			List<NonPlayerCharacter> nonPlayerCharacters) {
		AbstractCharacter result = null;

		if (name.startsWith("@")) {
			String userName = name.substring(1);

			result = new HumanPlayerCharacter(userName);
			int index = humanPlayerCharacters.indexOf(result);

			if (index > -1) {
				result = humanPlayerCharacters.get(index);
			} else {
				humanPlayerCharacters.add((HumanPlayerCharacter) result);
			}
		} else if (name.length() > 0) {
			result = new NonPlayerCharacter(name);
			int index = nonPlayerCharacters.indexOf(result);

			if (index > -1) {
				result = nonPlayerCharacters.get(index);
			} else {
				nonPlayerCharacters.add((NonPlayerCharacter) result);
			}
		}

		return result;
	}

	private Date calculateDate(String dateString) throws ParseException {
		Date date = null;
		date = Constants.dateFormat.parse(dateString);

		calender.setTimeInMillis(date.getTime());
		calender.set(Calendar.DAY_OF_MONTH, day);
		// check if the date changes
		if (oldDay > calender.getTimeInMillis()) {
			day++;
			calender.set(Calendar.DAY_OF_MONTH, day);
		}
		oldDay = calender.getTimeInMillis();

		return new Date(calender.getTimeInMillis());
	}
}
