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


public final class ValueUpdateQuery {
    private final UpdateQueryData _data;
    
    ValueUpdateQuery (Table table) {
        _data = new UpdateQueryData (table);
    }
    
    public ValueUpdateQuery value (Column column, String value) {
        _data.value (column, value);
        return this;
    }

    public UpdateQuery where (BooleanExpression condition) {
        return new UpdateQuery (_data, condition);
    }
}
