package org.powertools.database.expression;

import org.powertools.database.SelectQuery;


public abstract class Term implements Expression {
    public static Term rowNum () {
        return new RowNum ();
    }
    
    public final Condition equal (String value) {
        return equal (new StringValue (value));
    }

    public final Condition equal (int value) {
        return equal (new NumericValue (value));
    }

    public final Condition equal (Term term) {
        return new BinaryOperatorExpression (this, "=", term);
    }

    public final Condition unequal (String value) {
        return unequal (new StringValue (value));
    }

    public final Condition unequal (int value) {
        return unequal (new NumericValue (value));
    }

    public final Condition unequal (Term term) {
        return new BinaryOperatorExpression (this, "<>", term);
    }

    public final Condition greaterThan (String value) {
        return greaterThan (new StringValue (value));
    }

    public final Condition greaterThan (int value) {
        return greaterThan (new NumericValue (value));
    }

    public final Condition greaterThan (Term term) {
        return new BinaryOperatorExpression (this, ">", term);
    }

    public final Condition greaterThanOrEqual (String value) {
        return greaterThanOrEqual (new StringValue (value));
    }

    public final Condition greaterThanOrEqual (int value) {
        return greaterThanOrEqual (new NumericValue (value));
    }

    public final Condition greaterThanOrEqual (Term term) {
        return new BinaryOperatorExpression (this, ">=", term);
    }

    public final Condition lessThan (String value) {
        return lessThan (new StringValue (value));
    }

    public final Condition lessThan (int value) {
        return lessThan (new NumericValue (value));
    }

    public final Condition lessThan (Term term) {
        return new BinaryOperatorExpression (this, "<", term);
    }

    public final Condition lessThanOrEqual (String value) {
        return lessThanOrEqual (new StringValue (value));
    }
    
    public final Condition lessThanOrEqual (int value) {
        return lessThanOrEqual (new NumericValue (value));
    }
    
    public final Condition lessThanOrEqual (Term term) {
        return new BinaryOperatorExpression (this, "<=", term);
    }
    
    public final Condition like (String value) {
        return new BinaryOperatorExpression (this, "LIKE", new StringValue (value));
    }

    public final Condition notLike (String value) {
        return new BinaryOperatorExpression (this, "NOT LIKE", new StringValue (value));
    }

    public final Condition in (SelectQuery query) {
        return new InConditionWithSelect (this, query);
    }

    public final Condition in (String... values) {
        return new InConditionWithValues (this, values);
    }

    public final Condition notIn (SelectQuery query) {
        return new NotInConditionWithSelect (this, query);
    }

    public final Condition notIn (String... values) {
        return new NotInConditionWithValues (this, values);
    }

    public final Condition between (String value1, String value2) {
        return new BetweenCondition (this, value1, value2);
    }
    
    public final Condition isNull () {
        return new UnaryOperatorExpression ("IS NULL", false, this);
    }

    public final Condition isNotNull () {
        return new UnaryOperatorExpression ("IS NOT NULL", false, this);
    }

}
