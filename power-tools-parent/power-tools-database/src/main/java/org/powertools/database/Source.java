package org.powertools.database;


public abstract class Source {
    public abstract String getFullName ();
    
    public TablesForJoin join (Table table) {
        return new TablesForJoin ("JOIN", this, table);
    }

    public TablesForJoin leftJoin (Table table) {
        return new TablesForJoin ("LEFT JOIN", this, table);
    }

    public TablesForJoin rightJoin (Table table) {
        return new TablesForJoin ("RIGHT JOIN", this, table);
    }

    public TablesForJoin fullJoin (Table table) {
        return new TablesForJoin ("FULL JOIN", this, table);
    }
}
