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

import fit.Parse;
import fitnesse.testsystems.TestSummary;
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
import org.powertools.engine.reports.ReportFactory;
import org.powertools.engine.sources.Procedure;
import org.powertools.engine.symbol.Scope;


public final class FitNesseEngine2 extends Engine {
    private final TestSourceFactory mSourceFactory;
    private final String mLogFilePath;
    private final PowerToolsReporter mFitNesseReporter;
    private final TestSummary mSummary;
    
    private Reference mReference;


    FitNesseEngine2 () {
        super (new RunTimeImpl (new Context (FitNesse.ROOT_DIRECTORY + "files/testResults")));

        //ReportFactory.createConsole ();
        mLogFilePath = mRunTime.getContext ().getFullLogFilePath ();
        createLog ();
        if (!new ReportFactory (mPublisher).createTestCaseReport (mRunTime.getContext ())) {
            reportError ("could not open test case report");
        }
        mSummary          = new TestSummary ();
        mFitNesseReporter = new PowerToolsReporter (mSummary);
        mPublisher.subscribeToTestResults (mFitNesseReporter);

        registerBuiltinInstructions ();

        mPublisher.start (mRunTime.getContext ().getStartTime ());
        
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

    void executeTable (Parse table) {
        String tableType = getTableType (table);
        if ("instruction".equalsIgnoreCase (tableType)) {
            prepareInstructionTable (table);
        } else {
            FitNesseTestSource source = createSource (tableType, table);
            mFitNesseReporter.setSource (source);
            mRunTime.invokeSource (source);
        }
        run ();
    }

    private String getTableType (Parse table) {
        // the table type is in the first cell of the first line
        return table.parts.parts.body;
    }

    private FitNesseTestSource createSource (String tableType, Parse table) {
        Scope globalScope = mRunTime.getGlobalScope ();
        if ("scenario".equalsIgnoreCase (tableType)) {
            return mSourceFactory.createScenarioSource (table, globalScope, mLogFilePath, mPublisher, mReference);
        } else if ("data".equalsIgnoreCase (tableType)) {
            return mSourceFactory.createDataSource (table, globalScope, mLogFilePath, mPublisher, mReference);
        } else if ("testcase".equalsIgnoreCase (tableType) || "test case".equalsIgnoreCase (tableType)) {
            return mSourceFactory.createTestCaseSource (table, globalScope, mLogFilePath, mPublisher, mReference, true);
        } else {
            return mSourceFactory.createDummySource (table, mLogFilePath, mPublisher, mReference);
        }
    }

    private void prepareInstructionTable (Parse table) {
        InstructionSource source = mSourceFactory.createInstructionSource (table, mRunTime.getGlobalScope (), mLogFilePath, mPublisher, mReference);
        mFitNesseReporter.setSource (source);
        mRunTime.invokeSource (source);
        Procedure procedure = source.getProcedure ();
        if (procedure != null) {
            addProcedure (procedure);
        }
    }
    
    TestSummary getSummary () {
        return mSummary;
    }
}
