/* Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.symbol;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.powertools.engine.ExecutionException;
import org.powertools.engine.Symbol;


public final class Util {
    private Util () {
        // empty
    }

    public static void copy (Symbol sourceSymbol, Symbol targetSymbol, String[] sourceNames, String targetName) {
        Item sourceItem             = getItem (sourceSymbol, sourceNames);
        List<String> namesList      = new LinkedList<String> ();
        Map<String, String> results = new HashMap<String, String> ();
        process (sourceItem, namesList, results);
        for (String key : results.keySet ()) {
            targetSymbol.setValue (targetName + "." + key, results.get (key));
        }
    }


    private static Item getItem (Symbol symbol, String[] names) {
        if (symbol instanceof Structure) {
            return ((Structure) symbol).getItem (names);
        } else {
            throw new ExecutionException ("symbol '%s' is not a structure", symbol.getName ());
        }
    }

    private static void process (Item item, List<String> namesList, Map<String, String> results) {
        if (item instanceof SequenceItem) {
            for (Item child : ((SequenceItem) item).mChildren.values ()) {
                namesList.add (child.mName);
                process (child, namesList, results);
                namesList.remove (namesList.size () - 1);
            }
        } else {
            results.put (join (namesList), ((LeafItem) item).mValue);
        }
    }

    private static String join (Collection<String> s) {
        StringBuilder builder = new StringBuilder ();
        Iterator<String> iter = s.iterator();
        while (iter.hasNext ()) {
            builder.append (iter.next ());
            if (!iter.hasNext ()) {
                break;
            }
            builder.append (".");
        }
        return builder.toString ();
    }
}
