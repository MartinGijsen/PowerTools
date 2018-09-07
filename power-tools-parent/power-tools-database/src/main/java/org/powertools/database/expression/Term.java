package org.powertools.database.expression;

import org.powertools.database.SelectQuery;


public abstract class Term {
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
    
    public static Term toDate(String value) {
        return new ToDate(value, null);
    }
    
    public static Term toDate(String value, String format) {
        return new ToDate(value, format);
    }
    
    public static Term rowNum () {
        return new Term() {
            @Override
            public String toString () {
                return "ROWNUM";
            }
        };
    }
    
    public static Term count() {
        return new Term() {
            @Override
            public String toString () {
                return "COUNT(*)";
            }
        };
    }
    
    public final BooleanExpression equal (String value) {
        return equal (new StringValue (value));
    }

    public final BooleanExpression equal (int value) {
        return equal (new NumericValue (value));
    }

    public final BooleanExpression equal (Term term) {
        return ExpressionFactory.equal (this, term);
    }

    public final BooleanExpression unequal (String value) {
        return unequal (new StringValue (value));
    }

    public final BooleanExpression unequal (int value) {
        return unequal (new NumericValue (value));
    }

    public final BooleanExpression unequal (Term term) {
        return ExpressionFactory.unequal(this, term);
    }

    public final BooleanExpression greaterThan (String value) {
        return greaterThan (new StringValue (value));
    }

    public final BooleanExpression greaterThan (int value) {
        return greaterThan (new NumericValue (value));
    }

    public final BooleanExpression greaterThan (Term term) {
        return ExpressionFactory.greaterThan(this, term);
    }

    public final BooleanExpression greaterThanOrEqual (String value) {
        return greaterThanOrEqual (new StringValue (value));
    }

    public final BooleanExpression greaterThanOrEqual (int value) {
        return greaterThanOrEqual (new NumericValue (value));
    }

    public final BooleanExpression greaterThanOrEqual (Term term) {
        return ExpressionFactory.greaterThanOrEqual(this, term);
    }

    public final BooleanExpression lessThan (String value) {
        return lessThan (new StringValue (value));
    }

    public final BooleanExpression lessThan (int value) {
        return lessThan (new NumericValue (value));
    }

    public final BooleanExpression lessThan (Term term) {
        return ExpressionFactory.lessThan(this, term);
    }

    public final BooleanExpression lessThanOrEqual (String value) {
        return lessThanOrEqual (new StringValue (value));
    }
    
    public final BooleanExpression lessThanOrEqual (int value) {
        return lessThanOrEqual (new NumericValue (value));
    }
    
    public final BooleanExpression lessThanOrEqual (Term term) {
        return ExpressionFactory.lessThanOrEqual (this, term);
    }
    
    public final BooleanExpression like (String value) {
        return ExpressionFactory.like(this, new StringValue (value));
    }

    public final BooleanExpression notLike (String value) {
        return ExpressionFactory.notLike (this, new StringValue (value));
    }

    public final BooleanExpression in (SelectQuery query) {
        return ExpressionFactory.in(this, query);
    }

    public final BooleanExpression in (String... values) {
        return ExpressionFactory.in(this, values);
    }

    public final BooleanExpression notIn (SelectQuery query) {
        return ExpressionFactory.notIn(this, query);
    }

    public final BooleanExpression notIn (String... values) {
        return ExpressionFactory.notIn(this, values);
    }

    public final BooleanExpression between (String value1, String value2) {
        return ExpressionFactory.between(this, value1, value2);
    }
    
    public final BooleanExpression isNull () {
        return ExpressionFactory.isNull(this);
    }

    public final BooleanExpression isNotNull () {
        return ExpressionFactory.isNotNull (this);
    }
}
