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


public final class MyList<T> {
    private final List<T> _items;
    private final String  _separator;
    
    public MyList () {
        _items     = new LinkedList<> ();
        _separator = ", ";
    }
    
    public MyList (String separator) {
        _items     = new LinkedList<> ();
        _separator = separator;
    }
    
    public void add (T item) {
        _items.add (item);
    }

    public boolean isEmpty() {
        return _items.isEmpty ();
    }
    
    @Override
    public String toString () {
        if (_items.isEmpty ()) {
            throw new RuntimeException ("list is empty");
        }

        StringBuilder sb = new StringBuilder ();
        boolean isFirst  = true;
        for (T item : _items) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append (_separator);
            }
            sb.append (item.toString ());
        }
        return sb.toString ();
    }
}
