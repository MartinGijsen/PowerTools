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

package org.powertools.database;

import org.powertools.database.util.MyList;


final class InsertQueryData {
    private final Table          _table;
    private final MyList<String> _columnNames;
    private final MyList<String> _values;
    
    
    InsertQueryData (Table table) {
        _table       = table;
        _columnNames = new MyList<> ();
        _values      = new MyList<> ();
    }

    void columns(Column... columns) {
        for (Column column : columns) {
            _columnNames.add (column._name);
        }
    }

    void values(String... values) {
        for (String value : values) {
            _values.add (value);
        }
    }

    @Override
    public String toString () {
        return String.format ("INSERT INTO %s (%s) VALUES (%s)", _table.getName (), _columnNames.toString (), _values.toString ());
    }
}
