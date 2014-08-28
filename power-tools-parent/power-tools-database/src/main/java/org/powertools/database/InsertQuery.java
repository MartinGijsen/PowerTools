package org.powertools.database;


public class InsertQuery extends Query {
    public static void main (String[] args) {
        InsertQuery query = new InsertQuery ("t")
                .forColumn ("x1")
                .forColumn ("x2")
                .value ("y1")
                .value ("y2");
        System.out.println (query.toString ());
    }
    
    
    private final TableName mTableName;
    private final MyList mColumnNames;
    private final MyList mValues;
    
    
    public InsertQuery (String tableName) {
        this (new TableName (tableName));
    }
    
    public InsertQuery (TableName tableName) {
        super ();
        mTableName   = tableName;
        mColumnNames = new MyList ("column names");
        mValues      = new MyList ("values");
    }

    public InsertQuery forColumn (String columnName) {
        return forColumn (new ColumnName (columnName));
    }

    public InsertQuery forColumn (ColumnName columnName) {
        mColumnNames.add (columnName);
        return this;
    }

    public InsertQuery value (String value) {
        mValues.add (new Value (value));
        return this;
    }
    
    @Override
    public String toString () {
        return String.format ("INSERT INTO %s (%s) VALUES (%s)", mTableName, mColumnNames.toString (), mValues.toString ());
    }
}
