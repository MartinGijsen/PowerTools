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

package org.powertools.engine.sources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.powertools.engine.ExecutionException;


// A Procedure is a scripted instruction.
public final class Procedure {
    private final String mName;
    private final List<ProcedureParameter> mParameters;
    private final List<List<String>> mInstructions;


    public Procedure (String name) {
        mName         = name;
        mParameters   = new ArrayList<ProcedureParameter> ();
        mInstructions = new ArrayList<List<String>> ();
    }


    public String getName () {
        return mName;
    }


    public void addParameter (String name, boolean isOutput) {
        String realName = name;
        for (ProcedureParameter parameter : mParameters) {
            if (parameter.getName ().equalsIgnoreCase (realName)) {
                throw new ExecutionException (String.format ("duplicate parameter name '%s'", parameter.getName ()));
            }
        }

        mParameters.add (new ProcedureParameter (realName, isOutput));
    }

    public void addInstruction (List<String> instruction) {
        mInstructions.add (instruction);
    }

    List<ProcedureParameter> getParameters () {
        return mParameters;
    }

    Iterator<List<String>> instructionIterator () {
        return mInstructions.iterator ();
    }
}
