package org.powertools.database.expression;


public class UnaryOperatorExpression extends SimpleCondition {
    private final String     _operator;
    private final boolean    _isPrefix;
    private final Expression _expression;
    
    UnaryOperatorExpression (String operator, boolean isPrefix, Expression expression) {
        super ();
        _operator   = operator;
        _isPrefix   = isPrefix;
        _expression = expression;
    }
    
    @Override
    public String toString () {
        String first  = _isPrefix ? _operator : _expression.toString ();
        String second = _isPrefix ? _expression.toString () : _operator;
        return String.format ("%s %s", first, second);
    }
}
