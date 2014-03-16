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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.powertools.engine.ExecutionException;


public class StateTest {
    private static final String STATE_NAME = "name";
    private static final String GRAPH_NAME = "graphName";
    
    @Test
    public void testStateWithEmptyName () {
        try {
            new State ("", null);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testState () {
        DirectedGraph graph = new GraphImpl (GRAPH_NAME);
        State state = new State (STATE_NAME, graph);
        assertEquals (graph, state.mGraph);
        assertEquals ("", state.mLabel);
        assertEquals ("", state.mAction);
    }

    @Test
    public void testGetNameWithoutLabel () {
        assertEquals (STATE_NAME, new State (STATE_NAME, null).getName ());
    }

    @Test
    public void testGetNameWithLabel () {
        String LABEL = "a label";
        State state  = new State (STATE_NAME, null);
        state.mLabel = LABEL;
        assertTrue (state.getName ().startsWith (LABEL));
    }

    @Test
    public void testGetDescription() {
        State state = new State (STATE_NAME, new GraphImpl (GRAPH_NAME));
        assertEquals (GRAPH_NAME + "." + STATE_NAME, state.getDescription ());
    }
    
    private class GraphImpl implements DirectedGraph {
        private final String mName;
        
        GraphImpl (String name) {
            mName = name;
        }
        
        public void read (String path, String fileName) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String getName () {
            return mName;
        }

        public State addState(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public State getState(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public State getStateByLabel(String label) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public State getRootState() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public State getBeginState() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Transition addTransition(String sourceName, String targetName) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Transition addTransition(State source, State target) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Transition getTransition(State source, State target) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Set<Transition> getTransitions(State source) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
}
