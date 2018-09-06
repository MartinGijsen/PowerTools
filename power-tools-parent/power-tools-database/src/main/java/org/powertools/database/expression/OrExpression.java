package org.powertools.database.expression;


public final class OrExpression implements BooleanExpression {
    private final Expression _expression1;
    private final Expression _expression2;
    
    OrExpression (Expression expression1, Expression expression2) {
        _expression1 = expression1;
        _expression2 = expression2;
    }
    
    public AndExpression and(ComparisonExpression expression) {
        return new AndExpression(this, expression);
    }
    
    public AndExpression and(AndExpression expression) {
        return new AndExpression(this, expression);
    }
    
    public AndExpression and(OrExpression expression) {
        return new AndExpression(this, expression);
    }
    
    public AndExpression and(NotExpression expression) {
        return new AndExpression(this, expression);
    }
    
    public AndExpression and(BetweenCondition expression) {
        return new AndExpression(this, expression);
    }

    public AndExpression and(IsNullExpression expression) {
        return new AndExpression(this, expression);
    }

    public AndExpression and(IsNotNullExpression expression) {
        return new AndExpression(this, expression);
    }

    public AndExpression and(InConditionWithSelect expression) {
        return new AndExpression(this, expression);
    }

    public AndExpression and(InConditionWithValues expression) {
        return new AndExpression(this, expression);
    }

    public AndExpression and(NotInConditionWithSelect expression) {
        return new AndExpression(this, expression);
    }

    public AndExpression and(NotInConditionWithValues expression) {
        return new AndExpression(this, expression);
    }

    @Override
    public String toString () {
        return String.format("%s OR %s",
                             _expression1.toString (),
                             _expression2.toString ());
    }
}
