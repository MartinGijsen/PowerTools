/*	Copyright 2012-2013 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.fitnesse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.powertools.engine.Context;
import org.powertools.engine.core.BuiltinInstructions;
import org.powertools.engine.core.Engine;
import org.powertools.engine.core.RunTimeImpl;
import org.powertools.engine.reports.ReportFactory;
import org.powertools.engine.reports.TestRunResultPublisher;

import fit.Parse;


public final class FitNesseEngine extends Engine {
	static final String ROOT_DIRECTORY = "FitNesseRoot/";
	
	private static final FitNesseEngine mTheOne = new FitNesseEngine ();
	
	private final String mLogFilePath;
	private final FitNesseReporter mFitNesseReporter;

	
	private FitNesseEngine () {
		super (new RunTimeImpl (new Context (ROOT_DIRECTORY + "files/testResults/")));

		//ReportFactory.createConsole ();
		mLogFilePath = mRunTime.getContext ().getFullLogFilePath ();
		createLog ();
		if (!ReportFactory.createTestCaseReport (mRunTime.getContext ())) {
			System.err.println ("could not open test case report");
		}
		mFitNesseReporter = new FitNesseReporter ();
		mPublisher.subscribeToTestResults (mFitNesseReporter);

		registerBuiltinInstructions ();
		
		mPublisher.start (mRunTime.getContext ().getStartTime ());
		// TODO: send stop signal also, once integration with Fit is improved
	}

	private boolean createLog () {
		try {
			Context context	= mRunTime.getContext ();
			File file		= new File (context.getFullLogFilePath ());
			file.getParentFile ().mkdirs ();
			HtmlLog log							= new HtmlLog (new PrintWriter (new FileWriter (file)), context.getLogFileName ());
			TestRunResultPublisher publisher	= TestRunResultPublisher.getInstance ();
			publisher.subscribeToTestLines (log);
			publisher.subscribeToTestResults (log);
			return true;
		} catch (IOException ioe) {
			return false;
		}
	}

	public static FitNesseEngine getInstance () {
		return mTheOne;
	}

	public void run (InstructionFixture fixture, Parse table) {
		InstructionSource source = new InstructionSource (fixture, table, mLogFilePath);
		mFitNesseReporter.setSource (source);
		mRunTime.invokeSource (source);
		addProcedure (source.getProcedure ());
		run ();
	}

	public void run (ScenarioFixture fixture, Parse table) {
		run (new ScenarioSource (fixture, table, mLogFilePath));
	}

	public void run (TestCaseFixture fixture, Parse table) {
		run (new TestCaseSource (fixture, table, mRunTime, mLogFilePath));
	}

	public void run (DataFixture fixture, Parse table) {
		run (new DataSource (fixture, table, mLogFilePath));
	}

	private void run (BaseTestSource source) {
		mFitNesseReporter.setSource (source);
		mRunTime.invokeSource (source);
		run ();
	}
}