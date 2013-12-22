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

package org.powertools.engine.sources;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.TestLine;
import org.powertools.engine.symbol.Scope;


public class ProcedureTest {
	private final String PROCEDURE_NAME = "procedure name";
	private final String PARAMETER_NAME = "parameterName";

	private Procedure mProcedure;
	
	
	@Before
	public void setUp() {
		mProcedure = new Procedure (PROCEDURE_NAME);
	}
	
	@Test
	public void testGetName () {
		assertEquals (PROCEDURE_NAME, mProcedure.getName ());
	}

	@Test
	public void testAddParameterDuplicate () {
		mProcedure.addParameter ("x", false);
		mProcedure.addParameter ("y", false);
		try {
			mProcedure.addParameter ("y", false);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}

	@Test
	public void testAddParameterIn () {
		mProcedure.addParameter (PARAMETER_NAME, false);
		Scope scope       = new Scope (null);
		TestLine testLine = new TestLineImpl (new String[] { "instruction", "abc" });
		mProcedure.createParameters (scope, testLine);
		assertEquals ("abc", scope.get (PARAMETER_NAME).getValue ());
	}

	@Test
	public void testAddParameterOut () {
		String CONSTANT_NAME = "c";
		String CONSTANT_VALUE = "1";
		
		mProcedure.addParameter (PARAMETER_NAME, true);
		Scope parentScope = new Scope (null);
		parentScope.createConstant (CONSTANT_NAME, CONSTANT_VALUE);
		Scope childScope = new Scope (parentScope);
		TestLine testLine = new TestLineImpl (new String[] { "x", "c" });
		mProcedure.createParameters (childScope, testLine);
		assertEquals ("1", childScope.get (PARAMETER_NAME).getValue ());
	}

	@Test
	public void testAddParameter () {
		Scope scope       = new Scope (null);
		TestLine testLine = new TestLineImpl (new String[] { "instruction", "abc" });
		try {
			mProcedure.createParameters (scope, testLine);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}

	@Test
	public void testAddInstruction () {
		List<String> instruction1 = new LinkedList<String> ();
		instruction1.add ("a");
		mProcedure.addInstruction (instruction1);
		List<String> instruction2 = new LinkedList<String> ();
		instruction2.add ("b");
		mProcedure.addInstruction (instruction2);

		Iterator<List<String>> iter = mProcedure.instructionIterator ();
		assertEquals ("a", iter.next ().get (0));
		assertEquals ("b", iter.next ().get (0));
		assertEquals (false, iter.hasNext ());
	}
}
