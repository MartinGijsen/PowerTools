/* Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools engine.
 *
 * The PowerTools engine is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine.expression;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.powertools.engine.BusinessDayChecker;
import org.powertools.engine.ExecutionException;


final class DateValue extends Value {
    static BusinessDayChecker mBusinessDayChecker = new BusinessDayChecker ();

    private static SimpleDateFormat mFormat = new SimpleDateFormat ("dd-MM-yyyy");

    private Calendar mDate;


    public DateValue (Calendar date) {
        mDate = (Calendar) date.clone ();
    }

    public DateValue (String stringValue) {
        try {
            Date date = mFormat.parse (stringValue);
            mDate = GregorianCalendar.getInstance ();
            mDate.setTime (date);
        } catch (ParseException e) {
            throw new ExecutionException ("Date parse error: " + stringValue);
        }
    }


    @Override
    String getType () {
        return "date";
    }


    @Override
    public Value equal (Value v) {
        return new BooleanValue (mDate.equals (v.toDateValue ().mDate));
    }

    @Override
    public Value unequal (Value v) {
        return new BooleanValue (!mDate.equals (v.toDateValue ().mDate));
    }


    Value add (String number, String period) {
        int nr = parseInteger (number);
        return addPeriod (nr, period);
    }

    Value subtract (String number, String period) {
        int nr = parseInteger (number);
        return addPeriod (-nr, period);
    }

    @Override
    public StringValue toStringValue () {
        return new StringValue (toString());
    }

    @Override
    public DateValue toDateValue () {
        return this;
    }

    @Override
    public String toString () {
        return mFormat.format (mDate.getTime ());
    }

    private int parseInteger (String number) {
        try {
            return Integer.parseInt (number);
        } catch (NumberFormatException e) {
            throw new ExecutionException ("Number parse error: " + number);
        }
    }

    private Value addPeriod (int number, String period) {
        DateValue newDate = new DateValue (mDate);
        if ("days".equals (period)) {
            newDate.mDate.add (Calendar.DAY_OF_MONTH, number);
        } else if ("weeks".equals (period)) {
            newDate.mDate.add (Calendar.WEEK_OF_YEAR, number);
        } else if ("months".equals (period)) {
            newDate.mDate.add (Calendar.MONTH, number);
        } else if ("years".equals (period)) {
            newDate.mDate.add (Calendar.YEAR, number);
        } else {
            newDate.addBusinessDays (number);
        }
        return newDate;
    }

    private void addBusinessDays (int number) {
        int step = (number < 0 ? -1 : 1);
        for (int counter = 0; counter != number; counter += step) {
            do {
                mDate.add (Calendar.DAY_OF_MONTH, step);
            } while (!mBusinessDayChecker.isBusinessDay (mDate));
        }
    }
}
