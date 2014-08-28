package org.powertools.database;


public class CompareExpression implements BooleanExpression {
    private final Expression mExpression1;
    private final CompareOperator mOperator;
    private final Expression mExpression2;
    
    CompareExpression (Expression expression1, CompareOperator operator, Expression expression2) {
        super ();
        mExpression1 = expression1;
        mOperator    = operator;
        mExpression2 = expression2;
    }
    
    @Override
    public String toString () {
        return mExpression1.toString () + " " + mOperator.toString () + " " + mExpression2.toString ();
    }
}
