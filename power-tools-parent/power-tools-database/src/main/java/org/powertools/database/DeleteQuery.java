package org.powertools.database;

import static org.powertools.database.Query.column;


public class DeleteQuery {
    public static void main (String[] args) {
        DeleteQuery query = new DeleteQuery ("t")
                .where (column ("y1").equal ("abc"));
        System.out.println (query.toString ());
    }
    
    
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
