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


/**
 * Makes built-in instructions available in the form with arguments mixed
 * with the instruction name. Example:
 * set <symbol name> to <value> -> Set_To_ (String name, String value)
 */
public final class BuiltinInstructions {
	private final BuiltinLogic mLogic;


	private BuiltinInstructions (RunTimeImpl runTime, Instructions instructions) {
		mLogic = new BuiltinLogic (runTime, instructions);
	}
	
	
	public static void register (RunTimeImpl runTime, Instructions instructions) {
		new BuiltinInstructions (runTime, instructions).register ();
	}

	void register () {
		mLogic.register (this);
	}

	
	public boolean UseInstructionSet_ (String name) {
		return mLogic.useInstructionSet (name);
	}

	public boolean DefineNumberSequence_ (String name) {
		return DefineNumberSequence_From_ (name, 1);
	}

	public boolean DefineNumberSequence_From_ (String name, int initialValue) {
		return mLogic.defineNumberSequence (name, initialValue);
	}
	
	public boolean DefineStringSequence_ (String name) {
		return mLogic.defineStringSequence (name);
	}

	public boolean Add_ToSequence_ (String value, String name) {
		return mLogic.addStringToSequence (name, value);
	}

	public boolean DefineConstant_As_ (String name, String value) {
		return mLogic.defineConstant (name, value);
	}

	public boolean DefineVariable_ (String name) {
		return DefineVariable_As_ (name, "");
	}

	public boolean DefineVariable_As_ (String name, String value) {
		return mLogic.defineVariable (name, value);
	}

	public boolean DefineGlobalStructure_ (String name) {
		return mLogic.defineGlobalStructure (name);
	}

	public boolean DefineStructure_ (String name) {
		return mLogic.defineStructure (name);
	}

	public boolean ClearStructure_ (String name) {
		return mLogic.clearStructure (name);
	}

	public boolean Set_To_ (String name, String value) {
		return mLogic.set (name, value);
	}
	
	public boolean CopyStructure_To_ (String source, String target) {
		return mLogic.copyStructure (source, target);
	}
	
	public boolean Evaluate_ (String value) {
		return mLogic.evaluate (value);
	}

	public boolean Wait_Seconds (String nrOfSeconds) {
		return mLogic.waitSeconds (nrOfSeconds);
	}

	public boolean Wait_Minutes (String nrOfMinutes) {
		return mLogic.waitMinutes (nrOfMinutes);
	}
}