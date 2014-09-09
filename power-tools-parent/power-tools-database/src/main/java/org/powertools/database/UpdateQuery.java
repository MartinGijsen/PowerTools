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
import org.powertools.engine.ExecutionException;


public class UpdateQuery extends Query {
    private final TableName mTableName;
    private final Map<String, String> mValues;
    private final WhereClause mWhereClause;


    public UpdateQuery (String tableName) {
        this (new TableName (tableName));
    }
    
    public UpdateQuery (TableName tableName) {
        super ();
        mTableName   = tableName;
        mValues      = new HashMap<String, String> ();
        mWhereClause = new WhereClause ();
    }

    public UpdateQuery value (String columnName, String value) {
        mValues.put (columnName, value);
        return this;
    }

    public UpdateQuery where (BooleanExpression condition) {
        mWhereClause.add (condition);
        return this;
    }
    
    @Override
    public String toString () {
        return String.format ("UPDATE %s SET %s%s", mTableName, getValues (), mWhereClause.toString ());
    }
    
    private String getValues () {
        if (mValues.isEmpty ()) {
            throw new ExecutionException ("update query contains no values");
        }

        StringBuilder sb = new StringBuilder ();
        boolean isFirst  = true;
        for (String columnName : mValues.keySet ()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append (", ");
            }
            sb.append (columnName).append ("=").append (mValues.get (columnName));
        }
        return sb.toString ();
    }
}
