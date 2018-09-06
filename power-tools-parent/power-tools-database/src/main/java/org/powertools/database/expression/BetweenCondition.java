package org.powertools.database.expression;


public final class BetweenCondition implements BooleanExpression {
    private final Term   _term;
    private final String _value1;
    private final String _value2;
    
    BetweenCondition (Term term, String value1, String value2) {
        super ();
        _term   = term;
        _value1 = value1;
        _value2 = value2;
    }
    
    @Override
    public String toString () {
        return String.format("%s BETWEEN (%s, %s)", _term.toString (), _value1, _value2);
    }
}
