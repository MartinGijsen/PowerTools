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

import org.powertools.engine.ExecutionException;


final class LeafItem extends Item {
	String mValue;


	LeafItem (String name, Item parent) {
		super (name, parent);
	}

	LeafItem (String name, Item parent, String value) {
		super (name, parent);
		mValue = value;
	}

	
	@Override
	Item getChild (String name) {
		throw new ExecutionException ("leaf item has no children");
	}

	@Override
	void clear () {
		throw new ExecutionException ("symbol is not a structure");
	}
	
	@Override
	LeafItem createLeaf (String[] names, int position) {
		if (position != names.length) {
			throw new ExecutionException ("leaf item cannot have children");
		}
		return this;
	}
	
	@Override
	String getValue () {
		return mValue;
	}

	void setValue (String value) {
		mValue = value;
	}
}