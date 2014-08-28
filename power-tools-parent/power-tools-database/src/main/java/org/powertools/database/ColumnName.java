package org.powertools.database;


public final class ColumnName implements ListItem, Expression {
    private final TableName mTableName;
    private final String mName;
    
    private static boolean mAddQuotes = false;
    

    public static void setAddQuotes (boolean addQuotes) {
        mAddQuotes = addQuotes;
    }
    
    
    public ColumnName (String name) {
        this (null, name);
    }
    
    public ColumnName (TableName tableName, String name) {
        mTableName = tableName;
        mName      = name;
    }
    
    
    public BooleanExpression equal (String columnName) {
        return equal (new ColumnName (columnName));
    }
    
    public BooleanExpression equal (Expression expression) {
        return new CompareExpression (this, CompareOperator.EQUAL, expression);
    }
    
    public BooleanExpression unequal (String columnName) {
        return unequal (new ColumnName (columnName));
    }
    
    public BooleanExpression unequal (Expression expression) {
        return new CompareExpression (this, CompareOperator.UNEQUAL, expression);
    }
    
    public BooleanExpression greaterThan (String columnName) {
        return greaterThan (new ColumnName (columnName));
    }
    
    public BooleanExpression greaterThan (Expression expression) {
        return new CompareExpression (this, CompareOperator.GREATER_THAN, expression);
    }
    
    public BooleanExpression greaterThanOrEqual (String columnName) {
        return greaterThanOrEqual (new ColumnName (columnName));
    }
    
    public BooleanExpression greaterThanOrEqual (Expression expression) {
        return new CompareExpression (this, CompareOperator.GREATER_THAN_OR_EQUAL, expression);
    }
    
    public BooleanExpression lessThan (String columnName) {
        return lessThan (new ColumnName (columnName));
    }
    
    public BooleanExpression lessThan (Expression expression) {
        return new CompareExpression (this, CompareOperator.LESS_THAN, expression);
    }
    
    public BooleanExpression lessThanOrEqual (String columnName) {
        return lessThanOrEqual (new ColumnName (columnName));
    }
    
    public BooleanExpression lessThanOrEqual (Expression expression) {
        return new CompareExpression (this, CompareOperator.LESS_THAN_OR_EQUAL, expression);
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
