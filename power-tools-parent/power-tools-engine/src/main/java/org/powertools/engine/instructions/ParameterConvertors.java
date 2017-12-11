/* Copyright 2014-2017 by Martin Gijsen (www.DeAnalist.nl)
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.powertools.engine.Currency;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.Money;
import org.powertools.engine.ParameterConvertor;
import org.powertools.engine.RunTime;
import org.powertools.engine.Symbol;
import org.powertools.engine.symbol.SimpleSymbol;
import org.powertools.engine.symbol.Structure;


public final class ParameterConvertors {
    private final Map<Class<?>, ParameterConvertor> mConvertorMap;

    public ParameterConvertors (RunTime runTime) {
        ParameterConvertorFactory factory = new ParameterConvertorFactory (runTime);
        mConvertorMap = new HashMap<Class<?>, ParameterConvertor> ();
        
        add ( String.class, factory.createStringConvertor ());
        add (   char.class, factory.createCharConvertor ());
        add (boolean.class, factory.createBooleanConvertor ());
        add (   byte.class, factory.createByteConvertor ());
        add (  short.class, factory.createShortConvertor ());
        add (    int.class, factory.createIntegerConvertor ());
        add (   long.class, factory.createLongConvertor ());
        add (  float.class, factory.createFloatConvertor ());
        add ( double.class, factory.createDoubleConvertor ());

        add (LocalDate.class, factory.createLocalDateConvertor ());
        add (LocalTime.class, factory.createLocalTimeConvertor ());
        add (     Date.class, factory.createDateConvertor ());
        add ( Calendar.class, factory.createCalendarConvertor ());

        add (   Money.class, new MoneyConvertor (runTime.getCurrencies ()));
        add (Currency.class, new CurrencyConvertor (runTime.getCurrencies ()));
        
        add (   Structure.class, factory.createStructureConvertor ());
        add (SimpleSymbol.class, factory.createSimpleSymbolConvertor ());
        add (      Symbol.class, factory.createSymbolConvertor ());

        add ( String[].class, factory.createArrayOfStringsConvertor ());
        add (boolean[].class, factory.createArrayOfBooleansConvertor ());
        add (    int[].class, factory.createArrayOfIntsConvertor ());
        add (   long[].class, factory.createArrayOfLongsConvertor ());
        add (  float[].class, factory.createArrayOfFloatsConvertor ());
        add ( double[].class, factory.createArrayOfDoublesConvertor ());

        add (List.class, factory.createListConvertor ());
        add ( Set.class, factory.createSetConvertor ());
    }

    public void add (String parameterClassName, String parameterConvertorClassName) {
        Class<?> parameterClass = getClass (parameterClassName);
        Class<?> convertorClass = getClass (parameterConvertorClassName);
        if (!ParameterConvertor.class.isAssignableFrom (convertorClass)) {
            throw new ExecutionException ("not a parameter convertor class '%s'", parameterConvertorClassName);
        } else if (mConvertorMap.containsKey (parameterClass)) {
            throw new ExecutionException ("already registered a convertor for '%s'", parameterClassName);
        } else {
            try {
                add (parameterClass, (ParameterConvertor) convertorClass.newInstance ());
            } catch (InstantiationException ie) {
                // TODO
            } catch (IllegalAccessException iae) {
                // TODO
            }
        }
    }

    private Class<?> getClass (String className) {
        try {
            return Class.forName (className);
        } catch (ClassNotFoundException cnfe) {
            throw new ExecutionException ("unknown class '%s'", className);
        }
    }
    
    private void add (Class<?> aClass, ParameterConvertor convertor) {
        mConvertorMap.put (aClass, convertor);
    }
    
    ParameterConvertor get (Class<?> aClass) {
        ParameterConvertor convertor = mConvertorMap.get (aClass);
        if (convertor == null) {
            throw new ExecutionException ("no convertor registered for class '%s'", aClass.getName ());
        } else {
            return convertor;
        }
    }
}
