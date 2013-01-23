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

import java.util.HashMap;
import java.util.Map;

import org.powerTools.engine.ExecutionException;


final class SequenceItem extends Item {
	final Map<String, Item> mChildren;

	private Boolean mNumbersOnly;
	
	
	SequenceItem (String name, Item parent) {
		super (name, parent);
		mChildren = new HashMap<String, Item> ();
	}

	
	void add (String name, Item newItem) {
		final Item oldItem = mChildren.get (name);
		if (oldItem != null) {
			throw new ExecutionException ("structure field already exists");
		} else {
			final Boolean isNumber = Character.isDigit (name.charAt (0));
			if (mNumbersOnly == null) {
				mChildren.put (name, newItem);
				mNumbersOnly = isNumber;
			} else if (mNumbersOnly.equals (isNumber)){
				mChildren.put (name, newItem);
			} else {
				throw new ExecutionException ("mixing item names and indexes");
			}
		}
	}

	
	@Override
	Item getChild (String name) {
		final Item child = mChildren.get (name);
		if (child != null) {
			return child;
		} else {
			throw new ExecutionException ("structure field does not exist");
		}
	}

	@Override
	String getValue () {
		throw new ExecutionException ("structure field has no value");
	}
	
	@Override
	void clear () {
		mChildren.clear ();
	}

	@Override
	LeafItem createLeaf (String[] names, int positionToCreate) {
		String nextName = "";
		try {
			nextName = names[positionToCreate];
			return getChild (nextName).createLeaf (names, positionToCreate + 1);
		} catch (IndexOutOfBoundsException ioobe) {
			throw new ExecutionException ("cannot assign to a structure, only to a structure field");
		} catch (ExecutionException ee) {
			if (positionToCreate + 1 == names.length) {
				LeafItem leaf = new LeafItem (names[positionToCreate], this);
				add (nextName, leaf);
				return leaf;
			} else {
				SequenceItem child = new SequenceItem (names[positionToCreate], this);
				add (nextName, child);
				return child.createLeaf (names, positionToCreate + 1);
			}
		}
	}
}