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

package org.powerTools.engine;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ContextTest {
	@Before
	public void setUp () throws Exception {
		
	}

	@After
	public void tearDown () throws Exception {
		
	}

	
//	@Test
//	public void testContextString () {
//		Context context = new Context (PATH);
//		assertEquals (PATH, context.mResultsDirectory);
//	}
//
//	@Test
//	public void testContextDateString () {
//		Date date = GregorianCalendar.getInstance ().getTime ();
//		Context context = new Context (date, PATH);
//		assertEquals (date, context.mStartTime);
//		assertEquals (PATH, context.mResultsDirectory);
//	}
//
//	@Test
//	public void testContextStringString () {
//		Context context = new Context (PATH, LOGFILE_NAME);
//		assertEquals (PATH, context.mResultsDirectory);
//		assertEquals (LOGFILE_NAME, context.mLogFileName);
//		assertEquals (PATH + '/' + LOGFILE_NAME, context.mFullLogFilePath);
//	}
//
//	@Test
//	public void testContextDateStringString () {
//		Date date = GregorianCalendar.getInstance ().getTime ();
//		Context context = new Context (date, PATH, LOGFILE_NAME);
//		assertEquals (date, context.mStartTime);
//		assertEquals (PATH, context.mResultsDirectory);
//		assertEquals (LOGFILE_NAME, context.mLogFileName);
//		assertEquals (PATH + '/' + LOGFILE_NAME, context.mFullLogFilePath);
//	}


	// private members
	private static final String PATH			= "c:/test";
	private static final String LOGFILE_NAME	= "logfile";
}