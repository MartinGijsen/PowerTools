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
import static org.junit.Assert.fail;
import org.powertools.engine.ExecutionException;


public class NodeTest {
    private static final String NODE_NAME = "name";
    private static final String GRAPH_NAME = "graphName";
    
    @Test
    public void testNodeWithEmptyName () {
        try {
            new Node ("", null);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testNode () {
        GraphImpl graph = new GraphImpl (GRAPH_NAME);
        Node node = new Node (NODE_NAME, graph);
        assertEquals (graph, node.mGraph);
        assertEquals ("", node.mLabel);
        assertEquals ("", node.mAction);
    }

    @Test
    public void testGetName () {
        assertEquals (NODE_NAME, new Node (NODE_NAME, null).getName ());
    }

    @Test
    public void testGetDescription() {
        Node node = new Node (NODE_NAME, new GraphImpl (GRAPH_NAME));
        assertEquals (GRAPH_NAME + "." + NODE_NAME, node.getDescription ());
    }
    
    private class GraphImpl implements DirectedGraph {
        private final String mName;
        
        GraphImpl (String name) {
            mName = name;
        }
        
        public String getName () {
            return mName;
        }

        public Node addNode(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Node getNode(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Node getNodeByLabel(String label) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Node getRootNode() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Node getStartNode() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Edge addEdge(String sourceName, String targetName) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Edge addEdge(Node source, Node target) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Edge getEdge(Node source, Node target) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Set<Edge> getEdges(Node source) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
}
