package org.powertools.database.expression;

import org.powertools.database.SelectQuery;


final class ConditionWithSelect extends BooleanExpression {
    private final Term        _term;
    private final String      _operator;
    private final SelectQuery _query;
    
    ConditionWithSelect (Term term, String operator, SelectQuery query) {
        _term     = term;
        _operator = operator;
        _query    = query;
    }
    
    @Override
    public String toString () {
        return String.format ("%s %s\n(\n%s\n)", _term.toString (), _operator, _query.toString ());
    }
}