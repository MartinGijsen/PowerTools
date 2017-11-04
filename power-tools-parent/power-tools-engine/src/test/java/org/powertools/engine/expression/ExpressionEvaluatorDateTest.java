/*	Copyright 2012-2014 by Martin Gijsen (www.DeAnalist.nl)
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powertools.engine.BusinessDayChecker;
import org.powertools.engine.Scope;
import org.powertools.engine.symbol.ScopeImpl;


public class ExpressionEvaluatorDateTest {
    private static final SimpleDateFormat mFormat = new SimpleDateFormat ("dd-MM-yyyy");
	
    private final ExpressionEvaluator mEvaluator = new ExpressionEvaluator (null);

    private Scope mScope;


    @Before
    public void setUp () throws Exception {
        mScope = new ScopeImpl (null);
    }

    @After
    public void tearDown () throws Exception {
        ExpressionEvaluator.setBusinessDayChecker (new BusinessDayChecker ());
        mScope = null;
    }


    @Test
    public void testToday () {
        EvaluatedExpression result = mEvaluator.evaluate ("? today", mScope);
        String expectation = getDateWithOffsetForToday (0);
        Assert.assertEquals (expectation, result.mValue.toString ());
    }

    @Test
    public void testYesterday () {
        EvaluatedExpression result = mEvaluator.evaluate ("? yesterday", mScope);
        String expectation = getDateWithOffsetForToday (-1);
        Assert.assertEquals (expectation, result.mValue.toString ());
    }

    @Test
    public void testTomorrow () {
        EvaluatedExpression result = mEvaluator.evaluate ("? tomorrow", mScope);
        String expectation = getDateWithOffsetForToday (1);
        Assert.assertEquals (expectation, result.mValue.toString ());
    }

    @Test
    public void testNrOfDaysAsLiteral () {
        EvaluatedExpression result = mEvaluator.evaluate ("? today + 12 days", mScope);
        String expectation = getDateWithOffsetForToday (12);
        Assert.assertEquals (expectation, result.mValue.toString ());
    }

    @Test
    public void testSubtractionNrOfDaysAsLiteral () {
        EvaluatedExpression result = mEvaluator.evaluate ("? today - 12 days", mScope);
        String expectation = getDateWithOffsetForToday (-12);
        Assert.assertEquals (expectation, result.mValue.toString ());
    }

    @Test
    public void testNrOfDaysAsSymbol () {
        mScope.createVariable ("nrOfDays", "12");
        EvaluatedExpression result = mEvaluator.evaluate ("? today + nrOfDays days", mScope);
        String expectation = getDateWithOffsetForToday (12);
        Assert.assertEquals (expectation, result.mValue.toString ());
    }

    @Test
    public void testSubtractionNrOfDaysAsSymbol () {
        mScope.createVariable ("nrOfDays", "12");
        EvaluatedExpression result = mEvaluator.evaluate ("? today - nrOfDays days", mScope);
        String expectation = getDateWithOffsetForToday (-12);
        Assert.assertEquals (expectation, result.mValue.toString ());
    }

    @Test
    public void testSubtractionNrOfDaysNegativeAsSymbol () {
        mScope.createVariable ("nrOfDays", "-12");
        EvaluatedExpression result = mEvaluator.evaluate ("? today - nrOfDays days", mScope);
        String expectation = getDateWithOffsetForToday (12);
        Assert.assertEquals (expectation, result.mValue.toString ());
    }

    @Test
    public void testNrOfDaysNegativeAsSymbol () {
        mScope.createVariable ("nrOfDays", "-12");
        EvaluatedExpression result = mEvaluator.evaluate ("? today + nrOfDays days", mScope);
        String expectation = getDateWithOffsetForToday (-12);
        Assert.assertEquals (expectation, result.mValue.toString ());
    }

    @Test
    public void testDataAsSymbolNrOfDaysAsLiteral () {
        mScope.createVariable ("aDate", "12-01-2013");
        EvaluatedExpression result = mEvaluator.evaluate ("? aDate + 12 days", mScope);
        Assert.assertEquals ("24-01-2013", result.mValue.toString ());
    }

    @Test
    public void testSubtractionDataAsSymbolNrOfDaysNegativeAsSymbol () {
        mScope.createVariable ("aDate", "12-01-2013");
        mScope.createVariable ("nrOfDays", "-12");
        EvaluatedExpression result = mEvaluator.evaluate ("? aDate - nrOfDays days", mScope);
        Assert.assertEquals ("24-01-2013", result.mValue.toString ());
    }

    @Test
    public void testExpressionWithSubExpressions () {
        mScope.createVariable ("aDate", "31-01-2013");
        mScope.createVariable ("nrOfDays", "-28");
        mScope.createVariable ("nrOfWeeks", "-2");
        EvaluatedExpression result = mEvaluator.evaluate ("? aDate - nrOfDays days + (2 * nrOfWeeks) weeks", mScope);
        Assert.assertEquals ("31-01-2013", result.mValue.toString ());
    }

    @Test
    public void testExpressionAddDefaultBusinessDays () {
        mScope.createVariable ("aDate", "01-01-2013");
        EvaluatedExpression result = mEvaluator.evaluate ("? aDate + 2 business days", mScope);
        Assert.assertEquals ("03-01-2013", result.mValue.toString ());
    }

    @Test
    public void testExpressionAddDefaultBusinessDaysOverWeekend () {
        mScope.createVariable ("aDate", "03-01-2013");
        EvaluatedExpression result = mEvaluator.evaluate ("? aDate + 2 business days", mScope);
        Assert.assertEquals ("07-01-2013", result.mValue.toString ());
    }

    @Test
    public void testExpressionAddCustomBusinessDays () {
        mScope.createVariable ("aDate", "01-01-2013");
        ExpressionEvaluator.setBusinessDayChecker (new BusinessDayChecker() {
            @Override
            public boolean isBusinessDay (Calendar date) {
                    return date.get (Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
            }
        });
        EvaluatedExpression result = mEvaluator.evaluate ("? aDate + 2 business days", mScope);
        Assert.assertEquals ("14-01-2013", result.mValue.toString ());
    }

    @Test
    public void testExpressionSubtractDefaultBusinessDays () {
        mScope.createVariable ("aDate", "03-01-2013");
        EvaluatedExpression result = mEvaluator.evaluate ("? aDate - 2 business days", mScope);
        Assert.assertEquals ("01-01-2013", result.mValue.toString ());
    }

    @Test
    public void testExpressionSubtractCustomBusinessDays () {
        mScope.createVariable ("aDate", "14-01-2013");
        ExpressionEvaluator.setBusinessDayChecker (new BusinessDayChecker() {
            @Override
            public boolean isBusinessDay (Calendar date) {
                    return date.get (Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
            }
        });
        EvaluatedExpression result = mEvaluator.evaluate ("? aDate - 2 business days", mScope);
        Assert.assertEquals ("31-12-2012", result.mValue.toString ());
    }

    @Test
    public void testExpressionLiteralDate () {
        EvaluatedExpression result = mEvaluator.evaluate ("? 01-01-2013 + 2 days", mScope);
        Assert.assertEquals ("03-01-2013", result.mValue.toString ());
    }

    @Test
    public void testConcateDateExpressionToString () {
        mScope.createVariable ("aDate", "03-01-2013");
        EvaluatedExpression result = mEvaluator.evaluate ("? 'aStringLiteral' ++ (aDate + 1 days)", mScope);
        String expectation = "aStringLiteral04-01-2013";
        Assert.assertEquals (expectation, result.mValue.toString ());
    }	

    private String getDateWithOffsetForToday (int nrOfDays)	{
        Calendar mDate = Calendar.getInstance ();
        mDate.add (Calendar.DATE, nrOfDays);
        return mFormat.format (mDate.getTime ());
    }
}