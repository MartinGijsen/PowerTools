/* Copyright 2012-2014 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.fitnesse;

import fit.Fixture;
import fit.Parse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.powertools.engine.Context;
import org.powertools.engine.core.Engine;
import org.powertools.engine.core.RunTimeImpl;
import org.powertools.engine.fitnesse.sources.FitNesseTestSource;
import org.powertools.engine.fitnesse.sources.InstructionSource;
import org.powertools.engine.fitnesse.sources.TestSourceFactory;
import org.powertools.engine.fitnesse.sources.Reference;
import org.powertools.engine.reports.ReportFactory;


public final class FitNesseEngine extends Engine {
    private static final FitNesseEngine mTheOne = new FitNesseEngine ();

    private final TestSourceFactory mSourceFactory;
    private final String mLogFilePath;
    private final FitNesseReporter mFitNesseReporter;
    
    private Reference mReference;
    private InstructionSource mSource;


    private FitNesseEngine () {
        super (new RunTimeImpl (new Context (FitNesse.ROOT_DIRECTORY + "files/testResults/")));

        //ReportFactory.createConsole ();
        mLogFilePath = mRunTime.getContext ().getFullLogFilePath ();
        createLog ();
        if (!new ReportFactory (mPublisher).createTestCaseReport (mRunTime.getContext ())) {
            reportError ("could not open test case report");
        }
        mFitNesseReporter = new FitNesseReporter ();
        mPublisher.subscribeToTestResults (mFitNesseReporter);

        registerBuiltins ();

        mPublisher.start (mRunTime.getContext ().getStartTime ());
        // TODO: send stop signal also, once integration with Fit is improved
        
        mSourceFactory = new TestSourceFactory ();
    }

    private boolean createLog () {
        try {
            Context context = mRunTime.getContext ();
            File file       = new File (context.getFullLogFilePath ());
            file.getParentFile ().mkdirs ();
            HtmlLog log = new HtmlLog (new PrintWriter (new FileWriter (file)), context.getLogFileName ());
            mPublisher.subscribeToTestLines (log);
            mPublisher.subscribeToTestResults (log);
            mReference = log.getReference ();
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

    public static FitNesseEngine getInstance () {
        return mTheOne;
    }

    public void runInstructionFixture (Fixture fixture, Parse table) {
        mSource = mSourceFactory.createInstructionSource (fixture, table, mRunTime.getGlobalScope (), mLogFilePath, mPublisher, mReference);
        mFitNesseReporter.setSource (mSource);
        mRunTime.invokeSource (mSource);
        addProcedure (mSource.getProcedure ());
        if (mSource.addScenario (table)) {
            mSource = null;
        }
        run ();
    }

    public void runEndInstructionFixture (Fixture fixture, Parse table) {
        run (mSourceFactory.createEndInstructionSource (fixture, table, mRunTime.getGlobalScope (), mLogFilePath, mPublisher, mReference, mSource != null));
        mSource = null;
    }


    public void runScenarioFixture (Fixture fixture, Parse table) {
        if (mSource != null) {
            mSource.addScenario (table);
        } else {
            run (mSourceFactory.createScenarioSource (fixture, table, mRunTime.getGlobalScope (), mLogFilePath, mPublisher, mReference));
        }
    }

    public void runTestCaseFixture (Fixture fixture, Parse table) {
        run (mSourceFactory.createTestCaseSource (fixture, table, mRunTime.getGlobalScope (), mLogFilePath, mPublisher, mReference, mSource == null));
        mSource = null;
    }

    public void runDataFixture (Fixture fixture, Parse table) {
        if (mSource != null) {
            mSource.addData (table);
        } else {
            run (mSourceFactory.createDataSource (fixture, table, mRunTime.getGlobalScope (), mLogFilePath, mPublisher, mReference));
        }
    }

    public void runDataToFixture (Fixture fixture, Parse table) {
        if (mSource != null) {
            mSource.addDataTo (table);
        } else {
            run (mSourceFactory.createDataToSource (fixture, table, mRunTime.getGlobalScope (), mLogFilePath, mPublisher, mReference));
        }
    }

    private void run (FitNesseTestSource source) {
        mFitNesseReporter.setSource (source);
        mRunTime.invokeSource (source);
        run ();
    }
}
