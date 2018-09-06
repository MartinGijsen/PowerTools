package org.powertools.database.expression;

import org.powertools.database.SelectQuery;


final class NotInConditionWithSelect implements BooleanExpression {
    private final Term        _term;
    private final SelectQuery _query;
    
    public NotInConditionWithSelect (Term term, SelectQuery query) {
        _term  = term;
        _query = query;
    }
    
    @Override
    public String toString () {
        return String.format ("%s NOT IN\n(\n%s\n)", _term.toString (), _query.toString ());
    }
}
