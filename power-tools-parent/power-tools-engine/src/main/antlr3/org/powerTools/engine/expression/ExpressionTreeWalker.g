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

tree grammar ExpressionTreeWalker;

options {
	k		= 1;
	ASTLabelType	= CommonTree;
	tokenVocab	= expression;
}

@header {
package org.powerTools.engine.expression;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import org.powerTools.engine.symbol.Scope;
}

@members {
	// TODO use from elsewhere
	private final static Pattern cIntegerPattern	= Pattern.compile ("-?\\d+");
	private final static Pattern cRealPattern		= Pattern.compile ("-?\\d+\\.\\d+");

	private Scope mScope;
	
	private Value createValue (String valueString) {
		if (cIntegerPattern.matcher (valueString).matches ()) {
			return new IntegerValue (valueString);
		} else if (cRealPattern.matcher (valueString).matches ()) {
			return new RealValue (valueString);
		} else {
			return new StringValue (valueString);
		}
	}
}

main [Scope scope] returns [Value v]
	:		{ mScope = scope; }
	e=expr	{ v = $e.v; }
	;
	
expr returns [Value v]
	:	^('or' e1=expr e2=expr)	{ v = $e1.v.or ($e2.v); }
	|	^('and' e1=expr e2=expr){ v = $e1.v.and ($e2.v); }
	|	^('not' e1=expr )		{ v = $e1.v.not (); }
	|	^('=' e1=expr e2=expr)	{ v = $e1.v.equal ($e2.v); }
	|	^('<>' e1=expr e2=expr)	{ v = $e1.v.unequal ($e2.v); }
	|	^('<' e1=expr e2=expr)	{ v = $e1.v.lessThan ($e2.v); }
	|	^('<=' e1=expr e2=expr)	{ v = $e1.v.lessOrEqual ($e2.v); }
	|	^('>' e1=expr e2=expr)	{ v = $e1.v.greaterThan ($e2.v); }
	|	^('>=' e1=expr e2=expr)	{ v = $e1.v.greaterOrEqual ($e2.v); }
	|	^('++' e1=expr e2=expr)	{ v = $e1.v.concatenate ($e2.v); }
	|	^('-' e1=expr e2=expr)	{ v = $e1.v.subtract ($e2.v); }
	|	^('+' e1=expr e2=expr)	{ v = $e1.v.add ($e2.v); }
	|	^('*' e1=expr e2=expr)	{ v = $e1.v.multiply ($e2.v); }
	|	^('/' e1=expr e2=expr)	{ v = $e1.v.divide ($e2.v); }
	|	^(UnaryMinus e=expr)	{ v = $e.v.negate (); }
	|	'true'					{ v = BooleanValue.cTrueStringValue; }
	|	'false'					{ v = BooleanValue.cFalseStringValue; }
	|	s=StringLiteral			{ v = new StringValue ($s.getText ()); }
	|	n=NumberLiteral			{ v = createValue ($n.getText ()); }
	|	i=IdentifierPlus {
			final String name = $i.getText ();
			v = createValue (mScope.getSymbol (name).getValue (name));
		}
	|	'yesterday' {
			Calendar calendar = GregorianCalendar.getInstance ();
			calendar.add (Calendar.DATE, -1);
			v = new DateValue (calendar);
		}
	|	'today' {
			v = new DateValue (GregorianCalendar.getInstance ());
		}
	|	'tomorrow' {
			Calendar calendar = GregorianCalendar.getInstance ();
			calendar.add (Calendar.DATE, 1);
			v = new DateValue (calendar);
		}
	|	^(DatePlus day=expr e=expr (p='days' | p='weeks' | p='months' | p='years')) {
			v = ((DateValue) $day.v).add ($e.v.toString (), $p.getText ());
		}
	|	^(DateMinus day=expr e=expr (p='days' | p='weeks' | p='months' | p='years')) {
			v = ((DateValue) $day.v).add ("-" + $e.v.toString (), $p.getText ());
		}
	;