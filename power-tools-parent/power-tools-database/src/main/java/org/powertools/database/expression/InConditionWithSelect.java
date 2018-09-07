package org.powertools.database.expression;

import org.powertools.database.SelectQuery;


final class InConditionWithSelect extends BooleanExpression {
    private final Term        _term;
    private final SelectQuery _query;
    
    public InConditionWithSelect (Term term, SelectQuery query) {
        _term  = term;
        _query = query;
    }
    
    @Override
    public String toString () {
        return String.format ("%s IN\n(\n%s\n)", _term.toString (), _query.toString ());
    }
}
