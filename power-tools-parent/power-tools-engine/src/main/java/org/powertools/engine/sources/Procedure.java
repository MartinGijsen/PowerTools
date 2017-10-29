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
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import org.powertools.engine.ExecutionException;


// A Procedure is a scripted instruction.
public final class Procedure {
    private static final Pattern PARAMETER_NAME_PATTERN = Pattern.compile ("\\w+");
    
    private final String mName;
    private final List<ProcedureParameter> mParameters;
    private final List<List<List<String>>> mInstructionTables;


    public Procedure (String name) {
        mName              = name;
        mParameters        = new ArrayList<ProcedureParameter> ();
        mInstructionTables = new LinkedList<List<List<String>>> ();
    }


    public String getName () {
        return mName;
    }


    public void addParameter (String name, boolean isOutput) throws ParameterNameException {
        if (!PARAMETER_NAME_PATTERN.matcher (name).matches ()) {
            throw new ParameterNameException ("invalid parameter name '%s'", name);
        }
        for (ProcedureParameter parameter : mParameters) {
            if (parameter.getName ().equalsIgnoreCase (name)) {
                throw new ParameterNameException ("duplicate parameter name '%s'", name);
            }
        }

        mParameters.add (new ProcedureParameter (name, isOutput));
    }

    public void addTable (List<List<String>> table) {
        mInstructionTables.add (table);
    }

    List<ProcedureParameter> getParameters () {
        return mParameters;
    }

    Iterator<List<String>> instructionIterator () {
        return new MyIterator ();
    }
    
    private class MyIterator implements Iterator<List<String>> {
        private final Iterator<List<List<String>>> mTableIterator;
        
        private Iterator<List<String>> mInstructionIterator;
        
        
        MyIterator () {
            mTableIterator       = mInstructionTables.iterator ();
            mInstructionIterator = null;
        }
        
        public boolean hasNext () {
            if (mInstructionIterator == null) {
                if (mTableIterator.hasNext ()) {
                    mInstructionIterator = mTableIterator.next ().iterator ();
                } else {
                    return false;
                }
            }
            while (!mInstructionIterator.hasNext ()) {
                if (mTableIterator.hasNext ()) {
                    mInstructionIterator = mTableIterator.next ().iterator ();
                } else {
                    return false;
                }
            }
            return true;
        }

        public List<String> next () {
            return mInstructionIterator.next ();
        }

        public void remove () {
            throw new UnsupportedOperationException ("Not supported");
        }
        
    }
}
