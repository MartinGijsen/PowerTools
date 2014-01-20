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

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.Symbol;


public class ScopeTest {
	private Scope mScope;
	
	
	@Before
	public void setUp () {
		mScope = new Scope (null);
	}

	
	@Test
	public void testGetParent () {
		Scope child = new Scope (mScope);
		assertEquals (mScope, child.getParent ());
	}

	@Test
	public void testGet () {
		Scope parent	= new Scope (null);
		Scope child		= new Scope (parent);
		try {
			child.get ("x");
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
		Symbol parentX	= parent.createVariable ("x", "");
		assertEquals (parentX, child.get ("x"));
		Symbol localX	= child.createVariable ("x", "");
		assertEquals (localX, child.get ("x"));
	}

	@Test
	public void testGetLocal () {
		Scope parent	= new Scope (null);
		Scope child		= new Scope (parent);
		parent.createVariable ("x", "");
		assertNull (child.getLocal ("x"));
		Symbol localX = child.createVariable ("x", "");
		assertEquals (localX, child.getLocal ("x"));
	}

	@Test
	public void testGetSymbolVariable () {
		Scope parent	= new Scope (null);
		Symbol parentX	= parent.createVariable ("x", "");
		Scope child		= new Scope (parent);
		assertEquals (parentX, child.getSymbol ("x"));
		Symbol localX	= child.createVariable ("x", "");
		assertEquals (localX, child.getSymbol ("x"));
	}

	@Test
	public void testGetSymbolStructure () {
		Scope parent	= new Scope (null);
		Symbol parentX	= parent.createStructure ("x");
		Scope child		= new Scope (parent);
		assertEquals (parentX, child.getSymbol ("x.a"));
		Symbol localX	= child.createStructure ("x");
		assertEquals (localX, child.getSymbol ("x.a"));
	}

	@Test
	public void testCreateConstant () {
		assertNotNull (mScope.createConstant ("x", ""));
		assertNotNull (mScope.get ("x"));
	}

	@Test
	public void testCreateParameter () {
		assertNotNull (mScope.createParameter ("x", ""));
		assertNotNull (mScope.get ("x"));
	}

	@Test
	public void testCreateVariable () {
		assertNotNull (mScope.createStructure ("x"));
		assertNotNull (mScope.get ("x"));
	}

	@Test
	public void testCreateStructure () {
		assertNotNull (mScope.createStructure ("x"));
		assertNotNull (mScope.get ("x"));
	}

	@Test
	public void testCreateNumberSequence () {
		assertNotNull (mScope.createNumberSequence ("x", 1));
		assertNotNull (mScope.get ("x"));
	}

	@Test
	public void testCreateStringSequence () {
		assertNotNull (mScope.createStringSequence ("x"));
		assertNotNull (mScope.get ("x"));
	}

	@Test
	public void testCreateRepeatingStringSequence () {
		assertNotNull (mScope.createRepeatingStringSequence ("x"));
		assertNotNull (mScope.get ("x"));
	}

	@Test
	public void testDuplicate () {
		Scope scope = new Scope (null);
		scope.createVariable ("x", "");
		try {
			scope.createVariable ("x", "");
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}
}
