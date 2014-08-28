package org.powertools.database;


public abstract class Query {
    public static TableName table (String name) {
        return new TableName (name);
    }

    public static ColumnName column (String name) {
        return new ColumnName (name);
    }
}
