/*	Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
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
import static org.junit.Assert.*;
import org.powertools.engine.ExecutionException;


public class RealValueTest {
	private final Value mPositiveValue	= new RealValue (123.0);
	private final Value mOneValue		= new RealValue (1.0);
	private final Value mZeroValue		= new RealValue (0.0);
	private final Value mMinusOneValue	= new RealValue (-1.0);
	private final Value mNegativeValue	= new RealValue (-123.0);

	
	@Test
	public void testRealValue () {
		new RealValue (123.0);
		new RealValue (0.0);
		new RealValue (-123.0);
		new RealValue ("123.0");
		new RealValue ("0.0");
		new RealValue ("-123.0");
		new RealValue ("123");
		try {
			new RealValue ("1,23");
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}

	@Test
	public void testGetType () {
		assertEquals ("real number", mPositiveValue.getType ());
	}

	@Test
	public void testLessThan () {
		assertEquals ("true", mNegativeValue.lessThan (mZeroValue).toString ());
		assertEquals ("true", mNegativeValue.lessThan (mPositiveValue).toString ());
		assertEquals ("true", mZeroValue.lessThan (mPositiveValue).toString ());
		assertEquals ("false", mPositiveValue.lessThan (mZeroValue).toString ());
		assertEquals ("false", mPositiveValue.lessThan (mNegativeValue).toString ());
		assertEquals ("false", mZeroValue.lessThan (mNegativeValue).toString ());
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
	}

	@Test
	public void testGreaterThan () {
		assertEquals ("false", mNegativeValue.greaterThan (mNegativeValue).toString ());
		assertEquals ("true", mZeroValue.greaterThan (mNegativeValue).toString ());
		assertEquals ("false", mZeroValue.greaterThan (mZeroValue).toString ());
		assertEquals ("true", mPositiveValue.greaterThan (mNegativeValue).toString ());
		assertEquals ("true", mPositiveValue.greaterThan (mZeroValue).toString ());
		assertEquals ("false", mPositiveValue.greaterThan (mPositiveValue).toString ());
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
	}

	@Test
	public void testAdd () {
		assertEquals ("0.0", mPositiveValue.add (mNegativeValue).toString ());
		assertEquals ("123.0", mPositiveValue.add (mZeroValue).toString ());
		assertEquals ("-123.0", mZeroValue.add (mNegativeValue).toString ());
	}

	@Test
	public void testSubtract () {
		assertEquals ("0.0", mPositiveValue.subtract (mPositiveValue).toString ());
		assertEquals ("123.0", mZeroValue.subtract (mNegativeValue).toString ());
		assertEquals ("-123.0", mZeroValue.subtract (mPositiveValue).toString ());
	}

	@Test
	public void testMultiply () {
		assertEquals ("0.0", mZeroValue.multiply (mPositiveValue).toString ());
		assertEquals ("123.0", mOneValue.multiply (mPositiveValue).toString ());
		assertEquals ("-123.0", mMinusOneValue.multiply (mPositiveValue).toString ());
		assertEquals ("123.0", mMinusOneValue.multiply (mNegativeValue).toString ());
	}

	@Test
	public void testDivide () {
		assertEquals ("0.25", divide (1, 4));
		assertEquals ("-0.25", divide (1, -4));
		assertEquals ("-0.25", divide (-1, 4));
		assertEquals ("0.25", divide (-1, -4));
	}

	private String divide (int value1, int value2) {
		return new RealValue (value1).divide (new RealValue (value2)).toString ();
	}
	
	@Test
	public void testNegate () {
		assertEquals ("-0.0", mZeroValue.negate ().toString ());
		assertEquals ("-123.0", mPositiveValue.negate ().toString ());
		assertEquals ("123.0", mNegativeValue.negate ().toString ());
	}

	@Test
	public void testToStringValue () {
		assertEquals ("-123.0", mNegativeValue.toStringValue ().toString ());
		assertEquals ("0.0", mZeroValue.toStringValue ().toString ());
		assertEquals ("123.0", mPositiveValue.toStringValue ().toString ());
	}

	@Test
	public void testToRealValue () {
		assertEquals ("-123.0", mNegativeValue.toRealValue ().toString ());
		assertEquals ("0.0", mZeroValue.toRealValue ().toString ());
		assertEquals ("123.0", mPositiveValue.toRealValue ().toString ());
	}
}
