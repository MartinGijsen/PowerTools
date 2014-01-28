/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
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


package org.powertools.engine.sources.model;

import org.junit.Test;
import static org.junit.Assert.*;


public class ModelTest {
//    @Test
//    public void testInitialize () {
//        Model model = new ModelImpl ();
//        model.initialize ();
//    }
//
//    @Test
//    public void testGetNextAction() {
//        System.out.println("getNextAction");
//        Model instance = new ModelImpl ();
//        String expResult = "";
//        String result = instance.getNextAction();
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testSelectEdge() {
//        System.out.println("selectEdge");
//        Model instance = new ModelImpl ();
//        Edge expResult = null;
//        Edge result = instance.selectEdge();
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testCleanup() {
//        System.out.println("cleanup");
//        Model instance = new ModelImpl ();
//        instance.cleanup();
//        fail("The test case is a prototype.");
//    }

    public class ModelImpl extends Model {
        ModelImpl (String path, String fileName, DoneCondition doneCondition) {
            super (path, fileName, doneCondition);
        }
        
        public void reportStopConditionAndSelector () {
            //
        }

        public Edge selectEdge () {
            return null;
        }
    }
}
