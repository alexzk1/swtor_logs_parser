package de.swtor.combatlog.analyzer;

import java.math.BigDecimal;
import java.util.List;

import de.swtor.combatlog.Utilities;
import de.swtor.combatlog.data.AbsorbedDamage;
import de.swtor.combatlog.data.CalculatedResult;
import de.swtor.combatlog.data.Damage;
import de.swtor.combatlog.data.DataStore;
import de.swtor.combatlog.data.Death;
import de.swtor.combatlog.data.Fight;
import de.swtor.combatlog.data.Heal;
import de.swtor.combatlog.data.HumanPlayerCharacter;

/*
 * Copyright (c) 2012 Thomas Rath
 */

public class MasterAnalyzer {

	private DamageAnalyzer damageAnalyzer = new DamageAnalyzer();
	private HealAnalyzer healAnalyzer = new HealAnalyzer();

	private int totalHeal = 0;
	private int totalDamage = 0;
	private int totalDamageTime = 0;
	private int totalHealTime = 0;

	public MasterAnalyzer() {
	}

	public void calculateAllResults(String userName) {
		totalHeal = 0;
		totalDamage = 0;
		totalDamageTime = 0;
		totalHealTime = 0;
		
		DataStore.calculatedResult = calculateResult(DataStore.mainHumanPlayerName, null);

		for (Fight fight : DataStore.fights) {
			fight.setCalculatedResult(calculateResult(DataStore.mainHumanPlayerName, fight));
		}

		calculateFightValues();
		
		calculateTotalHPSDPS();
	}

	private CalculatedResult calculateResult(String username, Fight fight) {
		CalculatedResult calculatedResult = new CalculatedResult();

		calculatedResult.setDamageDealed(damageAnalyzer.summerizeDamageDealedForUser(username, fight));
		calculatedResult.setDamageReceived(damageAnalyzer.summerizeDamageReceivedForUser(username, fight));
		calculatedResult.setHealedDealed(healAnalyzer.summerizeHealedForUser(username, fight));
		calculatedResult.setHealingReceived(healAnalyzer.summerizeHealingReceivedForUser(username, fight));
		calculatedResult.setDeaths(calculateKillsDeathsList(username, fight));
		calculatedResult.setAbsorbedDamages(calculateAbsorbedDamageList(username, fight));

		return calculatedResult;
	}

	private void calculateFightValues() {
		for (Fight fight : DataStore.fights) {
			long seconds = calculateSeconds(fight);
			calculateHPS(fight, seconds);
			calculateDPS(fight, seconds);
			calculateTargets(fight);
		}
	}

	private void calculateTotalHPSDPS() {
		// total dps
		if (totalDamageTime > 0) {
			BigDecimal calulator = new BigDecimal(totalDamage);
			calulator = calulator.divide(BigDecimal.valueOf(totalDamageTime), 2, BigDecimal.ROUND_HALF_EVEN);
			DataStore.totalDPS = calulator.doubleValue();
		} else {
			DataStore.totalDPS = 0;
		}

		// total hps
		if (totalHealTime > 0) {
			BigDecimal calulator = new BigDecimal(totalHeal);
			calulator = calulator.divide(BigDecimal.valueOf(totalHealTime), 2, BigDecimal.ROUND_HALF_EVEN);
			DataStore.totalHPS = calulator.doubleValue();
		} else {
			DataStore.totalHPS = 0;
		}
	}

	private long calculateSeconds(Fight fight) {
		long seconds = 0;

		if (null != fight.getEndDate() && null != fight.getStartDate()) {
			seconds = fight.getEndDate().getTime() - fight.getStartDate().getTime();
		}

		return seconds / 1000;
	}

	private void calculateHPS(Fight fight, long seconds) {
		double hps = 0;

		if (seconds > 0) {
			HumanPlayerCharacter character = fight.findHumanPlayerCharacter(DataStore.mainHumanPlayerName);

			if (null != character) {
				// count time only if the char done something
				if (!character.getHealDealed().isEmpty()) {
					totalHealTime += seconds;
				}

				for (Heal heal : character.getHealDealed()) {
					hps += heal.getValue();
					totalHeal += heal.getValue();
				}

				BigDecimal calulator = new BigDecimal(hps);
				calulator = calulator.divide(BigDecimal.valueOf(seconds), 2, BigDecimal.ROUND_HALF_EVEN);
				hps = calulator.doubleValue();
			}
		}

		fight.setHps(hps);
	}

	private void calculateDPS(Fight fight, long seconds) {
		double dps = 0;

		if (seconds > 0) {
			HumanPlayerCharacter character = fight.findHumanPlayerCharacter(DataStore.mainHumanPlayerName);

			if (null != character) {
				// count time only if the char done something
				if (!character.getDamageDealed().isEmpty()) {
					totalDamageTime += seconds;
				}

				for (Damage damage : character.getDamageDealed()) {
					dps += damage.getValue();
					totalDamage += damage.getValue();
				}

				BigDecimal calulator = new BigDecimal(dps);
				calulator = calulator.divide(BigDecimal.valueOf(seconds), 2, BigDecimal.ROUND_HALF_EVEN);
				dps = calulator.doubleValue();
			}
		}

		fight.setDps(dps);
	}

	private void calculateTargets(Fight fight) {
		String targets = "";

		HumanPlayerCharacter character = fight.findHumanPlayerCharacter(DataStore.mainHumanPlayerName);
		if (null != character) {
			for (Damage damage : character.getDamageDealed()) {
				String targetName = "";
				if (null != damage.getToCharacter()) {
					targetName = Utilities.displayName(damage.getToCharacter().getName());
				}

				// if a user dies during a fight, he sometime is listed as
				// target
				if (!targetName.equals(DataStore.mainHumanPlayerName)) {
					if (targets.indexOf(targetName) < 0) {
						if (targets.length() > 0) {
							targets = targets + ";" + targetName;
						} else {
							targets = targetName;
						}
					}
				}
			}
		}

		fight.setTargets(targets);
	}

	private List<Death> calculateKillsDeathsList(String userName, Fight fight) {
		List<Death> result = null;

		HumanPlayerCharacter humanPlayerCharacter;
		if (null != fight) {
			humanPlayerCharacter = fight.findHumanPlayerCharacter(userName);
		} else {
			humanPlayerCharacter = DataStore.findHumanPlayerCharacter(userName);
		}

		if (null != humanPlayerCharacter) {
			result = humanPlayerCharacter.getDeads();
		}

		return result;
	}

	private List<AbsorbedDamage> calculateAbsorbedDamageList(String userName, Fight fight) {
		List<AbsorbedDamage> result = null;

		HumanPlayerCharacter humanPlayerCharacter;
		if (null != fight) {
			humanPlayerCharacter = fight.findHumanPlayerCharacter(userName);
		} else {
			humanPlayerCharacter = DataStore.findHumanPlayerCharacter(userName);
		}

		if (null != humanPlayerCharacter) {
			result = humanPlayerCharacter.getDamageAbsorbed();
		}

		return result;
	}
}
