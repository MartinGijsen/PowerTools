/* Copyright 2016 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools engine.
 *
 * The PowerTools engine is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine;


public interface Scope {
    Scope getParent ();

    Symbol get (String name);
    Symbol getSymbol (String name);

    Symbol createConstant (String name, String value);
    Symbol createParameter (String name, String value);
    Symbol createVariable (String name, String value);
    Symbol createStructure (String name);
    Symbol createNumberSequence (String name, int value);
    Symbol createStringSequence (String name);
    Symbol createRepeatingStringSequence (String name);
}
