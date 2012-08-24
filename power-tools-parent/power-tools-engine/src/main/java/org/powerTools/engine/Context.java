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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * The Context describes the static part of the environment where the test executes.
 */
public class Context {
	public final Date mStartTime;
	public final String mResultsDirectory;
	public final String mLogFileName;
	public final String mFullLogFilePath;
	
	protected static final SimpleDateFormat mDateFormat = new SimpleDateFormat ("yyyy.MM.dd-HH.mm.ss");

	
	public Context (String resultsDirectory) {
		this (GregorianCalendar.getInstance ().getTime (), resultsDirectory);
	}

	public Context (Date startTime, String resultsDirectory) {
		this (startTime, resultsDirectory, "log" + mDateFormat.format (startTime) + ".html");
	}

	public Context (String resultsDirectory, String logFileName) {
		this (GregorianCalendar.getInstance ().getTime (), resultsDirectory, logFileName);
	}
	
	public Context (Date startTime, String resultsDirectory, String logFileName) {
		mStartTime			= startTime;
		mResultsDirectory	= resultsDirectory;
		mLogFileName		= logFileName;
		mFullLogFilePath	= resultsDirectory + '/' + logFileName;
	}
}