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

import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
import org.powertools.engine.ExecutionException;


public class DirectedGraphImplTest {
    final String GRAPH_NAME = "graph name";
//    @Test
//    public void testCreateGraph () {
//        DirectedGraphImpl expResult = null;
//        DirectedGraphImpl result = DirectedGraphImpl.createGraph(name);
//        assertEquals(expResult, result);
//    }

    @Test
    public void testGetName () {
        assertEquals (GRAPH_NAME, new DirectedGraphImpl (GRAPH_NAME).getName ());
    }

    @Test
    public void testAddState () {
        String STATE_NAME       = "state name";
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        State state             = graph.addState (STATE_NAME);
        assertEquals (state, graph.getState (STATE_NAME));
    }

    @Test
    public void testAddStateTwice () {
        String STATE_NAME       = "state name";
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        graph.addState (STATE_NAME);
        try {
            graph.addState (STATE_NAME);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testGetStateByLabel () {
        String LABEL            = "label";
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        assertNull (graph.getStateByLabel (LABEL));
        State state  = graph.addState ("state name");
        state.mLabel = LABEL;
        assertEquals (state, graph.getStateByLabel (LABEL));
    }

    @Test
    public void testGetRootState () {
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        State state1            = graph.addState ("state 1");
        State state2            = graph.addState ("state 2");
        State state3            = graph.addState ("state 3");
        graph.addTransition(state1, state2);
        graph.addTransition(state2, state3);
        assertEquals (state1, graph.getRootState ());
    }

    @Test
    public void testGetRootStateTwoRoots () {
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        State state1            = graph.addState ("state 1");
        State state2            = graph.addState ("state 2");
        State state3            = graph.addState ("state 3");
        graph.addTransition(state1, state3);
        graph.addTransition(state2, state3);
        try {
            graph.getRootState ();
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testGetRootStateNoRoot () {
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        State state1            = graph.addState ("state 1");
        State state2            = graph.addState ("state 2");
        State state3            = graph.addState ("state 3");
        graph.addTransition(state1, state2);
        graph.addTransition(state2, state3);
        graph.addTransition(state3, state1);
        try {
            graph.getRootState ();
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testGetStartState () {
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        State state1            = graph.addState ("state 1");
        State state2            = graph.addState ("state 2");
        State state3            = graph.addState ("state 3");
        state2.mLabel           = Model.BEGIN_STATE_LABEL;
        graph.addTransition(state1, state2);
        graph.addTransition(state2, state3);
        graph.addTransition(state3, state1);
        assertEquals (state2, graph.getBeginState ());
    }

    @Test
    public void testAddTransition_String_String () {
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        String sourceName       = "source";
        String targetName       = "target";
        State source             = graph.addState (sourceName);
        State target             = graph.addState (targetName);
        try {
            graph.getTransition (sourceName, targetName);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
        Transition transition = graph.addTransition (sourceName, targetName);
        assertEquals (transition, graph.getTransition (sourceName, targetName));
        assertEquals (transition, graph.getTransition (source, target));
    }

    @Test
    public void testAddTransition_State_State () {
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        String sourceName       = "source";
        String targetName       = "target";
        State source            = graph.addState (sourceName);
        State target            = graph.addState (targetName);
        Transition transition   = graph.addTransition (source, target);
        assertEquals (transition, graph.getTransition (source, target));
        assertEquals (transition, graph.getTransition (sourceName, targetName));
        assertEquals (transition, graph.getTransition (source, target));
    }

    @Test
    public void testAddTransitionTwice () {
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        String sourceName       = "source";
        String targetName       = "target";
        State source             = graph.addState (sourceName);
        State target             = graph.addState (targetName);
        graph.addTransition (source, target);
        try {
            graph.addTransition (source, target);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testGetTransitions () {
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        State state1            = graph.addState ("state 1");
        State state2            = graph.addState ("state 2");
        State state3            = graph.addState ("state 3");
        Transition from1To2     = graph.addTransition (state1, state2);
        Transition from1To3     = graph.addTransition (state1, state3);
        Transition from2To1     = graph.addTransition (state2, state1);
        Transition from2To3     = graph.addTransition (state2, state3);
        Set<Transition> transitions = graph.getTransitions ("state 1");
        assertTrue (transitions.contains (from1To2));
        assertTrue (transitions.contains (from1To3));
        assertFalse (transitions.contains (from2To1));
        assertFalse (transitions.contains (from2To3));
        transitions = graph.getTransitions (state1);
        assertTrue (transitions.contains (from1To2));
        assertTrue (transitions.contains (from1To3));
        assertFalse (transitions.contains (from2To1));
        assertFalse (transitions.contains (from2To3));
    }

//    @Test
//    public void testReportStatesAndTransitions () {
//        DirectedGraphImpl instance = null;
//        instance.reportStatesAndTransitions ();
//        fail("The test case is a prototype.");
//    }
}
