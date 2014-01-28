/*	Copyright 2012-2014 by Martin Gijsen (www.DeAnalist.nl)
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
	private static final String PATH			= "c:/tmp";
	private static final String LOG_FILE_NAME	= "otherLogfileName";

	
	@Before
	public void setUp () throws Exception {
		
	}

	@After
	public void tearDown () throws Exception {
		
	}

	
	@Test
	public void testContextString () {
		Context context = new Context (PATH);
		assertEquals (context.getResultsBaseDirectory (), PATH + "/");
		assertEquals (context.getResultsDirectory (), context.getResultsBaseDirectory () + Context.mDateFormat.format (context.getStartTime ()) + "/");
		assertEquals (context.getLogFileName (), Context.LOG_FILE_NAME);
		assertEquals (context.getFullLogFilePath (), context.getResultsDirectory () + Context.LOG_FILE_NAME);
	}

	@Test
	public void testContextStringWithAlternativeResultsDir () {
		final String ALTERNATE_PATH= "some other path";
		Context.setAlternativeResultBaseDirectory (ALTERNATE_PATH);
		Context context = new Context (PATH);
		assertEquals (context.getResultsBaseDirectory (), ALTERNATE_PATH + "/");
		assertEquals (context.getResultsDirectory (), context.getResultsBaseDirectory () + Context.mDateFormat.format (context.getStartTime ()) + "/");
		assertEquals (context.getLogFileName (), Context.LOG_FILE_NAME);
		assertEquals (context.getFullLogFilePath (), context.getResultsDirectory () + Context.LOG_FILE_NAME);
		Context.setResultBaseDirectory (null);
	}

	@Test
	public void testContextDateString () {
		Date date = GregorianCalendar.getInstance ().getTime ();
		Context context = new Context (date, PATH);
		assertEquals (context.getStartTime (), date);
		assertEquals (context.getResultsBaseDirectory (), PATH + "/");
		assertEquals (context.getResultsDirectory (), context.getResultsBaseDirectory () + Context.mDateFormat.format (date) + "/");
		assertEquals (context.getLogFileName (), Context.LOG_FILE_NAME);
		assertEquals (context.getFullLogFilePath (), context.getResultsDirectory () + Context.LOG_FILE_NAME);
	}

	@Test
	public void testContextStringString () {
		Context context = new Context (PATH, LOG_FILE_NAME);
		assertEquals (context.getResultsBaseDirectory (), PATH + "/");
		assertEquals (context.getResultsDirectory (), context.getResultsBaseDirectory () + Context.mDateFormat.format (context.getStartTime ()) + "/");
		assertEquals (context.getLogFileName (), LOG_FILE_NAME);
		assertEquals (context.getFullLogFilePath (), context.getResultsDirectory () + LOG_FILE_NAME);
	}

	@Test
	public void testContextDateStringString () {
		Date date = GregorianCalendar.getInstance ().getTime ();
		Context context = new Context (date, PATH, LOG_FILE_NAME);
		assertEquals (context.getStartTime (), date);
		assertEquals (context.getResultsBaseDirectory (), PATH + "/");
		assertEquals (context.getResultsDirectory (), context.getResultsBaseDirectory () + Context.mDateFormat.format (date) + "/");
		assertEquals (context.getLogFileName (), LOG_FILE_NAME);
		assertEquals (context.getFullLogFilePath (), context.getResultsDirectory () + LOG_FILE_NAME);
	}
}