package org.powertools.database.expression;


final class BinaryOperatorExpression extends Condition {
    private final Expression _expression1;
    private final String     _operator;
    private final Expression _expression2;
    
    BinaryOperatorExpression (Expression expression1, String operator, Expression expression2) {
        super ();
        _expression1 = expression1;
        _operator    = operator;
        _expression2 = expression2;
    }
    
    @Override
    public String toString () {
        return String.format("%s %s %s",
                             _expression1.toString (),
                             _operator,
                             _expression2.toString ());
    }
}
