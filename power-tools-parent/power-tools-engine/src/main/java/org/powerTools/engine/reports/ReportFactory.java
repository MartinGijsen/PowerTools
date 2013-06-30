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

package org.powerTools.engine.reports;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.powerTools.engine.Context;


public final class ReportFactory {
	private ReportFactory () { }

	
	public static boolean createKeywordsHtmlLog (Context context) {
		try {
			File file = new File (context.mFullLogFilePath);
			file.getParentFile ().mkdirs ();
			KeywordsHtmlLog log					= new KeywordsHtmlLog (new PrintWriter (new FileWriter (file)), context.mLogFileName);
			TestRunResultPublisher publisher	= TestRunResultPublisher.getInstance ();
			publisher.subscribeToTestLines (log);
			publisher.subscribeToTestResults (log);
			return true;
		} catch (IOException ioe) {
			return false;
		}
	}

	public static boolean createTestCaseReport (Context context) {
		try {
			File file = new File (context.mResultsDirectory + "testcasereport.html");
			file.getParentFile ().mkdirs ();
			TestCaseReport report				= new TestCaseReport (new PrintWriter (new FileWriter (file)));
			TestRunResultPublisher publisher	= TestRunResultPublisher.getInstance ();
			publisher.subscribeToTestCases (report);
			publisher.subscribeToTestResults (report);
			return true;
		} catch (IOException ioe) {
			return false;
		}
	}
	
	public static void createConsole () {
		Console console						= new Console ();
		TestRunResultPublisher publisher	= TestRunResultPublisher.getInstance ();
		publisher.subscribeToTestLines (console);
		publisher.subscribeToTestResults (console);
	}
}