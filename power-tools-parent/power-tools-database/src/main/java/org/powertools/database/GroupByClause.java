/* Copyright 2015 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools.
 *
 * The PowerTools are free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools are distributed in the hope that they will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.database;

import org.powertools.database.util.MyList;


final class GroupByClause {
    private final MyList<String> _columnNames;
    
    GroupByClause (Column column) {
        _columnNames = new MyList<> ();
        add (column);
    }
    
    void add (Column column) {
        _columnNames.add (column._name);
    }
    
    @Override
    public String toString () {
        return " GROUP BY " + _columnNames.toString ();
    }
}
