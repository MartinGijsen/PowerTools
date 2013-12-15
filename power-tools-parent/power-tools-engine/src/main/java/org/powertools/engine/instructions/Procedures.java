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

package org.powertools.engine.instructions;

import java.util.HashMap;
import java.util.Map;

import org.powertools.engine.sources.Procedure;


/**
 * The Procedures class is used to collect scripted instructions.
 */
public final class Procedures implements InstructionSet {
	private static final String INVALID_INSTRUCTION_SET_NAME = "something that will not match an instruction set name";

	private final Map<String, Procedure> mProcedures;
	private final ProcedureRunner mRunner;

	
	public Procedures (ProcedureRunner runner) {
		mProcedures	= new HashMap<String, Procedure> ();
		mRunner		= runner;
	}


	public void add (Procedure procedure) {
		mProcedures.put (procedure.getName (), procedure);
	}
	
	@Override
	public String getName () {
		return INVALID_INSTRUCTION_SET_NAME;
	}
	
	@Override
	public void setup () {
		// empty
	}

	@Override
	public Executor getExecutor (String instructionName) {
		Procedure procedure = mProcedures.get (instructionName);
		return procedure != null ? new ProcedureExecutor (procedure, mRunner) : null;
	}

	@Override
	public void cleanup () {
		// empty
	}
}