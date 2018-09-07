package org.powertools.database.expression;


public final class IsNullExpression extends BooleanExpression {
    private final Expression _expression;
    
    IsNullExpression (Expression expression) {
        super ();
        _expression = expression;
    }
    
    @Override
    public String toString () {
        return String.format ("%s IS NULL", _expression.toString ());
    }
}
