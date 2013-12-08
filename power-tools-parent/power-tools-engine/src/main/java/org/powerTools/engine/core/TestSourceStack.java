/*	Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
 *
 *	This file is part of the PowerTools engine.
 *
 *	The PowerTools engine is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the License,
 *	or (at your option) any later version.
 *
 *	The PowerTools engine is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU Affero General Public License for more details.
 *
 *	You should have received a copy of the GNU Affero General Public License
 *	along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine.core;

import java.util.EmptyStackException;
import java.util.Stack;

import org.powertools.engine.ExecutionException;
import org.powertools.engine.sources.TestCaseTestSource;
import org.powertools.engine.sources.TestLineImpl;
import org.powertools.engine.sources.TestSource;
import org.powertools.engine.symbol.Scope;


/**
 * The test source stack supports pushing a new test source and getting the next
 * test line. It automatically pops a test source when it is depleted.
 */
final class TestSourceStack {
	private final Stack<TestSource> mSourceStack;
	
	private boolean mInATestCase;

	
	TestSourceStack () {
		mSourceStack = new Stack<TestSource> ();
		mInATestCase = false;
	}


	TestSource getCurrentTestSource () {
		return mSourceStack.peek ();
	}

	boolean inATestCase () {
		return mInATestCase;
	}
	
	Scope getCurrentScope () {
		try {
			return mSourceStack.peek ().getScope ();
		} catch (EmptyStackException ese) {
			return Scope.getGlobalScope ();
		}
	}


	void initAndPush (TestSource source) {
		source.initialize ();
		mSourceStack.push (source);
	}
	
	
	void run (String fileName) {
		initAndPush (mSourceStack.peek ().create (fileName));
	}

	boolean createAndPushTestCase (String name, String description) {
		if (!mInATestCase) {
			// can push immediately
		} else if (mSourceStack.peek () instanceof TestCaseTestSource) {
			popTestSource ();
		} else {
			return false;
		}

		initAndPush (mSourceStack.peek ().createTestCase (name, description));
		mInATestCase = true;
		return true;
	}

	TestLineImpl getTestLine () {
		while (!mSourceStack.isEmpty ()) {
			TestLineImpl testLine = mSourceStack.peek ().getTestLine ();
			if (testLine != null) {
				return testLine;
			} else {
				popTestSource ();
			}
		}
		return null;
	}
	
	void popTestCase () {
		if (!(mSourceStack.peek () instanceof TestCaseTestSource)) {
			throw new ExecutionException ("not in a test case (in this test line source)");
		} else {
			popTestSource ();
			mInATestCase = false;
		}
	}
	
	private void popTestSource () {
		mSourceStack.pop ().cleanup ();
	}
}