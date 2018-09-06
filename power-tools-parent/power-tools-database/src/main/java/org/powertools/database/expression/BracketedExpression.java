package org.powertools.database.expression;


public final class BracketedExpression implements Expression {
    private final Expression _expression;
    
    BracketedExpression (Expression expression) {
        _expression = expression;
    }
    
    public BracketedExpression and(Expression expression) {
        return new BracketedExpression(expression);
    }
    
    public BracketedExpression or(Expression expression) {
        return new BracketedExpression(expression);
    }
    
    @Override
    public String toString () {
        return String.format("(%s)", _expression.toString ());
    }
}
