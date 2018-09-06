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

import java.util.HashMap;
import java.util.Map;
import org.powertools.database.expression.BooleanExpression;
import org.powertools.engine.ExecutionException;


public class UpdateQuery extends Query {
    private final String              _tableName;
    private final Map<String, String> _values;
    private WhereClause               _whereClause;


    public UpdateQuery (String tableName) {
        super ();
        _tableName   = tableName;
        _values      = new HashMap<> ();
        _whereClause = null;
    }

    public UpdateQuery value (String columnName, String value) {
        _values.put (columnName, value);
        return this;
    }

    public UpdateQuery where (BooleanExpression condition) {
        _whereClause = new WhereClause (condition);
        return this;
    }
    
    @Override
    public String toString () {
        String whereClause = _whereClause == null ? "" : _whereClause.toString ();
        return String.format ("UPDATE %s SET %s%s", _tableName, getValues (), whereClause);
    }
    
    private String getValues () {
        if (_values.isEmpty ()) {
            throw new ExecutionException ("update query contains no values");
        }

        StringBuilder sb = new StringBuilder ();
        boolean isFirst  = true;
        for (String columnName : _values.keySet ()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append (", ");
            }
            sb.append (columnName).append ("=").append (_values.get (columnName));
        }
        return sb.toString ();
    }
}
