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

import org.powertools.database.expression.Term;


public final class ColumnName extends Term implements Selectable {
    private final TableName _tableName;
    private final String    _name;

    private static boolean _addQuotes = false;
    

    ColumnName (String name) {
        this (null, name);
    }
    
    ColumnName (TableName tableName, String name) {
        _tableName = tableName;
        _name      = name;
    }

    public static void setAddQuotes (boolean addQuotes) {
        _addQuotes = addQuotes;
    }

    public TableName getTableName () {
        return _tableName;
    }
    
    @Override
    public String toString () {
        if (_tableName == null) {
            return _addQuotes ? ("'" + _name + "'") : _name;
        } else {
            return String.format (_addQuotes ? "%s.'%s'" : "%s.%s", _tableName.toString (), _name);
        }
    }
}
