package org.powertools.database.expression;


public final class ComparisonExpression extends BooleanExpression {
    private final Expression _expression1;
    private final String     _operator;
    private final Expression _expression2;
    
    ComparisonExpression (Term term1, String operator, Term term2) {
        _expression1 = term1;
        _operator    = operator;
        _expression2 = term2;
    }
    
    @Override
    public String toString () {
        return String.format("%s %s %s",
                             _expression1.toString (),
                             _operator,
                             _expression2.toString ());
    }
}
