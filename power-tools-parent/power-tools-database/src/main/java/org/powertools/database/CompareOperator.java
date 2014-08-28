package org.powertools.database;

enum CompareOperator {
    EQUAL ("="),
    UNEQUAL ("<>"),
    GREATER_THAN (">"),
    GREATER_THAN_OR_EQUAL (">="),
    LESS_THAN ("<"),
    LESS_THAN_OR_EQUAL ("<=");

    private final String mText;
    
    CompareOperator (String text) {
        mText = text;
    }

    @Override
    public String toString () {
        return mText;
    }
}
