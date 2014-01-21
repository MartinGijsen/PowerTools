/* Copyright 2013-2014 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools.
 *
 * The PowerTools are free software: you can redistribute them and/or
 * modify them under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools are distributed in the hope that they will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine.instructions;

import org.junit.Test;
import static org.junit.Assert.*;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.KeywordName;
import org.powertools.engine.ParameterOrder;


public class ClassInstructionSetTest {
    private static final String NAME = "name";
    
    @Test
    public void testGetName () {
        ClassInstructionSet instructionSet = new ClassInstructionSet (NAME, null);
        assertEquals (NAME, instructionSet.getName ());
    }

    @Test
    public void testSetupCleanup () {
        InstructionSetImpl userInstructionSet = new InstructionSetImpl ();
        ClassInstructionSet instructionSet    = new ClassInstructionSet (NAME, userInstructionSet);
        assertFalse (userInstructionSet.isSetupCalled ());
        assertFalse (userInstructionSet.isCleanupCalled ());
        instructionSet.setup ();
        assertTrue (userInstructionSet.isSetupCalled ());
        assertFalse (userInstructionSet.isCleanupCalled ());
        instructionSet.cleanup ();
        assertTrue (userInstructionSet.isSetupCalled ());
        assertTrue (userInstructionSet.isCleanupCalled ());
    }

    @Test
    public void testSetupCleanupWithReflection () {
        InstructionSetClass userInstructionSet = new InstructionSetClass ();
        ClassInstructionSet instructionSet     = new ClassInstructionSet (NAME, userInstructionSet);
        assertFalse (userInstructionSet.isSetupCalled ());
        assertFalse (userInstructionSet.isCleanupCalled ());
        instructionSet.setup ();
        assertTrue (userInstructionSet.isSetupCalled ());
        assertFalse (userInstructionSet.isCleanupCalled ());
        instructionSet.cleanup ();
        assertTrue (userInstructionSet.isSetupCalled ());
        assertTrue (userInstructionSet.isCleanupCalled ());
    }

    @Test
    public void testSetupCleanupWithExceptions () {
        InstructionSetClassWithIssues userInstructionSet = new InstructionSetClassWithIssues ();
        ClassInstructionSet instructionSet               = new ClassInstructionSet (NAME, userInstructionSet);
        try {
            instructionSet.setup ();
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ignore
        }
//        try {
//            instructionSet.cleanup ();
//            fail ("no exception");
//        } catch (ExecutionException ee) {
//            // ignore
//        }
    }

    @Test
    public void testGetExecutorUnknownInstruction () {
        String instructionName                = "unknown instruction name";
        InstructionSetImpl userInstructionSet = new InstructionSetImpl ();
        ClassInstructionSet instructionSet    = new ClassInstructionSet (NAME, userInstructionSet);
        assertNull (instructionSet.getExecutor (instructionName));
    }

    @Test
    public void testGetExecutorInstructionNoParameters () {
        String instructionName                = "instruction name";
        InstructionSetImpl userInstructionSet = new InstructionSetImpl ();
        ClassInstructionSet instructionSet    = new ClassInstructionSet (NAME, userInstructionSet);
        assertTrue (instructionSet.getExecutor (instructionName) instanceof MethodExecutor);
    }

    @Test
    public void testGetExecutorInstructionWithParameters () {
        String instructionName                = "instruction _ name _";
        InstructionSetImpl userInstructionSet = new InstructionSetImpl ();
        ClassInstructionSet instructionSet    = new ClassInstructionSet (NAME, userInstructionSet);
        assertTrue (instructionSet.getExecutor (instructionName) instanceof MethodExecutor);
    }

    @Test
    public void testGetExecutorAnnotatedInstructionWithParameters () {
        String instructionName                = "alternative instruction name";
        InstructionSetImpl userInstructionSet = new InstructionSetImpl ();
        ClassInstructionSet instructionSet    = new ClassInstructionSet (NAME, userInstructionSet);
        assertTrue (instructionSet.getExecutor (instructionName) instanceof ShuffledParametersMethodExecutor);
    }


    private class InstructionSetImpl implements org.powertools.engine.InstructionSet {
        final String NAME = "some name";
        
        private boolean mIsSetupCalled;
        private boolean mIsCleanupCalled;
        
        InstructionSetImpl () {
            mIsSetupCalled   = false;
            mIsCleanupCalled = false;
        }
        
        public String getName () {
            return NAME;
        }

        public void setup () {
            mIsSetupCalled = true;
        }
        
        boolean isSetupCalled () {
            return mIsSetupCalled;
        }

        public void cleanup () {
            mIsCleanupCalled = true;
        }

        boolean isCleanupCalled () {
            return mIsCleanupCalled;
        }
        
        public void InstructionName () {
            // nothing
        }
        
        @KeywordName ("AlternativeInstructionName")
        @ParameterOrder ({ 2, 1 })
        public void Instruction_Name_ (int i, int j) {
            // nothing
        }
    }
    
    
    private class InstructionSetClass {
        private boolean mIsSetupCalled;
        private boolean mIsCleanupCalled;

        InstructionSetClass () {
            mIsSetupCalled   = false;
            mIsCleanupCalled = false;
        }

        public void setup () {
            mIsSetupCalled = true;
        }

        boolean isSetupCalled () {
            return mIsSetupCalled;
        }

        public void cleanup () {
            mIsCleanupCalled = true;
        }

        boolean isCleanupCalled () {
            return mIsCleanupCalled;
        }
    }


    private class InstructionSetClassWithIssues {
        public void setup () {
            Object object = null;
            object.toString ();
        }

        public void cleanup () {
            // TODO: generate another exception
        }
    }
}
