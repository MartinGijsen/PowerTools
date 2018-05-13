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


public class InsertQuery extends Query {
    private final TableName      _tableName;
    private final MyList<String> _columnNames;
    private final MyList<String> _values;
    
    
    public InsertQuery (String tableName) {
        this (new TableName (tableName));
    }
    
    public InsertQuery (TableName tableName) {
        super ();
        _tableName   = tableName;
        _columnNames = new MyList<> ();
        _values      = new MyList<> ();
    }

    public InsertQuery column (String columnName) {
        _columnNames.add (columnName);
        return this;
    }

    public InsertQuery column (Column column) {
        _columnNames.add (column._name);
        return this;
    }

    public InsertQuery columns (String... columnNames) {
        for (String columnName : columnNames) {
            _columnNames.add (columnName);
        }
        return this;
    }

    // TODO: rename to 'value'?
    public InsertQuery withValue (String value) {
        _values.add (value);
        return this;
    }
    
    public InsertQuery values (String... values) {
        for (String value : values) {
            _values.add (value);
        }
        return this;
    }
    
    @Override
    public String toString () {
        return String.format ("INSERT INTO %s (%s) VALUES (%s)", _tableName, _columnNames.toString (), _values.toString ());
    }
}
