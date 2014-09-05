package org.powertools.database;


public class DeleteQuery {
    private final TableName mTableName;

    private BooleanExpression mCondition;
    
    
    public DeleteQuery (String tableName) {
        this (new TableName (tableName));
    }

    public DeleteQuery (TableName tableName) {
        super ();
        mTableName = tableName;
    }

    public DeleteQuery where (BooleanExpression condition) {
        mCondition = (mCondition == null ? condition : new AndCondition (mCondition, condition));
        return this;
    }

    @Override
    public String toString () {
        return String.format ("DELETE FROM %s WHERE %s", mTableName, mCondition.toString ());
    }
}
