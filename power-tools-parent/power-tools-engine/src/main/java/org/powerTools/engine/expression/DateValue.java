/*	Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
 *
 *	This file is part of the PowerTools engine.
 *
 *	The PowerTools engine is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the License,
 *	or (at your option) any later version.
 *
 *	The PowerTools engine is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU Affero General Public License for more details.
 *
 *	You should have received a copy of the GNU Affero General Public License
 *	along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powerTools.engine.expression;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.powerTools.engine.ExecutionException;


final class DateValue extends Value {
	private static SimpleDateFormat mFormat = new SimpleDateFormat ("dd-MM-yyyy");
	
	private Calendar mDate;

	
	public DateValue (Calendar date) {
		mDate = date;
	}
	
	
	@Override
	public String getType () {
		return "date";
	}

	
	@Override
	public Value equal (Value v) {
		return new BooleanValue (mDate.equals (v.toDateValue ().mDate));
	}
	
	@Override
	public Value unequal (Value v) {
		return new BooleanValue (mDate.equals (v.toDateValue ().mDate));
	}
	
	
	Value add (String number, String period) {
		int nr = parseInteger(number);
		return addPeriod(nr, period);
	}


	Value subtract (String number, String period) {
		int nr = parseInteger(number);
		return addPeriod(0 - nr, period);
	}

	
	@Override
	public StringValue toStringValue () {
		throwException ("cannot make string from date");
		return null;
	}
	
	@Override
	public DateValue toDateValue () {
		return this;
	}

	@Override
	public String toString () {
		return mFormat.format (mDate.getTime ());
	}
	
	
	private int parseInteger(String number) {
		try 
		{
			return Integer.parseInt (number);
		}
		catch (NumberFormatException e) {
			throw new ExecutionException("Number parse error: " + number);
		}
	}
		
	private Value addPeriod (int number, String period) {
		if (period.equals ("days")) {
			mDate.add (Calendar.DAY_OF_MONTH, number);
		} else if (period.equals ("weeks")) {
			mDate.add (Calendar.WEEK_OF_YEAR, number);
		} else if (period.equals ("months")) {
			mDate.add (Calendar.MONTH, number);
		} else {
			mDate.add (Calendar.YEAR, number);
		}
		return this;
	}	
}