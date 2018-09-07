package org.powertools.database.expression;

import org.powertools.database.util.MyList;


final class ConditionWithValues extends BooleanExpression {
    private final Term           _term;
    private final String         _operator;
    private final MyList<String> _values;
    
    ConditionWithValues (Term term, String operator, String[] values) {
        _term     = term;
        _operator = operator;
        _values   = new MyList<>();
        for (String value : values) {
            _values.add (value);
        }
    }
    
    @Override
    public String toString () {
        return String.format("%s %s (%s)", _term.toString (), _operator, _values.toString ());
    }
}