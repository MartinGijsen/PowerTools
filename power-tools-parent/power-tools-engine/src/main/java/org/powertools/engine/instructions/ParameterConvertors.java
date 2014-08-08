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

import org.powertools.engine.Currencies;
import java.util.HashMap;
import java.util.Map;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.Money;
import org.powertools.engine.ParameterConvertor;
import org.powertools.engine.RunTime;
import org.powertools.engine.Symbol;
import org.powertools.engine.symbol.SimpleSymbol;
import org.powertools.engine.symbol.Structure;


final class ParameterConvertors {
    private final Map<Class<?>, ParameterConvertor> mConvertorMap;

    ParameterConvertors (Currencies currencies, RunTime runTime) {
        mConvertorMap = new HashMap<Class<?>, ParameterConvertor> ();
        add (String.class, new StringConvertor ());
        add (boolean.class, new BooleanConvertor ());
        add (int.class, new IntegerConvertor ());
        add (long.class, new LongConvertor ());
        add (float.class, new FloatConvertor ());
        add (double.class, new DoubleConvertor ());
        add (Money.class, new MoneyConvertor (currencies));
        add (Structure.class, new StructureConvertor (runTime));
        add (SimpleSymbol.class, new SimpleSymbolConvertor (runTime));
        add (Symbol.class, new SymbolConvertor (runTime));
    }

    void add (Class<?> aClass, ParameterConvertor convertor) {
        mConvertorMap.put (aClass, convertor);
    }
    
    ParameterConvertor get (Class<?> aClass) {
        ParameterConvertor convertor = mConvertorMap.get (aClass);
        if (convertor == null) {
            throw new ExecutionException ("no convertor registered for class " + aClass.getName ());
        } else {
            return convertor;
        }
    }
    
    private class StringConvertor implements ParameterConvertor {
        @Override
        public Object toObject (String text) {
            return text;
        }
    }
    
    private class BooleanConvertor implements ParameterConvertor {
        @Override
        public Object toObject (String text) {
            if ("true".equalsIgnoreCase (text)) {
                return Boolean.TRUE;
            } else if ("false".equalsIgnoreCase (text)) {
                return Boolean.FALSE;
            } else {
                throw new ExecutionException ("invalid boolean: " + text);
            }
        }
    }
    
    private class IntegerConvertor implements ParameterConvertor {
        @Override
        public Object toObject (String text) {
            try {
                return Integer.valueOf (text);
            } catch (NumberFormatException nfe) {
                throw new ExecutionException ("invalid integer number: " + text);
            }
        }
    }
    
    private class LongConvertor implements ParameterConvertor {
        @Override
        public Object toObject (String text) {
            try {
                return new Long (text);
            } catch (NumberFormatException nfe) {
                throw new ExecutionException ("invalid long number: " + text);
            }
        }
    }
    
    private class FloatConvertor implements ParameterConvertor {
        @Override
        public Object toObject (String text) {
            try {
                return new Float (text);
            } catch (NumberFormatException nfe) {
                throw new ExecutionException ("invalid float number: " + text);
            }
        }
    }
    
    private class DoubleConvertor implements ParameterConvertor {
        @Override
        public Object toObject (String text) {
            try {
                return new Double (text);
            } catch (NumberFormatException nfe) {
                throw new ExecutionException ("invalid double number: " + text);
            }
        }
    }

    private class StructureConvertor implements ParameterConvertor {
        private final RunTime mRunTime;

        public StructureConvertor (RunTime runTime) {
            mRunTime = runTime;
        }

        @Override
        public Object toObject (String text) {
            Symbol symbol = mRunTime.getSymbol (text);
            if (symbol == null) { // possible???
                throw new ExecutionException ("unknown symbol " + text);
            } else if (!(symbol instanceof Structure)) {
                throw new ExecutionException ("symbol " + text + " is not a structure");
            } else {
                return symbol;
            }
        }
    }

    private class SimpleSymbolConvertor implements ParameterConvertor {
        private final RunTime mRunTime;

        public SimpleSymbolConvertor (RunTime runTime) {
            mRunTime = runTime;
        }

        @Override
        public Object toObject (String text) {
            Symbol symbol = mRunTime.getSymbol (text);
            if (symbol == null) { // possible???
                throw new ExecutionException ("unknown symbol " + text);
            } else if (symbol instanceof Structure) {
                throw new ExecutionException ("symbol " + text + " is a structure");
            } else {
                return symbol;
            }
        }
    }

    private class SymbolConvertor implements ParameterConvertor {
        private final RunTime mRunTime;

        public SymbolConvertor (RunTime runTime) {
            mRunTime = runTime;
        }

        @Override
        public Object toObject (String text) {
            Symbol symbol = mRunTime.getSymbol (text);
            if (symbol == null) { // possible???
                throw new ExecutionException ("unknown symbol " + text);
            } else {
                return symbol;
            }
        }
    }
}
