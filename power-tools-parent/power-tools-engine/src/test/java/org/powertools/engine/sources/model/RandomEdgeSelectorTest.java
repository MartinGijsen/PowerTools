/* Copyright 2013-2014 by Martin Gijsen (www.DeAnalist.nl)
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
import org.powertools.engine.ExecutionException;


public class RandomEdgeSelectorTest {
    @Test
    public void testGetDescription () {
        assertTrue (new RandomEdgeSelector (null, null, null, null).getDescription ().startsWith (RandomEdgeSelector.NAME));
    }

    @Test
    public void testSelectEdge_doneWithMainGraph () {
        DirectedGraph mainGraph     = new DirectedGraphImpl ("name");
        Node endNode                = mainGraph.addNode ("node name");
        endNode.mLabel              = Model.END_NODE_LABEL;
        RandomEdgeSelector selector = new RandomEdgeSelector (mainGraph, null, null, new AlwaysDoneCondition ());
        assertNull (selector.selectEdge (mainGraph, endNode));
    }
    
    @Test
    public void testSelectEdge_doneWithSubgraph () {
        DirectedGraph mainGraph     = new DirectedGraphImpl ("main graph");
        DirectedGraph subGraph      = new DirectedGraphImpl ("subgraph");
        Node endNode                = subGraph.addNode ("node name");
        RandomEdgeSelector selector = new RandomEdgeSelector (mainGraph, null, null, new AlwaysDoneCondition ());
        assertNull (selector.selectEdge (subGraph, endNode));
    }
    
    @Test
    public void testSelectEdge_notDoneWithMainGraph () {
        DirectedGraph mainGraph     = new DirectedGraphImpl ("main graph");
        Node node1                  = mainGraph.addNode ("node 1");
        Node node2                  = mainGraph.addNode ("node 2");
        Node node3                  = mainGraph.addNode ("node 3");
        Edge edge1                  = mainGraph.addEdge (node1, node2);
        Edge edge2                  = mainGraph.addEdge (node1, node3);
        RandomEdgeSelector selector = new RandomEdgeSelector (mainGraph, null, new NumberGeneratorThatReturnsOne (), new AlwaysDoneCondition ());
        assertNotNull (selector.selectEdge (mainGraph, node1));
    }
    
    @Test
    public void testSelectEdge_notDoneWithSubgraph () {
        DirectedGraph mainGraph     = new DirectedGraphImpl ("main graph");
        DirectedGraph subGraph      = new DirectedGraphImpl ("subgraph");
        Node node1                  = subGraph.addNode ("node 1");
        Node node2                  = subGraph.addNode ("node 2");
        Node node3                  = subGraph.addNode ("node 3");
        Edge edge1                  = subGraph.addEdge (node1, node2);
        Edge edge2                  = subGraph.addEdge (node1, node3);
        RandomEdgeSelector selector = new RandomEdgeSelector (mainGraph, null, new NumberGeneratorThatReturnsOne (), new AlwaysDoneCondition ());
        assertNotNull (selector.selectEdge (subGraph, node1));
    }
    
    @Test
    public void testSelectEdge_loopToStartNode () {
        DirectedGraph mainGraph     = new DirectedGraphImpl ("main graph");
        Node startNode              = mainGraph.addNode ("start node");
        Node endNode                = mainGraph.addNode ("end node");
        endNode.mLabel              = Model.END_NODE_LABEL;
        Edge edge                   = mainGraph.addEdge (startNode, endNode);
        RandomEdgeSelector selector = new RandomEdgeSelector (mainGraph, null, new NumberGeneratorThatReturnsOne (), new NeverDone ());
        assertNotNull (selector.selectEdge (mainGraph, endNode));
    }
    
    @Test
    public void testSelectEdge_notDoneButNoNextNode () {
        DirectedGraph mainGraph     = new DirectedGraphImpl ("main graph");
        Node startNode              = mainGraph.addNode ("start node");
        RandomEdgeSelector selector = new RandomEdgeSelector (mainGraph, null, null, new NeverDone ());
        try {
            selector.selectEdge (mainGraph, startNode);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }
    
    private class AlwaysDoneCondition extends DoneCondition {
        public AlwaysDoneCondition () {
            super ("always done");
            mDone = true;
        }
    }

    private class NumberGeneratorThatReturnsOne implements RandomNumberGenerator {
        public int generate (int max) {
            return 1;
        }
    }
}
