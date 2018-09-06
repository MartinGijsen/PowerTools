package org.powertools.database.expression;

import org.powertools.database.SelectQuery;


public abstract class Term implements Expression {
    private static final class RowNum extends Term {
        @Override
        public String toString () {
            return "ROWNUM";
        }
    }
    
    private static final class ToDate extends Term {
        private final String _date;
        private final String _format;
        
        ToDate(String date, String format) {
            _date   = date;
            _format = format;
        }
        
        @Override
        public String toString () {
            if (_format == null) {
                return String.format("TO_DATE(%s)", _date);
            } else {
                return String.format("TO_DATE(%s,%s)", _date, _format);
            }
        }
    }
    
    private static final class Count extends Term {
        @Override
        public String toString () {
            return "COUNT(*)";
        }
    }
    
    public static Term rowNum () {
        return new RowNum ();
    }
    
    public static Term toDate(String value) {
        return new ToDate(value, null);
    }
    
    public static Term count() {
        return new Count ();
    }
    
    public static Term toDate(String value, String format) {
        return new ToDate(value, format);
    }
    
    public final ComparisonExpression equal (String value) {
        return equal (new StringValue (value));
    }

    public final ComparisonExpression equal (int value) {
        return equal (new NumericValue (value));
    }

    public final ComparisonExpression equal (Term term) {
        return new ComparisonExpression (this, "=", term);
    }

    public final ComparisonExpression unequal (String value) {
        return unequal (new StringValue (value));
    }

    public final ComparisonExpression unequal (int value) {
        return unequal (new NumericValue (value));
    }

    public final ComparisonExpression unequal (Term term) {
        return new ComparisonExpression (this, "<>", term);
    }

    public final ComparisonExpression greaterThan (String value) {
        return greaterThan (new StringValue (value));
    }

    public final ComparisonExpression greaterThan (int value) {
        return greaterThan (new NumericValue (value));
    }

    public final ComparisonExpression greaterThan (Term term) {
        return new ComparisonExpression (this, ">", term);
    }

    public final ComparisonExpression greaterThanOrEqual (String value) {
        return greaterThanOrEqual (new StringValue (value));
    }

    public final ComparisonExpression greaterThanOrEqual (int value) {
        return greaterThanOrEqual (new NumericValue (value));
    }

    public final ComparisonExpression greaterThanOrEqual (Term term) {
        return new ComparisonExpression (this, ">=", term);
    }

    public final ComparisonExpression lessThan (String value) {
        return lessThan (new StringValue (value));
    }

    public final ComparisonExpression lessThan (int value) {
        return lessThan (new NumericValue (value));
    }

    public final ComparisonExpression lessThan (Term term) {
        return new ComparisonExpression (this, "<", term);
    }

    public final ComparisonExpression lessThanOrEqual (String value) {
        return lessThanOrEqual (new StringValue (value));
    }
    
    public final ComparisonExpression lessThanOrEqual (int value) {
        return lessThanOrEqual (new NumericValue (value));
    }
    
    public final ComparisonExpression lessThanOrEqual (Term term) {
        return new ComparisonExpression (this, "<=", term);
    }
    
    public final ComparisonExpression like (String value) {
        return new ComparisonExpression (this, "LIKE", new StringValue (value));
    }

    public final ComparisonExpression notLike (String value) {
        return new ComparisonExpression (this, "NOT LIKE", new StringValue (value));
    }

    public final InConditionWithSelect in (SelectQuery query) {
        return new InConditionWithSelect (this, query);
    }

    public final InConditionWithValues in (String... values) {
        return new InConditionWithValues (this, values);
    }

    public final NotInConditionWithSelect notIn (SelectQuery query) {
        return new NotInConditionWithSelect (this, query);
    }

    public final NotInConditionWithValues notIn (String... values) {
        return new NotInConditionWithValues (this, values);
    }

    public final BetweenCondition between (String value1, String value2) {
        return new BetweenCondition (this, value1, value2);
    }
    
    public final IsNullExpression isNull () {
        return new IsNullExpression (this);
    }

    public final IsNotNullExpression isNotNull () {
        return new IsNotNullExpression (this);
    }
}
