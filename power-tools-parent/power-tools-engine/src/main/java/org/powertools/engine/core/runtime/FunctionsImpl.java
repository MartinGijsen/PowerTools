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

package org.powertools.engine.core.runtime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.Function;
import org.powertools.engine.Functions;


public class FunctionsImpl implements Functions {
    private final Map<String, List<Function>> mFunctions;
    
    public FunctionsImpl () {
        mFunctions = new HashMap<String, List<Function>> ();
    }

    @Override
    public void add (Function function) {
        String name         = function.getName ();
        int nrOfParameters  = function.getNrOfParameters ();
        List<Function> list = getList (name);
        if (newNrOfParameters (list, nrOfParameters)) {
            list.add (function);
        } else {
            throw new ExecutionException ("function '%s' with %d parameters already defined (unregister it first?)", name, nrOfParameters);
        }
    }

    @Override
    public Function get (String name, int nrOfParameters) {
        checkExists (name);
        return getFunction (name, nrOfParameters);
    }

    @Override
    public void remove (String name) {
        checkExists (name);
        if (mFunctions.get (name).size () == 1) {
            mFunctions.remove (name);
        } else {
            throw new ExecutionException ("function '%s' is unknown", name);
        }
    }

    @Override
    public void remove (String name, int nrOfParameters) {
        checkExists (name);
        List<Function> list = mFunctions.get (name);
        Iterator<Function> iter = list.iterator ();
        while (iter.hasNext ()) {
            Function currentFunction = iter.next ();
            if (currentFunction.getNrOfParameters () == nrOfParameters) {
                iter.remove ();
                return;
            }
        }
        throw new ExecutionException ("function '%s' with %d parameters is unknown", name, nrOfParameters);
    }

    private void checkExists (String name) {
        if (!mFunctions.containsKey (name)) {
            throw new ExecutionException ("function '%s' is unknown", name);
        }
    }
    
    private List<Function> getList (String name) {
        List<Function> list = mFunctions.get (name);
        if (list == null) {
            list = new LinkedList<Function> ();
            mFunctions.put (name, list);
        }
        return list;
    }
    
    private boolean newNrOfParameters (List<Function> list, int nrOfParameters) {
        for (Function currentFunction : list) {
            if (currentFunction.getNrOfParameters () == nrOfParameters) {
                return false;
            }
        }
        return true;
    }

    private Function getFunction (String name, int nrOfParameters) {
        for (Function currentFunction : mFunctions.get (name)) {
            if (currentFunction.getNrOfParameters () == nrOfParameters) {
                return currentFunction;
            }
        }
        throw new ExecutionException ("function '%s' with %d parameters is unknown", name, nrOfParameters);
    }
}
