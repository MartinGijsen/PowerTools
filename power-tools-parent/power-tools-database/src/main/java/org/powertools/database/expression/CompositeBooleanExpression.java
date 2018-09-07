package org.powertools.database.expression;


final class CompositeBooleanExpression extends BooleanExpression {
    final BooleanExpression _expression1;
    final String            _operator;
    final BooleanExpression _expression2;

    CompositeBooleanExpression (BooleanExpression expression1, String operator, BooleanExpression expression2) {
        _expression1 = expression1;
        _operator    = operator;
        _expression2 = expression2;
    }

    @Override
    final BooleanExpression beAndedWith (BooleanExpression expression) {
        return ExpressionFactory.and(expression, new BracketedExpression (this));
    }
    
    @Override
    final BooleanExpression beOredWith (BooleanExpression expression) {
        return ExpressionFactory.or(expression, new BracketedExpression (this));
    }
    
    @Override
    final BooleanExpression not () {
        return new NotExpression(new BracketedExpression (this));
    }
    
    @Override
    public String toString () {
        return _expression1.toString () + _operator + _expression2.toString ();
    }
}
