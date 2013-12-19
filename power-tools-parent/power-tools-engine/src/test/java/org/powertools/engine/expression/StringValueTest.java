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
import static org.junit.Assert.*;


public class StringValueTest {
	private final StringValue mEmptyString	= new StringValue ("");
	private final StringValue mShortString	= new StringValue ("a");
	private final StringValue mLongString	= new StringValue ("abcdefghijklmnopqrstuvwxyz0123456789");

	
	@Test
	public void testGetType () {
		assertEquals ("string", mEmptyString.getType ());
	}

	@Test
	public void testEqual () {
		assertEquals ("true", mEmptyString.equal (new StringValue ("")).toString ());
		assertEquals ("true", mShortString.equal (new StringValue ("a")).toString ());
		assertEquals ("false", mShortString.equal (mLongString).toString ());
	}

	@Test
	public void testUnequal () {
		assertEquals ("false", mEmptyString.unequal (new StringValue ("")).toString ());
		assertEquals ("false", mShortString.unequal (new StringValue ("a")).toString ());
		assertEquals ("true", mShortString.unequal (mLongString).toString ());
	}

	
	@Test
	public void testToStringValue () {
		assertEquals ("a", mShortString.toStringValue ().toString ());
	}
}
