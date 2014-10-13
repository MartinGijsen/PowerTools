/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
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


abstract class BasicMethodExecutor implements Executor {
    protected final Object mObject;
    protected final Method mMethod;


    BasicMethodExecutor (Object object, Method method) {
        mObject = object;
        mMethod = method;
    }

    @Override
    public final boolean execute (TestLine testLine) {
        Object arguments = getArguments (testLine);
        return invokeMethodAndHandleExceptions (arguments);
    }

    abstract Object getArguments (TestLine testLine);

    final static void checkNrOfArguments (TestLine testLine, int maxNrOfArgs) {
        int actualNrOfArgs = testLine.getNrOfParts () - 1;
        if (actualNrOfArgs > maxNrOfArgs) {
            throw new ExecutionException (String.format ("instruction %s expects %d arguments but receives %d", testLine.getPart (0), maxNrOfArgs, actualNrOfArgs));
        }
    }

    private boolean invokeMethodAndHandleExceptions (Object arguments) {
        try {
            return invokeMethod (arguments);
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

    private boolean invokeMethod (Object arguments) throws IllegalAccessException, InvocationTargetException {
        Class<?> returnType = mMethod.getReturnType ();
        if (returnType == boolean.class) {
            return (Boolean) mMethod.invoke (mObject, arguments);
        } else if (returnType == void.class) {
            mMethod.invoke (mObject, arguments);
            return true;
        } else {
            mMethod.invoke (mObject, arguments);
            throw new ExecutionException ("method has invalid return type (must be boolean or void)");
        }
    }
}
