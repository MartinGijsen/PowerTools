package org.powertools.database;


public class SelectSelectQuery {
    private final SelectQueryData _data;

    SelectSelectQuery (boolean distinct, Selectable... selectables) {
        _data = new SelectQueryData (distinct);
        for (Selectable selectable : selectables) {
            _data.select (selectable);
        }
    }

    public SelectSelectQuery select () {
        _data.select (Asterisk.getInstance ());
        return this;
    }
    
    public SelectSelectQuery select (Selectable... selectables) {
        for (Selectable selectable : selectables) {
            _data.select (selectable);
        }
        return this;
    }

    public FromSelectQuery from (Source... sources) {
        return new FromSelectQuery (_data, sources);
    }
}
