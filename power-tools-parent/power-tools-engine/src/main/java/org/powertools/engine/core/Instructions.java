/* Copyright 2012-2014 by Martin Gijsen (www.DeAnalist.nl)
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

import java.util.HashMap;
import java.util.Map;

import org.powertools.engine.ExecutionException;
import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.instructions.Executor;
import org.powertools.engine.instructions.InstructionSet;
import org.powertools.engine.ProcedureRunner;
import org.powertools.engine.instructions.Procedures;
import org.powertools.engine.sources.Procedure;


/**
 * The Instructions contains all known instructions,
 * both scripted ones (i.e. procedures) and programmed ones.
 * <BR/><BR/>
 * An instruction set for procedures is already available.
 * Any number of instruction sets can be added.
 * <BR/><BR/>
 * An instruction name to search for can include the instruction set name.
 * An executor cache ensures that an instruction only has to be looked up once.
 * <BR/><BR/>
 * It is emptied when an instruction set is added to avoid name conflicts.
 */
final class Instructions {
    private final Map<String, InstructionSet> mInstructionSets;
    private final Map<String, Executor>       mExecutorCache;
    private final Procedures                  mProcedures;


    Instructions (ProcedureRunner runner, TestRunResultPublisher publisher) {
        mInstructionSets = new HashMap<> ();
        mExecutorCache   = new HashMap<> ();
        mProcedures      = new Procedures (runner, publisher);
        addInstructionSet (mProcedures);
    }

    void addInstructionSet (InstructionSet instructionSet) {
        String name = instructionSet.getName ();
        if (!mInstructionSets.containsKey (name)) {
            mInstructionSets.put (name, instructionSet);
            instructionSet.setup ();
            mExecutorCache.clear ();
        } else {
            throw new ExecutionException ("an instruction set '%s' is already registered", name);
        }
    }

    void removeInstructionSet (String name) {
        InstructionSet instructionSet = mInstructionSets.remove (name);
        if (instructionSet == null) {
            throw new ExecutionException ("no instruction set '%s' is registered", name);
        } else {
            instructionSet.cleanup ();
            mExecutorCache.clear ();
        }
    }

    void addProcedure (Procedure procedure) {
        mProcedures.add (procedure);
    }

    Executor getExecutor (String instructionName) {
        Executor executor = mExecutorCache.get (instructionName);
        return executor != null ? executor : createExecutor (instructionName);
    }

    private Executor createExecutor (String instructionName) {
        int position = instructionName.lastIndexOf ('.');
        if (position < 0) {
            return findMethodAndCreateExecutor (instructionName);
        } else {
            String instructionSetName  = instructionName.substring (0, position);
            String realInstructionName = instructionName.substring (position + 1);
            return getInstructionSet (instructionSetName).getExecutor (realInstructionName);
        }
    }

    private InstructionSet getInstructionSet (String instructionSetName) {
        InstructionSet instructionSet = mInstructionSets.get (instructionSetName);
        if (instructionSet == null) {
            throw new ExecutionException ("unknown instruction set '%s'", instructionSetName);
        } else {
            return instructionSet;
        }
    }

    private Executor findMethodAndCreateExecutor (String instructionName) {
        Executor executor = null;
        for (InstructionSet instructionSet : mInstructionSets.values ()) {
            Executor newExecutor = instructionSet.getExecutor (instructionName);
            if (executor == null) {
                executor = newExecutor;
            } else if (newExecutor != null) {
                throw new ExecutionException ("more than one implementation of '%s'", instructionName);
            }
        }
        if (executor != null) {
            mExecutorCache.put (instructionName, executor);
            return executor;
        } else {
                throw new ExecutionException ("unknown instruction '%s'", instructionName);
        }
    }

    void cleanup () {
        for (InstructionSet instructionSet : mInstructionSets.values ()) {
            instructionSet.cleanup ();
        }
    }
}
