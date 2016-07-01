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

package org.powertools.engine.fitnesse.sources;

import fit.Fixture;
import fit.Parse;
import java.util.List;
import org.powertools.engine.Scope;
import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.sources.TestLineImpl;


final class DataSource extends BaseDataSource {
    private String mInstructionName;

    DataSource (Fixture fixture, Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        super (fixture, table, scope, logFilePath, publisher, reference);
    }

    DataSource (Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        super (table, scope, logFilePath, publisher, reference);
    }


    @Override
    public void initialize () {
        processMyFixtureLine ();
        processHeaderLine ();
    }

    private void processMyFixtureLine () {
        copyRow (mRow);
        linkToLogFile (mRow.parts);
        mPublisher.publishTestLine (mTestLine);
        if (mTestLine.getNrOfParts () != 1) {
            mPublisher.publishError ("fixture line must have one or two cells");
        }
        mPublisher.publishEndOfTestLine ();
    }

    private void processHeaderLine () {
        mRow = mRow.more;
        if (mRow != null) {
            mTestLine.setParts (getDataTestLine ());
            mPublisher.publishTestLine (mTestLine);

            int nrOfParts                   = mTestLine.getNrOfParts ();
            StringBuilder instructionNameSb = new StringBuilder ();
            for (int partNr = 1; partNr < nrOfParts; ++partNr) {
                String part = mTestLine.getPart (partNr);
                if (part.isEmpty ()) {
                    mPublisher.publishError ("empty column name(s)");
                    break;
                } else {
                    for (String word : part.split (" +")) {
                        instructionNameSb.append (word).append (" ");
                    }
                    instructionNameSb.append ("_ ");
                }
            }
            mPublisher.publishEndOfTestLine ();

            mInstructionName = instructionNameSb.toString ().trim ();
        }
    }

    @Override
    public TestLineImpl getTestLine () {
        if (mRow != null) {
            while ((mRow = mRow.more) != null) {
                List<String> parts = getDataTestLine ();
                limitLength (parts, mNrOfParameters + 1);
                mTestLine.setParts (parts);
                mTestLine.setPart (0, mInstructionName);
                linkToLogFile (mRow.parts);
                return mTestLine;
            }
        }

        return null;
    }
}