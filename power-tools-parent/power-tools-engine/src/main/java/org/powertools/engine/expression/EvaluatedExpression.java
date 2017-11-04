package org.powertools.engine.expression;

import java.util.HashMap;
import java.util.Map;


public final class EvaluatedExpression {
    final Value mValue;
    final Map<String, String> mSymbolValues;
    
    public EvaluatedExpression (String value) {
        this (new StringValue (value), new HashMap<String, String> ());
    }

    EvaluatedExpression (Value value, Map<String, String> symbolValues) {
        mValue        = value;
        mSymbolValues = symbolValues;
    }
    
    public String getValue () {
        return mValue.toString ();
    }
    
    public Map<String, String> getSymbolValues () {
        return mSymbolValues;
    }
}
