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

package org.powerTools.engine.instructions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.powerTools.engine.ExecutionException;
import org.powerTools.engine.KeywordName;
import org.powerTools.engine.ParameterOrder;


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
	

	ClassInstructionSet (String name, Object object) {
		super ();
		mName	= name;
		mObject	= object;
	}

	@Override
	public String getName () {
		return mName;
	}
	
	@Override
	public void setup () {
		if (mObject instanceof org.powerTools.engine.InstructionSet) {
			((org.powerTools.engine.InstructionSet) mObject).setup ();
		}
	}

	@Override
	public Executor getExecutor (String instructionName) {
		Method method = getMethod (getMethodName (instructionName));
		if (method == null) {
			return null;
		} else if (method.isAnnotationPresent (KeywordName.class) && method.isAnnotationPresent (ParameterOrder.class)) {
			return new ShuffledParametersMethodExecutor (mObject, method);
		} else {
			return new MethodExecutor (mObject, method);
		}
	}
	
	private String getMethodName (String instructionName) {
		try {
			final StringBuffer sb	= new StringBuffer ();
			final String[] words	= instructionName.split (" ");
			addWord (words[0], sb);

			for (int partNr = 1; partNr < words.length; ++partNr) {
				final String part = words[partNr];
				if (!part.isEmpty ()) {
					addWord (part, sb);
				}
			}
			return sb.toString();
		} catch (IndexOutOfBoundsException ioobe) {
			throw new ExecutionException ("invalid instruction name");
		}
	}

	private void addWord (String word, StringBuffer sb) {
		sb.append (Character.toUpperCase (word.charAt (0)));
		sb.append (word.substring (1));
	}
	
	private Method getMethod (String methodName) {
	    for (Method method : mObject.getClass ().getMethods ()) {
	    	if (   method.getName ().equals (methodName)
	    		|| isAnnotatedWithKeyword (method, methodName)) {
	    		return method;
	    	}
	    }
	    
	    return null;
	}
	
	private boolean isAnnotatedWithKeyword (Method method, String methodName) {
		Annotation annotation = method.getAnnotation (KeywordName.class);
		return annotation != null && ((KeywordName) annotation).value ().equals (methodName);
	}

	@Override
	public void cleanup () {
		if (mObject instanceof org.powerTools.engine.InstructionSet) {
			((org.powerTools.engine.InstructionSet) mObject).setup ();
		}
	}
}