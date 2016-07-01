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

import org.junit.Before;
import org.junit.Test;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.Scope;
import org.powertools.engine.Symbol;


public class NumberSequenceTest {
	private static final String NAME		= "name";
	private static final int INITIAL_VALUE	= 0;
	
	private Scope          mScope;
	private NumberSequence mSequence;

        
        @Before
	public void setUp () throws Exception {
		mScope		= new ScopeImpl (null);
		mSequence	= new NumberSequence (NAME, mScope, INITIAL_VALUE);
	}

	
	@Test
	public void testNumberSequence () {
		assertNotNull (mSequence);
	}
	

	@Test
	public void testGetValue () {
		assertEquals (Integer.toString (INITIAL_VALUE), mSequence.getValue (NAME));
		assertEquals (Integer.toString (INITIAL_VALUE + 1), mSequence.getValue (NAME));
	}

	@Test (expected=ExecutionException.class)
	public void testSetValueStringString () {
		mSequence.setValue (NAME, "0");
	}

	@Test (expected=ExecutionException.class)
	public void testSetValueString () {
		mSequence.setValue ("0");
	}

	@Test (expected=ExecutionException.class)
	public void testClear () {
		mSequence.clear ("a.b".split (Symbol.PERIOD));
	}

	@Test
	public void testGetName () {
		assertEquals (NAME, mSequence.getName ());
	}
}