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


public final class IntegerValue extends Value {
	private long mValue;

	
	public IntegerValue (final int value) {
		mValue = value;
	}
	
	public IntegerValue (final String value) {
		mValue = Long.parseLong (value);
	}

	
	@Override
	public Value equal (final Value v) {
		return mValue == v.toIntegerValue ().mValue ? StringValue.cTrueStringValue : StringValue.cFalseStringValue;
	}
	
	@Override
	public Value unequal (final Value v) {
		return mValue == v.toIntegerValue ().mValue ? StringValue.cFalseStringValue : StringValue.cTrueStringValue;
	}
	
	@Override
	public Value lessThan (final Value v) {
		if (v instanceof IntegerValue) {
			return mValue < v.toIntegerValue ().mValue ? StringValue.cTrueStringValue : StringValue.cFalseStringValue;
		} else {
			return this.toRealValue ().lessThan (v);
		}
	}
	
	@Override
	public Value lessOrEqual (final Value v) {
		if (v instanceof IntegerValue) {
			return mValue <= v.toIntegerValue ().mValue ? StringValue.cTrueStringValue : StringValue.cFalseStringValue;
		} else {
			return this.toRealValue ().lessOrEqual (v);
		}
	}
	
	@Override
	public Value greaterThan (final Value v) {
		if (v instanceof IntegerValue) {
			return mValue > v.toIntegerValue ().mValue ? StringValue.cTrueStringValue : StringValue.cFalseStringValue;
		} else {
			return this.toRealValue ().greaterThan (v);
		}
	}
	
	@Override
	public Value greaterOrEqual (final Value v) {
		if (v instanceof IntegerValue) {
			return mValue >= v.toIntegerValue ().mValue ? StringValue.cTrueStringValue : StringValue.cFalseStringValue;
		} else {
			return this.toRealValue ().greaterOrEqual (v);
		}
	}
	
	@Override
	public Value add (final Value v) {
		if (v instanceof IntegerValue) {
			mValue += v.toIntegerValue ().mValue;
			return this;
		} else {
			return this.toRealValue ().add (v);
		}
	}
	
	@Override
	public Value subtract (final Value v) {
		if (v instanceof IntegerValue) {
			mValue -= v.toIntegerValue ().mValue;
			return this;
		} else {
			return this.toRealValue ().subtract(v);
		}
	}
	
	@Override
	public Value multiply (final Value v) {
		if (v instanceof IntegerValue) {
			mValue *= v.toIntegerValue ().mValue;
			return this;
		} else {
			return this.toRealValue ().multiply (v.toRealValue ());
		}
	}
	
	@Override
	public Value divide (final Value v) {
		return this.toRealValue ().divide (v);
	}
	
	@Override
	public Value negate () {
		mValue = -mValue;
		return this;
	}

	
	@Override
	public StringValue toStringValue () {
		return new StringValue (Long.toString (mValue));
	}

	@Override
	public RealValue toRealValue () {
		return new RealValue (new Double (mValue).doubleValue ());
	}

	@Override
	public IntegerValue toIntegerValue () {
		return this;
	}
	
	@Override
	public String toString () {
		return Long.toString (mValue);
	}
}