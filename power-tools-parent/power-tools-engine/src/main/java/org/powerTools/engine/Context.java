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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * The Context describes the static part of the environment where the test executes.
 */
public class Context {
	public final Date mStartTime;
	public final String mResultsBaseDirectory;
	public final String mResultsDirectory;
	public final String mLogFileName;
	public final String mFullLogFilePath;

	static final String LOG_FILE_NAME = "log.html";

	protected static final SimpleDateFormat mDateFormat = new SimpleDateFormat ("yyyy.MM.dd-HH.mm.ss");

	private static String defaulBaseDirectory = null;
	
	public static void setAlternativeResultBaseDirectory (String baseDirectory) {
		defaulBaseDirectory = baseDirectory;
	}
	
	public Context (String resultsBaseDirectory) {
		this (GregorianCalendar.getInstance ().getTime (), resultsBaseDirectory);
	}

	public Context (Date startTime, String resultsBaseDirectory) {
		this (startTime, resultsBaseDirectory, LOG_FILE_NAME);
	}

	public Context (String resultsBaseDirectory, String logFileName) {
		this (GregorianCalendar.getInstance ().getTime (), resultsBaseDirectory, logFileName);
	}
	
	public Context (Date startTime, String resultsBaseDirectory, String logFileName) {
		mStartTime				= startTime;
		
		if (defaulBaseDirectory == null) {
			mResultsBaseDirectory	= resultsBaseDirectory + "/";
		} else	{
			mResultsBaseDirectory	= defaulBaseDirectory + "/";
		}
		
		mResultsDirectory		= mResultsBaseDirectory + mDateFormat.format (startTime) + "/";
		mLogFileName			= logFileName;
		mFullLogFilePath		= mResultsDirectory + logFileName;
	}
}