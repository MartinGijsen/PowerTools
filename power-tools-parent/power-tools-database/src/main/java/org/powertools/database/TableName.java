package org.powertools.database;


public final class TableName implements ListItem {
    private final String mName;
    
    public TableName (String name) {
        mName = name;
    }
    
    String getName () {
        return mName;
    }
    
    public ColumnName column (String name) {
        return new ColumnName (this, name);
    }
    
    @Override
    public String toString () {
        return mName;
    }
}
