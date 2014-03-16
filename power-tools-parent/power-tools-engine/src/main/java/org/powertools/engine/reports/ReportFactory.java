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

package org.powertools.engine.reports;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.powertools.engine.Context;
import org.powertools.engine.TestRunResultPublisher;


public final class ReportFactory {
    private final TestRunResultPublisher mPublisher;

    public ReportFactory (TestRunResultPublisher publisher) {
        mPublisher = publisher;
    }

    public boolean createKeywordsHtmlLog (Context context) {
        try {
            File file = new File (context.getFullLogFilePath ());
            file.getParentFile ().mkdirs ();
            KeywordsHtmlLog log              = new KeywordsHtmlLog (new PrintWriter (new FileWriter (file)), context.getLogFileName ());
            mPublisher.subscribeToTestLines (log);
            mPublisher.subscribeToTestResults (log);
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

    public boolean createTestCaseReport (Context context) {
        try {
            File file = new File (context.getResultsDirectory () + "testcasereport.html");
            file.getParentFile ().mkdirs ();
            TestCaseReport report            = new TestCaseReport (new PrintWriter (new FileWriter (file)));
            mPublisher.subscribeToTestCases (report);
            mPublisher.subscribeToTestResults (report);
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

    public void createConsole () {
        Console console                  = new Console ();
        mPublisher.subscribeToTestLines (console);
        mPublisher.subscribeToTestResults (console);
    }

    public void createModelCoverageGraph (String resultsDirectory) {
        ModelCoverageGraph graph         = new ModelCoverageGraph (resultsDirectory);
        mPublisher.subscribeToModel (graph);
    }
}
