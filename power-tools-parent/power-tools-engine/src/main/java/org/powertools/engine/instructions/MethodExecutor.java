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

package org.powertools.engine.instructions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.TestLine;


class MethodExecutor implements Executor {
    protected final Object mObject;
    protected final Method mMethod;
    protected final Object[] mArguments;
    protected final ParameterConvertors mConvertors;


    MethodExecutor (Object object, Method method, ParameterConvertors convertors) {
        mObject     = object;
        mMethod     = method;
        mArguments  = new Object[method.getParameterTypes ().length];
        mConvertors = convertors;
    }

    @Override
    public final boolean execute (TestLine testLine) {
        getArguments (testLine);
        return invokeMethodAndHandleExceptions ();
    }


    protected void getArguments (TestLine testLine) {
        Class<?>[] parameterTypes = mMethod.getParameterTypes ();
        int nrOfParameters        = parameterTypes.length;
        checkNrOfArguments (testLine, nrOfParameters);

        for (int argNr = 0; argNr < nrOfParameters; ++argNr) {
            mArguments[argNr] = mConvertors.get (parameterTypes[argNr]).toObject (testLine.getPart (argNr + 1));
        }
    }

    protected final void checkNrOfArguments (TestLine testLine, int maxNrOfArgs) {
        int actualNrOfArgs = testLine.getNrOfParts () - 1;
        if (actualNrOfArgs > maxNrOfArgs) {
            throw new ExecutionException (String.format ("instruction %s expects %d arguments but receives %d", testLine.getPart (0), maxNrOfArgs, actualNrOfArgs));
        }
    }

    private boolean invokeMethodAndHandleExceptions () {
        try {
            return invokeMethod ();
        } catch (IllegalAccessException iae) {
            throw new ExecutionException ("illegal access exception: " + iae.getCause ().toString ());
        } catch (InvocationTargetException ite) {
            Throwable cause = ite.getCause ();
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
