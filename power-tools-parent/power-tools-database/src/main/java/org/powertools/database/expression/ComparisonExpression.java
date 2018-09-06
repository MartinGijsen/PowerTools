package org.powertools.database.expression;


public final class ComparisonExpression implements BooleanExpression {
    private final Expression _expression1;
    private final String     _operator;
    private final Expression _expression2;
    
    ComparisonExpression (Term term1, String operator, Term term2) {
        _expression1 = term1;
        _operator    = operator;
        _expression2 = term2;
    }
    
    public AndExpression and(ComparisonExpression expression) {
        return new AndExpression(this, expression);
    }
    
    public AndExpression and(AndExpression expression) {
        return new AndExpression(this, new BracketedExpression (expression));
    }
    
    public AndExpression and(OrExpression expression) {
        return new AndExpression(this, new BracketedExpression (expression));
    }
    
    public AndExpression and(NotExpression expression) {
        return new AndExpression(this, expression);
    }
    
    public OrExpression or(ComparisonExpression expression) {
        return new OrExpression(this, expression);
    }
    
    public OrExpression or(AndExpression expression) {
        return new OrExpression(this, new BracketedExpression (expression));
    }
    
    public OrExpression or(OrExpression expression) {
        return new OrExpression(this, new BracketedExpression (expression));
    }
    
    public OrExpression or(NotExpression expression) {
        return new OrExpression(this, expression);
    }
    
    @Override
    public String toString () {
        return String.format("%s %s %s",
                             _expression1.toString (),
                             _operator,
                             _expression2.toString ());
    }
}
