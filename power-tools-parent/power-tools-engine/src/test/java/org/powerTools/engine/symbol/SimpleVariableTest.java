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

package org.powertools.engine.symbol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.Symbol;


public class SimpleVariableTest {
	@Before
	public void setUp () throws Exception {
		mScope		= new Scope (Scope.getGlobalScope ());
		mVariable	= new SimpleVariable (NAME, mScope, VALUE1);
	}

	@After
	public void tearDown () throws Exception {
		mScope		= null;
		mVariable	= null;
	}

	
	@Test
	public void testSimpleVariable () {
		assertNotNull (mVariable);
	}

	@Test
	public void testGetValue () {
		assertEquals (VALUE1, mVariable.getValue (NAME));
	}

	@Test
	public void testSetValueStringString () {
		mVariable.setValue (NAME, VALUE2);
		assertEquals (VALUE2, mVariable.getValue (NAME));
	}

	@Test
	public void testSetValueString () {
		mVariable.setValue (VALUE2);
		assertEquals (VALUE2, mVariable.getValue (NAME));
	}

	@Test (expected=ExecutionException.class)
	public void testClear () {
		mVariable.clear ("a.b".split (Symbol.PERIOD));
	}

	@Test
	public void testGetName () {
		assertEquals (NAME, mVariable.getName ());
	}
	
	
	// private members
	private static final String NAME			= "name";
	private static final String VALUE1			= "value1";
	private static final String VALUE2			= "value2";

	private Scope mScope;
	private SimpleVariable mVariable;
}