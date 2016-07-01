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

package org.powertools.engine.core;

import org.powertools.engine.Function;
import org.powertools.engine.Functions;
import org.powertools.engine.util.PowerToolsParser;


public class BuiltinFunctions {
    private final PowerToolsParser mParser;
    
    private static final String PARSE_DATE  = "parseDate";
    private static final String FORMAT_DATE = "formatDate";

    BuiltinFunctions (Functions functions, PowerToolsParser parser) {
        mParser = parser;
        addAbsFunction (functions);
        addRandomFunction (functions);
        addParseDate1 (functions);
        addParseDate2 (functions);
        addFormatDate1 (functions);
        addFormatDate2 (functions);
        addFormatDate3 (functions);
        //addContains (functions);
    }


    private void addAbsFunction (Functions functions) {
        functions.add (new Function ("abs", 1) {
            public String execute (String[] args) {
                return Integer.toString (Math.abs (Integer.parseInt (args[0])));
            }
        });
    }

    private void addRandomFunction (Functions functions) {
        functions.add (new Function ("random", 1) {
            public String execute (String[] args) {
                String limit = args[0];
                return PowerToolsParser.formatReal (Math.floor (Math.random () * Integer.parseInt (limit)));
            }
        });
    }

    private void addParseDate1 (Functions functions) {
        functions.add (new Function (PARSE_DATE, 1) {
            public String execute (String[] args) {
                String dateString = args[0];
                return mParser.parseDate (dateString);
            }
        });
    }

    private void addParseDate2 (Functions functions) {
        functions.add (new Function (PARSE_DATE, 2) {
            public String execute (String[] args) {
                String dateString = args[0];
                String format     = args[1];
                return mParser.parseDate (dateString, format);
            }
        });
    }

    private void addFormatDate1 (Functions functions) {
        functions.add (new Function (FORMAT_DATE, 1) {
            public String execute (String[] args) {
                String dateString = args[0];
                return mParser.formatDate (dateString);
            }
        });
    }
    
    private void addFormatDate2 (Functions functions) {
        functions.add (new Function (FORMAT_DATE, 2) {
            public String execute (String[] args) {
                String dateString = args[0];
                String format     = args[1];
                return mParser.formatDate (dateString, format);
            }
        });
    }
    
    private void addFormatDate3 (Functions functions) {
        functions.add (new Function (FORMAT_DATE, 3) {
            public String execute (String[] args) {
                String dateString   = args[0];
                String inputFormat  = args[1];
                String outputFormat = args[2];
                String intermediateDate = mParser.parseDate (dateString, inputFormat);
                return mParser.formatDate (intermediateDate, outputFormat);
            }
        });
    }
}
