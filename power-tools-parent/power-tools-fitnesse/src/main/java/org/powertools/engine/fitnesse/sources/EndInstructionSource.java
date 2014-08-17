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

import fit.Fixture;
import fit.Parse;
import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.fitnesse.Reference;
import org.powertools.engine.sources.TestLineImpl;
import org.powertools.engine.symbol.Scope;


public final class EndInstructionSource extends FitNesseTestSource {
    private final boolean mInRightPlace;
    
    EndInstructionSource (Fixture fixture, Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference, boolean inRightPlace) {
        super (fixture, table, scope, logFilePath, publisher, reference);
        mInRightPlace = inRightPlace;
    }

//    EndInstructionSource (Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
//        super (table, scope, logFilePath, publisher, reference);
//    }


    @Override
    public void initialize () {
        if (mInRightPlace) {
            processFixtureLine ();
        } else {
            reportError ();
            copyRestOfTable ();
        }
    }

    private void reportError () {
        copyRow (mRow);
        linkToLogFile (mRow.parts);
        mPublisher.publishTestLine (mTestLine);
        mPublisher.publishError ("not in an instruction definition");
        mPublisher.publishEndOfTestLine ();
    }
    
    private void copyRestOfTable () {
        while ((mRow = mRow.more) != null) {
            copyRow (mRow);
            mPublisher.publishTestLine (mTestLine);
        }
    }

    @Override
    public TestLineImpl getTestLine () {
        return null;
    }
}
