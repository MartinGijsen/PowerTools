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

import static org.powertools.database.Query.asterisk;
import org.powertools.database.expression.BooleanExpression;
import org.powertools.database.util.MyList;


final class SelectQueryData {
    private final boolean            _distinct;
    private final MyList<Selectable> _selection;
    private final MyList<Source>     _sources;
    private BooleanExpression        _whereClause;
    private final MyList<String>     _groupByClause;
    

    SelectQueryData (boolean distinct) {
        _distinct      = distinct;
        _selection     = new MyList<> ();
        _sources       = new MyList<> (",\n");
        _whereClause   = null;
        _groupByClause = new MyList<> ();
    }


    void select (Selectable selectable) {
        _selection.add (selectable);
    }

    void from (Source... sources) {
        for (Source newSource : sources) {
            _sources.add (newSource);
        }
    }

    void where (BooleanExpression condition) {
        if (_whereClause == null) {
            _whereClause = condition;
        } else {
            // TODO: make impossible and remove
            throw new RuntimeException ("multiple where clauses");
        }
    }

    void groupBy (Column... columns) {
        for (Column column : columns) {
            _groupByClause.add (column.toString ());
        }
    }

    @Override
    public String toString () {
        if (_selection.isEmpty ()) {
            _selection.add (asterisk());
        }
        String distinct      = _distinct ? " DISTINCT" : "";
        String whereClause   = _whereClause == null ? "" : "\nWHERE " + _whereClause.toString ();
        String groupByClause = _groupByClause.isEmpty() ? "" : " GROUP BY " + _groupByClause.toString ();
        return String.format ("SELECT%s %s\nFROM %s%s%s",
                distinct, _selection.toString (), _sources.toString (), whereClause, groupByClause);
    }
}
