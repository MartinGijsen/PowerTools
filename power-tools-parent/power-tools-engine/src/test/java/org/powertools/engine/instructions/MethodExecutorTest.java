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

package org.powertools.engine.instructions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.sources.TestLineImpl;


public class MethodExecutorTest {
    private final Object mObject = new MethodExecutorObject ();


    @Test
    public void testExecute_voidReturningMethod () throws SecurityException, NoSuchMethodException {
        Method method           = mObject.getClass ().getDeclaredMethod ("voidReturningMethod");
        MethodExecutor executor = new MethodExecutor (mObject, method, null);
        TestLineImpl testLine   = new TestLineImpl ();
        assertTrue (executor.execute (testLine));
    }

    @Test
    public void testExecute_intReturningMethod () throws SecurityException, NoSuchMethodException {
        try {
            Method method           = mObject.getClass ().getDeclaredMethod ("intReturningMethod");
            MethodExecutor executor = new MethodExecutor (mObject, method, null);
            TestLineImpl testLine   = new TestLineImpl ();
            executor.execute (testLine);
            fail ();
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testExecute_failingMethod () throws SecurityException, NoSuchMethodException {
        Method method           = mObject.getClass ().getDeclaredMethod ("failingMethod");
        MethodExecutor executor = new MethodExecutor (mObject, method, null);
        TestLineImpl testLine   = new TestLineImpl ();
        assertFalse (executor.execute (testLine));
    }

    @Test (expected=ExecutionException.class)
    public void testExecute_tooManyParameters () throws SecurityException, NoSuchMethodException {
        Method method           = mObject.getClass ().getDeclaredMethod ("noParametersMethod");
        MethodExecutor executor = new MethodExecutor (mObject, method, null);
        TestLineImpl testLine   = new TestLineImpl ();
        testLine.createParts (2);
        testLine.setPart (1, "something");
        executor.execute (testLine);
    }

    @Test
    public void testExecute_noParametersMethod () throws SecurityException, NoSuchMethodException {
        Method method           = mObject.getClass ().getDeclaredMethod ("noParametersMethod");
        MethodExecutor executor = new MethodExecutor (mObject, method, null);
        TestLineImpl testLine   = new TestLineImpl ();
        assertTrue (executor.execute (testLine));
    }

//    @Test
//    public void testExecute_emptyParameter () throws SecurityException, NoSuchMethodException {
//        try {
//            Method method           = mObject.getClass ().getDeclaredMethod ("booleanParameterMethod", new Class<?>[] { boolean.class });
//            MethodExecutor executor = new MethodExecutor (mObject, method, new ParameterConvertors (null));
//            TestLineImpl testLine   = new TestLineImpl ();
//            testLine.createParts (2);
//            testLine.setPart (1, "");
//            executor.execute (testLine);
//            fail ();
//        } catch (ExecutionException ee) {
//            // ok
//        }
//    }
//
//    @Test
//    public void testExecute_booleanMethodTrue () throws SecurityException, NoSuchMethodException {
//        Method method           = mObject.getClass ().getDeclaredMethod ("booleanParameterMethod", new Class<?>[] { boolean.class });
//        MethodExecutor executor = new MethodExecutor (mObject, method, new ParameterConvertors (null));
//        TestLineImpl testLine   = new TestLineImpl ();
//        testLine.createParts (2);
//        testLine.setPart (1, "true");
//        assertTrue (executor.execute (testLine));
//    }
//
//    @Test
//    public void testExecute_booleanMethodFalse () throws SecurityException, NoSuchMethodException {
//        Method method           = mObject.getClass ().getDeclaredMethod ("booleanParameterMethod", new Class<?>[] { boolean.class });
//        MethodExecutor executor = new MethodExecutor (mObject, method, new ParameterConvertors (null));
//        TestLineImpl testLine   = new TestLineImpl ();
//        testLine.createParts (2);
//        testLine.setPart (1, "false");
//        assertTrue (executor.execute (testLine));
//    }
//
//    @Test (expected=ExecutionException.class)
//    public void testExecute_booleanMethodInvalid () throws SecurityException, NoSuchMethodException {
//        Method method           = mObject.getClass ().getDeclaredMethod ("booleanParameterMethod", new Class<?>[] { boolean.class });
//        MethodExecutor executor = new MethodExecutor (mObject, method, new ParameterConvertors (null));
//        TestLineImpl testLine   = new TestLineImpl ();
//        testLine.createParts (2);
//        testLine.setPart (1, "something");
//        executor.execute (testLine);
//    }
//
//    @Test
//    public void testExecute_intMethod () throws SecurityException, NoSuchMethodException {
//        Method method           = mObject.getClass ().getDeclaredMethod ("intParameterMethod", new Class<?>[] { int.class });
//        MethodExecutor executor = new MethodExecutor (mObject, method, new ParameterConvertors (null));
//        TestLineImpl testLine   = new TestLineImpl ();
//        testLine.createParts (2);
//        testLine.setPart (1, "0");
//        assertTrue (executor.execute (testLine));
//    }
//
//    @Test (expected=ExecutionException.class)
//    public void testExecute_intMethodInvalid () throws SecurityException, NoSuchMethodException {
//        Method method           = mObject.getClass ().getDeclaredMethod ("intParameterMethod", new Class<?>[] { int.class });
//        MethodExecutor executor = new MethodExecutor (mObject, method, new ParameterConvertors (null));
//        TestLineImpl testLine   = new TestLineImpl ();
//        testLine.createParts (2);
//        testLine.setPart (1, "something");
//        executor.execute (testLine);
//    }
//
//    @Test
//    public void testExecute_longMethod () throws SecurityException, NoSuchMethodException {
//        Method method           = mObject.getClass ().getDeclaredMethod ("longParameterMethod", new Class<?>[] { long.class });
//        MethodExecutor executor = new MethodExecutor (mObject, method, new ParameterConvertors (null));
//        TestLineImpl testLine   = new TestLineImpl ();
//        testLine.createParts (2);
//        testLine.setPart (1, "0");
//        assertTrue (executor.execute (testLine));
//    }
//
//    @Test (expected=ExecutionException.class)
//    public void testExecute_longMethodInvalid () throws SecurityException, NoSuchMethodException {
//        Method method           = mObject.getClass ().getDeclaredMethod ("longParameterMethod", new Class<?>[] { long.class });
//        MethodExecutor executor = new MethodExecutor (mObject, method, new ParameterConvertors (null));
//        TestLineImpl testLine   = new TestLineImpl ();
//        testLine.createParts (2);
//        testLine.setPart (1, "something");
//        executor.execute (testLine);
//    }
//
//    @Test
//    public void testExecute_floatMethod () throws SecurityException, NoSuchMethodException {
//        Method method           = mObject.getClass ().getDeclaredMethod ("floatParameterMethod", new Class<?>[] { float.class });
//        MethodExecutor executor = new MethodExecutor (mObject, method, new ParameterConvertors (null));
//        TestLineImpl testLine   = new TestLineImpl ();
//        testLine.createParts (2);
//        testLine.setPart (1, "1.2");
//        assertTrue (executor.execute (testLine));
//    }
//
//    @Test (expected=ExecutionException.class)
//    public void testExecute_floatMethodInvalid () throws SecurityException, NoSuchMethodException {
//        Method method           = mObject.getClass ().getDeclaredMethod ("floatParameterMethod", new Class<?>[] { float.class });
//        MethodExecutor executor = new MethodExecutor (mObject, method, new ParameterConvertors (null));
//        TestLineImpl testLine   = new TestLineImpl ();
//        testLine.createParts (2);
//        testLine.setPart (1, "something");
//        executor.execute (testLine);
//    }
//
//    @Test
//    public void testExecute_doubleMethod () throws SecurityException, NoSuchMethodException {
//        Method method           = mObject.getClass ().getDeclaredMethod ("doubleParameterMethod", new Class<?>[] { double.class });
//        MethodExecutor executor = new MethodExecutor (mObject, method, new ParameterConvertors (null));
//        TestLineImpl testLine   = new TestLineImpl ();
//        testLine.createParts (2);
//        testLine.setPart (1, "1.2");
//        assertTrue (executor.execute (testLine));
//    }
//
//    @Test (expected=ExecutionException.class)
//    public void testExecute_doubleMethodInvalid () throws SecurityException, NoSuchMethodException {
//        Method method           = mObject.getClass ().getDeclaredMethod ("doubleParameterMethod", new Class<?>[] { double.class });
//        MethodExecutor executor = new MethodExecutor (mObject, method, new ParameterConvertors (null));
//        TestLineImpl testLine   = new TestLineImpl ();
//        testLine.createParts (2);
//        testLine.setPart (1, "something");
//        executor.execute (testLine);
//    }
//
//    @Test (expected=ExecutionException.class)
//    public void testExecute_invalidParameterMethod () throws SecurityException, NoSuchMethodException {
//        Method method           = mObject.getClass ().getDeclaredMethod ("objectParameterMethod", new Class<?>[] { Object.class });
//        MethodExecutor executor = new MethodExecutor (mObject, method, new ParameterConvertors (null));
//        TestLineImpl testLine   = new TestLineImpl ();
//        testLine.createParts (2);
//        testLine.setPart (1, "something");
//        executor.execute (testLine);
//    }


    private class MethodExecutorObject {
        MethodExecutorObject () {
            // nothing
        }

        void voidReturningMethod () {
            // nothing
        }

        int intReturningMethod () {
            return 0;
        }

        boolean failingMethod () {
            return false;
        }

        boolean noParametersMethod () {
            return true;
        }

        boolean intParameterMethod (int i) {
            return true;
        }

        boolean longParameterMethod (long l) {
            return true;
        }

        boolean booleanParameterMethod (boolean b) {
            return true;
        }

        boolean floatParameterMethod (float f) {
            return true;
        }

        boolean doubleParameterMethod (double d) {
            return true;
        }

        boolean dateParameterMethod (Date d) {
            return true;
        }

        boolean calendarParameterMethod (Calendar c) {
            return true;
        }

        boolean objectParameterMethod (Object o) {
            return true;
        }
    }
}
