/* Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.symbol;

import org.powertools.engine.ExecutionException;


public abstract class SimpleSymbol extends SymbolImpl {
    protected String mValue;


    protected SimpleSymbol (String name, Scope scope) {
        this (name, scope, "");
    }

    protected SimpleSymbol (String name, Scope scope, String value) {
        super (name, scope);
        mValue = value;
    }


    @Override
    public String getValue (String name) {
        checkName (name);
        return mValue;
    }

    @Override
    public void setValue (String name, String value) {
        checkName (name);
        mValue = value;
    }

    @Override
    public final void clear (String[] names) {
        throw new ExecutionException ("symbol is not a structure");
    }

    protected final void checkName (String name) {
        if (name.contains (".")) {
            throw new ExecutionException ("symbol is not a structure");
        }
    }
}
