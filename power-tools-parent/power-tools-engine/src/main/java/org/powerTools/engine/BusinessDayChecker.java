package org.powerTools.engine;

import java.util.Calendar;


public class BusinessDayChecker {
	public boolean isBusinessDay (Calendar date) {
		int day = date.get (Calendar.DAY_OF_WEEK);
		return day >= Calendar.MONDAY && day <= Calendar.FRIDAY;
	}
}