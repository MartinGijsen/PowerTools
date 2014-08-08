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

package org.powertools.engine.instructions;

import org.powertools.engine.Money;
import org.powertools.engine.Currency;


public final class MoneyImpl implements Money {
    private final Currency mCurrency;
    private final long mAmount;


    public MoneyImpl (Currency currency, double amount) {
        this (currency, (long) (amount * currency.getFactor ()));
    }
    
    MoneyImpl (Currency currency, long amount) {
        mCurrency = currency;
        mAmount   = amount;
    }
    
    @Override
    public Currency getCurrency () {
        return mCurrency;
    }
    
    @Override
    public double getAmount () {
        return (double) mAmount / mCurrency.getFactor ();
    }

    
//    @Override
//    Value add (Value value) {
//        return new MoneyImpl (mCurrency, mAmount + asMoney (value).mAmount);
//    }
//
//    @Override
//    Value subtract (Value value) {
//        return new MoneyImpl (mCurrency, mAmount - asMoney (value).mAmount);
//    }
//
//    @Override
//    Value multiply (Value value) {
//        return null;
//        //return new Money (mCurrency, mAmount * asMoney (value).mAmount);
//    }
//
//    @Override
//    Value divide (Value value) {
//        return null;
//        //return new Money (mCurrency, mAmount / asMoney (value).mAmount);
//    }
//
//    
//    @Override
//    Value equal (Value value) {
//        return new BooleanValue (mAmount == asMoney (value).mAmount);
//    }
//
//    @Override
//    Value unequal (Value value) {
//        return new BooleanValue (mAmount != asMoney (value).mAmount);
//    }
//
//    @Override
//    Value smaller (Value value) {
//        return new BooleanValue (mAmount < asMoney (value).mAmount);
//    }
//
//    @Override
//    Value smallerOrEqual (Value value) {
//        return new BooleanValue (mAmount <= asMoney (value).mAmount);
//    }
//
//    @Override
//    Value greater (Value value) {
//        return new BooleanValue (mAmount > asMoney (value).mAmount);
//    }
//
//    @Override
//    Value greaterOrEqual (Value value) {
//        return new BooleanValue (mAmount >= asMoney (value).mAmount);
//    }
//
//    
//    @Override
//    MoneyImpl toMoney () {
//        return this;
//    }
    
    
    @Override
    public String toString () {
        return mCurrency.format (mAmount);
    }

    
//    private MoneyImpl asMoney (Value value) {
//        MoneyImpl moneyValue = value.toMoney ();
//        if (mCurrency != moneyValue.mCurrency) {
//            throw new ExecutionException ("currencies differ");
//        } else {
//            return moneyValue;
//        }
//    }
}
