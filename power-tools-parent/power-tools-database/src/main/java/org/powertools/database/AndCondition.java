package org.powertools.database;


public class AndCondition implements BooleanExpression {
    private final BooleanExpression mCondition1;
    private final BooleanExpression mCondition2;
    
    AndCondition (BooleanExpression condition1, BooleanExpression condition2) {
        mCondition1 = condition1;
        mCondition2 = condition2;
    }
    
    @Override
    public String toString () {
        return mCondition1.toString () + " AND " + mCondition2.toString ();
    }
}
