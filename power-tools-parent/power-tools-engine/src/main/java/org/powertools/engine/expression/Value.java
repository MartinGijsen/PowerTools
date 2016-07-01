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


abstract class Value {
    private static final String INVALID_OPERAND_MESSAGE    = "invalid operand(s) for '%s'";
    private static final String INVALID_CONVERSION_MESSAGE = "cannot convert %s to %s";

    abstract String getType ();

    Value or (Value v) {
        throw newOperandException ("or");
    }

    Value and (Value v) {
        throw newOperandException ("and");
    }

    Value not () {
        throw newOperandException ("not");
    }


    abstract Value equal (Value v);
    abstract Value unequal (Value v);


    Value lessThan (Value v) {
        throw newOperandException ("<");
    }

    Value lessOrEqual (Value v) {
        throw newOperandException ("<=");
    }

    Value greaterThan (Value v) {
        throw newOperandException (">");
    }

    Value greaterOrEqual (Value v) {
        throw newOperandException (">=");
    }


    Value add (Value v) {
        throw newOperandException ("+");
    }

    Value subtract (Value v) {
        throw newOperandException ("-");
    }

    Value multiply (Value v) {
        throw newOperandException ("*");
    }

    Value divide (Value v) {
        throw newOperandException ("/");
    }

    Value negate () {
        throw newOperandException ("-");
    }


    Value concatenate (Value v) {
        return new StringValue (toString () + v.toString ());
    }


    abstract StringValue toStringValue ();

    RealValue toRealValue () {
        throw new ExecutionException (INVALID_CONVERSION_MESSAGE, getType (), "real number");
    }

    IntegerValue toIntegerValue () {
        throw new ExecutionException (INVALID_CONVERSION_MESSAGE, getType (), "integer number");
    }

    BooleanValue toBooleanValue () {
        throw new ExecutionException (INVALID_CONVERSION_MESSAGE, getType (), "boolean");
    }

    DateValue toDateValue () {
        throw new ExecutionException (INVALID_CONVERSION_MESSAGE, getType (), "date");
    }


    ExecutionException newOperandException (String operator) {
        return new ExecutionException (INVALID_OPERAND_MESSAGE, operator);
    }
}
