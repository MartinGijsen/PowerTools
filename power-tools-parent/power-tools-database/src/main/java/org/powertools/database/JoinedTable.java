package org.powertools.database;


public class JoinedTable extends Source {
    private final String _type;
    private final Source _source1;
    private final Table  _table2;
    private final Column _column1;
    private final Column _column2;
    
    public JoinedTable (String type, Source source1, Table table2, Column column1, Column column2) {
        super ();
        _type    = type;
        _source1 = source1;
        _table2  = table2;
        _column1 = column1;
        _column2 = column2;
    }
    
    @Override
    public String getFullName () {
        return String.format ("%s %s %s ON %s = %s",
                _source1.getFullName (), _type, _table2.getFullName (), _column1.toString (), _column2.toString ());
    }
}
