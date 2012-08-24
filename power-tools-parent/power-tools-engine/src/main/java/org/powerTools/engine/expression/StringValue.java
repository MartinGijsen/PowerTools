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


public final class StringValue extends Value {
	private static final String TRUE_STRING		= "true";
	private static final String FALSE_STRING	= "false";

	private String mText;

	static final StringValue cTrueStringValue	= new StringValue (TRUE_STRING);
	static final StringValue cFalseStringValue	= new StringValue (FALSE_STRING);	

	
	public StringValue (final String text) {
		mText = text;
	}
	
	
	@Override
	public Value or (final Value v) {
		v.toBoolean ("or");
		if (toBoolean ("or")) {
			return this;
		} else {
			return v;
		}
	}
	
	@Override
	public Value and (final Value v) {
		v.toBoolean ("and");
		if (!toBoolean ("and")) {
			return this;
		} else {
			return v;
		}
	}
	
	@Override
	public Value not () {
		return toBoolean ("not") ? cFalseStringValue : cTrueStringValue;
	}
	
	@Override
	public Value equal (final Value v) {
		return mText.equals (v.toStringValue ().mText) ? StringValue.cTrueStringValue : StringValue.cFalseStringValue;
	}
	
	@Override
	public Value unequal (final Value v) {
		return mText.equals (v.toStringValue ().mText) ? StringValue.cFalseStringValue : StringValue.cTrueStringValue;
	}
	
	@Override
	public Value concatenate (final Value v) {
		mText += v.toStringValue ().mText;
		return this;
	}

	
	@Override
	public IntegerValue toIntegerValue () {
		return new IntegerValue (Integer.parseInt (mText));
	}

	@Override
	public RealValue toRealValue () {
		return new RealValue (Double.parseDouble (mText));
	}

	@Override
	public StringValue toStringValue () {
		return this;
	}
	
	@Override
	public String toString () {
		return mText;
	}
	
	@Override
	public boolean toBoolean (final String operator) {
		if (mText.equals (TRUE_STRING)) {
			return true;
		} else if (!mText.equals (FALSE_STRING)) {
			throwException ("invalid operand(s) for '" + operator + "'");
		}
		return false;
	}
}