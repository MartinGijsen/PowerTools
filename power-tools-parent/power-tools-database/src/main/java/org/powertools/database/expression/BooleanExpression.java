package org.powertools.database.expression;


public abstract class BooleanExpression {
    public static BooleanExpression not(BooleanExpression expression) {
        return new NotExpression(expression);
    }
    
    public final BooleanExpression and (BooleanExpression expression) {
        return expression.beAndedWith (this);
    }

    public final BooleanExpression or (BooleanExpression expression) {
        return expression.beOredWith (this);
    }

    BooleanExpression not () {
        return new NotExpression(this);
    }
    
    BooleanExpression beAndedWith (BooleanExpression expression) {
        return ExpressionFactory.and(expression, this);
    }
    
    BooleanExpression beOredWith (BooleanExpression expression) {
        return ExpressionFactory.or(expression, this);
    }
}
