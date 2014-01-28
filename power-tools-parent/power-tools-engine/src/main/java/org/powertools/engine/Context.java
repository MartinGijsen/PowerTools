/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * The Context describes the static part of the environment where the test executes.
 */
public class Context {
    protected static final SimpleDateFormat mDateFormat = new SimpleDateFormat ("yyyy.MM.dd-HH.mm.ss");

    protected static String mDefaulBaseDirectory = null;

    protected final Date mStartTime;
    protected final String mWorkingDirectory;
    protected final String mResultsBaseDirectory;
    protected final String mResultsDirectory;
    protected final String mLogFileName;
    protected final String mFullLogFilePath;

    static final String LOG_FILE_NAME = "log.html";


    @Deprecated
    public static void setAlternativeResultBaseDirectory (String directory) {
        setResultBaseDirectory (directory);
    }

    @Deprecated
    public static void setResultBaseDirectory (String directory) {
        mDefaulBaseDirectory = directory;
    }

    @Deprecated
    public Context (String resultsBaseDirectory) {
        this (GregorianCalendar.getInstance ().getTime (), resultsBaseDirectory);
    }

    @Deprecated
    public Context (Date startTime, String resultsBaseDirectory) {
        this (startTime, resultsBaseDirectory, LOG_FILE_NAME);
    }

    @Deprecated
    public Context (String resultsBaseDirectory, String logFileName) {
        this (GregorianCalendar.getInstance ().getTime (), resultsBaseDirectory, logFileName);
    }

    @Deprecated
    public Context (Date startTime, String resultsBaseDirectory, String logFileName) {
        mStartTime = startTime;

        mWorkingDirectory = resultsBaseDirectory;
        if (mDefaulBaseDirectory == null) {
            mResultsBaseDirectory = resultsBaseDirectory + "/";
        } else {
            mResultsBaseDirectory = mDefaulBaseDirectory + "/";
        }

        mResultsDirectory = mResultsBaseDirectory + mDateFormat.format (startTime) + "/";
        mLogFileName      = logFileName;
        mFullLogFilePath  = mResultsDirectory + logFileName;
    }

    
    public static Context create (String workingDirectory) {
        return create (GregorianCalendar.getInstance ().getTime (), workingDirectory, LOG_FILE_NAME);
    }

    public static Context create (Date startTime, String workingDirectory, String logFileName) {
        return new Context (GregorianCalendar.getInstance ().getTime (), workingDirectory, workingDirectory, logFileName);
    }

    public Context (Date startTime, String workingDirectory, String resultsBaseDirectory, String logFileName) {
        mStartTime            = startTime;
        mWorkingDirectory     = workingDirectory;
        mResultsBaseDirectory = resultsBaseDirectory + "/";
        mResultsDirectory     = mResultsBaseDirectory + mDateFormat.format (startTime) + "/";
        mLogFileName          = logFileName;
        mFullLogFilePath      = mResultsDirectory + logFileName;
    }

    
    public Date getStartTime () {
        return mStartTime;
    }

    public String getResultsBaseDirectory () {
        return mResultsBaseDirectory;
    }

    public String getResultsDirectory () {
        return mResultsDirectory;
    }

    public String getLogFileName () {
        return mLogFileName;
    }

    public String getFullLogFilePath () {
        return mFullLogFilePath;
    }
}
