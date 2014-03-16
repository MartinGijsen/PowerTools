/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools engine.
 *
 * The PowerTools engine is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine.core;

import org.junit.Test;
import static org.junit.Assert.*;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.TestLine;
import org.powertools.engine.instructions.Executor;
import org.powertools.engine.instructions.InstructionSet;
import org.powertools.engine.instructions.ProcedureExecutor;
import org.powertools.engine.sources.Procedure;


public class InstructionsTest {
    private final String INSTRUCTION_SET_NAME = "instruction set name";
    private final String INSTRUCTION_NAME     = "instruction name";

    @Test
    public void testAddInstructionSet () {
        Instructions instructions = new Instructions (null, null);
        try {
            instructions.getExecutor (INSTRUCTION_NAME);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
        try {
            instructions.getExecutor (INSTRUCTION_SET_NAME + "." + INSTRUCTION_NAME);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
        InstructionSet instructionSet = new InstructionSetImpl (INSTRUCTION_SET_NAME);
        instructions.addInstructionSet (instructionSet);
        assertNotNull (instructions.getExecutor (INSTRUCTION_NAME));
        assertNotNull (instructions.getExecutor (INSTRUCTION_SET_NAME + "." + INSTRUCTION_NAME));
        // second time exercises the cache
        assertNotNull (instructions.getExecutor (INSTRUCTION_NAME));
    }

    @Test
    public void testAddInstructionSetWithSameInstruction () {
        Instructions instructions = new Instructions (null, null);
        instructions.addInstructionSet (new InstructionSetImpl ("set 1"));
        instructions.addInstructionSet (new InstructionSetImpl ("set 2"));
        try {
            instructions.getExecutor (INSTRUCTION_NAME);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testAddInstructionSetTwice () {
        Instructions instructions = new Instructions (null, null);
        instructions.addInstructionSet (new InstructionSetImpl (INSTRUCTION_SET_NAME));
        try {
            instructions.addInstructionSet (new InstructionSetImpl (INSTRUCTION_SET_NAME));
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testAddProcedure () {
        String PROCEDURE_NAME = "procedure name";
        Procedure procedure = new Procedure (PROCEDURE_NAME);
        Instructions instructions = new Instructions (null, null);
        instructions.addProcedure (procedure);
        assertTrue (instructions.getExecutor (PROCEDURE_NAME) instanceof ProcedureExecutor);
    }

    @Test
    public void testCleanup () {
        Instructions instructions         = new Instructions (null, null);
        InstructionSetImpl instructionSet = new InstructionSetImpl ("instruction set name");
        instructions.addInstructionSet (instructionSet);
        assertFalse (instructionSet.isCleanupCalled ());
        instructions.cleanup ();
        assertTrue (instructionSet.isCleanupCalled ());
    }


    private class InstructionSetImpl implements InstructionSet {
        private final String mName;

        private boolean mCleanupCalled = false;
        
        private final Executor mExecutor = new Executor () {
            public boolean execute (TestLine testLine) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        
        InstructionSetImpl (String name) {
            mName = name;
        }
        
        public String getName () {
            return mName;
        }

        public void setup () {
            // ignore
        }

        public Executor getExecutor (String instructionName) {
            return mExecutor;
        }

        public void cleanup () {
            mCleanupCalled = true;
        }
        
        boolean isCleanupCalled () {
            return mCleanupCalled;
        }
    }
}
