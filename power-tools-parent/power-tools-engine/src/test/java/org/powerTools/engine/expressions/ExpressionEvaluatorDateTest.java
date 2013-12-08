package org.powertools.engine.expressions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powertools.engine.BusinessDayChecker;
import org.powertools.engine.expression.ExpressionEvaluator;
import org.powertools.engine.symbol.Scope;


public class ExpressionEvaluatorDateTest {
	private static SimpleDateFormat mFormat = new SimpleDateFormat ("dd-MM-yyyy");
	
	private Scope mScope;


	@Before
	public void setUp () throws Exception {
		mScope = new Scope (Scope.getGlobalScope ());
	}

	@After
	public void tearDown () throws Exception {
		ExpressionEvaluator.setBusinessDayChecker (new BusinessDayChecker ());
		mScope = null;
	}


	@Test
	public void testToday () {
		String result = ExpressionEvaluator.evaluate ("? today", mScope);
		String expectation = getDateWithOffsetForToday (0);
		Assert.assertEquals (expectation, result);
	}

	@Test
	public void testYesterday () {
		String result = ExpressionEvaluator.evaluate ("? yesterday", mScope);
		String expectation = getDateWithOffsetForToday (-1);
		Assert.assertEquals (expectation, result);
	}

	@Test
	public void testTomorrow () {
		String result = ExpressionEvaluator.evaluate ("? tomorrow", mScope);
		String expectation = getDateWithOffsetForToday (1);
		Assert.assertEquals (expectation, result);
	}

	@Test
	public void testNrOfDaysAsLiteral () {
		String result = ExpressionEvaluator.evaluate ("? today + 12 days", mScope);
		String expectation = getDateWithOffsetForToday (12);
		Assert.assertEquals (expectation, result);
	}

	@Test
	public void testSubtractionNrOfDaysAsLiteral () {
		String result = ExpressionEvaluator.evaluate ("? today - 12 days", mScope);
		String expectation = getDateWithOffsetForToday (-12);
		Assert.assertEquals (expectation, result);
	}

	@Test
	public void testNrOfDaysAsSymbol () {
		mScope.createVariable ("nrOfDays", "12");
		String result = ExpressionEvaluator.evaluate ("? today + nrOfDays days", mScope);
		String expectation = getDateWithOffsetForToday (12);
		Assert.assertEquals (expectation, result);
	}

	@Test
	public void testSubtractionNrOfDaysAsSymbol () {
		mScope.createVariable ("nrOfDays", "12");
		String result = ExpressionEvaluator.evaluate ("? today - nrOfDays days", mScope);
		String expectation = getDateWithOffsetForToday (-12);
		Assert.assertEquals (expectation, result);
	}

	@Test
	public void testSubtractionNrOfDaysNegativAsSymbol () {
		mScope.createVariable ("nrOfDays", "-12");
		String result = ExpressionEvaluator.evaluate ("? today - nrOfDays days", mScope);
		String expectation = getDateWithOffsetForToday (12);
		Assert.assertEquals (expectation, result);
	}
	
	@Test
	public void testNrOfDaysNegativAsSymbol () {
		mScope.createVariable ("nrOfDays", "-12");
		String result = ExpressionEvaluator.evaluate ("? today + nrOfDays days", mScope);
		String expectation = getDateWithOffsetForToday (-12);
		Assert.assertEquals (expectation, result);
	}

	@Test
	public void testDataAsSymbolNrOfDaysAsLiteral () {
		mScope.createVariable ("aDate", "12-01-2013");
		String result = ExpressionEvaluator.evaluate ("? aDate + 12 days", mScope);
		Assert.assertEquals ("24-01-2013", result);
	}

	@Test
	public void testSubtractionDataAsSymbolNrOfDaysNegativeAsSymbol () {
		mScope.createVariable ("aDate", "12-01-2013");
		mScope.createVariable ("nrOfDays", "-12");
		String result = ExpressionEvaluator.evaluate ("? aDate - nrOfDays days", mScope);
		Assert.assertEquals ("24-01-2013", result);
	}

	@Test
	public void testExpressionWithSubExpressions () {
		mScope.createVariable ("aDate", "31-01-2013");
		mScope.createVariable ("nrOfDays", "-28");
		mScope.createVariable ("nrOfWeeks", "-2");
		String result = ExpressionEvaluator.evaluate ("? aDate - nrOfDays days + (2 * nrOfWeeks) weeks", mScope);
		Assert.assertEquals ("31-01-2013", result);
	}

	@Test
	public void testExpressionAddDefaultBusinessDays () {
		mScope.createVariable ("aDate", "01-01-2013");
		String result = ExpressionEvaluator.evaluate ("? aDate + 2 business days", mScope);
		Assert.assertEquals ("03-01-2013", result);
	}

	@Test
	public void testExpressionAddDefaultBusinessDaysOverWeekend () {
		mScope.createVariable ("aDate", "03-01-2013");
		String result = ExpressionEvaluator.evaluate ("? aDate + 2 business days", mScope);
		Assert.assertEquals ("07-01-2013", result);
	}

	@Test
	public void testExpressionAddCustomBusinessDays () {
		mScope.createVariable ("aDate", "01-01-2013");
		ExpressionEvaluator.setBusinessDayChecker (new BusinessDayChecker() {
			public boolean isBusinessDay (Calendar date) {
				return date.get (Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
			}
		});
		String result = ExpressionEvaluator.evaluate ("? aDate + 2 business days", mScope);
		Assert.assertEquals ("14-01-2013", result);
	}

	@Test
	public void testExpressionSubtractDefaultBusinessDays () {
		mScope.createVariable ("aDate", "03-01-2013");
		String result = ExpressionEvaluator.evaluate ("? aDate - 2 business days", mScope);
		Assert.assertEquals ("01-01-2013", result);
	}

	@Test
	public void testExpressionSubtractCustomBusinessDays () {
		mScope.createVariable ("aDate", "14-01-2013");
		ExpressionEvaluator.setBusinessDayChecker (new BusinessDayChecker() {
			public boolean isBusinessDay (Calendar date) {
				return date.get (Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
			}
		});
		String result = ExpressionEvaluator.evaluate ("? aDate - 2 business days", mScope);
		Assert.assertEquals ("31-12-2012", result);
	}

	@Test
	public void testExpressionLiteralDate () {
		String result = ExpressionEvaluator.evaluate ("? 01-01-2013 + 2 days", mScope);
		Assert.assertEquals ("03-01-2013", result);
	}
	
	@Test
	public void testConcateDateExpressionToString () {
		mScope.createVariable ("aDate", "03-01-2013");
		String result = ExpressionEvaluator.evaluate ("? 'aStringLiteral' ++ (aDate + 1 days)", mScope);
		String expectation = "aStringLiteral04-01-2013";
		Assert.assertEquals (expectation, result);
	}	

	private String getDateWithOffsetForToday (int nrOfDays)	{
		Calendar mDate = Calendar.getInstance ();
		mDate.add (Calendar.DATE, nrOfDays);
		return mFormat.format (mDate.getTime ());
	}
}