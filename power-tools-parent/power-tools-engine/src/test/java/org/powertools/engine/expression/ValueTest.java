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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.powertools.engine.ExecutionException;


public class ValueTest {
	private final ValueImpl mValue = new ValueImpl ("something");


	@Test
	public void testLessThan () {
		try {
			mValue.lessThan (mValue);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}
	
	@Test
	public void testLessOrEqual () {
		try {
			mValue.lessOrEqual (mValue);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}
	
	@Test
	public void testGreaterThan () {
		try {
			mValue.greaterThan (mValue);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}
	
	@Test
	public void testGreaterOrEqual () {
		try {
			mValue.greaterOrEqual (mValue);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}

	
	@Test
	public void testAdd () {
		try {
			mValue.add (mValue);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}
	
	@Test
	public void testSubtract () {
		try {
			mValue.subtract (mValue);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}
	
	@Test
	public void testMultiply () {
		try {
			mValue.multiply (mValue);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}
	
	@Test
	public void testDivide () {
		try {
			mValue.divide (mValue);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}
	
	@Test
	public void testNegate () {
		try {
			mValue.negate ();
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}

	@Test
	public void testConcatenate () {
		assertEquals ("true-1.23", new ValueImpl ("true").concatenate (new ValueImpl ("-1.23")).toString ());
	}


	@Test
	public void testToBooleanValue () {
		try {
			mValue.toBooleanValue ();
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}

	@Test
	public void testToRealValue () {
		try {
			mValue.toRealValue ();
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}

	@Test
	public void testToIntegerValue () {
		try {
			mValue.toIntegerValue ();
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}

	@Test
	public void toDateValue () {
		try {
			mValue.toDateValue ();
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
	}

	
	private final class ValueImpl extends Value {
		private String mText;
		
		ValueImpl (String text) {
			mText = text;
		}
		
		public String getType () {
			return "";
		}

		public Value equal (Value v) {
			return null;
		}

		public Value unequal (Value v) {
			return null;
		}

		public StringValue toStringValue () {
			return null;
		}
		
		public String toString () {
			return mText;
		}
	}	
}
