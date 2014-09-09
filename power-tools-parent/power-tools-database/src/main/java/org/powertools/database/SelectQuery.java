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


public class SelectQuery extends Query {
    private final MyList mColumnNames;
    private final MyList mTableNames;
    private final WhereClause mWhereClause;
    
    
    public SelectQuery (String columnName) {
        this ();
        select (columnName);
    }

    public SelectQuery () {
        super ();
        mColumnNames = new MyList ("column names");
        mTableNames  = new MyList ("table names");
        mWhereClause = new WhereClause ();
    }


    // select
    public SelectQuery select (String columnName) {
        return select (new ColumnName (columnName));
    }

    public SelectQuery select (ColumnName columnName) {
        mColumnNames.add (columnName);
        return this;
    }

    public SelectQuery select (List<String> columnNames) {
        for (String columnName : columnNames) {
            mColumnNames.add (new ColumnName (columnName));
        }
        return this;
    }

    // from
    public SelectQuery from (String tableName) {
        return from (new TableName (tableName));
    }

    public SelectQuery from (TableName tableName) {
        mTableNames.add (tableName);
        return this;
    }

    // where
    public SelectQuery where (BooleanExpression condition) {
        mWhereClause.add (condition);
        return this;
    }

    // join
//    public SelectQuery join (ColumnName column1, ColumnName column2) {
//        TableName tableName = column1.getTableName ();
//        if (!mTableNames.contains (tableName)) {
//            from (tableName);
//        }
//        tableName = column2.getTableName ();
//        if (!mTableNames.contains (tableName)) {
//            from (tableName);
//        }
//        where (CompareExpression.equal (column1, column2));
//        return this;
//    }
    
    @Override
    public String toString () {
        return String.format ("SELECT %s FROM %s%s", mColumnNames.toString (), mTableNames.toString (), mWhereClause.toString ());
    }
}
