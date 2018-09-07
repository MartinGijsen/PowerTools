package org.powertools.database.expression;

public abstract class CompositeBooleanExpression extends BooleanExpression {
    @Override
    final AndExpression beAndedWith (BooleanExpression expression) {
        return new AndExpression (expression, new BracketedExpression (this));
    }
    
    @Override
    final OrExpression beOredWith (BooleanExpression expression) {
        return new OrExpression (expression, new BracketedExpression (this));
    }
}
