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

import org.powertools.engine.ExecutionException;


abstract class Value {
	private static final String INVALID_OPERAND_MESSAGE		= "invalid operand(s) for '%s'";
	private static final String INVALID_CONVERSION_MESSAGE	= "cannot convert %s to %s";
	
	abstract String getType ();

	public Value or (Value v) {
		return throwOperandException ("or");
	}
	
	public Value and (Value v) {
		return throwOperandException ("and");
	}
	
	public Value not () {
		return throwOperandException ("not");
	}
	
	
	public abstract Value equal (Value v);
	public abstract Value unequal (Value v);

	
	public Value lessThan (Value v) {
		return throwOperandException ("<");
	}
	
	public Value lessOrEqual (Value v) {
		return throwOperandException ("<=");
	}
	
	public Value greaterThan (Value v) {
		return throwOperandException (">");
	}
	
	public Value greaterOrEqual (Value v) {
		return throwOperandException (">=");
	}

	
	public Value add (Value v) {
		return throwOperandException ("+");
	}
	
	public Value subtract (Value v) {
		return throwOperandException ("-");
	}
	
	public Value multiply (Value v) {
		return throwOperandException ("*");
	}
	
	public Value divide (Value v) {
		return throwOperandException ("/");
	}
	
	public Value negate () {
		return throwOperandException ("-");
	}
	

	public Value concatenate (Value v) {
		return throwOperandException ("++");
	}

	
	public abstract StringValue toStringValue ();

	public RealValue toRealValue () {
		throwConversionException (getType (), "real number");
		return null;
	}
	
	public IntegerValue toIntegerValue () {
		throwConversionException (getType (), "integer number");
		return null;
	}
	
	public BooleanValue toBooleanValue () {
		throwConversionException (getType (), "boolean");
		return null;
	}
	public DateValue toDateValue () {
		throwConversionException (getType (), "date");
		return null;
	}

	
	protected void throwException (String message) {
		throw new ExecutionException (message);
	}
	
	protected Value throwOperandException (String operator) {
		throwException (String.format (INVALID_OPERAND_MESSAGE, operator));
		return null;
	}

	protected void throwConversionException (String sourceType, String targetType) {
		throwException (String.format (INVALID_CONVERSION_MESSAGE, sourceType, targetType));
	}
}