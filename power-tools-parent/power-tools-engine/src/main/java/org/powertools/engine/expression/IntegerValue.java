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


final class IntegerValue extends Value {
    private int mValue;


    IntegerValue (int value) {
        mValue = value;
    }

    IntegerValue (String value) {
        try {
            mValue = Integer.parseInt (value);
        } catch (NumberFormatException nfe) {
            throw newException ("not an integer number: " + value);
        }
    }


    @Override
    String getType () {
        return "integer number";
    }


    @Override
    Value equal (Value v) {
        return new BooleanValue (mValue == v.toIntegerValue ().mValue);
    }

    @Override
    Value unequal (Value v) {
        return new BooleanValue (mValue != v.toIntegerValue ().mValue);
    }

    @Override
    Value lessThan (Value v) {
        if (v instanceof IntegerValue) {
            return new BooleanValue (mValue < v.toIntegerValue ().mValue);
        } else {
            return toRealValue ().lessThan (v);
        }
    }

    @Override
    Value lessOrEqual (Value v) {
        if (v instanceof IntegerValue) {
            return new BooleanValue (mValue <= v.toIntegerValue ().mValue);
        } else {
            return toRealValue ().lessOrEqual (v);
        }
    }

    @Override
    Value greaterThan (Value v) {
        if (v instanceof IntegerValue) {
            return new BooleanValue (mValue > v.toIntegerValue ().mValue);
        } else {
            return toRealValue ().greaterThan (v);
        }
    }

    @Override
    Value greaterOrEqual (Value v) {
        if (v instanceof IntegerValue) {
            return new BooleanValue (mValue >= v.toIntegerValue ().mValue);
        } else {
            return toRealValue ().greaterOrEqual (v);
        }
    }

    @Override
    Value add (Value v) {
        if (v instanceof IntegerValue) {
            return new IntegerValue (mValue + v.toIntegerValue ().mValue);
        } else if (v instanceof RealValue) {
            return toRealValue ().add (v);
        } else {
            throw newOperandException ("+");
        }
    }

    @Override
    Value subtract (Value v) {
        if (v instanceof IntegerValue) {
            return new IntegerValue (mValue - v.toIntegerValue ().mValue);
        } else if (v instanceof RealValue) {
            return toRealValue ().subtract(v);
        } else {
            throw newOperandException ("-");
        }
    }

    @Override
    Value multiply (Value v) {
        if (v instanceof IntegerValue) {
            return new IntegerValue (mValue * v.toIntegerValue ().mValue);
        } else if (v instanceof RealValue) {
            return toRealValue ().multiply (v);
        } else {
            throw newOperandException ("*");
        }
    }

    @Override
    Value divide (Value v) {
        if (v instanceof IntegerValue) {
            int integerValueOfV = v.toIntegerValue ().mValue;
            if (mValue % integerValueOfV == 0) {
                return new IntegerValue (mValue / integerValueOfV);
            }
        } else if (!(v instanceof RealValue)) {
            throw newOperandException ("/");
        }
        return toRealValue ().divide (v);
    }

    @Override
    Value negate () {
        return new IntegerValue (-mValue);
    }

    @Override
    StringValue toStringValue () {
        return new StringValue (Integer.toString (mValue));
    }

    @Override
    RealValue toRealValue () {
        return new RealValue (new Double (mValue).doubleValue ());
    }

    @Override
    IntegerValue toIntegerValue () {
        return this;
    }

    @Override
    public String toString () {
        return Integer.toString (mValue);
    }
}
