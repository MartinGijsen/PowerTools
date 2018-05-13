package org.powertools.database.expression;


public class NotInConditionWithValues extends Condition {
    private final Term     _term;
    private final String[] _values;
    
    public NotInConditionWithValues (Term term, String[] values) {
        _term   = term;
        _values = values;
    }
    
    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder ();
        sb.append (_term.toString ()).append (" NOT IN (");
        boolean isFirst = true;
        for (String value : _values) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append (", ");
            }
            sb.append (value);
        }
        sb.append (")");
        return sb.toString ();
    }
}
