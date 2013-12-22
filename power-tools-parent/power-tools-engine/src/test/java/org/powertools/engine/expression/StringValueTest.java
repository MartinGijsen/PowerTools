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
import static org.junit.Assert.*;


public class StringValueTest {
	private final StringValue mEmptyString	= new StringValue ("");
	private final StringValue mShortString	= new StringValue ("a");
	private final StringValue mLongString	= new StringValue ("abcdefghijklmnopqrstuvwxyz0123456789");

	
	@Test
	public void testGetType () {
		assertEquals ("string", mEmptyString.getType ());
	}

	@Test
	public void testEqual () {
		assertEquals ("true", mEmptyString.equal (new StringValue ("")).toString ());
		assertEquals ("true", mShortString.equal (new StringValue ("a")).toString ());
		assertEquals ("false", mShortString.equal (mLongString).toString ());
	}

	@Test
	public void testUnequal () {
		assertEquals ("false", mEmptyString.unequal (new StringValue ("")).toString ());
		assertEquals ("false", mShortString.unequal (new StringValue ("a")).toString ());
		assertEquals ("true", mShortString.unequal (mLongString).toString ());
	}

	
	@Test
	public void testToStringValue () {
		assertEquals ("a", mShortString.toStringValue ().toString ());
	}

	@Test
	public void testToRealValue () {
		assertEquals ("1.23", new StringValue ("1.23").toRealValue ().toString ());
	}

	@Test
	public void testToIntegerValue () {
		assertEquals ("123", new StringValue ("123").toIntegerValue ().toString ());
	}

	@Test
	public void testToBooleanValue () {
		assertEquals ("true", new StringValue ("true").toBooleanValue ().toString ());
	}

	@Test
	public void testToDateValue () {
		Calendar calendar = GregorianCalendar.getInstance ();
		calendar.set (Calendar.YEAR, 2000);
		calendar.set (Calendar.MONTH, 0);
		calendar.set (Calendar.DAY_OF_MONTH, 1);		
		assertEquals ("01-01-2000", new DateValue (calendar).toStringValue ().toString ());
	}
}
