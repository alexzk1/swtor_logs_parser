package de.swtor.combatlog.analyzer;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import de.swtor.combatlog.data.Fight;
import de.swtor.combatlog.data.Heal;
import de.swtor.combatlog.data.HumanPlayerCharacter;

public class HealAnalyzer extends AbstractAnalyzer {
	
	private static class HealRow extends ResultRow {
		public HealRow(String casterName, String castName) {
			super(casterName, castName);
		}		
		
		public void addHeal(long heal, boolean crit) {
			sum = sum.add(BigInteger.valueOf(heal));
			countCast++;
			
			if (crit) {
				countCrits++;
			}
			
			setMinimum(heal);
			setMaximum(heal);
			
			calculateAverage();
		}				

		public BigInteger getSumHeal() {
			return sum;
		}

		public int getCountHeal() {
			return countCast;
		}

		public int getCountCrits() {
			return countCrits;
		}
	}
	
	public HealAnalyzer() {
		
	}
	
	public String[][] summerizeHealedForUser(String username, Fight fight) {
		HumanPlayerCharacter humanPlayerCharacter = determineHumanPlayerCharacter(username, fight);
		
		Hashtable<String, HealRow> healTable = new Hashtable<String, HealRow>();
		HealRow totalRow = new HealRow(username, "TOTAL");		
		
		if (null != humanPlayerCharacter) {
			for (Heal heal : humanPlayerCharacter.getHealDealed()) {
				if (heal.getToCharacter() instanceof HumanPlayerCharacter) {
					totalRow.addHeal(heal.getValue(), heal.isCritical());

					HealRow healRow = healTable.get(heal.getFromCharacter().getName() + heal.getName());
					if (null == healRow) {
						healRow = new HealRow(username, heal.getName());
						healTable.put(heal.getFromCharacter().getName() + heal.getName(), healRow);
					}

					healRow.addHeal(heal.getValue(), heal.isCritical());
				}
			}
		} 
		else {
			String[][] emptyResult = new String[1][10];
			emptyResult[0][0] = "NO DATA";
			
			return emptyResult;
		}
		
		// Columns are: "Name", "Number", "Number %",  "Heal", "Heal %", "Crits", "Crits %", "Minimum", "Maximum", "Average"
		String[][] result = new String[healTable.size()+1][10];
		
		// first the total row
		result[0][0] = totalRow.getCastDisplayName();
		result[0][1] = String.valueOf(totalRow.getCountHeal());
		result[0][2] = "100";
		result[0][3] = totalRow.getSumHeal().toString();
		result[0][4] = "100";
		result[0][5] = String.valueOf(totalRow.getCountCrits());
		result[0][6] = calculatePercent(totalRow.getCountCrits(), totalRow.getCountHeal());
		result[0][7] = String.valueOf(totalRow.getMinimum());
		result[0][8] = String.valueOf(totalRow.getMaximum());
		result[0][9] = String.valueOf(totalRow.getAverage());
		
		List<HealRow> healRowList = new ArrayList<HealRow>(healTable.values());
		Collections.sort(healRowList, sResultRowComperator);
		
		int row = 1;
		for (HealRow healRow : healRowList) {
			result[row][0] = healRow.getCastDisplayName();
			result[row][1] = String.valueOf(healRow.getCountHeal());
			result[row][2] = calculatePercent(healRow.getCountHeal(), totalRow.getCountHeal());
			result[row][3] = healRow.getSumHeal().toString();
			result[row][4] = calculatePercent(healRow.getSumHeal().longValue(), totalRow.getSumHeal().longValue());
			result[row][5] = String.valueOf(healRow.getCountCrits());
			result[row][6] = calculatePercent(healRow.getCountCrits(), healRow.getCountHeal());
			result[row][7] = String.valueOf(healRow.getMinimum());
			result[row][8] = String.valueOf(healRow.getMaximum());
			result[row][9] = String.valueOf(healRow.getAverage());
			
			row++;
		}
		
		return result;
	}

	public String[][] summerizeHealingReceivedForUser(String username, Fight fight) {
		HumanPlayerCharacter humanPlayerCharacter =  determineHumanPlayerCharacter(username, fight);
		
		Hashtable<String, HealRow> healTable = new Hashtable<String, HealRow>();
		HealRow totalRow = new HealRow("SUM", "TOTAL");		
		
		if (null != humanPlayerCharacter) {
			for (Heal heal : humanPlayerCharacter.getHealReceived()) {
				// sometimes there is no from char set, because of???, here are such examples:
				// [00:42:43.157] [] [@Todd] [Feldversorgung {2074172851224833}] [Effekt anwenden {836045448945477}: Heilung {836045448945500}] (0)
				// [00:42:43.158] [] [@Todd] [Feldversorgung {2074172851224576}] [Effekt entfernen {836045448945478}: Feldversorgung {2074172851224833}] ()
				if (null != heal.getFromCharacter()) {
					totalRow.addHeal(heal.getValue(), heal.isCritical());
					
					HealRow healRow = healTable.get(heal.getFromCharacter().getName()+heal.getName());
					if (null == healRow) {
						healRow = new HealRow(heal.getFromCharacter().getName(), heal.getName());
						healTable.put(heal.getFromCharacter().getName()+heal.getName(), healRow);
					}
					
					healRow.addHeal(heal.getValue(), heal.isCritical());
				}
			}
		} 
		else {
			String[][] emptyResult = new String[1][11];
			emptyResult[0][0] = "NO DATA";
			
			return emptyResult;
		}
		
		// Columns are: "Healer", "Name", "Number", "Number %",  "Heal", "Heal %", "Crits", "Crits %", "Minimum", "Maximum", "Average"
		String[][] result = new String[healTable.size()+1][11];
		
		// first the total row
		result[0][0] = totalRow.getCasterDisplayName();
		result[0][1] = totalRow.getCastDisplayName();
		result[0][2] = String.valueOf(totalRow.getCountHeal());
		result[0][3] = "100";
		result[0][4] = totalRow.getSumHeal().toString();
		result[0][5] = "100";
		result[0][6] = String.valueOf(totalRow.getCountCrits());
		result[0][7] = calculatePercent(totalRow.getCountCrits(), totalRow.getCountHeal());
		result[0][8] = String.valueOf(totalRow.getMinimum());
		result[0][9] = String.valueOf(totalRow.getMaximum());
		result[0][10] = String.valueOf(totalRow.getAverage());
		
		List<HealRow> healRowList = new ArrayList<HealRow>(healTable.values());
		Collections.sort(healRowList, sResultRowComperator);
		
		int row = 1;
		for (HealRow healRow : healRowList) {
			result[row][0] = healRow.getCasterDisplayName();
			result[row][1] = healRow.getCastDisplayName();
			result[row][2] = String.valueOf(healRow.getCountHeal());
			result[row][3] = calculatePercent(healRow.getCountHeal(), totalRow.getCountHeal());
			result[row][4] = healRow.getSumHeal().toString();
			result[row][5] = calculatePercent(healRow.getSumHeal().longValue(), totalRow.getSumHeal().longValue());
			result[row][6] = String.valueOf(healRow.getCountCrits());
			result[row][7] = calculatePercent(healRow.getCountCrits(), healRow.getCountHeal());
			result[row][8] = String.valueOf(healRow.getMinimum());
			result[row][9] = String.valueOf(healRow.getMaximum());
			result[row][10] = String.valueOf(healRow.getAverage());
			
			row++;
		}
		
		return result;
	}

}
