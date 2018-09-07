package org.powertools.database;


public final class Queries {
    public static SelectSelectQuery select (Selectable... selectables) {
        return new SelectSelectQuery (false, selectables);
    }
    
    public static SelectSelectQuery selectDistinct (Selectable... selectables) {
        return new SelectSelectQuery (true, selectables);
    }
}
