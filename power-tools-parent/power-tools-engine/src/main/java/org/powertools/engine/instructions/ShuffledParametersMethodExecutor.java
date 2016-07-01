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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.powertools.engine.ExecutionException;
import org.powertools.engine.ParameterOrder;
import org.powertools.engine.TestLine;


final class ShuffledParametersMethodExecutor extends MethodExecutor {
    private final int[] mParameterMapping;

    ShuffledParametersMethodExecutor (Object object, Method method, ParameterConvertors convertors) {
        super (object, method, convertors);
        mParameterMapping = mMethod.getAnnotation (ParameterOrder.class).value ();
        checkMapping ();
    }

    private void checkMapping () {
        int nrOfParams = mParameterMapping.length;
        
        if (nrOfParams != mMethod.getParameterTypes ().length) {
            throw createException ();
        } else {
            Set<Integer> set = new HashSet<Integer> ();
            for (int paramNr = 1; paramNr <= nrOfParams; ++paramNr) {
                set.add (paramNr);
            }

            for (int paramNr = 0; paramNr < nrOfParams; ++paramNr) {
                if (!set.remove (mParameterMapping[paramNr])) {
                    throw createException ();
                }
            }
        }
    }

    private ExecutionException createException () {
        return new ExecutionException ("incorrect @ParameterOrder annotation for instruction %s (all %d parameters must be referenced once)", mMethod.getName (), mMethod.getParameterTypes ().length);
    }

    @Override
    Object[] getArguments () {
        Class<?>[] parameterTypes = mMethod.getParameterTypes ();
        int nrOfParameters        = parameterTypes.length;
        checkNrOfArguments (mTestLine, nrOfParameters);

        for (int argNr = 0; argNr < nrOfParameters; ++argNr) {
            int mappedArgNr = mParameterMapping[argNr];
            mArguments[argNr] = mConvertors.get (parameterTypes[mappedArgNr - 1]).toObject (mTestLine.getPart (mappedArgNr));
        }
        return mArguments;
    }
}
