package org.powertools.database.expression;


final class PostfixExpression extends BooleanExpression {
    private final Term   _term;
    private final String _operator;
    
    PostfixExpression (Term term, String operator) {
        super ();
        _term     = term;
        _operator = operator;
    }
    
    @Override
    public String toString () {
        return _term.toString () + _operator;
    }
}