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

package org.powertools.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public final class Arguments {
    private final Map<String, String> mMap;

    public Arguments (Map<String, String> map) {
        this ();
        mMap.putAll (map);
    }

    public Arguments () {
        mMap = new HashMap<String, String> ();
    }

    public void add (String name, String value) {
        mMap.put (name, value);
    }
    
    public boolean hasArgument (String name) {
        return mMap.containsKey (name);
    }

    public String remove (String name) {
        if (mMap.containsKey (name)) {
            return mMap.remove (name);
        } else {
            throw new ExecutionException ("field name not found: " + name);
        }
    }

    public String get (String name) {
        String value = mMap.get (name);
        if (value == null) {
            throw new ExecutionException ("no such argument: " + name);
        }
        return value;
    }

    public List<String> getNames () {
        return new LinkedList<String> (mMap.keySet ());
    }

    public List<String> selectNames (Filter filter) {
        List<String> selectedNames = new LinkedList<String> ();
        for (String name : mMap.keySet ()) {
            if (filter.isOk (name)) {
                selectedNames.add (name);
            }
        }
        return selectedNames;
    }

    public Set<String> selectValues (Filter filter) {
        Set<String> selectedValues = new HashSet<String> ();
        for (String key : mMap.values ()) {
            if (filter.isOk (key)) {
                selectedValues.add (key);
            }
        }
        return selectedValues;
    }

    public static interface Filter {
        boolean isOk (String value);
    }
}
