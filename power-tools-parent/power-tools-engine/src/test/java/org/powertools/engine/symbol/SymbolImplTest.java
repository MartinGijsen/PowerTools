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

package org.powertools.engine.symbol;

import org.junit.Test;
import static org.junit.Assert.*;
import org.powertools.engine.ExecutionException;


public class SymbolImplTest {
	private final String NAME = "someName";
	private final String VALUE1 = "some value";
	private final String VALUE2 = "another value";

	@Test
	public void testSymbolImpl () {
		try {
			SymbolImpl symbol = new SymbolImplImpl ("", VALUE1);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
		try {
			SymbolImpl symbol = new SymbolImplImpl ("invalid name", VALUE1);
			fail ("no exception");
		} catch (ExecutionException ee) {
			// ok
		}
		assertNotNull (new SymbolImplImpl (NAME, VALUE1));
	}

	@Test
	public void testGetName () {
		SymbolImpl symbol = new SymbolImplImpl (NAME, VALUE1);
		assertEquals ("someName", symbol.getName ());
	}

	@Test
	public void testSetValue () {
		SymbolImpl symbol = new SymbolImplImpl (NAME, VALUE1);
		assertEquals (VALUE1, symbol.getValue (NAME));
		symbol.setValue (NAME, VALUE2);
		assertEquals (VALUE2, symbol.getValue (NAME));
	}

	@Test
	public void testClear () {
		SymbolImpl symbol = new SymbolImplImpl (NAME, VALUE1);
		symbol.clear ("a.b".split ("\\."));
		assertEquals ("", symbol.getValue ());
	}

	public final class SymbolImplImpl extends SymbolImpl {
		private String mValue;
		
		public SymbolImplImpl (String name, String value) {
			super (name, null);
			mValue = value;
		}

		public void setValue (String name, String value) {
			mValue = value;
		}
		
		public String getValue (String name) {
			return mValue;
		}
		
		public void clear (String[] name) {
			mValue = "";
		}
	}
}
