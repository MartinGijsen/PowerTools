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


public class StructureTest {
	private final String STRUCTURE_NAME	= "structureName";
	private final String FIELD_NAME		= "fieldName";
	private final String VALUE			= "some value";
	
	@Test
	public void testSetValue () {
		Structure structure = new Structure (STRUCTURE_NAME, null);
		structure.setValue (STRUCTURE_NAME + "." + FIELD_NAME, VALUE);
		assertEquals (VALUE, structure.getValue (STRUCTURE_NAME + "." + FIELD_NAME));
	}

	@Test
	public void testClear () {
		Structure structure = new Structure (STRUCTURE_NAME, null);
		structure.setValue (STRUCTURE_NAME + "." + FIELD_NAME, VALUE);
		assertEquals (VALUE, structure.getValue (STRUCTURE_NAME + "." + FIELD_NAME));
		structure.clear (STRUCTURE_NAME);
		try {
			structure.getValue (STRUCTURE_NAME + "." + FIELD_NAME);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}
}
