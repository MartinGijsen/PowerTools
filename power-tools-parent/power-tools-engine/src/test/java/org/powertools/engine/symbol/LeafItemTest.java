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

package org.powertools.engine.symbol;

import org.junit.Test;
import static org.junit.Assert.*;
import org.powertools.engine.ExecutionException;


public class LeafItemTest {
	private final String NAME  = "leaf";
	private final String VALUE = "some value";
	
	@Test
	public void testGetChild () {
		try {
			new LeafItem (NAME, null).getChild ("anything");
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}

	@Test
	public void testClear () {
		try {
			new LeafItem (NAME, null).clear ();
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}

	@Test
	public void testCreateLeaf () {
		assertNotNull (new LeafItem (NAME, null).createLeaf (new String[] { "structure", NAME }, 2));
	}

	@Test
	public void testCreateLeaf22222 () {
		try {
			new LeafItem (NAME, null).createLeaf (new String[] { "structure", NAME, "more" }, 2);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}

	@Test
	public void testSetValueGetValue () {
		LeafItem leaf = new LeafItem (NAME, null);
		leaf.setValue (VALUE);
		assertEquals (VALUE, leaf.getValue ());
	}
}
