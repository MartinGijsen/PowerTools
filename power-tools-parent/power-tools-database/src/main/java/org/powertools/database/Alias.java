package org.powertools.database;


public class Alias implements ListItem {
    private final ColumnName mColumnName;
    private final String     mName;
    
    Alias (ColumnName columnName, String name) {
        mColumnName = columnName;
        mName       = name;
    }
    
    @Override
    public String toString () {
        return mColumnName.toString () + " AS " + mName;
    }
}
