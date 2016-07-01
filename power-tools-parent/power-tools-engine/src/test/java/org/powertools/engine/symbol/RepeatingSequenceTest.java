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


public class RepeatingSequenceTest {
	private static final String NAME	= "name";
	private static final String VALUE1	= "something";
	private static final String VALUE2	= "something else";
	
	private Scope                   mScope;
	private RepeatingStringSequence mSequence;

	
	@Before
	public void setUp () throws Exception {
		mScope		= new ScopeImpl (null);
		mSequence	= new RepeatingStringSequence (NAME, mScope);
	}

	
	@Test
	public void testGetName () {
		assertNotNull (mSequence);
		assertEquals(mSequence.getName (), NAME);
	}

	
	@Test (expected=ExecutionException.class)
	public void testGetValueEmptyWithName () {
		mSequence.getValue (NAME);
	}

	@Test (expected=ExecutionException.class)
	public void testGetValueEmptyWithoutName () {
		mSequence.getValue ();
	}

	@Test
	public void testGetValueOneValueWithName () {
		mSequence.setValue (NAME, VALUE1);
		assertEquals (VALUE1, mSequence.getValue (NAME));
		assertEquals (VALUE1, mSequence.getValue (NAME));
	}

	@Test
	public void testGetValueOneValueWithoutName () {
		mSequence.setValue (VALUE1);
		assertEquals (VALUE1, mSequence.getValue ());
		assertEquals (VALUE1, mSequence.getValue ());
	}

	@Test
	public void testGetValueTwoValuesWithName () {
		mSequence.setValue (NAME, VALUE1);
		mSequence.setValue (NAME, VALUE2);
		assertEquals (VALUE1, mSequence.getValue (NAME));
		assertEquals (VALUE2, mSequence.getValue (NAME));
		assertEquals (VALUE1, mSequence.getValue (NAME));
	}

	@Test
	public void testGetValueTwoValuesWithoutName () {
		mSequence.setValue (VALUE1);
		mSequence.setValue (VALUE2);
		assertEquals (VALUE1, mSequence.getValue ());
		assertEquals (VALUE2, mSequence.getValue ());
		assertEquals (VALUE1, mSequence.getValue ());
	}

	@Test (expected=ExecutionException.class)
	public void testSetValueAfterUse () {
		mSequence.setValue (VALUE1);
		assertEquals (VALUE1, mSequence.getValue (NAME));
		mSequence.setValue (VALUE1);
	}

	@Test (expected=ExecutionException.class)
	public void testClear () {
		mSequence.clear (NAME);
	}
}