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


public final class ColumnName implements ListItem, Expression {
    private final TableName mTableName;
    private final String mName;

    private static boolean mAddQuotes = false;
    

    ColumnName (String name) {
        this (null, name);
    }
    
    ColumnName (TableName tableName, String name) {
        mTableName = tableName;
        mName      = name;
    }

    public static void setAddQuotes (boolean addQuotes) {
        mAddQuotes = addQuotes;
    }
    

    public Alias as (String name) {
        return new Alias (this, name);
    }
    
    public TableName getTableName () {
        return mTableName;
    }
    
    public BooleanExpression equal (Expression expression) {
        return CompareExpression.equal (this, expression);
    }
    
    public BooleanExpression unequal (Expression expression) {
        return CompareExpression.unequal (this, expression);
    }
    
    public BooleanExpression greaterThan (Expression expression) {
        return CompareExpression.greaterThan (this, expression);
    }
    
    public BooleanExpression greaterThanOrEqual (Expression expression) {
        return CompareExpression.greaterThanOrEqual (this, expression);
    }
    
    public BooleanExpression lessThan (Expression expression) {
        return CompareExpression.lessThan (this, expression);
    }
    
    public BooleanExpression lessThanOrEqual (Expression expression) {
        return CompareExpression.lessThanOrEqual (this, expression);
    }

    
    @Override
    public String toString () {
        if (mTableName == null) {
            return mAddQuotes ? ("'" + mName + "'") : mName;
        } else {
            return String.format (mAddQuotes ? "%s.'%s'" : "%s.%s", mTableName.toString (), mName);
        }
    }
}
