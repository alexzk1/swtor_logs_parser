package de.swtor.combatlog.analyzer;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;

import de.swtor.combatlog.Utilities;
import de.swtor.combatlog.data.DataStore;
import de.swtor.combatlog.data.Fight;
import de.swtor.combatlog.data.HumanPlayerCharacter;

public abstract class AbstractAnalyzer {
	
	public static class ResultRow {
		public ResultRow(String casterName, String castName) {
			this.casterName = casterName;
			this.castName = castName;
			
			casterDisplayName = Utilities.displayName(this.casterName);
			if (null == casterDisplayName || casterDisplayName.length() == 0) {
				casterDisplayName = " "; // for sorting
			}
			
			castDisplayName = Utilities.displayName(this.castName);
		}
		
		protected String casterName;
		protected String casterDisplayName;
		protected String castName;
		protected String castDisplayName;
		protected BigInteger sum = new BigInteger("0");
		protected int countCast = 0;
		protected int countCrits  = 0;
		protected long minimum = 0;
		protected long maximum = 0;
		protected long average  = 0;	
		
		protected void calculateAverage() {
			if (countCast > 0) {
				average = sum.longValue()/countCast;
			} else {
				average = 0;
			}
		}
					
		public String getCasterName() {
			return casterName;
		}
		
		public String getCasterDisplayName() {
			return casterDisplayName;
		}
		
		public String getCastName() {
			return castName;
		}
		
		public String getCastDisplayName() {
			return castDisplayName;
		}
		
		public int getCountCrits() {
			return countCrits;
		}

		public long getMinimum() {
			return minimum;
		}

		public void setMinimum(long minimum) {
			if (this.minimum == 0) {
				this.minimum = minimum;
			}
			
			if (minimum < this.minimum) {
				this.minimum = minimum;
			}
		}

		public long getMaximum() {
			return maximum;
		}

		public void setMaximum(long maximum) {
			if (maximum > this.maximum) {
				this.maximum = maximum;
			}
		}

		public BigInteger getSum() {
			return sum;
		}

		public long getAverage() {
			return average;
		}	
		
		public String sortString() {			
			return casterDisplayName+castDisplayName;
		}
	}
	
	public static class ResultRowComperator implements Comparator<ResultRow> {

		@Override
		public int compare(ResultRow o1, ResultRow o2) {			
			return o1.sortString().compareTo(o2.sortString());
		}		
	}
	
	public static ResultRowComperator sResultRowComperator = new ResultRowComperator();

	protected String calculatePercent(long part, long total) {
		if (total > 0) {
			BigDecimal partBig = BigDecimal.valueOf(part, 2);
			BigDecimal totalBig = BigDecimal.valueOf(total, 2);
			BigDecimal resultBig = partBig.divide(totalBig, BigDecimal.ROUND_HALF_UP);
			resultBig = resultBig.multiply(new BigDecimal("100"));
			
			return resultBig.toBigInteger().toString();
		}
		
		return "0";
	}

	protected HumanPlayerCharacter determineHumanPlayerCharacter(String username, Fight fight) {
		HumanPlayerCharacter humanPlayerCharacter;
		if (null != fight) {
			humanPlayerCharacter = fight.findHumanPlayerCharacter(username);
		} else {
			humanPlayerCharacter = DataStore.findHumanPlayerCharacter(username);
		}
		return humanPlayerCharacter;
	}
	
}