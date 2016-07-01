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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.powertools.engine.Currency;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.Money;
import org.powertools.engine.ParameterConvertor;
import org.powertools.engine.RunTime;
import org.powertools.engine.Symbol;
import org.powertools.engine.util.PowerToolsParser;
import org.powertools.engine.symbol.SimpleSymbol;
import org.powertools.engine.symbol.Structure;


public final class ParameterConvertors {
    private static final String ARRAY_SEPARATOR_REGULAR_EXPRESSION = " *, +";

    private final Map<Class<?>, ParameterConvertor> mConvertorMap;

    public ParameterConvertors (RunTime runTime) {
        mConvertorMap = new HashMap<Class<?>, ParameterConvertor> ();
        
        add ( String.class, new StringConvertor ());
        add (boolean.class, new BooleanConvertor ());
        add (    int.class, new IntegerConvertor ());
        add (   long.class, new LongConvertor ());
        add (  float.class, new FloatConvertor ());
        add ( double.class, new DoubleConvertor ());

        add (        Date.class, new DateConvertor ());
        add (    Calendar.class, new CalendarConvertor ());
        add (       Money.class, new MoneyConvertor (runTime.getCurrencies ()));
        add (    Currency.class, new CurrencyConvertor (runTime.getCurrencies ()));
        add (   Structure.class, new StructureConvertor (runTime));
        add (SimpleSymbol.class, new SimpleSymbolConvertor (runTime));
        add (      Symbol.class, new SymbolConvertor (runTime));

        add ( String[].class, new ArrayOfStringsConvertor ());
        add (boolean[].class, new ArrayOfBooleansConvertor ());
        add (    int[].class, new ArrayOfIntsConvertor ());
        add (   long[].class, new ArrayOfLongsConvertor ());
        add (  float[].class, new ArrayOfFloatsConvertor ());
        add ( double[].class, new ArrayOfDoublesConvertor ());

        add (List.class, new ListConvertor ());
        add ( Set.class, new SetConvertor ());
    }

    public void add (String parameterClassName, String parameterConvertorClassName) {
        Class<?> parameterClass = getClass (parameterClassName);
        Class<?> convertorClass = getClass (parameterConvertorClassName);
        if (!(ParameterConvertor.class.isAssignableFrom (convertorClass))) {
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
    
    private class StringConvertor implements ParameterConvertor {
        @Override
        public String toObject (String text) {
            return text;
        }
    }
    
    private class BooleanConvertor implements ParameterConvertor {
        @Override
        public Boolean toObject (String text) {
            return PowerToolsParser.parseBoolean (text);
        }
    }
    
    private class IntegerConvertor implements ParameterConvertor {
        @Override
        public Integer toObject (String text) {
            return parseInt (text);
        }
    }
    
    private class LongConvertor implements ParameterConvertor {
        @Override
        public Long toObject (String text) {
            return parseLong (text);
        }
    }
    
    private class FloatConvertor implements ParameterConvertor {
        @Override
        public Float toObject (String text) {
            return PowerToolsParser.parseFloat (text);
        }
    }
    
    private class DoubleConvertor implements ParameterConvertor {
        @Override
        public Double toObject (String text) {
            return PowerToolsParser.parseDouble (text);
        }
    }

    private class DateConvertor implements ParameterConvertor {
        @Override
        public Date toObject (String text) {
            try {
                SimpleDateFormat format = new SimpleDateFormat (PowerToolsParser.getDefaultDateFormat ());
                return format.parse (text);
            } catch (ParseException pe) {
                throw new ExecutionException ("invalid date '%s'", text);
            }
        }
    }
    
    private class CalendarConvertor implements ParameterConvertor {
        @Override
        public Calendar toObject (String text) {
            Calendar calendar = GregorianCalendar.getInstance ();
            calendar.setTime (PowerToolsParser.parseDateToDate (text));
            return calendar;
        }
    }
    
    private class StructureConvertor implements ParameterConvertor {
        private final RunTime mRunTime;

        public StructureConvertor (RunTime runTime) {
            mRunTime = runTime;
        }

        @Override
        public Structure toObject (String text) {
            Symbol symbol = mRunTime.getSymbol (text);
            if (symbol instanceof Structure) {
                return (Structure) symbol;
            } else {
                throw new ExecutionException ("symbol '%s' is not a structure", text);
            }
        }
    }

    private class SimpleSymbolConvertor implements ParameterConvertor {
        private final RunTime mRunTime;

        public SimpleSymbolConvertor (RunTime runTime) {
            mRunTime = runTime;
        }

        // TODO: remove instanceof?
        @Override
        public SimpleSymbol toObject (String text) {
            Symbol symbol = mRunTime.getSymbol (text);
            if (symbol instanceof Structure) {
                throw new ExecutionException ("symbol '%s' is a structure", text);
            } else {
                return (SimpleSymbol) symbol;
            }
        }
    }

    private class SymbolConvertor implements ParameterConvertor {
        private final RunTime mRunTime;

        public SymbolConvertor (RunTime runTime) {
            mRunTime = runTime;
        }

        @Override
        public Symbol toObject (String text) {
            return mRunTime.getSymbol (text);
        }
    }

    private class ArrayOfStringsConvertor implements ParameterConvertor {
        @Override
        public String[] toObject (String text) {
            return text.split (ARRAY_SEPARATOR_REGULAR_EXPRESSION);
        }
    }
    
    private class ArrayOfBooleansConvertor implements ParameterConvertor {
        @Override
        public boolean[] toObject (String text) {
            String[] values           = text.split (ARRAY_SEPARATOR_REGULAR_EXPRESSION);
            boolean[] arrayOfBooleans = new boolean[values.length];
            for (int counter = 0; counter < values.length; ++counter) {
                arrayOfBooleans[counter] = PowerToolsParser.parseBoolean (values[counter]);
            }
            return arrayOfBooleans;
        }
    }
    
    private class ArrayOfIntsConvertor implements ParameterConvertor {
        @Override
        public int[] toObject (String text) {
            String[] values   = text.split (ARRAY_SEPARATOR_REGULAR_EXPRESSION);
            int[] arrayOfInts = new int[values.length];
            for (int counter = 0; counter < values.length; ++counter) {
                arrayOfInts[counter] = parseInt (values[counter]);
            }
            return arrayOfInts;
        }
    }
    
    private class ArrayOfLongsConvertor implements ParameterConvertor {
        @Override
        public long[] toObject (String text) {
            String[] values     = text.split (ARRAY_SEPARATOR_REGULAR_EXPRESSION);
            long[] arrayOfLongs = new long[values.length];
            for (int counter = 0; counter < values.length; ++counter) {
                arrayOfLongs[counter] = parseLong (values[counter]);
            }
            return arrayOfLongs;
        }
    }

    private class ArrayOfFloatsConvertor implements ParameterConvertor {
        @Override
        public float[] toObject (String text) {
            String[] values       = text.split (ARRAY_SEPARATOR_REGULAR_EXPRESSION);
            float[] arrayOfFloats = new float[values.length];
            for (int counter = 0; counter < values.length; ++counter) {
                arrayOfFloats[counter] = PowerToolsParser.parseFloat (values[counter]);
            }
            return arrayOfFloats;
        }
    }
    
    private class ArrayOfDoublesConvertor implements ParameterConvertor {
        @Override
        public double[] toObject (String text) {
            String[] values         = text.split (ARRAY_SEPARATOR_REGULAR_EXPRESSION);
            double[] arrayOfDoubles = new double[values.length];
            for (int counter = 0; counter < values.length; ++counter) {
                arrayOfDoubles[counter] = PowerToolsParser.parseDouble (values[counter]);
            }
            return arrayOfDoubles;
        }
    }

    private class ListConvertor implements ParameterConvertor {
        @Override
        public List<String> toObject (String text) {
            return Arrays.asList (text.split (ARRAY_SEPARATOR_REGULAR_EXPRESSION));
        }
    }

    private class SetConvertor implements ParameterConvertor {
        @Override
        public Set<String> toObject (String text) {
            String[] values = text.split (ARRAY_SEPARATOR_REGULAR_EXPRESSION);
            Set<String> set = new HashSet<String> ();
            for (String value : values) {
                set.add (value);
            }
            return set;
        }
    }

    private int parseInt (String text) {
        try {
            return new Integer (text);
        } catch (NumberFormatException nfe) {
            throw new ExecutionException ("invalid integer number '%s'", text);
        }
    }

    private long parseLong (String text) {
        try {
            return new Long (text);
        } catch (NumberFormatException nfe) {
            throw new ExecutionException ("invalid long number '%s'", text);
        }
    }
}
