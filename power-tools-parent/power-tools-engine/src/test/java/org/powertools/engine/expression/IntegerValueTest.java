/*	Copyright 2013 by Martin Gijsen (www.DeAnalist.nl)
 *
 *	This file is part of the PowerTools engine.
 *
 *	The PowerTools engine is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the License,
 *	or (at your option) any later version.
 *
 *	The PowerTools engine is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU Affero General Public License for more details.
 *
 *	You should have received a copy of the GNU Affero General Public License
 *	along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine.expression;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.powertools.engine.ExecutionException;


public class IntegerValueTest {
    private final Value mPositiveValue      = new IntegerValue (123);
    private final Value mOneValue           = new IntegerValue (1);
    private final Value mZeroValue          = new IntegerValue (0);
    private final Value mMinusOneValue      = new IntegerValue (-1);
    private final Value mNegativeValue      = new IntegerValue (-123);

    private final Value mRealOneValue       = new RealValue (1.0);
    private final Value mRealHalfValue      = new RealValue (0.5);
    private final Value mRealMinusOneValue  = new RealValue (-1.0);


    @Test
    public void testIntegerValue () {
        assertEquals ("123", new IntegerValue (123).toString ());
        assertEquals ("0", new IntegerValue (0).toString ());
        assertEquals ("-123", new IntegerValue (-123).toString ());
        assertEquals ("123", new IntegerValue ("123").toString ());
        assertEquals ("0", new IntegerValue ("0").toString ());
        assertEquals ("-123", new IntegerValue ("-123").toString ());
        assertEquals ("0123", new IntegerValue ("0123").toString ());
    }

    @Test
    public void testGetType () {
        assertEquals ("integer number", mPositiveValue.getType ());
    }


    @Test
    public void testEqual () {
        assertEquals ("true", mPositiveValue.equal (new IntegerValue (123)).toString ());
        assertEquals ("true", mZeroValue.equal (new IntegerValue (0)).toString ());
        assertEquals ("true", mNegativeValue.equal (new IntegerValue (-123)).toString ());
        assertEquals ("false", mPositiveValue.equal (new IntegerValue (0)).toString ());
        assertEquals ("false", mZeroValue.equal (new IntegerValue (1)).toString ());
        assertEquals ("false", mNegativeValue.equal (new IntegerValue (0)).toString ());
    }

    @Test
    public void testUnequal () {
        assertEquals ("false", mPositiveValue.unequal (new IntegerValue (123)).toString ());
        assertEquals ("false", mZeroValue.unequal (new IntegerValue (0)).toString ());
        assertEquals ("false", mNegativeValue.unequal (new IntegerValue (-123)).toString ());
        assertEquals ("true", mPositiveValue.unequal (new IntegerValue (0)).toString ());
        assertEquals ("true", mZeroValue.unequal (new IntegerValue (1)).toString ());
        assertEquals ("true", mNegativeValue.unequal (new IntegerValue (0)).toString ());
    }

    @Test
    public void testLessThan () {
        assertEquals ("true", mNegativeValue.lessThan (mZeroValue).toString ());
        assertEquals ("true", mNegativeValue.lessThan (mPositiveValue).toString ());
        assertEquals ("true", mZeroValue.lessThan (mPositiveValue).toString ());
        assertEquals ("false", mPositiveValue.lessThan (mZeroValue).toString ());
        assertEquals ("false", mPositiveValue.lessThan (mNegativeValue).toString ());
        assertEquals ("false", mZeroValue.lessThan (mNegativeValue).toString ());

        assertEquals ("true", mZeroValue.lessThan (mRealOneValue).toString ());
        assertEquals ("false", mZeroValue.lessThan (mRealMinusOneValue).toString ());
    }

    @Test
    public void testLessOrEqual () {
        assertEquals ("true", mNegativeValue.lessOrEqual (mNegativeValue).toString ());
        assertEquals ("true", mNegativeValue.lessOrEqual (mZeroValue).toString ());
        assertEquals ("false", mZeroValue.lessOrEqual (mNegativeValue).toString ());
        assertEquals ("true", mZeroValue.lessOrEqual (mZeroValue).toString ());
        assertEquals ("true", mZeroValue.lessOrEqual (mPositiveValue).toString ());
        assertEquals ("false", mPositiveValue.lessOrEqual (mZeroValue).toString ());
        assertEquals ("true", mPositiveValue.lessOrEqual (mPositiveValue).toString ());

        assertEquals ("true", mZeroValue.lessOrEqual (mRealOneValue).toString ());
        assertEquals ("false", mZeroValue.lessOrEqual (mRealMinusOneValue).toString ());
    }

    @Test
    public void testGreaterThan () {
        assertEquals ("false", mNegativeValue.greaterThan (mNegativeValue).toString ());
        assertEquals ("true", mZeroValue.greaterThan (mNegativeValue).toString ());
        assertEquals ("false", mZeroValue.greaterThan (mZeroValue).toString ());
        assertEquals ("true", mPositiveValue.greaterThan (mNegativeValue).toString ());
        assertEquals ("true", mPositiveValue.greaterThan (mZeroValue).toString ());
        assertEquals ("false", mPositiveValue.greaterThan (mPositiveValue).toString ());

        assertEquals ("true", mZeroValue.greaterThan (mRealMinusOneValue).toString ());
        assertEquals ("false", mZeroValue.greaterThan (mRealOneValue).toString ());
    }

    @Test
    public void testGreaterOrEqual () {
        assertEquals ("true", mNegativeValue.greaterOrEqual (mNegativeValue).toString ());
        assertEquals ("false", mNegativeValue.greaterOrEqual (mZeroValue).toString ());
        assertEquals ("true", mZeroValue.greaterOrEqual (mNegativeValue).toString ());
        assertEquals ("true", mZeroValue.greaterOrEqual (mZeroValue).toString ());
        assertEquals ("false", mZeroValue.greaterOrEqual (mPositiveValue).toString ());
        assertEquals ("true", mPositiveValue.greaterOrEqual (mZeroValue).toString ());
        assertEquals ("true", mPositiveValue.greaterOrEqual (mPositiveValue).toString ());

        assertEquals ("true", mZeroValue.greaterOrEqual (mRealMinusOneValue).toString ());
        assertEquals ("false", mZeroValue.greaterOrEqual (mRealOneValue).toString ());
    }

    @Test
    public void testAdd () {
        assertEquals ("0", mPositiveValue.add (mNegativeValue).toString ());
        assertEquals ("123", mPositiveValue.add (mZeroValue).toString ());
        assertEquals ("-123", mZeroValue.add (mNegativeValue).toString ());

        assertEquals ("0,5", mZeroValue.add (mRealHalfValue).toString ());

        try {
            mZeroValue.add (new BooleanValue (true));
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testSubtract () {
        assertEquals ("0", mPositiveValue.subtract (mPositiveValue).toString ());
        assertEquals ("123", mZeroValue.subtract (mNegativeValue).toString ());
        assertEquals ("-123", mZeroValue.subtract (mPositiveValue).toString ());

        assertEquals ("-0,5", mZeroValue.subtract (mRealHalfValue).toString ());

        try {
            mZeroValue.subtract (new BooleanValue (true));
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testMultiply () {
        assertEquals ("0", mZeroValue.multiply (mPositiveValue).toString ());
        assertEquals ("123", mOneValue.multiply (mPositiveValue).toString ());
        assertEquals ("-123", mMinusOneValue.multiply (mPositiveValue).toString ());
        assertEquals ("123", mMinusOneValue.multiply (mNegativeValue).toString ());

        assertEquals ("0,5", mOneValue.multiply (mRealHalfValue).toString ());

        try {
            mZeroValue.multiply (new BooleanValue (true));
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testDivide () {
        assertEquals ("0,25", divide (1, 4));
        assertEquals ("-0,25", divide (1, -4));
        assertEquals ("-0,25", divide (-1, 4));
        assertEquals ("0,25", divide (-1, -4));

        assertEquals ("2", divide (6, 3));
        assertEquals ("-2", divide (6, -3));
        assertEquals ("-2", divide (-6, 3));
        assertEquals ("2", divide (-6, -3));

        assertEquals ("2", mOneValue.divide (mRealHalfValue).toString ());

        try {
            mZeroValue.divide (new BooleanValue (true));
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    private String divide (int value1, int value2) {
        return new IntegerValue (value1).divide (new IntegerValue (value2)).toString ();
    }

    @Test
    public void testNegate () {
        assertEquals ("0", mZeroValue.negate ().toString ());
        assertEquals ("-123", mPositiveValue.negate ().toString ());
        assertEquals ("123", mNegativeValue.negate ().toString ());
    }

    @Test
    public void testToStringValue () {
        assertEquals ("-123", mNegativeValue.toStringValue ().toString ());
        assertEquals ("0", mZeroValue.toStringValue ().toString ());
        assertEquals ("123", mPositiveValue.toStringValue ().toString ());
    }

    @Test
    public void testToRealValue () {
        assertEquals ("-123", mNegativeValue.toRealValue ().toString ());
        assertEquals ("0", mZeroValue.toRealValue ().toString ());
        assertEquals ("123", mPositiveValue.toRealValue ().toString ());
    }

    @Test
    public void testToIntegerValue () {
        assertEquals ("-123", mNegativeValue.toIntegerValue ().toString ());
        assertEquals ("0", mZeroValue.toIntegerValue ().toString ());
        assertEquals ("123", mPositiveValue.toIntegerValue ().toString ());
    }
}
