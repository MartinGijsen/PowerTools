package org.powertools.database;


public abstract class Table {
    public final String _tableName;
    // TODO: use reflection to determine name?
    public final String _instanceName;
    
    public Column all = createColumn("*");
    
    public Table(String tableName, String instanceName) {
        this._tableName    = tableName;
        this._instanceName = instanceName;
    }
    
    // TODO: use reflection to determine name?
    protected final Column createColumn(String name) {
        return new Column(this, name);
    }
    
    final boolean hasDefaultName() {
        return _tableName.equals (_instanceName);
    }
    
    @Override
    public final String toString() {
        return _instanceName;
    }
}
