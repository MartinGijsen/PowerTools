/* Copyright 2012-2013 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.fitnesse.sources;

import fit.Fixture;
import fit.Parse;
import java.util.ArrayList;
import java.util.List;
import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.fitnesse.Reference;
import org.powertools.engine.symbol.Scope;


public final class TestCaseSource extends ScenarioSource {
    private final String[] mArgs;
    private final boolean mInRightPlace;

    TestCaseSource (Fixture fixture, Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference, boolean inRightPlace) {
        super (fixture, table, new Scope (scope), logFilePath, publisher, reference);
        mArgs         = getArgs (table);
        mInRightPlace = inRightPlace;
    }

    TestCaseSource (Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference, boolean inRightPlace) {
        super (table, new Scope (scope), logFilePath, publisher, reference);
        mArgs         = getArgs (table);
        mInRightPlace = inRightPlace;
    }

    private String[] getArgs (Parse table) {
        List<String> argumentList = new ArrayList<String> ();
        for (Parse parameter = table.parts.parts.more; parameter != null; parameter = parameter.more) {
            argumentList.add (parameter.text());
        }
        return argumentList.toArray (new String[argumentList.size ()]);
    }

    @Override
    public void initialize () {
        String id          = mArgs.length >= 1 ? mArgs[0] : "";
        String description = mArgs.length >= 2 ? mArgs[1] : "";

        copyFixtureLine ();
        linkToLogFile (mRow.parts);
        mPublisher.publishTestLine (mTestLine);
        if (mArgs.length > 2) {
            mPublisher.publishError ("too many arguments (test case name and description expected)");
        }
        mPublisher.publishEndOfTestLine ();
        mPublisher.publishTestCaseBegin (id, description);
    }

    private void copyFixtureLine () {
        int nrOfArgs = mArgs.length;
        mTestLine.createParts (nrOfArgs + 1);
        mTestLine.setPart (0, mRow.parts.text ());
        for (int argNr = 0; argNr < nrOfArgs; ++argNr) {
            mTestLine.setPart (argNr + 1, mArgs[argNr]);
        }
    }

    @Override
    public boolean isATestCase () {
        return true;
    }

    @Override
    public void cleanup () {
        mPublisher.publishTestCaseEnd ();
    }
}
