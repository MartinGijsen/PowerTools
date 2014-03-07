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

grammar Expression;

options {
	k				= 1;
	output			= AST;
	ASTLabelType	= CommonTree;
}

tokens {
	UnaryMinus;
	DatePlus;
	DateMinus;
}

@lexer::header {
package org.powertools.engine.expression;
}

@parser::header {
package org.powertools.engine.expression;
}

@rulecatch {
	catch (RecognitionException e) {
		throw e;
	}
}

root
	:	'?'! expr EOF!
	;
	
expr
	:	andExpr ('or'^ expr)?
	;

andExpr
	:	notExpr ('and'^ andExpr)?
	;

notExpr
	:	('not'^)? booleanExpr
	;

booleanExpr
	:	comparableExpression
		(	(	'='^
			|	'<>'^
			|	'<'^
			|	'<='^
			|	'>'^
			|	'>='^
			)
			comparableExpression
		)?
	;

comparableExpression
	:	(term '++') => stringExpr
	|	(day | IdentifierPlus dateOperator addExpr period) => dateExpr
	|	addExpr
	;

stringExpr
	:	term ('++'^ term)+
	;

dateExpr
	:	(	day
		|	IdentifierPlus dateOperator^ addExpr period
		)
		( dateOperator^ addExpr period)*
	;

dateOperator
	:	'+' -> DatePlus
	|	'-' -> DateMinus
	;

day
	:	'yesterday' | 'today' | 'tomorrow' | DateLiteral
	;

period
	:	'business'? 'days' | 'weeks' | 'months' | 'years'
	;

addExpr
	:	mulExpr (('+'^ | '-'^) mulExpr)*
	;

mulExpr
	:	unaryExpr (('*'^ | '/'^) unaryExpr)*
	;

unaryExpr
	:	'-' unaryExpr -> ^(UnaryMinus unaryExpr)
	|	term
	;

term
	:	StringLiteral
	|	'true'
	|	'false'
	|	IdentifierPlus
	|	NumberLiteral
	|	'('! expr ')'!
	;

StringLiteral
	:	(	'"' (~('"'))* '"'
		|	'\'' (~('\''))* '\''
		|	('\u201c'|'\u201d') (~('\u201c'|'\u201d'))* ('\u201c'|'\u201d')
		) {
			String text = getText ();
			setText (text.substring (1, text.length () - 1));
		};
Spaces:                 (' ')+ { skip(); };
IdentifierPlus:         Identifier ('.' (Digits | Identifier) )*;
fragment Identifier:    Alpha (Alpha | Digit | '_')*;
fragment Alpha:         'a'..'z'|'A'..'Z';
DateLiteral:            Digit Digit '-' Digit Digit '-' Digit Digit Digit Digit;
NumberLiteral:          Digits ('.' Digits)?;
fragment Digits:        Digit+;
fragment Digit:         '0'..'9';
