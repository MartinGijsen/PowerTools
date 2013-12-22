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
import org.powertools.engine.Symbol;


public class ParameterTest {
	private final String PARAMETER_NAME = "parameterName";
	private final String VALUE1			= "some value";
	private final String VALUE2			= "another value";
	
	@Test
	public void testSetValue () {
		String VARIABLE_NAME = "variableName";
		Scope parentScope = new Scope (null);
		parentScope.createVariable (VARIABLE_NAME, VALUE1);
		Parameter parameter = new Parameter (PARAMETER_NAME, new Scope (parentScope), VARIABLE_NAME);
		assertEquals (VALUE1, parameter.getValue (VARIABLE_NAME));
		parameter.setValue (VARIABLE_NAME, VALUE2);
		assertEquals (VALUE2, parameter.getValue (VARIABLE_NAME));
	}

	@Test
	public void testClear () {
		String STRUCTURE_NAME	= "structureName";
		String FIELD_NAME		= "fieldName";
		Scope parentScope		= new Scope (null);
		Symbol structure		= parentScope.createStructure (STRUCTURE_NAME);
		structure.setValue (STRUCTURE_NAME + "." + FIELD_NAME, VALUE1);

		Parameter parameter = new Parameter (PARAMETER_NAME, new Scope (parentScope), STRUCTURE_NAME);
		assertEquals (VALUE1, parameter.getValue (STRUCTURE_NAME + "." + FIELD_NAME));
		parameter.clear (STRUCTURE_NAME);
		try {
			parameter.getValue (STRUCTURE_NAME + "." + FIELD_NAME);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}
}
