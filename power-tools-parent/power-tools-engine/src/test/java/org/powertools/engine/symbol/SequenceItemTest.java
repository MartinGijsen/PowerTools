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

import org.junit.Test;
import static org.junit.Assert.*;
import org.powertools.engine.ExecutionException;


public class SequenceItemTest {
	private final String SEQUENCE_NAME = "sequence";
	private final String LEAF_NAME     = "leaf";

	@Test
	public void testGetValue () {
		try {
			new SequenceItem (SEQUENCE_NAME, null).getValue ();
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}

	@Test
	public void testClear () {
		SequenceItem sequence = new SequenceItem (SEQUENCE_NAME, null);
		sequence.createLeaf (new String[] { LEAF_NAME }, 0);
		assertNotNull (sequence.getChild (LEAF_NAME));
		sequence.clear();
		try {
			sequence.getChild (LEAF_NAME);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}

	@Test
	public void testCreateLeafGetChild () {
		SequenceItem sequence = new SequenceItem (SEQUENCE_NAME, null);
		LeafItem leaf = sequence.createLeaf (new String[] { LEAF_NAME }, 0);
		assertEquals (leaf, sequence.getChild (LEAF_NAME));
	}

	@Test
	public void testCreateLeafAtSequence () {
		SequenceItem sequence = new SequenceItem (SEQUENCE_NAME, null);
		sequence.createLeaf (new String[] { SEQUENCE_NAME, LEAF_NAME }, 0);
		try {
			sequence.createLeaf (new String[] { SEQUENCE_NAME }, 0);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}

	@Test
	public void testCreateLeafWithNameBesideNumber () {
		SequenceItem sequence = new SequenceItem (SEQUENCE_NAME, null);
		sequence.createLeaf (new String[] { "0" }, 0);
		try {
			sequence.createLeaf (new String[] { LEAF_NAME }, 0);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}
}
