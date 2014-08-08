/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.core.runtime;

import org.powertools.engine.Currency;
import java.util.HashSet;
import java.util.Set;
import org.powertools.engine.ExecutionException;


final class CurrencyImpl implements Currency {
    private final Set<String> mNames;
    private final String mDefaultName;
    private final long mFactor;
    
    public CurrencyImpl (String name, int nrOfDecimals) {
        checkName (name);
        mNames       = new HashSet<String> ();
        mDefaultName = name;
        mFactor      = powerOf10 (nrOfDecimals);
        mNames.add (name);
    }

    @Override
    public CurrencyImpl addName (String name) {
        checkName (name);
        if (!mNames.add (name)) {
            throw new ExecutionException ("known currency name " + name);
        }
        return this;
    }
    
    private void checkName (String name) {
        if ("".equals (name)) {
            throw new ExecutionException ("invalid currency name: " + name);
        }
    }

    @Override
    public Set<String> getNames () {
        return mNames;
    }
    
    @Override
    public long getFactor () {
        return mFactor;
    }
    
    @Override
    public String format (long amount) {
        return format ((double) amount / mFactor, mDefaultName);
    }
    
    @Override
    public String format (double amount, String name) {
        return String.format ("%2f %s", amount, name);
    }
    
    private long powerOf10 (long power) {
        if (power < 0L) {
            throw new ExecutionException ("negative power ");
        } else if (power == 0L) {
            return 1;
        } else {
            return 10 * powerOf10 (power - 1);
        }
    }
}
