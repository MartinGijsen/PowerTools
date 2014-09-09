/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools engine.
 *
 * The PowerTools engine is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.database;


public class CompareExpression implements BooleanExpression {
    private enum Operator {
        EQUAL ("="),
        UNEQUAL ("<>"),
        GREATER_THAN (">"),
        GREATER_THAN_OR_EQUAL (">="),
        LESS_THAN ("<"),
        LESS_THAN_OR_EQUAL ("<=");

        private final String mText;

        Operator (String text) {
            mText = text;
        }

        @Override
        public String toString () {
            return mText;
        }
    }


    private final Expression mExpression1;
    private final Operator mOperator;
    private final Expression mExpression2;
    
    private CompareExpression (Expression expression1, Operator operator, Expression expression2) {
        super ();
        mExpression1 = expression1;
        mOperator    = operator;
        mExpression2 = expression2;
    }
    
    static CompareExpression equal (Expression expression1, Expression expression2) {
        return new CompareExpression (expression1, Operator.EQUAL, expression2);
    }
    
    static CompareExpression unequal (Expression expression1, Expression expression2) {
        return new CompareExpression (expression1, Operator.UNEQUAL, expression2);
    }
    
    static CompareExpression greaterThan (Expression expression1, Expression expression2) {
        return new CompareExpression (expression1, Operator.GREATER_THAN, expression2);
    }
    
    static CompareExpression greaterThanOrEqual (Expression expression1, Expression expression2) {
        return new CompareExpression (expression1, Operator.GREATER_THAN_OR_EQUAL, expression2);
    }
    
    static CompareExpression lessThan (Expression expression1, Expression expression2) {
        return new CompareExpression (expression1, Operator.LESS_THAN, expression2);
    }
    
    static CompareExpression lessThanOrEqual (Expression expression1, Expression expression2) {
        return new CompareExpression (expression1, Operator.LESS_THAN_OR_EQUAL, expression2);
    }
    
    @Override
    public String toString () {
        return mExpression1.toString () + " " + mOperator.toString () + " " + mExpression2.toString ();
    }
}
