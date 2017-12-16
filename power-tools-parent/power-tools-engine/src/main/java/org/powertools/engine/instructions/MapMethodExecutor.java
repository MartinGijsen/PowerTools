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
import java.util.HashMap;
import java.util.Map;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.TestLine;


final class MapMethodExecutor extends BaseMethodExecutor {
    private final Map<String, String> mArguments;


    MapMethodExecutor (Object object, Method method) {
        super (object, method);
        mArguments = new HashMap<> ();
    }

    @Override
    Object getArguments () {
        mArguments.clear ();
        int nrOfArgs = mTestLine.getNrOfParts () - 1;
        for (int argNr = 0; argNr < nrOfArgs; argNr += 2) {
            mArguments.put (mTestLine.getPart (argNr + 1), mTestLine.getPart (argNr + 2));
        }
        return mArguments;
    }

    // TODO: why not use mArguments field?
    @Override
    boolean invokeMethod () throws IllegalAccessException, InvocationTargetException {
        Class<?> returnType = mMethod.getReturnType ();
        Object arguments = getArguments ();
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
