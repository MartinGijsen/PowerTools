package org.powertools.database;


public class SelectQuery extends Query {
    public static void main (String[] args) {
        SelectQuery query = new SelectQuery ()
                .select ("x")
                .select (table ("t").column ("x"))
                .from ("t")
                .from (table ("t"))
                //.where (equals ("y", "value"))
                //.where (equals (table ("t").column ("y"), "value"))
                .where (table ("t").column ("y").equal ("value"))
                .where (table ("t1").column ("y").equal (table ("t2").column ("z")));
        System.out.println (query.toString ());
    }
    
    
    private final MyList mColumnNames;
    private final MyList mTableNames;

    private BooleanExpression mCondition;
    
    
    public SelectQuery () {
        super ();
        mColumnNames = new MyList ("column names");
        mTableNames  = new MyList ("table names");
        mCondition   = null;
    }

    // select
    public SelectQuery select (String columnName) {
        return select (new ColumnName (columnName));
    }

    public SelectQuery select (ColumnName columnName) {
        mColumnNames.add (columnName);
        return this;
    }

    // from
    public SelectQuery from (String tableName) {
        return from (new TableName (tableName));
    }

    public SelectQuery from (TableName tableName) {
        mTableNames.add (tableName);
        return this;
    }

    // where & join
    public SelectQuery where (BooleanExpression condition) {
        mCondition = (mCondition == null ? condition : new AndCondition (mCondition, condition));
        return this;
    }

    public SelectQuery joinOn (ColumnName columnName1, ColumnName columnName2) {
        return this.where (new CompareExpression (columnName1, CompareOperator.EQUAL, columnName2));
    }

    @Override
    public String toString () {
        return String.format ("select %s from %s%s", mColumnNames.toString (), mTableNames.toString (), getWhereClause ());
    }
    
    private String getWhereClause () {
        return (mCondition == null ? "" : " where " + mCondition.toString ());
    }
}
