package org.powerTools.engine.instructions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powerTools.engine.ExecutionException;
import org.powerTools.engine.sources.TestLineImpl;


public class MethodExecutorTest {
	private static final Object mObject = new MethodExecutorObject ();
	
	
	@Before
	public void setUp () throws Exception {
		;
	}

	@After
	public void tearDown () throws Exception {
		;
	}

//	@Test
//	public void testMethodExecutor () {
//		assertNotNull (new MethodExecutor (null, null));
//	}

	@Test
	public void testExecuteVoidReturningMethod () throws SecurityException, NoSuchMethodException {
		Method method = mObject.getClass ().getDeclaredMethod ("voidReturningMethod");
		MethodExecutor executor = new MethodExecutor (mObject, method);
		TestLineImpl testLine = new TestLineImpl ();
		assertTrue (executor.execute (testLine));
	}

	@Test
	public void testExecuteFailingMethod () throws SecurityException, NoSuchMethodException {
		Method method = mObject.getClass ().getDeclaredMethod ("failingMethod");
		MethodExecutor executor = new MethodExecutor (mObject, method);
		TestLineImpl testLine = new TestLineImpl ();
		assertFalse (executor.execute (testLine));
	}

	@Test (expected=ExecutionException.class)
	public void testExecuteTooManyParameters () throws SecurityException, NoSuchMethodException {
		Method method = mObject.getClass ().getDeclaredMethod ("noParametersMethod");
		MethodExecutor executor = new MethodExecutor (mObject, method);
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (2);
		testLine.setPart (1, "something");
		executor.execute (testLine);
	}

	@Test
	public void testExecuteNoParametersMethod () throws SecurityException, NoSuchMethodException {
		Method method = mObject.getClass ().getDeclaredMethod ("noParametersMethod");
		MethodExecutor executor = new MethodExecutor (mObject, method);
		TestLineImpl testLine = new TestLineImpl ();
		assertTrue (executor.execute (testLine));
	}

	@Test
	public void testExecuteBooleanMethodTrue () throws SecurityException, NoSuchMethodException {
		Method method = mObject.getClass ().getDeclaredMethod ("booleanParameterMethod", new Class<?>[] { boolean.class });
		MethodExecutor executor = new MethodExecutor (mObject, method);
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (2);
		testLine.setPart (1, "true");
		assertTrue (executor.execute (testLine));
	}

	@Test
	public void testExecuteBooleanMethodFalse () throws SecurityException, NoSuchMethodException {
		Method method = mObject.getClass ().getDeclaredMethod ("booleanParameterMethod", new Class<?>[] { boolean.class });
		MethodExecutor executor = new MethodExecutor (mObject, method);
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (2);
		testLine.setPart (1, "false");
		assertTrue (executor.execute (testLine));
	}

	@Test (expected=ExecutionException.class)
	public void testExecuteBooleanMethodInvalid () throws SecurityException, NoSuchMethodException {
		Method method = mObject.getClass ().getDeclaredMethod ("booleanParameterMethod", new Class<?>[] { boolean.class });
		MethodExecutor executor = new MethodExecutor (mObject, method);
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (2);
		testLine.setPart (1, "something");
		executor.execute (testLine);
	}

	@Test
	public void testExecuteIntMethod () throws SecurityException, NoSuchMethodException {
		Method method = mObject.getClass ().getDeclaredMethod ("intParameterMethod", new Class<?>[] { int.class });
		MethodExecutor executor = new MethodExecutor (mObject, method);
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (2);
		testLine.setPart (1, "0");
		assertTrue (executor.execute (testLine));
	}

	@Test (expected=ExecutionException.class)
	public void testExecuteIntMethodInvalid () throws SecurityException, NoSuchMethodException {
		Method method = mObject.getClass ().getDeclaredMethod ("intParameterMethod", new Class<?>[] { int.class });
		MethodExecutor executor = new MethodExecutor (mObject, method);
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (2);
		testLine.setPart (1, "something");
		executor.execute (testLine);
	}

	@Test
	public void testExecuteLongMethod () throws SecurityException, NoSuchMethodException {
		Method method = mObject.getClass ().getDeclaredMethod ("longParameterMethod", new Class<?>[] { long.class });
		MethodExecutor executor = new MethodExecutor (mObject, method);
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (2);
		testLine.setPart (1, "0");
		assertTrue (executor.execute (testLine));
	}

	@Test (expected=ExecutionException.class)
	public void testExecuteLongMethodInvalid () throws SecurityException, NoSuchMethodException {
		Method method = mObject.getClass ().getDeclaredMethod ("longParameterMethod", new Class<?>[] { long.class });
		MethodExecutor executor = new MethodExecutor (mObject, method);
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (2);
		testLine.setPart (1, "something");
		executor.execute (testLine);
	}

	@Test
	public void testExecuteFloatMethod () throws SecurityException, NoSuchMethodException {
		Method method = mObject.getClass ().getDeclaredMethod ("floatParameterMethod", new Class<?>[] { float.class });
		MethodExecutor executor = new MethodExecutor (mObject, method);
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (2);
		testLine.setPart (1, "1.2");
		assertTrue (executor.execute (testLine));
	}

	@Test (expected=ExecutionException.class)
	public void testExecuteFloatMethodInvalid () throws SecurityException, NoSuchMethodException {
		Method method = mObject.getClass ().getDeclaredMethod ("floatParameterMethod", new Class<?>[] { float.class });
		MethodExecutor executor = new MethodExecutor (mObject, method);
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (2);
		testLine.setPart (1, "something");
		executor.execute (testLine);
	}

	@Test
	public void testExecuteDoubleMethod () throws SecurityException, NoSuchMethodException {
		Method method = mObject.getClass ().getDeclaredMethod ("doubleParameterMethod", new Class<?>[] { double.class });
		MethodExecutor executor = new MethodExecutor (mObject, method);
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (2);
		testLine.setPart (1, "1.2");
		assertTrue (executor.execute (testLine));
	}

	@Test (expected=ExecutionException.class)
	public void testExecuteDoubleMethodInvalid () throws SecurityException, NoSuchMethodException {
		Method method = mObject.getClass ().getDeclaredMethod ("doubleParameterMethod", new Class<?>[] { double.class });
		MethodExecutor executor = new MethodExecutor (mObject, method);
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (2);
		testLine.setPart (1, "something");
		executor.execute (testLine);
	}

//	@Test
//	public void testExecuteDateMethod () throws SecurityException, NoSuchMethodException {
//		Method method = mObject.getClass ().getDeclaredMethod ("dateParameterMethod", new Class<?>[] { Date.class });
//		MethodExecutor executor = new MethodExecutor (mObject, method);
//		TestLineImpl testLine = new TestLineImpl ();
//		testLine.createParts (2);
//		testLine.setPart (1, "a date");
//		assertTrue (executor.execute (testLine));
//	}
//
//	@Test (expected=ExecutionException.class)
//	public void testExecuteDateMethodInvalid () throws SecurityException, NoSuchMethodException {
//		Method method = mObject.getClass ().getDeclaredMethod ("dateParameterMethod", new Class<?>[] { Date.class });
//		MethodExecutor executor = new MethodExecutor (mObject, method);
//		TestLineImpl testLine = new TestLineImpl ();
//		testLine.createParts (2);
//		testLine.setPart (1, "something");
//		executor.execute (testLine);
//	}
//
//	@Test
//	public void testExecuteCalendarMethod () throws SecurityException, NoSuchMethodException {
//		Method method = mObject.getClass ().getDeclaredMethod ("calendarParameterMethod", new Class<?>[] { Calendar.class });
//		MethodExecutor executor = new MethodExecutor (mObject, method);
//		TestLineImpl testLine = new TestLineImpl ();
//		testLine.createParts (2);
//		testLine.setPart (1, "a date");
//		assertTrue (executor.execute (testLine));
//	}
//
//	@Test (expected=ExecutionException.class)
//	public void testExecuteCalendarMethodInvalid () throws SecurityException, NoSuchMethodException {
//		Method method = mObject.getClass ().getDeclaredMethod ("calendarParameterMethod", new Class<?>[] { Calendar.class });
//		MethodExecutor executor = new MethodExecutor (mObject, method);
//		TestLineImpl testLine = new TestLineImpl ();
//		testLine.createParts (2);
//		testLine.setPart (1, "something");
//		executor.execute (testLine);
//	}

	@Test (expected=ExecutionException.class)
	public void testExecuteInvalidParameterMethod () throws SecurityException, NoSuchMethodException {
		Method method = mObject.getClass ().getDeclaredMethod ("objectParameterMethod", new Class<?>[] { Object.class });
		MethodExecutor executor = new MethodExecutor (mObject, method);
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (2);
		testLine.setPart (1, "something");
		executor.execute (testLine);
	}
}