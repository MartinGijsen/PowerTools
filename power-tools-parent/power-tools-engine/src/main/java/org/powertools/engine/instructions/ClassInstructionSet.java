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

package org.powertools.engine.instructions;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.KeywordName;
import org.powertools.engine.ParameterOrder;


/**
 * A ClassInstructionSet provides access to instructions that are implemented
 * as Java methods. Only methods with CamelCase method names are considered as
 * instruction implementations, with underscores indicating where the parameters go.
 * Example: boolean DefineVariable_As_ (String name, String value) { ... }
 * A method name is created from an instruction name by CamelCasing the words
 * and stringing them together.
 */
final class ClassInstructionSet implements InstructionSet {
    private final String mName;
    private final Object mObject;
    private final ParameterConvertors mParameterConvertors;


    ClassInstructionSet (String name, Object object, ParameterConvertors parameterConvertors) {
        super ();
        mName                = name;
        mObject              = object;
        mParameterConvertors = parameterConvertors;
    }

    @Override
    public String getName () {
        return mName;
    }

    @Override
    public void setup () {
        if (mObject instanceof org.powertools.engine.InstructionSet) {
            ((org.powertools.engine.InstructionSet) mObject).setup ();
        } else {
            runMethod ("setup");
        }
    }

    @Override
    public Executor getExecutor (String instructionName) {
        String methodName = getMethodName (instructionName);
        Method method     = getMethod (methodName);
        if (method == null) {
            method = getAnnotatedMethod (methodName);
            if (method == null) {
                return null;
            } else if (method.isAnnotationPresent (KeywordName.class) && method.isAnnotationPresent (ParameterOrder.class)) {
                return new ShuffledParametersMethodExecutor (mObject, method, mParameterConvertors);
            }
        } else if (method.getParameterTypes ().length == 1 && Map.class.isAssignableFrom (method.getParameterTypes ()[0])) {
            return new MapMethodExecutor (mObject, method);
        }
        return new MethodExecutor (mObject, method, mParameterConvertors);
    }

    private String getMethodName (String instructionName) {
        String[] parts   = instructionName.split (" +");
        StringBuilder sb = new StringBuilder ();
        for (String part : parts) {
            addWord (part, sb);
        }
        return sb.toString ();
    }

    private void addWord (String word, StringBuilder sb) {
        if (!word.isEmpty ()) {
            sb.append (Character.toUpperCase (word.charAt (0)));
            sb.append (word.substring (1));
        }
    }

    private Method getMethod (String methodName) {
        String methodName2 = Character.toLowerCase (methodName.charAt (0)) + methodName.substring (1);
        for (Method method : mObject.getClass ().getMethods ()) {
            if (method.getName ().equals (methodName) || method.getName ().equals (methodName2)) {
                return method;
            }
        }

        return null;
    }

    private Method getAnnotatedMethod (String methodName) {
        for (Method method : mObject.getClass ().getMethods ()) {
            if (isAnnotatedWithKeyword (method, methodName)) {
                return method;
            }
        }

        return null;
    }

    private boolean isAnnotatedWithKeyword (Method method, String methodName) {
        Annotation annotation = method.getAnnotation (KeywordName.class);
        return annotation == null ? false : ((KeywordName) annotation).value ().equals (methodName);
    }

    @Override
    public void cleanup () {
        if (mObject instanceof org.powertools.engine.InstructionSet) {
            ((org.powertools.engine.InstructionSet) mObject).cleanup ();
        } else {
            runMethod ("cleanup");
        }
    }

    private void runMethod (String name) {
        try {
            Method method = mObject.getClass ().getMethod (name, new Class<?>[0]);
            method.invoke (mObject);
        } catch (NoSuchMethodException nsme) {
            // ignore
        } catch (SecurityException se) {
            throw new ExecutionException ("security exception invoking method '%s'", name);
        } catch (IllegalAccessException iae) {
            throw new ExecutionException ("instruction class method '%s' is not public", name);
        } catch (IllegalArgumentException iae) {
            throw new ExecutionException ("illegal argument to method '%s'", name);
        } catch (InvocationTargetException ite) {
            Throwable cause = ite.getCause ();
            throw new ExecutionException (cause.getMessage () +  " invoking method " + name, cause.getStackTrace ());
        }
    }
}
