/*	Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
 *
 *	This file is part of the PowerTools engine.
 *
 *	The PowerTools engine is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the License,
 *	or (at your option) any later version.
 *
 *	The PowerTools engine is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU Affero General Public License for more details.
 *
 *	You should have received a copy of the GNU Affero General Public License
 *	along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powerTools.engine.expression;


final class RealValue extends Value {
	private double mValue;

	
	public RealValue (double value) {
		mValue = value;
	}
	
	public RealValue (String value) {
		mValue = Double.parseDouble (value);
	}
	
	
	@Override
	public Value equal (Value v) {
		return mValue == v.toRealValue ().mValue ? StringValue.cTrueStringValue : StringValue.cFalseStringValue;
	}
	
	@Override
	public Value unequal (Value v) {
		return mValue == v.toRealValue ().mValue ? StringValue.cFalseStringValue : StringValue.cTrueStringValue;
	}
	
	@Override
	public Value lessThan (Value v) {
		return mValue < v.toRealValue ().mValue ? StringValue.cTrueStringValue : StringValue.cFalseStringValue;
	}
	
	@Override
	public Value lessOrEqual (Value v) {
		return mValue <= v.toRealValue ().mValue ? StringValue.cTrueStringValue : StringValue.cFalseStringValue;
	}
	
	@Override
	public Value greaterThan (Value v) {
		return mValue > v.toRealValue ().mValue ? StringValue.cTrueStringValue : StringValue.cFalseStringValue;
	}
	
	@Override
	public Value greaterOrEqual (Value v) {
		return mValue >= v.toRealValue ().mValue ? StringValue.cTrueStringValue : StringValue.cFalseStringValue;
	}
	
	@Override
	public Value add (Value v) {
		mValue += v.toRealValue ().mValue;
		return this;
	}
	
	@Override
	public Value subtract (Value v) {
		mValue -= v.toRealValue ().mValue;
		return this;
	}
	
	@Override
	public Value multiply (Value v) {
		mValue *= v.toRealValue ().mValue;
		return this;
	}
	
	@Override
	public Value divide (Value v) {
		mValue /= v.toRealValue ().mValue;
		return this;
	}
	
	@Override
	public Value negate () {
		mValue = -mValue;
		return this;
	}

	
	@Override
	public StringValue toStringValue () {
		return new StringValue (Double.toString (mValue));
	}

	@Override
	public DateValue toDateValue () {
		throwException ("cannot make date from real number");
		return null;
	}

	@Override
	public RealValue toRealValue () {
		return this;
	}

	@Override
	public IntegerValue toIntegerValue () {
		throwException ("cannot make integer number from real number");
		return null;
	}
	
	@Override
	public String toString () {
		return Double.toString (mValue);
	}
} 