package org.powertools.database;


// TODO: move static expression methods into this class?
public final class Query {
    public static SelectSelectQuery select(Selectable... selectables) {
        return new SelectSelectQuery(false, selectables);
    }
    
    public static SelectSelectQuery selectDistinct(Selectable... selectables) {
        return new SelectSelectQuery(true, selectables);
    }
    
    public static FromSelectQuery selectFrom(Source... sources) {
        return select().from (sources);
    }
    
    public static FromSelectQuery selectDistinctFrom(Source... sources) {
        return selectDistinct().from (sources);
    }
    
    public static Selectable asterisk() {
        return Asterisk.getInstance();
    }
    
    public static WhereDeleteQuery deleteFrom(Table table) {
        return new WhereDeleteQuery(table);
    }
    
    public static ColumnInsertQuery insertInto(Table table) {
        return new ColumnInsertQuery(table);
    }
    
    public static ValueUpdateQuery update(Table table) {
        return new ValueUpdateQuery(table);
    }
}
