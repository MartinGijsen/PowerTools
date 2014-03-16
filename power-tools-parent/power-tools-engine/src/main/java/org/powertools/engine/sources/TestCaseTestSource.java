/* Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.sources;

import java.util.LinkedList;
import java.util.List;

import org.powertools.engine.symbol.Scope;


/**
 * A TestCaseTestSource represents a subset of the test lines of another test source.
 * The test case has its own scope.
 * It does not nest and must terminate before the test source does.
 */
public class TestCaseTestSource extends TestSource {
    private final TestSource mParent;


    TestCaseTestSource (TestSource parent) {
        super (new Scope (parent.mScope), parent.mPublisher);
        mParent = parent;
    }

    @Override
    public boolean isATestCase () {
        return true;
    }

    @Override
    public TestLineImpl getTestLine () {
        TestLineImpl testLine = mParent.getTestLine ();
        if (testLine != null) {
            return testLine;
        } else {
            List<String> parts = new LinkedList<String> ();
            parts.add ("end test case");
            testLine = new TestLineImpl ();
            testLine.setParts (parts);
            mPublisher.publishTestLine (testLine);
            mPublisher.publishError ("missing end of test case before end of file");
            mPublisher.publishEndOfTestLine ();
            mPublisher.publishTestCaseEnd ();
            return null;
        }
    }
}
