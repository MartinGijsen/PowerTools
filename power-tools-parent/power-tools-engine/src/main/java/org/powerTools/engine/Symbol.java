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

package org.powertools.engine;


/**
 * A symbol is a named data item appearing in a test.
 * Its name is a simple identifier, like 'url' or 'cityOfBirth'.<BR/>
 * Some types of symbols contains a single item of data.
 * Others are structures, containing multiple fields, possibly nested.
 * Each field in a structure also has a simple identifier.
 * A structure field is referenced by prefixing it with its parent field names,
 * separated by periods, like 'client.address.zipCode'.<BR/>
 * A (field in a) structure can be cleared, removing all the fields it contains. 
 */
public interface Symbol {
	String PERIOD = "\\.";

	
	String getName ();

	void setValue (String value);
	void setValue (String name, String value);

	String getValue ();
	String getValue (String name);

	void clear (String[] names);
	void clear (String name);
}