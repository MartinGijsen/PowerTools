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

package org.powertools.engine.core.runtime;

import org.junit.Test;
import static org.junit.Assert.*;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.Function;
import org.powertools.engine.Functions;


public class FunctionsImplTest {
	private final Functions mFunctions = new FunctionsImpl ();

    @Test
    public void testBuiltins () {
        assertNotNull (mFunctions.get ("abs"));
    }
    
    @Test
    public void testAddGetRemove () {
        String NAME = "unknown";
        try {
            mFunctions.get (NAME);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
        
        Function function = new MyFunction (NAME);
        mFunctions.add (function);
        assertEquals (function, mFunctions.get (NAME));
        mFunctions.remove (NAME);

        try {
            mFunctions.get (NAME);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }
    
    private class MyFunction extends Function {
        MyFunction (String name) {
            super (name, 0);
        }
        
        @Override
        public String execute (String[] args) {
            return null;
        }
    }

}
