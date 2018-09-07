package org.powertools.database.expression;


public final class BracketedExpression implements Expression {
    private final CompositeBooleanExpression _expression;
    
    BracketedExpression (CompositeBooleanExpression expression) {
        _expression = expression;
    }
    
    @Override
    public String toString () {
        return String.format("(%s)", _expression.toString ());
    }
}
