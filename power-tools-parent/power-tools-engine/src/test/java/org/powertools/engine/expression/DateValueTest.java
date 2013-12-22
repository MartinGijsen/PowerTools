/*	Copyright 2013 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.expression;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.powertools.engine.ExecutionException;


public class DateValueTest {
	private DateValue mDate;
	private DateValue mOtherDate;


	@Before
	public void beforeTest () {
		Calendar calendar;
		calendar = GregorianCalendar.getInstance ();
		calendar.set (Calendar.YEAR, 2000);
		calendar.set (Calendar.MONTH, 0);
		calendar.set (Calendar.DAY_OF_MONTH, 1);
		mDate		= new DateValue (calendar);
		calendar	= GregorianCalendar.getInstance ();
		calendar.add (Calendar.DATE, 1);
		mOtherDate	= new DateValue (calendar);
	}
	
	@Test
	public void testDateValue () {
		try {
			new DateValue ("something invalid");
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}

	@Test
	public void testGetType () {
		assertEquals ("date", mDate.getType ());
	}

	@Test
	public void testEqual () {
		assertEquals ("true", mDate.equal (mDate).toString ());
		assertEquals ("false", mDate.equal (mOtherDate).toString ());
	}

	@Test
	public void testUnequal () {
		assertEquals ("false", mDate.unequal (mDate).toString ());
		assertEquals ("true", mDate.unequal (mOtherDate).toString ());
	}

	@Test
	public void testAdd () {
		assertEquals ("02-01-2000", mDate.add ("1", "days").toString ());
		assertEquals ("01-02-2000", mDate.add ("1", "months").toString ());
		assertEquals ("01-01-2001", mDate.add ("1", "years").toString ());
		assertEquals ("07-01-2000", mDate.add ("5", "business").toString ());
		assertEquals ("01-01-2000", mDate.add ("0", "business").toString ());
		
		try {
			mDate.add ("a", "days");
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}

	@Test
	public void testSubtract () {
		assertEquals ("31-12-1999", mDate.subtract ("1", "days").toString ());
		assertEquals ("01-12-1999", mDate.subtract ("1", "months").toString ());
		assertEquals ("01-01-1999", mDate.subtract ("1", "years").toString ());
		assertEquals ("27-12-1999", mDate.subtract ("5", "business").toString ());
	}

	@Test
	public void testToStringValue () {
		assertEquals ("01-01-2000", mDate.toStringValue ().toString ());
	}

	@Test
	public void testToDateValue () {
		assertEquals ("01-01-2000", mDate.toDateValue ().toString ());
	}
}
