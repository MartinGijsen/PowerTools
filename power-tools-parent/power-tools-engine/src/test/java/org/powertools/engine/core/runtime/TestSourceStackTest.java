/*	Copyright 2013-2014 by Martin Gijsen (www.DeAnalist.nl)
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
		TestSource testSource = new TestSourceImpl (stack.getCurrentScope ());
		stack.initAndPush (testSource);
		assertEquals (testSource, stack.getCurrentTestSource ());
	}

	@Test
	public void testGetGlobalScopeGetCurrentScope () {
		TestSourceStack stack = new TestSourceStack ();
		Scope globalScope     = stack.getGlobalScope ();
		assertNotNull (globalScope);
		Scope localScope      = new Scope (globalScope);
		stack.initAndPush (new TestSourceImpl (localScope));
		assertEquals (localScope, stack.getCurrentScope ());
	}

	@Test
	public void testRun () {
        TestSourceStack stack = new TestSourceStack ();
        TestSourceImpl source = new TestSourceImpl (stack.getCurrentScope ());
        stack.initAndPush (source);
		assertFalse (source.isCreateCalled ());
        stack.run ("some filename");
		assertTrue (source.isCreateCalled ());
	}

	@Test
	public void testGetTestLine () {
		TestSourceStack stack = new TestSourceStack ();
		stack.initAndPush (new TestSourceImpl (stack.getCurrentScope ()));
		stack.initAndPush (new TestSourceImpl (stack.getCurrentScope ()));
		assertNotNull (stack.getTestLine ());
		assertNotNull (stack.getTestLine ());
		assertNull (stack.getTestLine ());
	}

	@Test
	public void testCreateAndPushTestCase () {
		TestSourceStack stack = new TestSourceStack ();
		stack.initAndPush (new TestSourceImpl (stack.getCurrentScope ()));
		assertTrue (stack.createAndPushTestCase ("test case name 1", "test case description 1"));
		assertTrue (stack.inATestCase ());
		assertTrue (stack.createAndPushTestCase ("test case name 2", "test case description 2"));
		assertTrue (stack.inATestCase ());
		stack.popTestCase ();
		assertFalse (stack.inATestCase ());
	}

	@Test
	public void pushTestCaseNestedAndNotInTestCase () {
		TestSourceStack stack = new TestSourceStack ();
		stack.initAndPush (new TestSourceImpl (stack.getCurrentScope ()));
		assertTrue (stack.createAndPushTestCase ("test case name 1", "test case description 1"));
		stack.initAndPush (new TestSourceImpl (stack.getCurrentScope ()));
		assertFalse (stack.createAndPushTestCase ("test case name 1", "test case description 1"));
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

	@Test
	public void testPopTestCase_NoTestCase () {
		try {
			TestSourceStack stack = new TestSourceStack ();
    		stack.initAndPush (new TestSourceImpl (stack.getCurrentScope ()));
			stack.popTestCase ();
			fail ("no exception");
		} catch (ExecutionException ee) {
			assertTrue (ee.getMessage ().contains ("not in a test case"));
		}
	}


	private class TestSourceImpl extends TestSource {
		private boolean mFirstTime = true;
        private boolean mCreateCalled = false;
		
		TestSourceImpl (Scope scope) {
			super (scope, null);
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
        
        @Override
        public TestSource create (String fileName) {
            mCreateCalled = true;
            return new TestSourceImpl (null);
        }
        
        boolean isCreateCalled () {
            return mCreateCalled;
        }
	}
}
