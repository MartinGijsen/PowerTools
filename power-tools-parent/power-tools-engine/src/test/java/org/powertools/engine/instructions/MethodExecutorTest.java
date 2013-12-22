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

package org.powertools.engine.instructions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.sources.TestLineImpl;


public class MethodExecutorTest {
	private static final Object mObject = new MethodExecutorObject ();
	
	
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
