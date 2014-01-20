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

import org.powertools.engine.sources.TestLineImpl;

import fit.Fixture;
import fit.Parse;
import org.powertools.engine.symbol.Scope;


class ScenarioSource extends BaseTestSource {
    ScenarioSource (Fixture fixture, Parse table, Scope scope, String logFilePath) {
        super (fixture, table.parts, scope, logFilePath);
    }


    @Override
    public void initialize () {
        processFixtureLine ();
    }

    @Override
    public TestLineImpl getTestLine () {
        while ((mRow = mRow.more) != null) {
            mTestLine.setParts (readSentence (mRow.parts));
            if (mTestLine.getPart (0).isEmpty ()) {
                return mTestLine;
            } else if (!mTestLine.isEmpty ()) {
                linkToLogFile (mRow.parts);
                return mTestLine;
            }
        }

        return null;
    }
}
