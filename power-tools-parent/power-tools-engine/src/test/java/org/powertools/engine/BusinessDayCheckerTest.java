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

package org.powertools.engine;

import java.util.Calendar;
import java.util.GregorianCalendar;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


public class BusinessDayCheckerTest {
	@Test
	public void testIsBusinessDay () {
		BusinessDayChecker checker = new BusinessDayChecker ();
		Calendar date = GregorianCalendar.getInstance ();
		date.set (Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		assertTrue (checker.isBusinessDay (date));
		date.set (Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		assertTrue (checker.isBusinessDay (date));
		date.set (Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		assertTrue (checker.isBusinessDay (date));
		date.set (Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		assertTrue (checker.isBusinessDay (date));
		date.set (Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		assertTrue (checker.isBusinessDay (date));
		date.set (Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		assertFalse (checker.isBusinessDay (date));
		date.set (Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		assertFalse (checker.isBusinessDay (date));
	}
}
