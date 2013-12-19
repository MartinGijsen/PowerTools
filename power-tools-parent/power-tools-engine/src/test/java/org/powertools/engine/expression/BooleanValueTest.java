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


public class BooleanValueTest {
	private final Value mTrueValue  = new BooleanValue (true);
	private final Value mFalseValue = new BooleanValue ("false");
	
	@Test
	public void testBooleanValue () {
		new BooleanValue (true);
		new BooleanValue (false);
		new BooleanValue ("true");
		new BooleanValue ("false");
		try {
			new BooleanValue ("something else");
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}
	
	@Test
	public void testGetType () {
		assertEquals ("boolean", mTrueValue.getType ());
		assertEquals ("boolean", mFalseValue.getType ());
	}

	@Test
	public void testOr () {
		assertEquals ("true", mTrueValue.or (mTrueValue).toString ());
		assertEquals ("true", mTrueValue.or (mFalseValue).toString ());
		assertEquals ("true", mFalseValue.or (mTrueValue).toString ());
		assertEquals ("false", mFalseValue.or (mFalseValue).toString ());
	}

	@Test
	public void testAnd () {
		assertEquals ("true", mTrueValue.and (mTrueValue).toString ());
		assertEquals ("false", mTrueValue.and (mFalseValue).toString ());
		assertEquals ("false", mFalseValue.and (mTrueValue).toString ());
		assertEquals ("false", mFalseValue.and (mFalseValue).toString ());
	}

	@Test
	public void testNot () {
		assertEquals ("false", mTrueValue.not ().toString ());
		assertEquals ("true", mFalseValue.not ().toString ());
	}

	@Test
	public void testEqual () {
		assertEquals ("true", mTrueValue.equal (mTrueValue).toString ());
		assertEquals ("false", mTrueValue.equal (mFalseValue).toString ());
		assertEquals ("false", mFalseValue.equal (mTrueValue).toString ());
		assertEquals ("true", mFalseValue.equal (mFalseValue).toString ());
	}

	@Test
	public void testUnequal () {
		assertEquals ("false", mTrueValue.unequal (mTrueValue).toString ());
		assertEquals ("true", mTrueValue.unequal (mFalseValue).toString ());
		assertEquals ("true", mFalseValue.unequal (mTrueValue).toString ());
		assertEquals ("false", mFalseValue.unequal (mFalseValue).toString ());
	}

	
	@Test
	public void testToStringValue () {
		assertEquals ("true", mTrueValue.toStringValue ().toString ());
		assertEquals ("false", mFalseValue.toStringValue ().toString ());
	}

	@Test
	public void testToBooleanValue () {
		assertEquals ("true", mTrueValue.toBooleanValue ().toString ());
		assertEquals ("false", mFalseValue.toBooleanValue ().toString ());
	}
}
