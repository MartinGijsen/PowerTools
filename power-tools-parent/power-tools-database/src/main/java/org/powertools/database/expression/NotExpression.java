package org.powertools.database.expression;


final class NotExpression extends BooleanExpression {
    private final BooleanExpression _expression;
    
    NotExpression (BooleanExpression expression) {
        _expression = expression;
    }
    
    @Override
    public String toString () {
        return String.format("NOT %s", _expression.toString ());
    }
}
