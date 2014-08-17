/*	Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine;

import org.junit.Test;
import static org.junit.Assert.*;


public class FunctionTest {
    @Test
    public void testFunction () {
        Function function = new MyFunction ();
		assertEquals (MyFunction.NAME, function.getName ());
		assertEquals (MyFunction.NR_OF_PARAMS, function.getNrOfParameters ());
    }
    
    @Test
    public void testTooFewArguments () {
		try {
            new MyFunction ().checkNrOfArgsAndExecute (new String[] { });
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }
    
    @Test
    public void testTooManyArguments () {
		try {
            new MyFunction ().checkNrOfArgsAndExecute (new String[] { "x", "y"});
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }
    
    @Test
    public void testExecute () {
		assertEquals (MyFunction.RESULT, new MyFunction ().checkNrOfArgsAndExecute (new String[] { "x"}));
    }

    private class MyFunction extends Function {
        static final String NAME = "myFunction";
        static final int NR_OF_PARAMS = 1;
        static final String RESULT = "something";
        
        MyFunction () {
            super (NAME, NR_OF_PARAMS);
        }
        
        @Override
        public String execute (String[] args) {
            return RESULT;
        }
    }
}
