package org.powertools.database.expression;


class BinaryOperatorExpression extends BooleanExpression {
    private final Term   _term1;
    private final String _operator;
    private final Term   _term2;
    
    BinaryOperatorExpression (Term term1, String operator, Term term2) {
        super ();
        _term1    = term1;
        _operator = operator;
        _term2    = term2;
    }
    
    @Override
    public String toString () {
        return _term1.toString () + _operator + _term2.toString ();
    }
}
