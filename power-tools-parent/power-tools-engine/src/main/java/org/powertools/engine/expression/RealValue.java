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

package org.powertools.engine.expression;


final class RealValue extends Value {
	private double mValue;

	
	public RealValue (double value) {
		mValue = value;
	}
	
	public RealValue (String value) {
		mValue = Double.parseDouble (value);
	}
	
	
	@Override
	String getType () {
		return "real number";
	}

	
	@Override
	public Value equal (Value v) {
		return new BooleanValue (mValue == v.toRealValue ().mValue);
	}
	
	@Override
	public Value unequal (Value v) {
		return new BooleanValue (mValue == v.toRealValue ().mValue);
	}
	
	@Override
	public Value lessThan (Value v) {
		return new BooleanValue (mValue < v.toRealValue ().mValue);
	}
	
	@Override
	public Value lessOrEqual (Value v) {
		return new BooleanValue (mValue <= v.toRealValue ().mValue);
	}
	
	@Override
	public Value greaterThan (Value v) {
		return new BooleanValue (mValue > v.toRealValue ().mValue);
	}
	
	@Override
	public Value greaterOrEqual (Value v) {
		return new BooleanValue (mValue >= v.toRealValue ().mValue);
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
	public RealValue toRealValue () {
		return this;
	}

	@Override
	public String toString () {
		return Double.toString (mValue);
	}
}