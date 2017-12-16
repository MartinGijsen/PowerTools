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


abstract class BaseMethodExecutor implements Executor {
    protected final Object mObject;
    protected final Method mMethod;

    protected TestLine mTestLine;

    BaseMethodExecutor (Object object, Method method) {
        mObject = object;
        mMethod = method;
    }

    @Override
    public final boolean execute (TestLine testLine) {
        mTestLine = testLine;
        return invokeMethodAndHandleExceptions ();
    }

    abstract Object getArguments ();

    protected final void checkNrOfArguments (TestLine testLine, int maxNrOfArgs) {
        int actualNrOfArgs = testLine.getNrOfParts () - 1;
        if (actualNrOfArgs > maxNrOfArgs) {
            throw new ExecutionException ("instruction %s expects %d arguments but receives %d", testLine.getPart (0), maxNrOfArgs, actualNrOfArgs);
        }
    }

    private boolean invokeMethodAndHandleExceptions () {
        try {
            return invokeMethod ();
        } catch (IllegalAccessException iae) {
            throw new ExecutionException ("no access to method '%s' (is class public?)", mMethod.getName ());
        } catch (InvocationTargetException ite) {
            Throwable cause = ite.getCause ();
            if (cause instanceof ExecutionException) {
                throw (ExecutionException) cause;
            } else {
                throw new ExecutionException ("instruction exception: " + cause.toString (), cause.getStackTrace ());
            }
        }
    }

    abstract boolean invokeMethod () throws IllegalAccessException, InvocationTargetException;
}
