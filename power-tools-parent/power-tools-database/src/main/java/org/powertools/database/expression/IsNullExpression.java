package org.powertools.database.expression;


final class IsNullExpression extends BooleanExpression {
    private final Term _term;
    
    IsNullExpression (Term term) {
        super ();
        _term = term;
    }
    
    @Override
    public String toString () {
        return String.format ("%s IS NULL", _term.toString ());
    }
}
