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

import java.util.List;


public class InsertQuery extends Query {
    private final TableName mTableName;
    private final MyList mColumnNames;
    private final MyList mValues;
    
    
    public InsertQuery (String tableName) {
        this (new TableName (tableName));
    }
    
    public InsertQuery (TableName tableName) {
        super ();
        mTableName   = tableName;
        mColumnNames = new MyList ("column names");
        mValues      = new MyList ("values");
    }

    public InsertQuery forColumn (String columnName) {
        return forColumn (new ColumnName (columnName));
    }

    public InsertQuery forColumn (ColumnName columnName) {
        mColumnNames.add (columnName);
        return this;
    }

    public InsertQuery forColumns (List<String> columnNames) {
        for (String columnName : columnNames) {
            mColumnNames.add (new ColumnName (columnName));
        }
        return this;
    }

    public InsertQuery withValue (String value) {
        mValues.add (new Value (value));
        return this;
    }
    
    public InsertQuery withValues (List<String> values) {
        for (String value : values) {
            mValues.add (new Value (value));
        }
        return this;
    }
    
    @Override
    public String toString () {
        return String.format ("INSERT INTO %s (%s) VALUES (%s)", mTableName, mColumnNames.toString (), mValues.toString ());
    }
}
