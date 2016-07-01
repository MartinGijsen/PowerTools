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
import org.powertools.engine.util.PowerToolsParser;


final class CurrencyImpl implements Currency {
    private final Set<String> mNames;
    private final String      mDefaultName;
    private final int         mNrOfDecimals;
    private final long        mFactor;
    
    public CurrencyImpl (String name, int nrOfDecimals) {
        checkName (name);
        mNames        = new HashSet<String> ();
        mDefaultName  = name;
        mNrOfDecimals = nrOfDecimals;
        mFactor       = powerOf10 (nrOfDecimals);
        mNames.add (name);
    }

    @Override
    public CurrencyImpl addName (String name) {
        checkName (name);
        if (!mNames.add (name)) {
            throw new ExecutionException ("known currency name '%s'", name);
        }
        return this;
    }
    
    private void checkName (String name) {
        if ("".equals (name)) {
            throw new ExecutionException ("invalid currency name '%s'", name);
        }
    }

    // TODO: use comma or period
    @Override
    public long parseAmount (String amountText) {
        int positionOfComma = amountText.indexOf (',');
        int nrOfDecimals    = positionOfComma < 0 ? 0 : amountText.length () - positionOfComma - 1;
        if (nrOfDecimals > mNrOfDecimals) {
            throw new ExecutionException ("precision of %s is too high", amountText);
        }
        
        double amount = PowerToolsParser.parseDouble (amountText) * mFactor;
        return (long) amount;
    }
    
    @Override
    public String getName () {
        return mDefaultName;
    }
    
    @Override
    public Set<String> getNames () {
        return mNames;
    }
    
    @Override
    public int getNrOfDecimals () {
        return mNrOfDecimals;
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
    
    @Override
    public String toString () {
        return mDefaultName;
    }
}
