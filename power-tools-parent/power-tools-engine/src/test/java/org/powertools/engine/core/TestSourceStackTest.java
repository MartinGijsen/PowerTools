/*	Copyright 2013 by Martin Gijsen (www.DeAnalist.nl)
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

import org.junit.Test;
import static org.junit.Assert.*;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.sources.TestLineImpl;
import org.powertools.engine.sources.TestSource;
import org.powertools.engine.symbol.Scope;


public class TestSourceStackTest {
	@Test
	public void testGetCurrentTestSource () {
		TestSourceStack stack = new TestSourceStack ();
		TestSource testSource = new TestSourceImpl (new Scope (null));
		stack.initAndPush (testSource);
		assertEquals (testSource, stack.getCurrentTestSource ());
	}

	@Test
	public void testGetCurrentScope () {
		TestSourceStack stack = new TestSourceStack ();
		Scope globalScope     = stack.getCurrentScope ();
		assertEquals (globalScope, stack.getCurrentScope ());
		Scope localScope      = new Scope (globalScope);
		stack.initAndPush (new TestSourceImpl (localScope));
		assertEquals (localScope, stack.getCurrentScope ());
	}

	@Test
	public void testRun () {
		try {
			TestSourceStack stack = new TestSourceStack ();
			stack.initAndPush (new TestSourceImpl (new Scope (null)));
			stack.run ("some filename");
		} catch (ExecutionException ee) {
			assertTrue (ee.getMessage ().contains ("not supported"));
		}
	}

	@Test
	public void testGetTestLine () {
		TestSourceStack stack = new TestSourceStack ();
		stack.initAndPush (new TestSourceImpl (new Scope (null)));
		stack.initAndPush (new TestSourceImpl (new Scope (null)));
		assertNotNull (stack.getTestLine ());
		assertNotNull (stack.getTestLine ());
		assertNull (stack.getTestLine ());
	}

	@Test
	public void testCreateAndPushTestCase () {
		TestSourceStack stack = new TestSourceStack ();
		stack.initAndPush (new TestSourceImpl (new Scope (null)));
		assertFalse (stack.inATestCase ());
		assertTrue (stack.createAndPushTestCase ("test case name", "test case description"));
		assertTrue (stack.inATestCase ());
		stack.popTestCase ();
		assertFalse (stack.inATestCase ());
	}

	@Test
	public void testPopTestCase_EmptyStack () {
		try {
			TestSourceStack stack = new TestSourceStack ();
			stack.popTestCase ();
			fail ("no exception");
		} catch (ExecutionException ee) {
			assertTrue (ee.getMessage ().contains ("no current test source"));
		}
	}


	private class TestSourceImpl extends TestSource {
		private boolean mFirstTime = true;
		
		TestSourceImpl (Scope scope) {
			super (scope);
		}

		@Override
		public TestLineImpl getTestLine () {
			if (mFirstTime) {
				mFirstTime = false;
				return new TestLineImpl (new String[] { "" });
			} else {
				return null;
			}
		}
	}
}
