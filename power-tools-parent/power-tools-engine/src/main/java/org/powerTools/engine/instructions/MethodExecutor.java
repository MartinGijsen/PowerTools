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

package org.powerTools.engine.instructions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.powerTools.engine.ExecutionException;
import org.powerTools.engine.TestLine;


final class MethodExecutor implements Executor {
	private final Object mObject;
	private final Method mMethod;
	private final Object[] mArguments;

	
	MethodExecutor (Object object, Method method) {
		mObject		= object;
		mMethod		= method;
		mArguments	= new Object[method.getParameterTypes ().length];
	}

	public boolean execute (TestLine testLine) {
		getArguments (testLine);
		return invokeMethodAndHandleExceptions ();
	}
	
	
	private void getArguments (TestLine testLine) {
		Class<?>[] parameterTypes	= mMethod.getParameterTypes ();
		int nrOfParameters			= parameterTypes.length;
		checkNrOfArguments (testLine, nrOfParameters);
		
		for (int argNr = 0; argNr < nrOfParameters; ++argNr) {
			mArguments[argNr] = getArgument (parameterTypes[argNr], testLine.getPart (argNr + 1));
		}
	}

	private void checkNrOfArguments (TestLine testLine, int maxNrOfArgs) {
		final int actualNrOfArgs = testLine.getNrOfParts () - 1;
		if (actualNrOfArgs > maxNrOfArgs) {
			throw new ExecutionException (String.format ("instruction %s expects %d arguments but receives %d", testLine.getPart (0), maxNrOfArgs, actualNrOfArgs));
		}
	}
	
	private Object getArgument (Class<?> parameterType, String arg) {
		if (parameterType == String.class) {
			return arg;
		} else if (arg.isEmpty ()) {
			throw new ExecutionException ("empty argument");
		} else if (parameterType == boolean.class) {
			return getBooleanArgument (arg);
		} else if (parameterType == int.class) {
			return getIntegerArgument (arg);
		} else if (parameterType == long.class) {
			return getLongArgument (arg);
		} else if (parameterType == float.class) {
			return getFloatArgument (arg);
		} else if (parameterType == double.class) {
			return getDoubleArgument (arg);
		} else {
			throw new ExecutionException ("unsupported parameter type");
		}
	}

	private Object getBooleanArgument (String arg) {
		if (arg.equalsIgnoreCase ("true")) {
			return Boolean.TRUE;
		} else if (arg.equalsIgnoreCase ("false")) {
			return Boolean.FALSE;
		} else {
			throw new ExecutionException ("invalid boolean: " + arg);
		}
	}
	
	private Object getIntegerArgument (String arg) {
		try {
			return Integer.valueOf (arg);
		} catch (NumberFormatException nfe) {
			throw new ExecutionException ("invalid integer number: " + arg);
		}
	}
	
	private Object getLongArgument (String arg) {
		try {
			return new Long (arg);
		} catch (NumberFormatException nfe) {
			throw new ExecutionException ("invalid long number: " + arg);
		}
	}
	
	private Object getFloatArgument (String arg) {
		try {
			return new Float (arg);
		} catch (NumberFormatException nfe) {
			throw new ExecutionException ("invalid float number: " + arg);
		}
	}
	
	private Object getDoubleArgument (String arg) {
		try {
			return new Double (arg);
		} catch (NumberFormatException nfe) {
			throw new ExecutionException ("invalid double number: " + arg);
		}
	}
	
	private boolean invokeMethodAndHandleExceptions () {
		try {
			return invokeMethod ();
		} catch (IllegalAccessException iae) {
			throw new ExecutionException ("illegal access exception: " + iae.getCause ().toString ());
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause ();
			if (cause instanceof ExecutionException) {
				throw (ExecutionException) cause;
			} else {
				throw new ExecutionException ("instruction exception: " + cause.toString (), cause.getStackTrace ());
			}
		}
	}
	
	private boolean invokeMethod () throws IllegalAccessException, InvocationTargetException {
		Class<?> returnType = mMethod.getReturnType ();
		if (returnType == boolean.class) {
			return (Boolean) mMethod.invoke (mObject, mArguments);
		} else if (returnType == void.class) {
			mMethod.invoke (mObject, mArguments);
			return true;
		} else {
			mMethod.invoke (mObject, mArguments);
			throw new ExecutionException ("method has invalid return type (must be boolean or void)");
		}
	}
}