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

package org.powerTools.engine.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.powerTools.engine.ExecutionException;
import org.powerTools.engine.instructions.Executor;
import org.powerTools.engine.instructions.InstructionSet;
import org.powerTools.engine.instructions.ProcedureRunner;
import org.powerTools.engine.instructions.Procedures;
import org.powerTools.engine.sources.Procedure;


/**
 * The Instructions contains all known instructions,
 * both scripted ones (i.e. procedures) and programmed ones.
 * 
 * An instruction set for procedures is already available.
 * Any number of instruction sets can be added.
 * To find the executor for an instruction and ensure it is unique,
 * all instruction sets are asked for one.
 * 
 * An executor cache ensures that an instruction only has to be looked up once.
 * It is emptied when an instruction set is added to avoid name conflicts.
 */
final class Instructions {
	private final Map<String, InstructionSet> mInstructionSets;
	private final Map<String, Executor> mExecutorCache;
	private final Procedures mProcedures;

	
	Instructions (ProcedureRunner runner) {
		mInstructionSets	= new HashMap<String, InstructionSet> ();
		mExecutorCache		= new HashMap<String, Executor> ();
		mProcedures			= new Procedures (runner);
		addInstructionSet (mProcedures);
	}

	
	void addInstructionSet (InstructionSet set) {
		if (!mInstructionSets.containsKey (set.getName ())) {
			mInstructionSets.put (set.getName (), set);
			mExecutorCache.clear ();
		} else {
			throw new ExecutionException ("an instruction set with this name already exists");
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
		final String[] parts = instructionName.split ("\\.");
		switch (parts.length) {
		case 1:
			return findMethodAndCreateExecutor (instructionName);
		case 2:
			return createExecutor (parts[0], parts[1]);
		default:
			throw new ExecutionException ("an instruction name can only contain one period");
		}
	}
	
	private Executor createExecutor (String instructionSetName, String instructionName) {
		//InstructionSet instructionSet = mInstructionSets.get (instructionSetName);
		//Executor executor = instructionSet.getExecutor (instructionName);
		return null;
	}
	
	private Executor findMethodAndCreateExecutor (String instructionName) {
		Executor executor = null;
		for (InstructionSet set : mInstructionSets.values ()) {
			final Executor newExecutor = set.getExecutor (instructionName);
			if (executor == null) {
				executor = newExecutor;
			} else if (newExecutor != null) {
				throw new ExecutionException ("more than one implementation of " + instructionName);
			}
		}
		if (executor != null) {
			mExecutorCache.put (instructionName, executor);
			return executor;
		} else {
			throw new ExecutionException ("unknown instruction");
		}
	}

	Set<String> getMethodNames () {
		final Set<String> methodNames = new HashSet<String> ();
		for (InstructionSet instructionSet : mInstructionSets.values ()) {
			for (Method method : instructionSet.getClass ().getMethods ()) {
				final String methodName = method.getName ();
				if (Character.isUpperCase (methodName.charAt (0))) {
					methodNames.add (methodName);
				}
			}
		}
		return methodNames;
	}
}