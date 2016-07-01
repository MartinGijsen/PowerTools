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
import java.util.ArrayList;
import java.util.List;
import org.powertools.engine.Scope;
import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.sources.TestLineImpl;


abstract class BaseDataSource extends FitNesseTestSource {
    int mNrOfParameters;

    BaseDataSource (Fixture fixture, Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        super (fixture, table, scope, logFilePath, publisher, reference);
    }

    BaseDataSource (Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        super (table, scope, logFilePath, publisher, reference);
    }

    List<String> getDataTestLine () {
        List<String> parts = new ArrayList<String> ();
        parts.add ("");
        Parse currentCell = mRow.parts;
        do {
            parts.add (currentCell.text ());
            currentCell = currentCell.more;
        } while (currentCell != null);
        return parts;
    }
    
    void limitLength (List<String> parts, int maxNrOfParts) {
        while (parts.size () > maxNrOfParts) {
            parts.remove (maxNrOfParts);
        }
    }
}