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

package org.powertools.engine.fitnesse.sources;

import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.sources.TestLineImpl;

import fit.Parse;


final class DummySource extends FitNesseTestSource {
    DummySource (Parse table, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        super (table, null, logFilePath, publisher, reference);
    }


    @Override
    public void initialize () {
        mTestLine.createParts (1);
        mTestLine.setPart (0, mRow.parts.text ());
        linkToLogFile (mRow.parts);
        mPublisher.publishTestLine (mTestLine);
        mPublisher.publishError ("unknown table type");
        if (mTestLine.getNrOfParts () != 1) {
            mPublisher.publishError ("first line must have one cell");
        }
        mPublisher.publishEndOfTestLine ();
    }

    @Override
    public TestLineImpl getTestLine () {
        while ((mRow = mRow.more) != null) {
            mTestLine.setParts (readSentence (mRow.parts));
            mPublisher.publishCommentLine (mTestLine);
        }

        return null;
    }
}
