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


class MethodExecutor extends BaseMethodExecutor {
    protected final Object[]            mArguments;
    protected final ParameterConvertors mConvertors;


    MethodExecutor (Object object, Method method, ParameterConvertors convertors) {
        super (object, method);
        mArguments  = new Object[method.getParameterTypes ().length];
        mConvertors = convertors;
    }

    @Override
    Object[] getArguments () {
        Class<?>[] parameterTypes = mMethod.getParameterTypes ();
        int nrOfParameters        = parameterTypes.length;
        checkNrOfArguments (mTestLine, nrOfParameters);

        for (int argNr = 0; argNr < nrOfParameters; ++argNr) {
            mArguments[argNr] = mConvertors.get (parameterTypes[argNr]).toObject (mTestLine.getPart (argNr + 1));
        }
        return mArguments;
    }

    @Override
    boolean invokeMethod () throws IllegalAccessException, InvocationTargetException {
        Class<?> returnType = mMethod.getReturnType ();
        Object[] arguments  = getArguments ();
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
