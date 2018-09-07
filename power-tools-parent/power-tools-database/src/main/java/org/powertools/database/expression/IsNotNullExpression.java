package org.powertools.database.expression;


final class IsNotNullExpression extends BooleanExpression {
    private final Term _term;
    
    IsNotNullExpression (Term term) {
        super ();
        _term = term;
    }
    
    @Override
    public String toString () {
        return String.format ("%s IS NOT NULL", _term.toString ());
    }
}
