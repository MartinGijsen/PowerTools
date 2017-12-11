/* Copyright 2017 by Martin Gijsen (www.DeAnalist.nl)
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.ParameterConvertor;
import org.powertools.engine.RunTime;
import org.powertools.engine.Symbol;
import org.powertools.engine.util.PowerToolsParser;
import org.powertools.engine.symbol.SimpleSymbol;
import org.powertools.engine.symbol.Structure;


final class ParameterConvertorFactory {
    private static final String ARRAY_SEPARATOR_REGULAR_EXPRESSION = " *, +";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern ("HH:mm");
    
    private final RunTime _runTime;
    
    public ParameterConvertorFactory (RunTime runTime) {
        _runTime = runTime;
    }

    ParameterConvertor createStringConvertor () {
        return new ParameterConvertor () {
            @Override
            public String toObject (String text) {
                return text;
            }
        };
    }

    ParameterConvertor createCharConvertor () {
        return new ParameterConvertor () {
            @Override
            public Character toObject (String text) {
                return PowerToolsParser.parseChar (text);
            }
        };
    }
    
    ParameterConvertor createBooleanConvertor () {
        return new ParameterConvertor () {
            @Override
            public Boolean toObject (String text) {
                return PowerToolsParser.parseBoolean (text);
            }
        };
    }
    
    ParameterConvertor createByteConvertor () {
        return new ParameterConvertor () {
            @Override
            public Byte toObject (String text) {
                return PowerToolsParser.parseByte (text);
            }
        };
    }
    
    ParameterConvertor createShortConvertor () {
        return new ParameterConvertor () {
            @Override
            public Short toObject (String text) {
                return PowerToolsParser.parseShort (text);
            }
        };
    }
    
    ParameterConvertor createIntegerConvertor () {
        return new ParameterConvertor () {
            @Override
            public Integer toObject (String text) {
                return PowerToolsParser.parseInt (text);
            }
        };
    }
    
    ParameterConvertor createLongConvertor () {
        return new ParameterConvertor () {
            @Override
            public Long toObject (String text) {
                return PowerToolsParser.parseLong (text);
            }
        };
    }
    
    ParameterConvertor createFloatConvertor () {
        return new ParameterConvertor () {
            @Override
            public Float toObject (String text) {
                return PowerToolsParser.parseFloat (text);
            }
        };
    }
    
    ParameterConvertor createDoubleConvertor () {
        return new ParameterConvertor () {
            @Override
            public Double toObject (String text) {
                return PowerToolsParser.parseDouble (text);
            }
        };
    }

    ParameterConvertor createLocalDateConvertor () {
        return new ParameterConvertor () {
            @Override
            public LocalDate toObject (String text) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern (PowerToolsParser.getDefaultDateFormat ());
                    return LocalDate.parse (text, formatter);
                } catch (DateTimeParseException dtpe) {
                    throw new ExecutionException ("invalid date '%s'", text);
                }
            }
        };
    }

    ParameterConvertor createLocalTimeConvertor () {
        return new ParameterConvertor () {
            @Override
            public LocalTime toObject (String text) {
                try {
                    return LocalTime.parse (text, TIME_FORMATTER);
                } catch (DateTimeParseException dtpe) {
                    throw new ExecutionException ("invalid time '%s'", text);
                }
            }
        };
    }

    ParameterConvertor createDateConvertor () {
        return new ParameterConvertor () {
            @Override
            public Date toObject (String text) {
                try {
                    SimpleDateFormat format = new SimpleDateFormat (PowerToolsParser.getDefaultDateFormat ());
                    return format.parse (text);
                } catch (ParseException pe) {
                    throw new ExecutionException ("invalid date '%s'", text);
                }
            }
        };
    }
    
    ParameterConvertor createCalendarConvertor () {
        return new ParameterConvertor () {
            @Override
            public Calendar toObject (String text) {
                Calendar calendar = GregorianCalendar.getInstance ();
                calendar.setTime (PowerToolsParser.parseDateToDate (text));
                return calendar;
            }
        };
    }
    
    ParameterConvertor createStructureConvertor () {
        return new ParameterConvertor () {
            @Override
            public Structure toObject (String text) {
                Symbol symbol = _runTime.getSymbol (text);
                if (symbol instanceof Structure) {
                    return (Structure) symbol;
                } else {
                    throw new ExecutionException ("symbol '%s' is not a structure", text);
                }
            }
        };
    }

    ParameterConvertor createSimpleSymbolConvertor () {
        return new ParameterConvertor () {
            // TODO: remove instanceof?
            @Override
            public SimpleSymbol toObject (String text) {
                Symbol symbol = _runTime.getSymbol (text);
                if (symbol instanceof Structure) {
                    throw new ExecutionException ("symbol '%s' is a structure", text);
                } else {
                    return (SimpleSymbol) symbol;
                }
            }
        };
    }

    ParameterConvertor createSymbolConvertor () {
        return new ParameterConvertor () {
            @Override
            public Symbol toObject (String text) {
                return _runTime.getSymbol (text);
            }
        };
    }

    ParameterConvertor createArrayOfStringsConvertor () {
        return new ParameterConvertor () {
            @Override
            public String[] toObject (String text) {
                return text.split (ARRAY_SEPARATOR_REGULAR_EXPRESSION);
            }
        };
    }
    
    ParameterConvertor createArrayOfBooleansConvertor () {
        return new ParameterConvertor () {
            @Override
            public boolean[] toObject (String text) {
                String[] values           = text.split (ARRAY_SEPARATOR_REGULAR_EXPRESSION);
                boolean[] arrayOfBooleans = new boolean[values.length];
                for (int counter = 0; counter < values.length; ++counter) {
                    arrayOfBooleans[counter] = PowerToolsParser.parseBoolean (values[counter]);
                }
                return arrayOfBooleans;
            }
        };
    }
    
    ParameterConvertor createArrayOfIntsConvertor () {
        return new ParameterConvertor () {
            @Override
            public int[] toObject (String text) {
                String[] values   = text.split (ARRAY_SEPARATOR_REGULAR_EXPRESSION);
                int[] arrayOfInts = new int[values.length];
                for (int counter = 0; counter < values.length; ++counter) {
                    arrayOfInts[counter] = PowerToolsParser.parseInt (values[counter]);
                }
                return arrayOfInts;
            }
        };
    }
    
    ParameterConvertor createArrayOfLongsConvertor () {
        return new ParameterConvertor () {
            @Override
            public long[] toObject (String text) {
                String[] values     = text.split (ARRAY_SEPARATOR_REGULAR_EXPRESSION);
                long[] arrayOfLongs = new long[values.length];
                for (int counter = 0; counter < values.length; ++counter) {
                    arrayOfLongs[counter] = PowerToolsParser.parseLong (values[counter]);
                }
                return arrayOfLongs;
            }
        };
    }

    ParameterConvertor createArrayOfFloatsConvertor () {
        return new ParameterConvertor () {
            @Override
            public float[] toObject (String text) {
                String[] values       = text.split (ARRAY_SEPARATOR_REGULAR_EXPRESSION);
                float[] arrayOfFloats = new float[values.length];
                for (int counter = 0; counter < values.length; ++counter) {
                    arrayOfFloats[counter] = PowerToolsParser.parseFloat (values[counter]);
                }
                return arrayOfFloats;
            }
        };
    }
    
    ParameterConvertor createArrayOfDoublesConvertor () {
        return new ParameterConvertor () {
            @Override
            public double[] toObject (String text) {
                String[] values         = text.split (ARRAY_SEPARATOR_REGULAR_EXPRESSION);
                double[] arrayOfDoubles = new double[values.length];
                for (int counter = 0; counter < values.length; ++counter) {
                    arrayOfDoubles[counter] = PowerToolsParser.parseDouble (values[counter]);
                }
                return arrayOfDoubles;
            }
        };
    }

    ParameterConvertor createListConvertor () {
        return new ParameterConvertor () {
            @Override
            public List<String> toObject (String text) {
                return Arrays.asList (text.split (ARRAY_SEPARATOR_REGULAR_EXPRESSION));
            }
        };
    }

    ParameterConvertor createSetConvertor () {
        return new ParameterConvertor () {
            @Override
            public Set<String> toObject (String text) {
                String[] values = text.split (ARRAY_SEPARATOR_REGULAR_EXPRESSION);
                Set<String> set = new HashSet<> ();
                for (String value : values) {
                    set.add (value);
                }
                return set;
            }
        };
    }
}
