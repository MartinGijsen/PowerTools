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
    public void testAddNode () {
        String NODE_NAME        = "node name";
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        Node node = graph.addNode (NODE_NAME);
        assertEquals (node, graph.getNode (NODE_NAME));
    }

    @Test
    public void testAddNodeTwice () {
        String NODE_NAME        = "node name";
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        graph.addNode (NODE_NAME);
        try {
            graph.addNode (NODE_NAME);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testGetNodeByLabel () {
        String LABEL            = "label";
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        assertNull (graph.getNodeByLabel (LABEL));
        Node node = graph.addNode ("node name");
        node.mLabel = LABEL;
        assertEquals (node, graph.getNodeByLabel (LABEL));
    }

    @Test
    public void testGetRootNode () {
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        Node node1              = graph.addNode ("node 1");
        Node node2              = graph.addNode ("node 2");
        Node node3              = graph.addNode ("node 3");
        graph.addEdge(node1, node2);
        graph.addEdge(node2, node3);
        assertEquals (node1, graph.getRootNode ());
    }

    @Test
    public void testGetRootNodeTwoRoots () {
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        Node node1              = graph.addNode ("node 1");
        Node node2              = graph.addNode ("node 2");
        Node node3              = graph.addNode ("node 3");
        graph.addEdge(node1, node3);
        graph.addEdge(node2, node3);
        try {
            graph.getRootNode ();
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testGetRootNodeNoRoot () {
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        Node node1              = graph.addNode ("node 1");
        Node node2              = graph.addNode ("node 2");
        Node node3              = graph.addNode ("node 3");
        graph.addEdge(node1, node2);
        graph.addEdge(node2, node3);
        graph.addEdge(node3, node1);
        try {
            graph.getRootNode ();
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testGetStartNode () {
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        Node node1              = graph.addNode ("node 1");
        Node node2              = graph.addNode ("node 2");
        Node node3              = graph.addNode ("node 3");
        node2.mLabel            = Model.START_NODE_LABEL;
        graph.addEdge(node1, node2);
        graph.addEdge(node2, node3);
        graph.addEdge(node3, node1);
        assertEquals (node2, graph.getStartNode ());
    }

    @Test
    public void testAddEdge_String_String () {
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        String sourceName       = "source";
        String targetName       = "target";
        Node source             = graph.addNode (sourceName);
        Node target             = graph.addNode (targetName);
        try {
            graph.getEdge (sourceName, targetName);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
        Edge edge               = graph.addEdge (sourceName, targetName);
        assertEquals (edge, graph.getEdge (sourceName, targetName));
        assertEquals (edge, graph.getEdge (source, target));
    }

    @Test
    public void testAddEdge_Node_Node () {
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        String sourceName       = "source";
        String targetName       = "target";
        Node source             = graph.addNode (sourceName);
        Node target             = graph.addNode (targetName);
        Edge edge               = graph.addEdge (source, target);
        assertEquals (edge, graph.getEdge (source, target));
        assertEquals (edge, graph.getEdge (sourceName, targetName));
        assertEquals (edge, graph.getEdge (source, target));
    }

    @Test
    public void testAddEdgeTwice () {
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        String sourceName       = "source";
        String targetName       = "target";
        Node source             = graph.addNode (sourceName);
        Node target             = graph.addNode (targetName);
        graph.addEdge (source, target);
        try {
            graph.addEdge (source, target);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testGetEdges () {
        DirectedGraphImpl graph = new DirectedGraphImpl (GRAPH_NAME);
        Node node1              = graph.addNode ("node 1");
        Node node2              = graph.addNode ("node 2");
        Node node3              = graph.addNode ("node 3");
        Edge edge1To2           = graph.addEdge (node1, node2);
        Edge edge1To3           = graph.addEdge (node1, node3);
        Edge edge2To1           = graph.addEdge (node2, node1);
        Edge edge2To3           = graph.addEdge (node2, node3);
        Set<Edge> edges         = graph.getEdges ("node 1");
        assertTrue (edges.contains (edge1To2));
        assertTrue (edges.contains (edge1To3));
        assertFalse (edges.contains (edge2To1));
        assertFalse (edges.contains (edge2To3));
        edges = graph.getEdges (node1);
        assertTrue (edges.contains (edge1To2));
        assertTrue (edges.contains (edge1To3));
        assertFalse (edges.contains (edge2To1));
        assertFalse (edges.contains (edge2To3));
    }

//    @Test
//    public void testReportNodesAndEdges () {
//        DirectedGraphImpl instance = null;
//        instance.reportNodesAndEdges();
//        fail("The test case is a prototype.");
//    }
}
