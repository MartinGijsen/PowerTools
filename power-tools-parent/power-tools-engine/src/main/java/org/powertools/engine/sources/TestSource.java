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

package org.powertools.engine.sources;

import org.powertools.engine.ExecutionException;
import org.powertools.engine.Scope;
import org.powertools.engine.TestRunResultPublisher;


/*
 * A TestSource contains instructions and provides them one by one.
 */
public abstract class TestSource {
    protected static final String OUTPUT_PARAMETER_PREFIX = "out ";

    protected final TestRunResultPublisher mPublisher;
    protected final Scope                  mScope;

    protected TestLineImpl mTestLine;


    protected TestSource (Scope scope, TestRunResultPublisher publisher) {
        mScope     = scope;
        mPublisher = publisher;
        mTestLine  = new TestLineImpl ();
    }


    public void initialize () {
        // empty
    }

    public boolean isATestCase () {
        return false;
    }

    public abstract TestLineImpl getTestLine ();

    public void cleanup () {
        // empty
    }


    public final Scope getScope () {
        return mScope;
    }

    public TestSource create (String sourceName) {
        throw new ExecutionException ("operation not supported for this test source");
    }

    public final TestSource createTestCase (String name, String description) {
        return new TestCaseTestSource (this);
    }
}
