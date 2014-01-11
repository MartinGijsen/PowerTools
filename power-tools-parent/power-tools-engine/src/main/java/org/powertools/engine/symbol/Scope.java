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

import java.util.HashMap;
import java.util.Map;

import org.powertools.engine.ExecutionException;
import org.powertools.engine.Symbol;


public final class Scope {
    private static final Scope mGlobalScope = new Scope (null);

    private final Map<String, Symbol> mSymbols;
    private final Scope mParent;


    public static Scope getGlobalScope () {
        return mGlobalScope;
    }


    public Scope (Scope parent) {
        mSymbols = new HashMap<String, Symbol> ();
        mParent  = parent;
    }


    public Scope getParent () {
        return mParent;
    }

    public Symbol get (String name) {
        Symbol symbol = mSymbols.get (name);
        if (symbol != null) {
            return symbol;
        } else if (mParent == null) {
            throw new ExecutionException (String.format ("symbol '%s' not found", name));
        } else {
            return mParent.get (name);
        }
    }

    public Symbol getLocal (String name) {
        return mSymbols.get (name);
    }

    public Symbol getSymbol (String name) {
        int index = name.indexOf ('.');
        if (index < 0) {
            return get (name);
        } else {
            return get (name.substring (0, index));
        }
    }

    public Symbol createConstant (String name, String value) {
        return add (new Constant (name, new Scope (this), value));
    }

    public Symbol createParameter (String name, String value) {
        return add (new Parameter (name, new Scope (this), value));
    }

    public Symbol createVariable (String name, String value) {
        return add (new SimpleVariable (name, new Scope (this), value));
    }

    public Symbol createStructure (String name) {
        return add (new Structure (name, new Scope (this)));
    }

    public Symbol createNumberSequence (String name, int value) {
        return add (new NumberSequence (name, new Scope (this), value));
    }

    public StringSequence createStringSequence (String name) {
        StringSequence sequence = new StringSequence (name, new Scope (this));
        add (sequence);
        return sequence;
    }

    public RepeatingStringSequence createRepeatingStringSequence (String name) {
        RepeatingStringSequence sequence = new RepeatingStringSequence (name, new Scope (this));
        add (sequence);
        return sequence;
    }


    private Symbol add (Symbol symbol) {
        String name = symbol.getName ();
        if (mSymbols.get (name) != null) {
            throw new ExecutionException (String.format ("a symbol '%s' already exists", name));
        } else {
            mSymbols.put (symbol.getName (), symbol);
            return symbol;
        }
    }
}
