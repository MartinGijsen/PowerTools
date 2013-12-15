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

package org.powertools.web;

import org.powertools.web.WebLibrary.IItemType;
import org.powertools.web.WebLibrary.IKeyType;


public class Item {
	Item (String logicalName, Item parent, IItemType type, IKeyType keyType, String value) {
		mLogicalName	= logicalName;
		mParent			= parent;
		mType			= type;
		mKeyType		= keyType;
		mValue			= value;
	}

	
	boolean resetParameter (int paramNr) {
		return false;
	}
	
	boolean resetParameters () {
		return false;
	}
	
	public boolean setParameterValue (int position, String value) {
		return false;
	}

	public String getValue () {
		return mValue;
		//return mParent != null ? null : mValue;
	}
	

	final String mLogicalName;
	final Item mParent;
	final IItemType mType;
	final IKeyType mKeyType;
	
	
	// protected members
	protected final String mValue;
}