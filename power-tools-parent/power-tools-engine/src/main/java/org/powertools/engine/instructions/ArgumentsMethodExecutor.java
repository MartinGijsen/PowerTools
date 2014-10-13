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

import java.lang.reflect.Method;
import org.powertools.engine.Arguments;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.TestLine;


final class ArgumentsMethodExecutor extends BasicMethodExecutor {
    ArgumentsMethodExecutor (Object object, Method method) {
        super (object, method);
    }

    @Override
    public Object getArguments (TestLine testLine) {
        if ((testLine.getNrOfParts () % 2) == 0) {
            throw new ExecutionException ("using Arguments class with odd number of arguments");
        } else {
            Arguments args = new Arguments ();
            int nrOfArgs   = testLine.getNrOfParts () - 1;
            for (int argNr = 0; argNr < nrOfArgs; argNr += 2) {
                args.add (testLine.getPart (argNr + 1), testLine.getPart (argNr + 2));
            }
            return args;
        }
    }
}
