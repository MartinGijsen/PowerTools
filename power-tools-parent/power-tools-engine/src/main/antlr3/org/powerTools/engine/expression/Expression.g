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
	k		= 1;
	output		= AST;
	ASTLabelType	= CommonTree;
}

tokens {
	UnaryMinus;
}

@lexer::header {
package org.powerTools.engine.expression;
}

@parser::header {
package org.powerTools.engine.expression;
}

root:	
	expr
	EOF!;
	
expr
	:	andExpr ('or'^ expr)?
	;

andExpr
	:	notExpr ('and'^ andExpr)?
	;

notExpr
	:	'not'^ booleanExpr
	|	booleanExpr*
	;

booleanExpr
	:	comparableExpression
		(	'='^   comparableExpression
		|	'<>'^  comparableExpression
		|	'<'^   comparableExpression
		|	'<='^  comparableExpression
		|	'>'^   comparableExpression
		|	'>='^  comparableExpression
		)?
	;

comparableExpression
	:	(term '++') => stringExpr
	|	addExpr
	;

stringExpr
	:	term ('++'^ term)+
	;

addExpr
	:	mulExpr (('+'^ | '-'^) mulExpr)*
	;

mulExpr
	:	unaryExpr (('*'^ | '/'^) unaryExpr)*
	;

unaryExpr
	:	'-' term -> ^(UnaryMinus term)
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

Spaces:				(' ')+ { skip(); };
StringLiteral:		(	'"' (~('"'))* '"'
			|	('\u201c'|'\u201d') (~('\u201c'|'\u201d'))* ('\u201c'|'\u201d')
			|	'\'' (~('\''))* '\''
			)
			{ setText (getText ().substring (1, getText ().length () - 1)); };
fragment Identifier:	Alpha (Alpha | Digit | '_')*;
IdentifierPlus:		Identifier ('.' (NumberLiteral | Identifier) )*;
fragment Alpha:		'a'..'z'|'A'..'Z';
fragment Digit:		'0'..'9';
NumberLiteral:		Digit+ ('.' Digit+)?;
