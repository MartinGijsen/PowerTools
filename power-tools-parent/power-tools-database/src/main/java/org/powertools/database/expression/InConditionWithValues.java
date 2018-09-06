package org.powertools.database.expression;


final class InConditionWithValues implements BooleanExpression {
    private final Term     _term;
    private final String[] _values;
    
    public InConditionWithValues (Term term, String[] values) {
        _term   = term;
        _values = values;
    }
    
    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder ();
        sb.append (_term.toString ()).append (" IN (");
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
