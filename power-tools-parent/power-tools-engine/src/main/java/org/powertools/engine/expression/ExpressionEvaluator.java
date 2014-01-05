/* Copyright 2012-2013 by Martin Gijsen (www.DeAnalist.nl)
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
import org.powertools.engine.symbol.Scope;


/**
 * The ExpressionEvaluator first parses the expression, creating an Abstract Syntax Tree.
 * This AST is then evaluated by a tree walker (also a kind of parser).
 */
public final class ExpressionEvaluator {
    private static final ExpressionLexer mLexer   = new ExpressionLexer ();
    private static final ExpressionParser mParser = new ExpressionParser (new CommonTokenStream (mLexer));


    private ExpressionEvaluator () {
        // empty
    }

    public static void setBusinessDayChecker (BusinessDayChecker checker) {
        DateValue.mBusinessDayChecker = checker;
    }

    public static BusinessDayChecker getBusinessDayChecker () {
        return DateValue.mBusinessDayChecker;
    }

    public static String evaluate (String expression, Scope scope) {
        try {
            // parse the expression, create AST (Abstract Syntax Tree)
            mLexer.setCharStream (new ANTLRStringStream (expression));
            mParser.setTokenStream (new CommonTokenStream (mLexer));
            CommonTree tree = (CommonTree) mParser.root ().getTree ();

            // evaluate the AST using a tree walker parser
            ExpressionTreeWalker walker = new ExpressionTreeWalker (new CommonTreeNodeStream (tree));
            return walker.main (scope).toString ();
        } catch (RecognitionException re) {
            throw new ExecutionException ("invalid expression: " + expression);
        }
    }
}
