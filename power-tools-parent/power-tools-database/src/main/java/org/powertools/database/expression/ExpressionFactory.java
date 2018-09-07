package org.powertools.database.expression;

import org.powertools.database.SelectQuery;


final class ExpressionFactory {
    static BooleanExpression or(BooleanExpression expression1, BooleanExpression expression2) {
        return new CompositeBooleanExpression(expression1, "\nOR ", expression2);
    }

    static BooleanExpression and(BooleanExpression expression1, BooleanExpression expression2) {
        return new CompositeBooleanExpression(expression1, "\nAND ", expression2);
    }

    static BooleanExpression equal(Term term1, Term term2) {
        return new BinaryOperatorExpression (term1, " = ", term2);
    }

    static BooleanExpression unequal(Term term1, Term term2) {
        return new BinaryOperatorExpression (term1, " <> ", term2);
    }

    static BooleanExpression greaterThan(Term term1, Term term2) {
        return new BinaryOperatorExpression (term1, " > ", term2);
    }

    static BooleanExpression greaterThanOrEqual(Term term1, Term term2) {
        return new BinaryOperatorExpression (term1, " >= ", term2);
    }

    static BooleanExpression lessThan(Term term1, Term term2) {
        return new BinaryOperatorExpression (term1, " < ", term2);
    }

    static BooleanExpression lessThanOrEqual(Term term1, Term term2) {
        return new BinaryOperatorExpression (term1, " <= ", term2);
    }

    static BooleanExpression like(Term term1, Term term2) {
        return new BinaryOperatorExpression (term1, " LIKE ", term2);
    }

    static BooleanExpression notLike(Term term1, Term term2) {
        return new BinaryOperatorExpression (term1, " NOT LIKE ", term2);
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

    static BooleanExpression isNull(Term term) {
        return new PostfixExpression(term, " IS NULL");
    }

    static BooleanExpression isNotNull(Term term) {
        return new PostfixExpression(term, " IS NOT NULL");
    }

    static BooleanExpression between(Term term, String value1, String value2) {
        return new BetweenCondition (term, value1, value2);
    }
}
