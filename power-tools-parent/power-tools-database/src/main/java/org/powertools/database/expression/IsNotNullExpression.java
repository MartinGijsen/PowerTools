package org.powertools.database.expression;


public final class IsNotNullExpression extends BooleanExpression {
    private final Expression _expression;
    
    IsNotNullExpression (Expression expression) {
        super ();
        _expression = expression;
    }
    
    @Override
    public String toString () {
        return String.format ("%s IS NOT NULL", _expression.toString ());
    }
}
