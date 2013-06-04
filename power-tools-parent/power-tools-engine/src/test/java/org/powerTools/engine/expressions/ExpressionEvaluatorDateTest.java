package org.powerTools.engine.expressions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powerTools.engine.expression.ExpressionEvaluator;
import org.powerTools.engine.symbol.Scope;

public class ExpressionEvaluatorDateTest {


	private static SimpleDateFormat mFormat = new SimpleDateFormat ("dd-MM-yyyy");
	
	@Before
	public void setUp () throws Exception {
		mScope		= new Scope (Scope.getGlobalScope ());
	}

	@After
	public void tearDown () throws Exception {
		mScope		= null;
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
		String result = ExpressionEvaluator.evaluate ("? (aDate - nrOfDays days) + (2 * nrOfWeeks) weeks", mScope);
		Assert.assertEquals ("31-01-2013", result);
	}
	
	
	private String getDateWithOffsetForToday (int nrOfDays)	{
		Calendar mDate = Calendar.getInstance ();
		mDate.add (Calendar.DATE, nrOfDays);
		return mFormat.format (mDate.getTime ());
	}
	
	private Scope mScope;
}
