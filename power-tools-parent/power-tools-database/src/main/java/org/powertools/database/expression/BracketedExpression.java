package org.powertools.database.expression;


final class BracketedExpression extends BooleanExpression {
    private final BooleanExpression _expression;
    
    BracketedExpression (BooleanExpression expression) {
        _expression = expression;
    }
    
    @Override
    public String toString () {
        return String.format("(%s)", _expression.toString ());
    }
}
