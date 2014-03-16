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

import java.util.ArrayList;
import java.util.List;

import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.fitnesse.FitNesse;
import org.powertools.engine.fitnesse.Reference;
import org.powertools.engine.sources.TestSource;
import org.powertools.engine.symbol.Scope;

import fit.Fixture;
import fit.Parse;


public abstract class FitNesseTestSource extends TestSource {
    protected Parse mRow;

    private final RowResultReporter mReporter;
    private final String mLogFilePath;

    private Reference mReference;


    protected FitNesseTestSource (Fixture fixture, Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        this (table, new FixtureReporter (fixture), scope, logFilePath, publisher, reference);
    }

    protected FitNesseTestSource (Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        this (table, new NewReporter (), scope, logFilePath, publisher, reference);
    }

    private FitNesseTestSource (Parse table, RowResultReporter reporter, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        super (scope, publisher);
        mRow         = table.parts;
        mReporter    = reporter;
        mLogFilePath = logFilePath.startsWith (FitNesse.ROOT_DIRECTORY) ? logFilePath.substring (FitNesse.ROOT_DIRECTORY.length ()) : logFilePath;
        mReference   = reference;
    }

    
    protected final void processFixtureLine () {
        mTestLine.createParts (1);
        mTestLine.setPart (0, mRow.parts.text ());
        linkToLogFile (mRow.parts);
        mPublisher.publishTestLine (mTestLine);
        if (mTestLine.getNrOfParts () != 1) {
            mPublisher.publishError ("fixture line must have one cell");
        }
        mPublisher.publishEndOfTestLine ();
    }


    public void processError () {
        mReporter.processError (mRow);
    }

    public void processFinished (boolean anyErrors) {
        mReporter.processFinished (mRow, anyErrors);
    }


    protected final void linkToLogFile (Parse cell) {
        cell.body = String.format ("<A href=\"%s#id%s\">%s</A>", mLogFilePath, mReference.advance (), cell.body);
    }

    protected final List<String> readSentence (Parse instructionNameCell) {
        String instructionName = instructionNameCell.text ();
        Parse currentCell      = instructionNameCell;

        final List<String> parts = new ArrayList<String> ();
        parts.add ("");
        boolean isAParameter = true;
        while ((currentCell = currentCell.more) != null) {
            final String text = currentCell.text ();
            if (isAParameter) {
                parts.add (text);
                if (!instructionName.isEmpty ()) {
                    instructionName += " _";
                }
            } else if (!text.isEmpty ()) {
                instructionName += " " + text;
            }
            isAParameter = !isAParameter;
        }
        parts.set (0, instructionName);
        return parts;
    }


    private interface RowResultReporter {
        void processError (Parse row);
        void processFinished (Parse row, boolean anyErrors);
    }
    
    private static class FixtureReporter implements RowResultReporter {
        private final Fixture mFixture;

        FixtureReporter (Fixture fixture) {
            mFixture = fixture;
        }

        @Override
        public void processError (Parse row) {
            mFixture.wrong (row);
        }

        @Override
        public void processFinished (Parse row, boolean anyErrors) {
            if (!anyErrors) {
                mFixture.right (row);
            }
        }
    }
    
    private static class NewReporter implements RowResultReporter {
        @Override
        public final void processError (Parse row) {
            row.addToTag (" class=\"fail\"");
        }

        @Override
        public final void processFinished (Parse row, boolean anyErrors) {
            if (!anyErrors) {
                row.addToTag (" class=\"pass\"");
            }
        }
    }
}