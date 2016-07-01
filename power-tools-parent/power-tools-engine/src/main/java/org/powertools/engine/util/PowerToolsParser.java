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


// TODO: the parser is used both static and normal. make up your mind?
public final class PowerToolsParser {
    private static final String DEFAULT_DEFAULT_DATE_FORMAT = "yyyyMMdd";

    public static String mDefaultDateFormat = DEFAULT_DEFAULT_DATE_FORMAT;


    public static boolean parseBoolean (String text) {
        if ("true".equalsIgnoreCase (text)) {
            return true;
        } else if ("false".equalsIgnoreCase (text)) {
            return false;
        } else {
            throw new ExecutionException ("invalid boolean '%s'", text);
        }
    }

    
    public static float parseFloat (String text) {
        try {
            ParsePosition position = new ParsePosition (0);
            float result = NumberFormat.getInstance ().parse (text, position).floatValue ();
            if (position.getIndex () == text.length ()) {
                return result;
            }
        } catch (NullPointerException npe) {
            // continue
        }
        throw new ExecutionException ("invalid float number '%s'", text);
    }

    public static double parseDouble (String text) {
        try {
            ParsePosition position = new ParsePosition (0);
            double result = NumberFormat.getInstance ().parse (text, position).doubleValue ();
            if (position.getIndex () == text.length ()) {
                return result;
            }
        } catch (NullPointerException npe) {
            // continue
        }
        throw new ExecutionException ("invalid double number '%s'", text);
    }

//    public static double parseReal (String text) {
//        NumberFormat nf        = NumberFormat.getNumberInstance ();
//        ParsePosition position = new ParsePosition (0);
//        Number number          = nf.parse (text, position);
//        if (position.getIndex () == text.length ()) {
//            return number.doubleValue ();
//        } else {
//            throw new ExecutionException ("invalid real number '%s'", text);
//        }
//    }

    public static String formatReal (double number) {
        return NumberFormat.getNumberInstance ().format (number);
    }

    public static void setDefaultDateFormat (String format) {
        mDefaultDateFormat = format;
    }

    public static String getDefaultDateFormat () {
        return mDefaultDateFormat;
    }


    public String parseDate (String dateString) {
        return parseDate (dateString, mDefaultDateFormat);
    }

    public String parseDate (String dateString, String format) {
//        Date date;
//        try {
//            ParsePosition position = new ParsePosition (0);
//            date                   = new SimpleDateFormat (format).parse (dateString, position);
//            if (position.getIndex () != dateString.length ()) {
//                throw new ExecutionException ("date '%s' does not have format '%s'", dateString, format);
//            }
//        } catch (IllegalArgumentException iae) {
//            throw new ExecutionException ("invalid date format '%s'", format);
//        }
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

    public String formatDate (String dateString) {
        return formatDate (dateString, mDefaultDateFormat);
    }
    
    public String formatDate (String dateString, String format) {
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
