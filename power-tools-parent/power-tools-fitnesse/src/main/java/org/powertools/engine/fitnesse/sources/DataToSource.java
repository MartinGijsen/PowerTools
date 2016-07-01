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
import java.util.LinkedList;
import java.util.List;
import org.powertools.engine.Scope;
import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.sources.TestLineImpl;


final class DataToSource extends BaseDataSource {
    private final List<String> mColumnNames;

    private String mInstructionName;

    
    DataToSource (Fixture fixture, Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        super (fixture, table, scope, logFilePath, publisher, reference);
        mColumnNames = new LinkedList<String> ();
    }

    DataToSource (Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        super (table, scope, logFilePath, publisher, reference);
        mColumnNames = new LinkedList<String> ();
    }


    @Override
    public void initialize () {
        checkFixtureLine ();
        processHeaderLine ();
    }

    private void checkFixtureLine () {
        copyRow (mRow);
        linkToLogFile (mRow.parts);
        mPublisher.publishTestLine (mTestLine);
        mInstructionName = (mTestLine.getNrOfParts () >= 2 ? mTestLine.getPart (1) : null);
        if (mTestLine.getNrOfParts () != 2) {
            mPublisher.publishError ("fixture line must have two cells");
        }
        mPublisher.publishEndOfTestLine ();
    }

    private void processHeaderLine () {
        mRow = mRow.more;
        if (mRow != null) {
            mTestLine.setParts (getDataTestLine ());
            //linkToLogFile (mRow.parts);
            mPublisher.publishTestLine (mTestLine);

            int nrOfParts = mTestLine.getNrOfParts ();
            for (int partNr = 1; partNr < nrOfParts; ++partNr) {
                String part = mTestLine.getPart (partNr);
                if (part.isEmpty ()) {
                    mPublisher.publishError ("empty column name(s)");
                    break;
                } else if (mColumnNames.contains (part)) {
                    mPublisher.publishError ("duplicate column name(s)");
                    break;
                } else {
                    mColumnNames.add (part);
                }
            }

            mPublisher.publishEndOfTestLine ();
        }
    }

    @Override
    public TestLineImpl getTestLine () {
        if (mRow != null) {
            while ((mRow = mRow.more) != null) {
                if (mColumnNames.size () == countCells (mRow)) {
                    setParts ();
                    linkToLogFile (mRow.parts);
                    return mTestLine;
                } else {
                    List<String> parts = getDataTestLine ();
                    limitLength (parts, mNrOfParameters + 1);
                    mTestLine.setParts (parts);
                    mTestLine.setPart (0, mInstructionName);
                    mPublisher.publishTestLine (mTestLine);
                    linkToLogFile (mRow.parts);
                    mPublisher.publishError ("instruction length does not match header length");
                    mPublisher.publishEndOfTestLine ();
                }
            }
        }

        return null;
    }
    
    private void setParts () {
        List<String> parts = new LinkedList<String> ();
        parts.add (mInstructionName);
        Parse cell = mRow.parts;
        for (String columnName : mColumnNames) {
            parts.add (columnName);
            parts.add (cell.text ());
            cell = cell.more;
        }
        mTestLine.setParts (parts);
    }
}