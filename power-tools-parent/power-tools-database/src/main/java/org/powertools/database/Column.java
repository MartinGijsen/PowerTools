package org.powertools.database;

import org.powertools.database.expression.Term;


public class Column extends Term implements Selectable {
    public final String _name;
    
    protected final Table _table;

    Column (Table table, String name) {
        _table = table;
        _name  = name;
    }

    public ColumnAlias as (String aliasName) {
        return new ColumnAlias (this, aliasName);
    }
    
    @Override
    public String toString () {
        String prefix = _table.hasDefaultName () ? "" : _table._instanceName + ".";
        return prefix + _name;
    }
}
