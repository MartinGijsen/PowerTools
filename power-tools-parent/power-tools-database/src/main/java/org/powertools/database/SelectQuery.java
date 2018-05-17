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

import java.util.LinkedList;
import java.util.List;
import org.powertools.database.util.MyList;
import org.powertools.database.expression.Condition;


public final class SelectQuery extends Query {
    private boolean                  _distinct;
    private final MyList<Selectable> _selection;
    private final List<Source>       _sources;
    private WhereClause              _whereClause;
    private GroupByClause            _groupByClause;
    

    public SelectQuery (Column column) {
        this ();
        select (column);
    }

    public SelectQuery () {
        super ();
        _distinct      = false;
        _selection     = new MyList<> ();
        _sources       = new LinkedList<> ();
        _whereClause   = null;
        _groupByClause = null;
    }


    public SelectQuery select (Selectable selectable) {
        _selection.add (selectable);
        return this;
    }

    public SelectQuery distinct () {
        _distinct = true;
        return this;
    }
    
    public SelectQuery from (Table newTable) {
        for (Source source : _sources) {
            if (source instanceof Table && newTable._instanceName.equals (((Table) source)._instanceName)) {
                throw new RuntimeException ("duplicate table name: " + ((Table) source)._instanceName);
            }
        }
        _sources.add (newTable);
        return this;
    }

    public SelectQuery from (JoinedTable joinedTable) {
        _sources.add (joinedTable);
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

    public SelectQuery groupBy (Column column) {
        if (_groupByClause == null) {
            _groupByClause = new GroupByClause (column);
        } else {
            _groupByClause.add (column);
        }
        return this;
    }

    @Override
    public String toString () {
        String distinct      = _distinct ? " DISTINCT" : "";
        String whereClause   = _whereClause == null ? "" : _whereClause.toString ();
        String groupByClause = _groupByClause == null ? "" : _groupByClause.toString ();
        return String.format ("SELECT%s %s\n%s%s%s",
                distinct, _selection.toString (), getFromClause (), whereClause, groupByClause);
    }
    
    private String getFromClause () {
        StringBuilder sb = new StringBuilder ();
        sb.append ("FROM ");
        boolean isFirst = true;
        for (Source source : _sources) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append (",\n");
            }
            sb.append (source.getFullName ());
        }
        return sb.toString ();
    }
}
