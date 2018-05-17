package org.powertools.database;


public final class TablesForJoin {
    private final String _type;
    private final Source _source1;
    private final Table _table2;
    
    TablesForJoin (String type, Source source1, Table table2) {
        _type    = type;
        _source1 = source1;
        _table2  = table2;
    }
    
    public JoinedTable on (Column column1, Column column2) {
        return new JoinedTable (_type, _source1, _table2, column1, column2);
    }
}
