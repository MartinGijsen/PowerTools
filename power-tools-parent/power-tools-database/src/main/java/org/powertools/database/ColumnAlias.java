package org.powertools.database;


final class ColumnAlias implements Selectable {
    private final Column _column;
    private final String _alias;
    
    ColumnAlias (Column column, String alias) {
        _column = column;
        _alias = alias;
    }
    
    @Override
    public String toString () {
        return _column.toString () + " AS " + _alias;
    }
}
