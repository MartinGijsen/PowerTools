package org.powertools.database.expression;


public final class AndExpression extends CompositeBooleanExpression {
    private final Expression _expression1;
    private final Expression _expression2;

    AndExpression (Expression expression1, Expression expression2) {
        _expression1 = expression1;
        _expression2 = expression2;
    }
    
    @Override
    public String toString () {
        return String.format("%s\nAND %s",
                             _expression1.toString (),
                             _expression2.toString ());
    }
}
