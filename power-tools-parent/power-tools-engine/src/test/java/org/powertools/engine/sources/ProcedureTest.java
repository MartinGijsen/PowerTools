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

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;


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
    public void testAddParameterInvalid () {
        try {
            mProcedure.addParameter ("a a", false);
            fail ("no exception");
        } catch (ParameterNameException pne) {
            // ok
        }
    }

    @Test
    public void testAddParameterDuplicate () {
        try {
            mProcedure.addParameter ("x", false);
            mProcedure.addParameter ("y", false);
        } catch (ParameterNameException pne) {
            fail ("exception: " + pne.getMessage ());
        }
        try {
            mProcedure.addParameter ("y", false);
            fail ("no exception");
        } catch (ParameterNameException pne) {
            // ok
        }
    }

    @Test
    public void testAddParameterIn () {
        try {
            mProcedure.addParameter (PARAMETER_NAME, false);
            List<ProcedureParameter> parameters = mProcedure.getParameters ();
            assertEquals (1, parameters.size ());
            ProcedureParameter parameter = mProcedure.getParameters ().get (0);
            assertEquals (PARAMETER_NAME, parameter.getName ());
            assertEquals (false, parameter.isOutput ());
        } catch (ParameterNameException pne) {
            fail ("exception: " + pne.getMessage ());
        }
    }

    @Test
    public void testAddParameterOut () {
        try {
            mProcedure.addParameter (PARAMETER_NAME, true);
            List<ProcedureParameter> parameters = mProcedure.getParameters ();
            assertEquals (1, parameters.size ());
            ProcedureParameter parameter = mProcedure.getParameters ().get (0);
            assertEquals (PARAMETER_NAME, parameter.getName ());
            assertEquals (true, parameter.isOutput ());
        } catch (ParameterNameException pne) {
            fail ("exception: " + pne.getMessage ());
        }
    }

//	@Test
//	public void testAddInstruction () {
//		List<String> instruction1 = new LinkedList<String> ();
//		instruction1.add ("a");
//		mProcedure.addInstruction (instruction1);
//		List<String> instruction2 = new LinkedList<String> ();
//		instruction2.add ("b");
//		mProcedure.addInstruction (instruction2);
//
//		Iterator<List<String>> iter = mProcedure.instructionIterator ();
//		assertEquals ("a", iter.next ().get (0));
//		assertEquals ("b", iter.next ().get (0));
//		assertEquals (false, iter.hasNext ());
//	}
}
