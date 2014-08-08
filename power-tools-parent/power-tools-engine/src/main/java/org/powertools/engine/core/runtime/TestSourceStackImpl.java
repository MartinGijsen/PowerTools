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

package org.powertools.engine.core.runtime;

import java.util.EmptyStackException;
import java.util.Stack;

import org.powertools.engine.ExecutionException;
import org.powertools.engine.sources.TestLineImpl;
import org.powertools.engine.sources.TestSource;
import org.powertools.engine.symbol.Scope;


/**
 * The test source stack supports pushing a new test source and getting the next
 * test line. It automatically pops a test source when it is depleted, returning
 * to the test source pushed before.
 */
final class TestSourceStackImpl implements TestSourceStack {
    private final Stack<TestSource> mSourceStack;
    private final Scope mGlobalScope;
    
    private boolean mAborting;


    TestSourceStackImpl () {
        mSourceStack = new Stack<TestSource> ();
        mGlobalScope = new Scope (null);
        mAborting    = false;
    }


    @Override
    public void abort () {
        mAborting = true;
    }
    
    @Override
    public TestSource getCurrentTestSource () {
        return mSourceStack.peek ();
    }

    @Override
    public Scope getGlobalScope () {
        return mGlobalScope;
    }

    @Override
    public Scope getCurrentScope () {
        try {
            return mSourceStack.peek ().getScope ();
        } catch (EmptyStackException ese) {
            return mGlobalScope;
        }
    }


    @Override
    public void initAndPush (TestSource source) {
        source.initialize ();
        mSourceStack.push (source);
    }


    @Override
    public void run (String fileName) {
        initAndPush (mSourceStack.peek ().create (fileName));
    }

    @Override
    public TestLineImpl getTestLine () {
        while (!mSourceStack.isEmpty ()) {
            if (mAborting) {
                popTestSource ();
            } else {
                TestLineImpl testLine = mSourceStack.peek ().getTestLine ();
                if (testLine != null) {
                    return testLine;
                } else {
                    popTestSource ();
                }
            }
        }
        return null;
    }

    @Override
    public boolean createAndPushTestCase (String name, String description) {
        if (!inATestCase ()) {
            // can push immediately
        } else if (mSourceStack.peek ().isATestCase ()) {
            popTestSource ();
        } else {
            return false;
        }

        initAndPush (mSourceStack.peek ().createTestCase (name, description));
        return true;
    }

    @Override
    public boolean inATestCase () {
        int nrOfSources = mSourceStack.size ();
        for (int position = 0; position < nrOfSources; ++position) {
            if (mSourceStack.get (position).isATestCase ()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void popTestCase () {
        if (mSourceStack.isEmpty ()) {
            throw new ExecutionException ("no current test source");
        } else if (!(mSourceStack.peek ().isATestCase ())) {
            throw new ExecutionException ("not in a test case (in this test line source)");
        } else {
            popTestSource ();
        }
    }

    private void popTestSource () {
        mSourceStack.pop ().cleanup ();
    }
}
