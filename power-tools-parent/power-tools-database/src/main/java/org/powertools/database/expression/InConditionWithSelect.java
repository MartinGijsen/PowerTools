package org.powertools.database.expression;

import org.powertools.database.SelectQuery;


public class InConditionWithSelect extends Condition {
    private final Term        _term;
    private final SelectQuery _query;
    
    public InConditionWithSelect (Term term, SelectQuery query) {
        _term  = term;
        _query = query;
    }
    
    @Override
    public String toString () {
        return String.format ("%s IN (%s)", _term.toString (), _query.toString ());
    }
}
