/* Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.expression;

import org.powertools.engine.ExecutionException;


final class BooleanValue extends Value {
    private static final String TRUE_STRING  = "true";
    private static final String FALSE_STRING = "false";

    private final boolean mValue;

    static final BooleanValue cTrueStringValue  = new BooleanValue (true);
    static final BooleanValue cFalseStringValue = new BooleanValue (false);


    BooleanValue (boolean value) {
        mValue = value;
    }

    BooleanValue (String text) {
        if (!text.equals (TRUE_STRING) && !text.equals (FALSE_STRING)) {
            throw new ExecutionException ("'%s' is not a boolean value", text);
        }
        mValue = text.equals (TRUE_STRING);
    }


    @Override
    String getType () {
        return "boolean";
    }


    @Override
    Value or (Value v) {
        return mValue || v.toBooleanValue ().mValue ? cTrueStringValue : cFalseStringValue;
    }

    @Override
    Value and (Value v) {
        return mValue && v.toBooleanValue ().mValue ? cTrueStringValue : cFalseStringValue;
    }

    @Override
    Value not () {
        return !mValue ? cTrueStringValue : cFalseStringValue;
    }


    @Override
    Value equal (Value v) {
        return mValue == v.toBooleanValue ().mValue ? cTrueStringValue : cFalseStringValue;
    }

    @Override
    Value unequal (Value v) {
        return mValue != v.toBooleanValue ().mValue ? cTrueStringValue : cFalseStringValue;
    }


    @Override
    StringValue toStringValue () {
        return new StringValue (toString ());
    }

    @Override
    BooleanValue toBooleanValue () {
        return this;
    }

    @Override
    public String toString () {
        return mValue ? TRUE_STRING : FALSE_STRING;
    }
}
