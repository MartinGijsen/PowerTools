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

package org.powerTools.engine.core;

import java.util.Stack;

import org.powerTools.engine.sources.TestLineImpl;
import org.powerTools.engine.sources.TestSource;
import org.powerTools.engine.symbol.Scope;


final class TestSourceStack {
	private final Stack<TestSource> mSourceStack;

	
	TestSourceStack () {
		mSourceStack = new Stack<TestSource> ();
	}


	Scope getCurrentScope () {
		return mSourceStack.peek ().getScope ();
	}


	void initAndPush (TestSource source) {
		source.initialize ();
		mSourceStack.push (source);
	}
	
	
	void run (String fileName) {
		initAndPush (mSourceStack.peek ().create (fileName));
	}

	TestLineImpl getTestLine () {
		while (!mSourceStack.isEmpty ()) {
			TestLineImpl testLine = mSourceStack.peek ().getTestLine ();
			if (testLine != null) {
				return testLine;
			} else {
				mSourceStack.pop ();
			}
		}
		return null;
	}
}