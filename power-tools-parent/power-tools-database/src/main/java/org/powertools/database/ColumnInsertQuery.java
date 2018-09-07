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


public final class ColumnInsertQuery {
    private final InsertQueryData _data;
    
    ColumnInsertQuery(Table table) {
        _data = new InsertQueryData(table);
    }
    
    public ColumnInsertQuery column(Column... columns) {
        return columns(columns);
    }

    public ColumnInsertQuery columns(Column... columns) {
        _data.columns(columns);
        return this;
    }

    public ColumnInsertQuery columnValue(Column column, String value) {
        _data.columns(column);
        _data.values(value);
        return this;
    }

    public InsertQuery value(String... values) {
        return values(values);
    }

    public InsertQuery values(String... values) {
        return new InsertQuery(_data, values);
    }
}
