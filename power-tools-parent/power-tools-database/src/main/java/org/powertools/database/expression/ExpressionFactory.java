package org.powertools.database.expression;

import org.powertools.database.SelectQuery;


final class ExpressionFactory {
    static BooleanExpression or(BooleanExpression expression1, BooleanExpression expression2) {
        return new CompositeBooleanExpression(expression1, "\nOR ", expression2);
    }

    static BooleanExpression and(BooleanExpression expression1, BooleanExpression expression2) {
        return new CompositeBooleanExpression(expression1, "\nAND ", expression2);
    }

    static BooleanExpression in(Term term, SelectQuery query) {
        return new ConditionWithSelect (term, "IN", query);
    }

    static BooleanExpression in(Term term, String[] values) {
        return new ConditionWithValues (term, "IN", values);
    }

    static BooleanExpression notIn(Term term, SelectQuery query) {
        return new ConditionWithSelect (term, "NOT IN", query);
    }

    static BooleanExpression notIn(Term term, String[] values) {
        return new ConditionWithValues (term, "NOT IN", values);
    }
}
