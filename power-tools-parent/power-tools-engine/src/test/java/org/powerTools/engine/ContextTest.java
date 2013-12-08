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

package org.powertools.engine;

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

	
	@Test
	public void testContextString () {
		Context context = new Context (PATH);
		assertEquals (context.mResultsBaseDirectory, PATH + "/");
		assertEquals (context.mResultsDirectory, context.mResultsBaseDirectory + Context.mDateFormat.format (context.mStartTime) + "/");
		assertEquals (context.mLogFileName, Context.LOG_FILE_NAME);
		assertEquals (context.mFullLogFilePath, context.mResultsDirectory + Context.LOG_FILE_NAME);
	}

	@Test
	public void testContextDateString () {
		Date date = GregorianCalendar.getInstance ().getTime ();
		Context context = new Context (date, PATH);
		assertEquals (context.mStartTime, date);
		assertEquals (context.mResultsBaseDirectory, PATH + "/");
		assertEquals (context.mResultsDirectory, context.mResultsBaseDirectory + Context.mDateFormat.format (date) + "/");
		assertEquals (context.mLogFileName, Context.LOG_FILE_NAME);
		assertEquals (context.mFullLogFilePath, context.mResultsDirectory + Context.LOG_FILE_NAME);
	}

	@Test
	public void testContextStringString () {
		Context context = new Context (PATH, LOG_FILE_NAME);
		assertEquals (context.mResultsBaseDirectory, PATH + "/");
		assertEquals (context.mResultsDirectory, context.mResultsBaseDirectory + Context.mDateFormat.format (context.mStartTime) + "/");
		assertEquals (context.mLogFileName, LOG_FILE_NAME);
		assertEquals (context.mFullLogFilePath, context.mResultsDirectory + LOG_FILE_NAME);
	}

	@Test
	public void testContextDateStringString () {
		Date date = GregorianCalendar.getInstance ().getTime ();
		Context context = new Context (date, PATH, LOG_FILE_NAME);
		assertEquals (context.mStartTime, date);
		assertEquals (context.mResultsBaseDirectory, PATH + "/");
		assertEquals (context.mResultsDirectory, context.mResultsBaseDirectory + Context.mDateFormat.format (date) + "/");
		assertEquals (context.mLogFileName, LOG_FILE_NAME);
		assertEquals (context.mFullLogFilePath, context.mResultsDirectory + LOG_FILE_NAME);
	}


	// private members
	private static final String PATH			= "c:/tmp";
	private static final String LOG_FILE_NAME	= "otherLogfileName";
}