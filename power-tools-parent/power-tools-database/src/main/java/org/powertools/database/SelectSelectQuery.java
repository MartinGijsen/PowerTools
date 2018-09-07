package org.powertools.database;


public class SelectSelectQuery {
    private final SelectQueryData _data;

    public SelectSelectQuery (boolean distinct, Selectable... selectables) {
        _data = new SelectQueryData (distinct);
        for (Selectable selectable : selectables) {
            _data.select (selectable);
        }
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
