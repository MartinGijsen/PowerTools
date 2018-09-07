package org.powertools.database;


final class Asterisk implements Selectable {
    private static final Asterisk THE_ONE = new Asterisk ();
    
    private final Table _table;
    
    private Asterisk() {
        this(null);
    }
    
    Asterisk(Table table) {
        _table = table;
    }
    
    static Asterisk getInstance() {
        return THE_ONE;
    }
    
    @Override
    public String toString () {
        return _table == null ? "*" : _table._instanceName + ".*";
    }
}
