/* Copyright 2014-2018 by Martin Gijsen (www.DeAnalist.nl)
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


public final class FromSelectQuery extends SelectQuery {
    FromSelectQuery (SelectQueryData data, Source... sources) {
        super(data);
        _data.from (sources);
    }
    
    public FromSelectQuery from (Source... sources) {
        _data.from (sources);
        return this;
    }

    public GroupBySelectQuery where (BooleanExpression condition) {
        return new GroupBySelectQuery (_data, condition);
    }

    public GroupBySelectQuery groupBy (Column... columns) {
        return new GroupBySelectQuery (_data, columns);
    }
}
