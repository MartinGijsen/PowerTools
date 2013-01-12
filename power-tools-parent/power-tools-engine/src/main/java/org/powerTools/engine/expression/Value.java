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

import org.powerTools.engine.ExecutionException;


abstract class Value {
	private static final String INVALID_OPERAND_MESSAGE = "invalid operand(s) for '%s'";

	public Value or (Value v)				{ return throwOperandException ("or"); }
	public Value and (Value v)				{ return throwOperandException ("and"); }
	public Value not ()						{ return throwOperandException ("not"); }
	
	public abstract Value equal (Value v);
	public abstract Value unequal (Value v);

	public Value lessThan (Value v)			{ return throwOperandException ("<"); }
	public Value lessOrEqual (Value v)		{ return throwOperandException ("<="); }
	public Value greaterThan (Value v)		{ return throwOperandException (">"); }
	public Value greaterOrEqual (Value v)	{ return throwOperandException (">="); }
	public Value add (Value v)				{ return throwOperandException ("+"); }
	public Value subtract (Value v)			{ return throwOperandException ("-"); }
	public Value multiply (Value v)			{ return throwOperandException ("*"); }
	public Value divide (Value v)			{ return throwOperandException ("/"); }
	public Value concatenate (Value v)		{ return throwOperandException ("++"); }
	public Value negate ()					{ return throwOperandException ("-"); }

	public boolean toBoolean (String operator)	{ throw new ExecutionException (String.format (INVALID_OPERAND_MESSAGE, operator)); }

	public abstract StringValue toStringValue ();
	public abstract RealValue toRealValue ();
	public abstract IntegerValue toIntegerValue ();
	public abstract DateValue toDateValue ();
	public abstract String toString ();

	
	protected void throwException (String message) {
		throw new ExecutionException (message);
	}
	
	private Value throwOperandException (String operator) {
		throwException (String.format (INVALID_OPERAND_MESSAGE, operator));
		return null;
	}
}