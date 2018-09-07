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

import org.powertools.database.expression.BooleanExpression;
import org.powertools.database.util.MyList;


final class UpdateQueryData {
    private static class Pair {
        Column _column;
        String _value;
        
        Pair(Column column, String value) {
            _column = column;
            _value  = value;
        }
        
        @Override
        public String toString() {
            return _column._name + "=" + _value;
        }
    }
    
    private final Table        _table;
    private final MyList<Pair> _values;
    private BooleanExpression  _whereClause;


    UpdateQueryData (Table table) {
        _table       = table;
        _values      = new MyList<>();
        _whereClause = null;
    }

    void value (Column column, String value) {
        _values.add (new Pair(column, value));
    }

    void where (BooleanExpression condition) {
        _whereClause = condition;
    }
    
    @Override
    public String toString () {
        return String.format ("UPDATE %s SET %s\nWHERE %s",
                              _table.getName (), _values.toString (), _whereClause.toString ());
    }
}
