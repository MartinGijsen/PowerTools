/*	Copyright 2013-2014 by Martin Gijsen (www.DeAnalist.nl)
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

import java.util.Calendar;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.powertools.engine.BusinessDayChecker;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.core.runtime.Factory;
import org.powertools.engine.symbol.ScopeImpl;


public class ExpressionEvaluatorTest {
    private final ExpressionEvaluator mEvaluator = new ExpressionEvaluator (Factory.createFunctions ());

    @Test
    public void testSetBusinessDayCheckerGetBusinessDayChecker () {
        BusinessDayChecker checker = new BusinessDayChecker () {
            @Override
            public boolean isBusinessDay (Calendar date) {
                return date.get (Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
            }
        };
        ExpressionEvaluator.setBusinessDayChecker (checker);
        Assert.assertEquals (checker, ExpressionEvaluator.getBusinessDayChecker ());
    }

    @Test
    public void testEvaluate () {
        assertEquals ("1", mEvaluator.evaluate ("? 1", new ScopeImpl (null)).mValue.toString ());
    }

    @Test
    public void testEvaluate_InvalidExpression () {
        try {
            mEvaluator.evaluate ("?'abc", new ScopeImpl (null));
            fail ("no exception");
        } catch (ExecutionException ee) {
            assertTrue (ee.getMessage ().contains ("invalid expression"));
        }
    }

//    @Test
//    public void testFunction () {
//        assertEquals ("1", mEvaluator.evaluate ("? abs(-1)", null));
//    }
}
