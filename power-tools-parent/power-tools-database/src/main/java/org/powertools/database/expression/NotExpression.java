package org.powertools.database.expression;


public final class NotExpression implements BooleanExpression {
    private final Expression _expression;
    
    NotExpression (ComparisonExpression expression) {
        _expression = expression;
    }
    
    NotExpression (AndExpression expression) {
        _expression = new BracketedExpression (expression);
    }
    
    NotExpression (OrExpression expression) {
        _expression = new BracketedExpression (expression);
    }
    
    NotExpression (NotExpression expression) {
        _expression = expression;
    }
    
    public static NotExpression not(ComparisonExpression expression) {
        return new NotExpression(expression);
    }
    
    public static NotExpression not(AndExpression expression) {
        return new NotExpression(expression);
    }
    
    public static NotExpression not(OrExpression expression) {
        return new NotExpression(expression);
    }
    
    public static NotExpression not(NotExpression expression) {
        return new NotExpression(expression);
    }
    
    @Override
    public String toString () {
        return String.format("NOT %s", _expression.toString ());
    }
}
