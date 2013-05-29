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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.powerTools.engine.ExecutionException;
import org.powerTools.engine.InstructionSet;
import org.powerTools.engine.RunTime;
import org.powerTools.engine.Symbol;
import org.powerTools.engine.instructions.InstructionSetFactory;
import org.powerTools.engine.symbol.Scope;
import org.powerTools.engine.symbol.StringSequence;


/**
 * Implements the builtin actions that are available from a test.
 */
final class BuiltinLogic {
	static final String INSTRUCTION_SET_NAME = "engine";

	private static final int MILLIS_PER_SECOND		= 1000;
	private static final int SECONDS_PER_MINUTE		= 60;
	private static final int MAX_NR_OF_FIELD_NAMES	= 20;
	
	private final RunTimeImpl mRunTime;
	private final Instructions mInstructions;
	private final String[] mFieldNames;


	BuiltinLogic (RunTimeImpl runTime, Instructions instructions) {
		mRunTime		= runTime;
		mInstructions	= instructions;
		mFieldNames		= new String[MAX_NR_OF_FIELD_NAMES];
		for (int fieldNameNr = 0; fieldNameNr < MAX_NR_OF_FIELD_NAMES; ++fieldNameNr) {
			mFieldNames[fieldNameNr] = "";
		}
	}


	protected void register (Object object) {
		register (INSTRUCTION_SET_NAME, object);
	}
	
	boolean register (String name, Object object) {
		mInstructions.addInstructionSet (InstructionSetFactory.createClassInstructionSet (name, object));
		return true;
	}


	// instruction sets
	boolean useInstructionSet (String className) {
		String name				= className;
		Object instructionSet	= getObject (className);
		if (instructionSet instanceof InstructionSet) {
			name = ((InstructionSet) instructionSet).getName ();
		}
		return register (name, instructionSet);
	}

	boolean useInstructionSet (String name, String className) {
		return register (name, getObject (className));
	}

	private Object getObject (String className) {
		try {
			Class<?> theClass = getClass (className);
			try {
				Constructor<?> constructor = theClass.getConstructor (new Class<?>[] { RunTime.class });
				mRunTime.reportInfo ("using constructor with RunTime parameter");
				return constructor.newInstance (mRunTime);
			} catch (SecurityException e) {
				;
			} catch (NoSuchMethodException e) {
				;
			}
			mRunTime.reportInfo ("using default constructor");
			return theClass.newInstance ();
		} catch (InvocationTargetException ite) {
			mRunTime.reportError (ite.toString ());
			mRunTime.reportError (ite.getCause ().toString ());
			return false;
		} catch (Exception e) {
			throw new ExecutionException (e.toString ());
		}
	}

	private Class<?> getClass (String className) {
		try {
			return Class.forName (className);
		} catch (ClassNotFoundException cnfe) {
			throw new ExecutionException (String.format ("class '%s' not found on the class path", className));
		}
	}

//	private Constructor<?> getConstructorWithRuntimeParameter (Class<?> aClass) {
//		try {
//			return aClass.getConstructor (new Class<?>[] { RunTime.class });
//		} catch (Exception e) {
//			return null;
//		}
////		Constructor<?> constructors[] = aClass.getConstructors ();
////	    for (Constructor<?> constructor : constructors) {
////	    	Class<?>[] parameterTypes = constructor.getParameterTypes(); 
////	    	if (parameterTypes.length == 1 && parameterTypes[0] == RunTime.class) {
////	    		return constructor;
////	    	}
////	    }
////	    
////	    return null;
//	}

	
//	boolean defineAlias (String newInstructionName, String oldInstructionName) {
//		if (containsInstructionSet (newInstructionName)) {
//			throw new ExecutionException ("instruction set not allowed in alias name");
//		} else if (mInstructions.getExecutor (newInstructionName) != null) {
//			throw new ExecutionException ("alias already exists");
//		} else {
//			final Executor instructionExecutor = mInstructions.getExecutor (oldInstructionName);
//			;
//			return mInstructions.createAlias (newInstructionName, oldInstructionName);
//		}
//	}
	
//	private boolean containsInstructionSet (String name) {
//		return name.contains (".");
//	}


	// number and string sequences
	boolean defineNumberSequence (String name, int initialValue) {
		mRunTime.getCurrentScope ().createNumberSequence (name, initialValue);
		return true;
	}
	
	boolean defineStringSequence (String name) {
		mRunTime.getCurrentScope ().createStringSequence (name);
		return true;
	}

	boolean addStringToSequence (String name, String value) {
		final Symbol symbol = mRunTime.getSymbol (name);
		if (symbol instanceof StringSequence) {
			symbol.setValue (value);
			return true;
		} else {
			throw new ExecutionException (String.format ("symbol '%s' is not a string sequence", name));
		}
	}


	// constants, variable and structures
	boolean defineConstant (String name, String value) {
		mRunTime.getCurrentScope ().createConstant (name, value);
		return true;
	}

	boolean defineVariable (String name, String value) {
		mRunTime.getCurrentScope ().createVariable (name, value);
		return true;
	}

	boolean defineGlobalStructure (String name) {
		Scope.getGlobalScope ().createStructure (name);
		return true;
	}

	boolean defineStructure (String name) {
		mRunTime.getCurrentScope ().createStructure (name);
		return true;
	}

	boolean clearStructure (String name) {
		mRunTime.getSymbol (name).clear (name);
		return true;
	}

	boolean set (String name, String value) {
		mRunTime.getSymbol (name).setValue (name, value);
		return true;
	}
	
	boolean copyStructure (String source, String target) {
		mRunTime.copyStructure (source, target);
		return true;
	}
	
//	boolean endInstruction () {
//		throw new EngineException ("not inside an instruction definition");
//	}
	
	boolean evaluate (String value) {
		return true;
	}


	boolean waitMilliseconds (String nrOfMilliseconds) {
		return sleep (Long.parseLong (nrOfMilliseconds));
	}

	boolean waitSeconds (String nrOfSeconds) {
		return sleep (Long.parseLong (nrOfSeconds) * MILLIS_PER_SECOND);
	}

	boolean waitMinutes (String nrOfMinutes) {
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
	
	boolean toFields (String structure, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20) {
		if (!structure.equalsIgnoreCase ("structure")) {
			throw new ExecutionException ("first argument must be 'structure'");
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

	boolean assign (String structure, String value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8, String value9, String value10, String value11, String value12, String value13, String value14, String value15, String value16, String value17, String value18, String value19, String value20) {
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

	boolean runSheet (String sheetName) {
		mRunTime.mSourceStack.run (sheetName);
		return true;
	}
	
	boolean declareRole (String system, String role, String domain, String username, String password) {
		mRunTime.getRoles ().addRole (system, role, domain, username, password);
		return true;
	}
}