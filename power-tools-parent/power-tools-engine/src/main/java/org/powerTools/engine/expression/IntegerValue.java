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


final class IntegerValue extends Value {
	private int  mValue;


	public IntegerValue (int value) {
		mValue = value;
	}
	
	public IntegerValue (String value) {
		mValue = Integer.parseInt (value);
	}

	
	@Override
	public String getType () {
		return "integer number";
	}

	
	@Override
	public Value equal (Value v) {
		return new BooleanValue (mValue == v.toIntegerValue ().mValue);
	}
	
	@Override
	public Value unequal (Value v) {
		return new BooleanValue (mValue != v.toIntegerValue ().mValue);
	}
	
	@Override
	public Value lessThan (Value v) {
		if (v instanceof IntegerValue) {
			return new BooleanValue (mValue < v.toIntegerValue ().mValue);
		} else {
			return this.toRealValue ().lessThan (v);
		}
	}
	
	@Override
	public Value lessOrEqual (Value v) {
		if (v instanceof IntegerValue) {
			return new BooleanValue (mValue <= v.toIntegerValue ().mValue);
		} else {
			return this.toRealValue ().lessOrEqual (v);
		}
	}
	
	@Override
	public Value greaterThan (Value v) {
		if (v instanceof IntegerValue) {
			return new BooleanValue (mValue > v.toIntegerValue ().mValue);
		} else {
			return this.toRealValue ().greaterThan (v);
		}
	}
	
	@Override
	public Value greaterOrEqual (Value v) {
		if (v instanceof IntegerValue) {
			return new BooleanValue (mValue >= v.toIntegerValue ().mValue);
		} else {
			return this.toRealValue ().greaterOrEqual (v);
		}
	}
	
	@Override
	public Value add (Value v) {
		if (v instanceof IntegerValue) {
			mValue += v.toIntegerValue ().mValue;
			return this;
		} else if (v instanceof RealValue) {
			return this.toRealValue ().add (v);
		} else {
			return super.add (v);
		}
	}
	
	@Override
	public Value subtract (Value v) {
		if (v instanceof IntegerValue) {
			mValue -= v.toIntegerValue ().mValue;
			return this;
		} else if (v instanceof RealValue) {
			return this.toRealValue ().subtract(v);
		} else {
			return super.subtract (v);
		}
	}
	
	@Override
	public Value multiply (Value v) {
		if (v instanceof IntegerValue) {
			mValue *= v.toIntegerValue ().mValue;
			return this;
		} else if (v instanceof RealValue) {
			return this.toRealValue ().multiply (v.toRealValue ());
		} else {
			return super.multiply (v);
		}
	}
	
	@Override
	public Value divide (Value v) {
		return this.toRealValue ().divide (v);
	}
	
	@Override
	public Value negate () {
		mValue = -mValue;
		return this;
	}

	
	@Override
	public StringValue toStringValue () {
		return new StringValue (Integer.toString (mValue));
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
		return Integer.toString (mValue);
	}
}