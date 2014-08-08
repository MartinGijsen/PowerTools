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

import org.powertools.engine.Function;
import org.powertools.engine.Functions;
import java.util.HashMap;
import java.util.Map;
import org.powertools.engine.ExecutionException;


public class FunctionsImpl implements Functions {
    private final Map<String, Function> mFunctions;
    
    public FunctionsImpl () {
        mFunctions = new HashMap<String, Function> ();
        
        addBuiltins ();
    }

    private void addBuiltins () {
        add (new Function ("abs", 1) {
            public String execute (String[] args) {
                return Integer.toString (Math.abs (Integer.parseInt (args[0])));
            }
        });
    }
    
    @Override
    public void add (Function function) {
        String name = function.getName ();
        if (mFunctions.containsKey (name)) {
            throw new ExecutionException (String.format ("function '%s' already exists (unregister it first?)", name));
        } else {
            mFunctions.put (name, function);
        }
    }

    @Override
    public Function get (String name) {
        if (!mFunctions.containsKey (name)) {
            throw new ExecutionException (String.format ("function '%s' is unknown", name));
        } else {
            return mFunctions.get (name);
        }
    }

    @Override
    public void remove (String name) {
        if (mFunctions.remove (name) == null) {
            throw new ExecutionException (String.format ("function '%s' is unknown", name));
        }
    }
}
