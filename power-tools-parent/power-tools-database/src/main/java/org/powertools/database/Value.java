package org.powertools.database;


final class Value implements Expression {
    private final String mValue;
    
    Value (String value) {
        mValue = value;
    }
    
    @Override
    public String toString () {
        return mValue;
    }
}
