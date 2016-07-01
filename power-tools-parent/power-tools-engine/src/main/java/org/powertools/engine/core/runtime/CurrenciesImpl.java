/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.powertools.engine.core.runtime;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.powertools.engine.Currency;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.Currencies;


final class CurrenciesImpl implements Currencies {
    private final Map<String, Currency> mCurrencyMap;
    
    public CurrenciesImpl () {
        mCurrencyMap = new HashMap<String, Currency> ();
        
        add (new CurrencyImpl ("euro", 2).addName ("euros").addName ("EUR").addName ("€"));
        add (new CurrencyImpl ("pound", 2).addName ("pounds").addName ("GBP").addName ("£"));
        add (new CurrencyImpl ("dollar", 2).addName ("dollars").addName ("USD").addName ("$"));
    }
    
    @Override
    public void add (Currency currency) {
        Set<String> duplicateNames = new HashSet<String> ();
        for (String name : currency.getNames ()) {
            if (isKnownCurrency (name)) {
                duplicateNames.add (name);
            } else {
                mCurrencyMap.put (name, currency);
            }
        }
        if (!duplicateNames.isEmpty ()) {
            throw new ExecutionException ("registering known currency name(s) '%s'", duplicateNames.toString ());
        }
    }
    
    @Override
    public void add (String name, int nrOfDecimals) {
        add (new CurrencyImpl (name, nrOfDecimals));
    }
    
    @Override
    public Currency get (String name) {
        Currency currency = mCurrencyMap.get (name);
        if (currency == null) {
            throw new ExecutionException ("unknown currency '%s'", name);
        } else {
            return currency;
        }
    }

    @Override
    public void remove (String name) {
        if (mCurrencyMap.remove (name) == null) {
            throw new ExecutionException ("unknown currency '%s'", name);
        }
    }
    
    @Override
    public void addAlias (String alias, String name) {
        if (isKnownCurrency (alias)) {
            throw new ExecutionException ("known currency '%s'", alias);
        } else {
            mCurrencyMap.put (alias, get (name));
        }
    }

    private boolean isKnownCurrency (String name) {
        return mCurrencyMap.containsKey (name);
    }
}
