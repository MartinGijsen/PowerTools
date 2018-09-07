package org.powertools.database.expression;


public abstract class BooleanExpression implements Expression {
    public final AndExpression and (BooleanExpression expression) {
        return expression.beAndedWith (this);
    }

    public final OrExpression or (BooleanExpression expression) {
        return expression.beOredWith (this);
    }
    
    AndExpression beAndedWith (BooleanExpression expression) {
        return new AndExpression (expression, this);
    }
    
    OrExpression beOredWith (BooleanExpression expression) {
        return new OrExpression (expression, this);
    }
}
