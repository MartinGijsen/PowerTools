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

package org.powerTools.engine.symbol;

import java.util.regex.Pattern;

import org.powerTools.engine.ExecutionException;
import org.powerTools.engine.Symbol;


// A Symbol is a named item, containing either a single value or any number of values.
abstract class SymbolImpl implements Symbol {
	protected String mName;
	protected Scope mScope;

	private static final Pattern mNamePattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");


	protected SymbolImpl (String name, Scope scope) {
		if (name.isEmpty ()) {
			throw new ExecutionException ("empty name");
		} else if (!mNamePattern.matcher (name).matches ()) {
			throw new ExecutionException ("invalid name: " + name);
		} else {
			mName	= name;
			mScope	= scope;
		}
	}

	@Override
	public final String getName () {
		return mName;
	}
	
	@Override
	public final String getValue () {
		return getValue (getName ());
	}

	@Override
	public final void setValue (String value) {
		setValue (getName (), value);
	}
	
	@Override
	public final void clear (String name) {
		clear (name.split (PERIOD));
	}
}