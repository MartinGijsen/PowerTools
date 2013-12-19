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

package org.powertools.engine.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.powertools.engine.BusinessDayChecker;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.InstructionSet;
import org.powertools.engine.KeywordName;
import org.powertools.engine.ParameterOrder;
import org.powertools.engine.RunTime;
import org.powertools.engine.Symbol;
import org.powertools.engine.instructions.InstructionSetFactory;
import org.powertools.engine.symbol.BaseSequence;
import org.powertools.engine.symbol.Scope;


/**
 * Makes built-in instructions available in the form with arguments mixed
 * with the instruction name.
 * <BR/><BR/>
 * Example: set &lt;symbol name> to &lt;value> -> Set_To_ (String name, String value)
 */
public final class BuiltinInstructions implements InstructionSet {
	private static final int MAX_NR_OF_FIELD_NAMES	= 20;	
	private static final int MILLIS_PER_SECOND		= 1000;
	private static final int SECONDS_PER_MINUTE		= 60;

	private final RunTimeImpl mRunTime;
	private final Instructions mInstructions;
	private final String[] mFieldNames;


	private BuiltinInstructions (RunTimeImpl runTime, Instructions instructions) {
		mRunTime		= runTime;
		mInstructions	= instructions;
		
		mFieldNames = new String[MAX_NR_OF_FIELD_NAMES];
		for (int fieldNameNr = 0; fieldNameNr < MAX_NR_OF_FIELD_NAMES; ++fieldNameNr) {
			mFieldNames[fieldNameNr] = "";
		}
	}
	

	@Override
	public String getName () {
		return "engine";
	}
	
	@Override
	public void setup () {
		// empty
	}
	
	@Override
	public void cleanup () {
		// empty
	}
	
	
	public static void register (RunTimeImpl runTime, Instructions instructions) {
		BuiltinInstructions instructionSet = new BuiltinInstructions (runTime, instructions);
		instructionSet.register (instructionSet.getName (), instructionSet);
	}


	// instruction sets
	public boolean UseInstructionSet_ (String className) {
		String name				= className;
		Object instructionSet	= instantiateInstructionSet (className);
		if (instructionSet instanceof InstructionSet) {
			name = ((InstructionSet) instructionSet).getName ();
		}
		return register (name, instructionSet);
	}

	@KeywordName ("UseInstructionSet")
	@ParameterOrder ({ 2, 1 })
	public boolean UseInstructionSet_As_ (String className, String name) {
		return register (name, instantiateInstructionSet (className));
	}
	
	boolean register (String name, Object object) {
		mInstructions.addInstructionSet (InstructionSetFactory.createClassInstructionSet (name, object));
		return true;
	}

	private Object instantiateInstructionSet (String className) {
		Class<?> theClass = getClass (className);
		Object object = instantiateWithRunTimeParameter (theClass);
		if (object == null) {
			object = instantiateWithDefaultConstructor (theClass);
		}
		return object;
	}
	
	private Object instantiateWithRunTimeParameter (Class<?> theClass) {
		try {
			Constructor<?> constructor = theClass.getConstructor (new Class<?>[] { RunTime.class });
			mRunTime.reportInfo ("using constructor with RunTime parameter");
			return constructor.newInstance (mRunTime);
		} catch (NoSuchMethodException nsme) {
			mRunTime.reportInfo ("no constructor with RunTime parameter");
		} catch (IllegalAccessException iae) {
			mRunTime.reportWarning ("no access to constructor with RunTime parameter");
		} catch (InstantiationException ie) {
			mRunTime.reportWarning ("failed to call constructor with RunTime parameter");
		} catch (InvocationTargetException ite) {
			mRunTime.reportError (ite.toString ());
			throw new ExecutionException (ite.getCause ().toString ());
		}
		return null;
	}

	private Object instantiateWithDefaultConstructor (Class<?> theClass) {
		try {
			mRunTime.reportInfo ("using default constructor");
			return theClass.newInstance ();
		} catch (IllegalAccessException iae) {
			throw new ExecutionException ("no access to default constructor");
		} catch (InstantiationException ie) {
			throw new ExecutionException ("failed to call default constructor");
		}
	}
	
	private Class<?> getClass (String className) {
		try {
			return Class.forName (className);
		} catch (ClassNotFoundException cnfe) {
			throw new ExecutionException (String.format ("class '%s' not found on the class path", className));
		}
	}

	// test cases
	@KeywordName ("BeginTestCase")
	public boolean BeginTestCase_To_ (String name, String description) {
		return mRunTime.enterTestCase (name, description);
	}

	@KeywordName ("EndTestCase")
	public boolean EndTestCase () {
		return mRunTime.leaveTestCase ();
	}
	
	public boolean DefineNumberSequence_ (String name) {
		return DefineNumberSequence_From_ (name, 1);
	}

	@KeywordName ("DefineNumberSequence")
	public boolean DefineNumberSequence_From_ (String name, int initialValue) {
		mRunTime.getCurrentScope ().createNumberSequence (name, initialValue);
		return true;
	}
	
	@KeywordName ("DefineStringSequence")
	public boolean DefineStringSequence_ (String name) {
		mRunTime.getCurrentScope ().createStringSequence (name);
		return true;
	}

	@KeywordName ("DefineRepeatingStringSequence")
	public boolean DefineRepeatingStringSequence_ (String name) {
		mRunTime.getCurrentScope ().createRepeatingStringSequence (name);
		return true;
	}

	@KeywordName ("AddSequenceString")
	public boolean Add_ToSequence_ (String value, String name) {
		Symbol symbol = mRunTime.getSymbol (name);
		if (symbol instanceof BaseSequence) {
			symbol.setValue (value);
			return true;
		} else {
			throw new ExecutionException (String.format ("symbol '%s' is not a string sequence", name));
		}
	}

	@KeywordName ("DefineConstant")
	public boolean DefineConstant_As_ (String name, String value) {
		mRunTime.getCurrentScope ().createConstant (name, value);
		return true;
	}

	public boolean DefineVariable_ (String name) {
		return DefineVariable_As_ (name, "");
	}

	@KeywordName ("DefineVariable")
	public boolean DefineVariable_As_ (String name, String value) {
		mRunTime.getCurrentScope ().createVariable (name, value);
		return true;
	}

	@KeywordName ("DefineGlobalStructure")
	public boolean DefineGlobalStructure_ (String name) {
		Scope.getGlobalScope ().createStructure (name);
		return true;
	}

	@KeywordName ("DefineStructure")
	public boolean DefineStructure_ (String name) {
		mRunTime.getCurrentScope ().createStructure (name);
		return true;
	}

	@KeywordName ("ClearStructure")
	public boolean ClearStructure_ (String name) {
		mRunTime.getSymbol (name).clear (name);
		return true;
	}

	@KeywordName ("Set")
	public boolean Set_To_ (String name, String value) {
		mRunTime.getSymbol (name).setValue (name, value);
		return true;
	}
	
	@KeywordName ("CopyStructure")
	public boolean CopyStructure_To_ (String source, String target) {
		mRunTime.copyStructure (source, target);
		return true;
	}
	
	@KeywordName ("Evaluate")
	public boolean Evaluate_ (String value) {
		return true;
	}

	@KeywordName ("WaitMilliseconds")
	public boolean Wait_Milliseconds (String nrOfMilliseconds) {
		return sleep (Long.parseLong (nrOfMilliseconds));
	}

	@KeywordName ("WaitSeconds")
	public boolean Wait_Seconds (String nrOfSeconds) {
		return sleep (Long.parseLong (nrOfSeconds) * MILLIS_PER_SECOND);
	}

	@KeywordName ("WaitMinutes")
	public boolean Wait_Minutes (String nrOfMinutes) {
		return sleep (Long.parseLong (nrOfMinutes) * SECONDS_PER_MINUTE * MILLIS_PER_SECOND);
	}
	
	private boolean sleep (long nrOfMilliseconds) {
		try {
			Thread.sleep (nrOfMilliseconds);
			return true;
		} catch (InterruptedException ie) {
			throw new ExecutionException ("wait was interrupted");
		}
	}
	

	// roles
	public boolean Role_Username_Password_ (String role, String username, String password) {
		return System_Role_Domain_Username_Password_ ("", role, "", username, password);
	}
	
	@KeywordName ("DeclareRole")
	public boolean System_Role_Domain_Username_Password_ (String system, String role, String domain, String username, String password) {
		mRunTime.getRoles ().addRole (system, role, domain, username, password);
		return true;
	}
	
	@KeywordName ("SetBusinessDayChecker")
	public boolean SetBusinessDayCheckerTo_ (String className) {
		try {
			Object instance = Class.forName (className).newInstance ();
			mRunTime.setBusinessDayChecker ((BusinessDayChecker) instance);
			return true;
		} catch (ClassNotFoundException cnfe) {
			throw new ExecutionException ("unknown class: " + className);
		} catch (IllegalAccessException iae) {
			throw new ExecutionException ("no access to class " + className);
		} catch (InstantiationException ie) {
			throw new ExecutionException ("failed to create instance of class " + className);
		}
	}

	//@KeywordName ("")
	public boolean CheckThat_ (boolean value) {
		return value;
	}
	
	//@KeywordName ("")
	public boolean CheckThat_Contains_ (String value1, String value2) {
		return value1.indexOf (value2) >= 0;
	}

	//@KeywordName ("")
	public boolean CheckThat_DoesNotContain_ (String value1, String value2) {
		return !CheckThat_Contains_ (value1, value2);
	}

	//@KeywordName ("")
	public boolean CheckThat_IsWithin_Of_ (String value1String, String marginString, String value2String) {
		final double value1	= Double.parseDouble (value1String);
		final double margin	= Double.parseDouble (marginString);
		final double value2	= Double.parseDouble (value2String);
		return value1 >= value2 - margin && value1 <= value2 + margin;
	}

	//@KeywordName ("")
	public boolean CheckThat_IsNotWithin_Of_ (String value1String, String marginString, String value2String) {
		return !CheckThat_IsWithin_Of_ (value1String, marginString, value2String);
	}
	
	//@KeywordName ("")
	public boolean CheckThat_IsBetween_And_ (String valueString, String lowerBoundString, String upperBoundString) {
		final double value		= Double.parseDouble (valueString);
		final double lowerBound	= Double.parseDouble (lowerBoundString);
		final double upperBound	= Double.parseDouble (upperBoundString);
		return value >= lowerBound && value <= upperBound;
	}

	//@KeywordName ("")
	public boolean CheckThat_IsNotBetween_And_ (String valueString, String lowerBoundString, String upperBoundString) {
		return !CheckThat_IsBetween_And_ (valueString, lowerBoundString, upperBoundString);
	}
	
	
	// Keywords only (has parameters but not the corresponding underscores)
	public boolean RunSheet (String sheetName) {
		mRunTime.mSourceStack.run (sheetName);
		return true;
	}

	public boolean ToFields (String structure, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20) {
		if (!structure.isEmpty() && !"structure".equalsIgnoreCase (structure)) {
			throw new ExecutionException ("first argument must be empty or 'structure'");
		} else {
			mFieldNames[0]  = field1;
			mFieldNames[1]  = field2;
			mFieldNames[2]  = field3;
			mFieldNames[3]  = field4;
			mFieldNames[4]  = field5;
			mFieldNames[5]  = field6;
			mFieldNames[6]  = field7;
			mFieldNames[7]  = field8;
			mFieldNames[8]  = field9;
			mFieldNames[9]  = field10;
			mFieldNames[10] = field11;
			mFieldNames[11] = field12;
			mFieldNames[12] = field13;
			mFieldNames[13] = field14;
			mFieldNames[14] = field15;
			mFieldNames[15] = field16;
			mFieldNames[16] = field17;
			mFieldNames[17] = field18;
			mFieldNames[18] = field19;
			mFieldNames[19] = field20;
			return true;
		}
	}

	public boolean Assign (String structure, String value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8, String value9, String value10, String value11, String value12, String value13, String value14, String value15, String value16, String value17, String value18, String value19, String value20) {
		if (structure.isEmpty ()) {
			throw new ExecutionException ("missing structure");
		} else {
			assign (structure, mFieldNames[0], value1);
			assign (structure, mFieldNames[1], value2);
			assign (structure, mFieldNames[2], value3);
			assign (structure, mFieldNames[3], value4);
			assign (structure, mFieldNames[4], value5);
			assign (structure, mFieldNames[5], value6);
			assign (structure, mFieldNames[6], value7);
			assign (structure, mFieldNames[7], value8);
			assign (structure, mFieldNames[8], value9);
			assign (structure, mFieldNames[9], value10);
			assign (structure, mFieldNames[10], value11);
			assign (structure, mFieldNames[11], value12);
			assign (structure, mFieldNames[12], value13);
			assign (structure, mFieldNames[13], value14);
			assign (structure, mFieldNames[14], value15);
			assign (structure, mFieldNames[15], value16);
			assign (structure, mFieldNames[16], value17);
			assign (structure, mFieldNames[17], value18);
			assign (structure, mFieldNames[18], value19);
			assign (structure, mFieldNames[19], value20);
			return true;
		}
	}

	private void assign (String structure, String fieldName, String value) {
		if (!fieldName.isEmpty ()) {
			mRunTime.setValue (structure + "." + fieldName, value);
		} else if (value != null && !value.isEmpty ()) {
			throw new ExecutionException ("value specified without a field name");
		}
	}
}