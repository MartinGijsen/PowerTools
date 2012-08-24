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
 * Makes built-in instructions available in the form with the instruction name
 * separate from the arguments. Example:
 * set <symbol name> <value> -> set (String name, String value)
 */
public final class BuiltinKeywords {
	private final BuiltinLogic mLogic;


	private BuiltinKeywords (RunTimeImpl runTime, Instructions instructions) {
		mLogic = new BuiltinLogic (runTime, instructions);
	}
	
	static void register (RunTimeImpl runTime, Instructions instructions) {
		new BuiltinKeywords (runTime, instructions).register ();
	}
	
	void register () {
		mLogic.register (this);
	}


	public boolean UseKeywordSet (String name) {
		return mLogic.useInstructionSet (name);
	}
	
	public boolean DefineNumberSequence (String name, int initialValue) {
		return mLogic.defineNumberSequence (name, initialValue);
	}
	
	public boolean DefineStringSequence (String name) {
		return mLogic.defineStringSequence (name);
	}

	public boolean AddSequenceString (String value, String name) {
		return mLogic.addStringToSequence (value, name);
	}

	public boolean DefineConstant (String name, String value) {
		return mLogic.defineConstant (name, value);
	}

	public boolean DefineVariable (String name, String value) {
		return mLogic.defineVariable (name, value);
	}

	public boolean DefineGlobalStructure (String name) {
		return mLogic.defineGlobalStructure (name);
	}

	public boolean DefineStructure (String name) {
		return mLogic.defineStructure (name);
	}

	public boolean ClearStructure (String name) {
		return mLogic.clearStructure (name);
	}

	public boolean Set (String name, String value) {
		return mLogic.set (name, value);
	}
	
	public boolean CopyStructure (String source, String target) {
		return mLogic.copyStructure (source, target);
	}
	
	public boolean Evaluate (String value) {
		return mLogic.evaluate (value);
	}

	public boolean WaitSeconds (String nrOfSeconds) {
		return mLogic.waitSeconds (nrOfSeconds);
	}
	
	public boolean WaitMinutes (String nrOfMinutes) {
		return mLogic.waitMinutes (nrOfMinutes);
	}
	
	public boolean ToFields (String structure, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20) {
		return mLogic.toFields (structure, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20);
	}

	public boolean Assign (String structure, String value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8, String value9, String value10, String value11, String value12, String value13, String value14, String value15, String value16, String value17, String value18, String value19, String value20) {
		return mLogic.assign (structure, value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20);
	}

	public boolean RunSheet (String sheetName) {
		return mLogic.RunSheet (sheetName);
	}
}