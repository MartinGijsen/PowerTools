package org.powertools.database;


public abstract class Table extends Source {
    public final String _tableName;
    // TODO: use reflection to determine name?
    public final String _instanceName;
    
    public Table(String tableName, String instanceName) {
        this._tableName    = tableName;
        this._instanceName = instanceName;
    }
    
    // TODO: use reflection to determine name?
    protected final Column createColumn(String name) {
        return new Column (this, name);
    }

    public final Selectable asterisk() {
        return new Asterisk (this);
    }
    
    @Override
    boolean hasName(String name) {
        return _instanceName.equals (name);
    }
    
    @Override
    String getName() {
        return _instanceName;
    }
    
    @Override
    public String getFullName () {
        if (hasDefaultName ()) {
            return _tableName;
        } else {
            return _tableName + " " + _instanceName;
        }
    }
    
    final boolean hasDefaultName() {
        return _tableName.equals (_instanceName);
    }
    
    @Override
    public final String toString() {
        return getFullName ();
    }
}
