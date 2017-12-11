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
package org.powertools.engine.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.powertools.engine.ExecutionException;


public final class PowerToolsParser {
    private static final String DEFAULT_DEFAULT_DATE_FORMAT = "yyyyMMdd";

    public static String mDefaultDateFormat = DEFAULT_DEFAULT_DATE_FORMAT;

    
    public static boolean parseBoolean (String text) {
        switch (text.toLowerCase ()) {
        case "true":
        case "yes":
            return true;
        case "false":
        case "no":
            return false;
        default:
            throw new ExecutionException ("invalid boolean '%s'", text);
        }
    }

    public static char parseChar (String text) {
        if (text.length () == 1) {
            return text.charAt (0);
        } else if ("space".equalsIgnoreCase (text)) {
            return ' ';
        } else if ("tab".equalsIgnoreCase (text)) {
            return '\t';
        } else {
            throw new ExecutionException ("invalid char '%s'", text);
        }
    }
  
    public static byte parseByte (String text) {
        try {
            return new Byte (text);
        } catch (NumberFormatException nfe) {
            throw new ExecutionException ("invalid byte number '%s'", text);
        }
    }

    public static short parseShort (String text) {
        try {
            return new Short (text);
        } catch (NumberFormatException nfe) {
            throw new ExecutionException ("invalid short number '%s'", text);
        }
    }

    public static int parseInt (String text) {
        try {
            return new Integer (text);
        } catch (NumberFormatException nfe) {
            throw new ExecutionException ("invalid integer number '%s'", text);
        }
    }

    public static long parseLong (String text) {
        try {
            return new Long (text);
        } catch (NumberFormatException nfe) {
            throw new ExecutionException ("invalid long number '%s'", text);
        }
    }

    public static float parseFloat (String text) {
        return parseReal (text, "float").floatValue ();
    }

    public static double parseDouble (String text) {
        return parseReal (text, "double").doubleValue ();
    }

    public static Number parseReal (String text, String type) {
        try {
            ParsePosition position = new ParsePosition (0);
            Number number = NumberFormat.getInstance ().parse (text, position);
            if (position.getIndex () == text.length ()) {
                return number;
            }
        } catch (NullPointerException npe) {
            // continue
        }
        throw new ExecutionException ("invalid %s number '%s'", type, text);
    }

    public static String formatReal (double number) {
        return NumberFormat.getNumberInstance ().format (number);
    }

    public static void setDefaultDateFormat (String format) {
        mDefaultDateFormat = format;
    }

    public static String getDefaultDateFormat () {
        return mDefaultDateFormat;
    }


    public static String parseDate (String dateString) {
        return parseDate (dateString, mDefaultDateFormat);
    }

    public static String parseDate (String dateString, String format) {
        Date date = parseDateToDate (dateString, format);
        return new SimpleDateFormat ("yyyyMMdd").format (date);
    }

    public static Date parseDateToDate (String dateString) {
        return parseDateToDate (dateString, mDefaultDateFormat);
    }
    
    public static Date parseDateToDate (String dateString, String format) {
        try {
            ParsePosition position = new ParsePosition (0);
            Date date              = new SimpleDateFormat (format).parse (dateString, position);
            if (position.getIndex () == dateString.length ()) {
                return date;
            } else {
                throw new ExecutionException ("date '%s' does not have format '%s'", dateString, format);
            }
        } catch (IllegalArgumentException iae) {
            throw new ExecutionException ("invalid date format '%s'", format);
        }
    }

    public static String formatDate (String dateString) {
        return formatDate (dateString, mDefaultDateFormat);
    }
    
    public static String formatDate (String dateString, String format) {
        Date date;
        try {
            date = new SimpleDateFormat ("yyyyMMdd").parse (dateString);
        } catch (ParseException pe) {
            throw new ExecutionException ("date %s does not have format yyyyMMdd", dateString);
        } catch (IllegalArgumentException iae) {
            throw new ExecutionException ("invalid date format '%s'", format);
        }
        return new SimpleDateFormat (format).format (date);
    }
}
