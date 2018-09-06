package org.powertools.database;


public class SelectSelectQuery {
    private final SelectQueryData _data;

    public SelectSelectQuery (boolean distinct, Selectable... selectables) {
        this (distinct);
        for (Selectable selectable : selectables) {
            _data.select (selectable);
        }
    }

    public SelectSelectQuery (boolean distinct) {
        _data = new SelectQueryData (distinct);
    }
    
    public SelectSelectQuery select (Selectable... selectables) {
        for (Selectable selectable : selectables) {
            _data.select (selectable);
        }
        return this;
    }

    public FromSelectQuery from (Table... tables) {
        return new FromSelectQuery (_data, tables);
    }

    public FromSelectQuery from (JoinedTable table) {
        return new FromSelectQuery (_data, table);
    }
}
