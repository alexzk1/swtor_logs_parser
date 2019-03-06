package de.swtor.combatlog.analyzer;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import de.swtor.combatlog.data.Damage;
import de.swtor.combatlog.data.Fight;
import de.swtor.combatlog.data.HumanPlayerCharacter;

public class DamageAnalyzer extends AbstractAnalyzer {
	
	private static class DamageRow extends ResultRow {
		public DamageRow(String casterName, String castName) {
			super(casterName, castName);
		}
		
		public void addDamage(long damage, boolean crit) {
			sum = sum.add(BigInteger.valueOf(damage));
			countCast++;
			
			if (crit) {
				countCrits++;
			}
			
			setMinimum(damage);
			setMaximum(damage);
			
			calculateAverage();
		}			

		public BigInteger getSumDamage() {
			return sum;
		}

		public int getCountDamage() {
			return countCast;
		}
	}
	
	public DamageAnalyzer() {
		
	}

	public String[][] summerizeDamageDealedForUser(String username, Fight fight) {
		HumanPlayerCharacter humanPlayerCharacter = determineHumanPlayerCharacter(username, fight);
		
		
		Hashtable<String, DamageRow> damageTable = new Hashtable<String, DamageRow>();
		DamageRow totalRow = new DamageRow(username, "TOTAL");		
		
		if (null != humanPlayerCharacter) {
			for (Damage damage : humanPlayerCharacter.getDamageDealed()) {
				totalRow.addDamage(damage.getValue(), damage.isCritical());
				
				DamageRow damageRow = damageTable.get(damage.getFromCharacter().getName()+damage.getName());
				if (null == damageRow) {
					damageRow = new DamageRow(username, damage.getName());
					damageTable.put(damage.getFromCharacter().getName()+damage.getName(), damageRow);
				}
				
				damageRow.addDamage(damage.getValue(), damage.isCritical());
			}
		} 
		else {
			String[][] emptyResult = new String[1][10];
			emptyResult[0][0] = "NO DATA";
			
			return emptyResult;
		}
		
		// Columns are: "Name", "Number", "Number %",  "Damage", "Damage %", "Crits", "Crits %", "Minimum", "Maximum", "Average"
		String[][] result = new String[damageTable.size()+1][10];
		
		// first the total row
		result[0][0] = totalRow.getCastDisplayName();
		result[0][1] = String.valueOf(totalRow.getCountDamage());
		result[0][2] = "100";
		result[0][3] = totalRow.getSumDamage().toString();
		result[0][4] = "100";
		result[0][5] = String.valueOf(totalRow.getCountCrits());
		result[0][6] = calculatePercent(totalRow.getCountCrits(), totalRow.getCountDamage());
		result[0][7] = String.valueOf(totalRow.getMinimum());
		result[0][8] = String.valueOf(totalRow.getMaximum());
		result[0][9] = String.valueOf(totalRow.getAverage());
		
		List<DamageRow> damageRowList = new ArrayList<DamageRow>(damageTable.values());
		Collections.sort(damageRowList, sResultRowComperator);
		
		int row = 1;
		for (DamageRow damageRow : damageRowList) {
			result[row][0] = damageRow.getCastDisplayName();
			result[row][1] = String.valueOf(damageRow.getCountDamage());
			result[row][2] = calculatePercent(damageRow.getCountDamage(), totalRow.getCountDamage());
			result[row][3] = damageRow.getSumDamage().toString();
			result[row][4] = calculatePercent(damageRow.getSumDamage().longValue(), totalRow.getSumDamage().longValue());
			result[row][5] = String.valueOf(damageRow.getCountCrits());
			result[row][6] = calculatePercent(damageRow.getCountCrits(), damageRow.getCountDamage());
			result[row][7] = String.valueOf(damageRow.getMinimum());
			result[row][8] = String.valueOf(damageRow.getMaximum());
			result[row][9] = String.valueOf(damageRow.getAverage());
			
			row++;
		}
		
		return result;
	}

	public String[][] summerizeDamageReceivedForUser(String username, Fight fight) {
		HumanPlayerCharacter humanPlayerCharacter =  determineHumanPlayerCharacter(username, fight);
		
		Hashtable<String, DamageRow> damageTable = new Hashtable<String, DamageRow>();
		DamageRow totalRow = new DamageRow(username, "TOTAL");		
		
		if (null != humanPlayerCharacter) {
			for (Damage damage : humanPlayerCharacter.getDamageReceived()) {
				if (null != damage.getFromCharacter()) {
					totalRow.addDamage(damage.getValue(), damage.isCritical());
					
					DamageRow damageRow = damageTable.get(damage.getFromCharacter().getName()+damage.getName());
					if (null == damageRow) {
						damageRow = new DamageRow(damage.getFromCharacter().getName(), damage.getName());
						damageTable.put(damage.getFromCharacter().getName()+damage.getName(), damageRow);
					}
					
					damageRow.addDamage(damage.getValue(), damage.isCritical());
				}
			}
		} 
		else {
			String[][] emptyResult = new String[1][11];
			emptyResult[0][0] = "NO DATA";
			
			return emptyResult;
		}
		
		// Columns are: "Player Name", "Damage Name", "Number", "Number %",  "Damage", "Damage %", "Crits", "Crits %", "Minimum", "Maximum", "Average"
		String[][] result = new String[damageTable.size()+1][11];
		
		// first the total row
		result[0][0] = "SUM";
		result[0][1] = totalRow.getCastDisplayName();
		result[0][2] = String.valueOf(totalRow.getCountDamage());
		result[0][3] = "100";
		result[0][4] = totalRow.getSumDamage().toString();
		result[0][5] = "100";
		result[0][6] = String.valueOf(totalRow.getCountCrits());
		result[0][7] = calculatePercent(totalRow.getCountCrits(), totalRow.getCountDamage());
		result[0][8] = String.valueOf(totalRow.getMinimum());
		result[0][9] = String.valueOf(totalRow.getMaximum());
		result[0][10] = String.valueOf(totalRow.getAverage());
		
		List<DamageRow> damageRowList = new ArrayList<DamageRow>(damageTable.values());
		Collections.sort(damageRowList, sResultRowComperator);
		
		int row = 1;
		for (DamageRow damageRow : damageRowList) {
			result[row][0] = damageRow.getCasterDisplayName();
			result[row][1] = damageRow.getCastDisplayName();
			result[row][2] = String.valueOf(damageRow.getCountDamage());
			result[row][3] = calculatePercent(damageRow.getCountDamage(), totalRow.getCountDamage());
			result[row][4] = damageRow.getSumDamage().toString();
			result[row][5] = calculatePercent(damageRow.getSumDamage().longValue(), totalRow.getSumDamage().longValue());
			result[row][6] = String.valueOf(damageRow.getCountCrits());
			result[row][7] = calculatePercent(damageRow.getCountCrits(), damageRow.getCountDamage());
			result[row][8] = String.valueOf(damageRow.getMinimum());
			result[row][9] = String.valueOf(damageRow.getMaximum());
			result[row][10] = String.valueOf(damageRow.getAverage());
			
			row++;
		}
		
		return result;
	}
}
