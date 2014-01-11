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


final class RealValue extends Value {
    private double mValue;


    RealValue (double value) {
        mValue = value;
    }

    RealValue (String value) {
        try {
            mValue = Double.parseDouble (value);
        } catch (NumberFormatException nfe) {
            throw newException ("not a real number: " + value);
        }
    }


    @Override
    String getType () {
        return "real number";
    }


    @Override
    Value equal (Value v) {
        return new BooleanValue (mValue == v.toRealValue ().mValue);
    }

    @Override
    Value unequal (Value v) {
        return new BooleanValue (mValue == v.toRealValue ().mValue);
    }

    @Override
    Value lessThan (Value v) {
        return new BooleanValue (mValue < v.toRealValue ().mValue);
    }

    @Override
    Value lessOrEqual (Value v) {
        return new BooleanValue (mValue <= v.toRealValue ().mValue);
    }

    @Override
    Value greaterThan (Value v) {
        return new BooleanValue (mValue > v.toRealValue ().mValue);
    }

    @Override
    Value greaterOrEqual (Value v) {
        return new BooleanValue (mValue >= v.toRealValue ().mValue);
    }

    @Override
    Value add (Value v) {
        return new RealValue (mValue + v.toRealValue ().mValue);
    }

    @Override
    Value subtract (Value v) {
        return new RealValue (mValue - v.toRealValue ().mValue);
    }

    @Override
    Value multiply (Value v) {
        return new RealValue (mValue * v.toRealValue ().mValue);
    }

    @Override
    Value divide (Value v) {
        return new RealValue (mValue / v.toRealValue ().mValue);
    }

    @Override
    Value negate () {
        mValue = -mValue;
        return this;
    }


    @Override
    StringValue toStringValue () {
        return new StringValue (Double.toString (mValue));
    }

    @Override
    RealValue toRealValue () {
        return this;
    }

    @Override
    public String toString () {
        return Double.toString (mValue);
    }
}
