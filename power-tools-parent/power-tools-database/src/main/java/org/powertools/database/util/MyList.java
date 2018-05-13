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

package org.powertools.database.util;

import java.util.LinkedList;
import java.util.List;
import org.powertools.engine.ExecutionException;


public final class MyList<T> {
    private final List<T> _items;
    
    public MyList () {
        _items = new LinkedList<> ();
    }
    
    public void add (T item) {
        _items.add (item);
    }
    
    @Override
    public String toString () {
        if (_items.isEmpty ()) {
            throw new ExecutionException ("<empty>");
        }

        StringBuilder sb = new StringBuilder ();
        boolean isFirst  = true;
        for (T item : _items) {
            if (isFirst) {
                sb.append (item.toString ());
                isFirst = false;
            } else {
                sb.append (", ").append (item.toString());
            }
        }
        return sb.toString ();
    }
}
