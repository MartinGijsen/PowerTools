/* Copyright 2012-2014 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools engine.
 *
 * The PowerTools engine is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine.expression;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.powertools.engine.BusinessDayChecker;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.Functions;
import org.powertools.engine.Scope;


/**
 * The ExpressionEvaluator first parses the expression, creating an Abstract Syntax Tree.
 * This AST is then evaluated by a tree walker (also a kind of parser).
 */
public final class ExpressionEvaluator {
    private final ExpressionLexer mLexer;
    private final ExpressionParser mParser;
    private final ExpressionTreeWalker mWalker;


    public ExpressionEvaluator (Functions functions) {
        mLexer  = new ExpressionLexer ();
        mParser = new ExpressionParser (null);
        mWalker = new ExpressionTreeWalker (functions, null);
    }

    public static void setBusinessDayChecker (BusinessDayChecker checker) {
        DateValue.mBusinessDayChecker = checker;
    }

    public static BusinessDayChecker getBusinessDayChecker () {
        return DateValue.mBusinessDayChecker;
    }

    public EvaluatedExpression evaluate (String expression, Scope scope) {
        try {
            mLexer.setCharStream (new ANTLRStringStream (expression));
            mParser.setTokenStream (new CommonTokenStream (mLexer));
            CommonTree tree = (CommonTree) mParser.root ().getTree ();
            mWalker.setTreeNodeStream (new CommonTreeNodeStream (tree));
            return mWalker.main (scope);
        } catch (RecognitionException re) {
            throw new ExecutionException ("invalid expression '%s'", expression);
        }
    }
}
