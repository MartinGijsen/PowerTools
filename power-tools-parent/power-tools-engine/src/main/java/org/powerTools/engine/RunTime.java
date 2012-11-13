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

package org.powerTools.engine;

import org.powerTools.engine.Roles;
import org.powerTools.engine.symbol.Scope;


/**
 * The runtime provides all engine functionality that an instruction may need.
 * It supports reporting errors and other execution information,
 * creating symbols in the current scope and getting and setting symbols.
 * <BR/>
 * It also allows an instruction (set) to create or get a shared object,
 * so data or logic can be shared between instruction sets.
 */
public interface RunTime {
	Context getContext ();
	
	boolean addSharedObject (String name, Object object);
	Object getSharedObject (String name);

	void reportValueError (String expression, String actualValue, String expectedValue);
	void reportError      (String message);
	void reportStackTrace (Exception e);
	void reportWarning    (String message);
	void reportValue      (String expression, String value);
	void reportInfo       (String message);

	Scope getGlobalScope ();
	Scope getCurrentScope ();

	Symbol getSymbol (String name);
	void setValue (String name, String value);
	void copyStructure (String target, String source);
	void clearStructure (String name);
	
	Roles getRoles ();
}