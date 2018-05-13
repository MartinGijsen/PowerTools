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
import org.powertools.database.expression.Condition;


public final class SelectQuery extends Query {
    private boolean                  _distinct;
    private final MyList<Selectable> _selection;
    private final MyList<Table>      _tableNames;
    private WhereClause              _whereClause;
    private GroupByClause            _groupByClause;
    

    public SelectQuery (String columnName) {
        this ();
        select (columnName);
    }

//    // TODO: enable while avoiding clash between static and non-static select
//    public static SelectQuery select (Column column) {
//        return new SelectQuery (column);
//    }
    
    public static SelectQuery select () {
        return new SelectQuery ();
    }
    
    public SelectQuery (Column column) {
        this ();
        select (column);
    }

    public SelectQuery () {
        super ();
        _distinct      = false;
        _selection     = new MyList<> ();
        _tableNames    = new MyList<> ();
        _whereClause   = null;
        _groupByClause = null;
    }


    public SelectQuery select (String columnName) {
        return select (new ColumnName (columnName));
    }

    public SelectQuery select (Selectable selectable) {
        _selection.add (selectable);
        return this;
    }

    public SelectQuery distinct () {
        _distinct = true;
        return this;
    }
    
    public SelectQuery from (Table table) {
        _tableNames.add (table);
        return this;
    }

    public SelectQuery where (Condition condition) {
        if (_whereClause == null) {
            _whereClause = new WhereClause (condition);
        } else {
            _whereClause.add (condition);
        }
        return this;
    }

    public SelectQuery groupBy (String columnName) {
        return groupBy (new ColumnName (columnName));
    }

    public SelectQuery groupBy (ColumnName columnName) {
        if (_groupByClause == null) {
            _groupByClause = new GroupByClause (columnName);
        } else {
            _groupByClause.add (columnName);
        }
        return this;
    }

    @Override
    public String toString () {
        String distinct      = _distinct ? " DISTINCT" : "";
        String whereClause   = _whereClause == null ? "" : _whereClause.toString ();
        String groupByClause = _groupByClause == null ? "" : _groupByClause.toString ();
        return String.format ("SELECT%s %s FROM %s%s%s",
                distinct, _selection.toString (), _tableNames.toString (), whereClause, groupByClause);
    }
}
