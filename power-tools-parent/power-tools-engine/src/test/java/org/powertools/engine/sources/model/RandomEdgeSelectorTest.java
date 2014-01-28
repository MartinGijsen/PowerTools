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
import org.powertools.engine.BusinessDayChecker;
import org.powertools.engine.Context;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.Roles;
import org.powertools.engine.RunTime;
import org.powertools.engine.Symbol;
import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.symbol.Scope;


public class RandomEdgeSelectorTest {
    @Test
    public void testGetDescription () {
        assertTrue (new RandomEdgeSelector (null, null, null).getDescription ().startsWith (RandomEdgeSelector.NAME));
    }

    @Test
    public void testSelectEdge_doneWithSubgraph () {
        DirectedGraph mainGraph     = new DirectedGraphImpl ("main graph");
        DirectedGraph subGraph      = new DirectedGraphImpl ("subgraph");
        Node endNode                = subGraph.addNode ("node name");
        RandomEdgeSelector selector = new RandomEdgeSelector (mainGraph, null, null);
        assertNull (selector.selectEdge (subGraph, endNode));
    }
    
    @Test
    public void testSelectEdge_conditionTrue () {
        DirectedGraph mainGraph     = new DirectedGraphImpl ("main graph");
        Node node1                  = mainGraph.addNode ("node 1");
        Node node2                  = mainGraph.addNode ("node 2");
        Edge edge                   = mainGraph.addEdge (node1, node2);
        edge.mCondition             = "?true";
        RunTime runTime             = new RunTimeImpl ("true");
        RandomEdgeSelector selector = new RandomEdgeSelector (mainGraph, runTime, new DefaultRandomNumberGenerator ());
        assertEquals (edge, selector.selectEdge (mainGraph, node1));
    }

    @Test
    public void testSelectEdge_conditionFalse () {
        DirectedGraph mainGraph     = new DirectedGraphImpl ("main graph");
        Node node1                  = mainGraph.addNode ("node 1");
        Node node2                  = mainGraph.addNode ("node 2");
        Edge edge                   = mainGraph.addEdge (node1, node2);
        edge.mCondition             = "?false";
        RunTime runTime             = new RunTimeImpl ("false");
        RandomEdgeSelector selector = new RandomEdgeSelector (mainGraph, runTime, new DefaultRandomNumberGenerator ());
        try {
            selector.selectEdge (mainGraph, node1);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testSelectEdge_notDoneWithMainGraph () {
        DirectedGraph mainGraph     = new DirectedGraphImpl ("main graph");
        Node node1                  = mainGraph.addNode ("node 1");
        Node node2                  = mainGraph.addNode ("node 2");
        Node node3                  = mainGraph.addNode ("node 3");
        Edge edge1                  = mainGraph.addEdge (node1, node2);
        Edge edge2                  = mainGraph.addEdge (node1, node3);
        RandomEdgeSelector selector = new RandomEdgeSelector (mainGraph, null, new NumberGeneratorThatReturnsOne ());
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
        RandomEdgeSelector selector = new RandomEdgeSelector (mainGraph, null, new NumberGeneratorThatReturnsOne ());
        assertNotNull (selector.selectEdge (subGraph, node1));
    }
    
    @Test
    public void testSelectEdge_loopToStartNode () {
        DirectedGraph mainGraph     = new DirectedGraphImpl ("main graph");
        Node startNode              = mainGraph.addNode ("start node");
        Node endNode                = mainGraph.addNode ("end node");
        endNode.mLabel              = Model.END_NODE_LABEL;
        Edge edge                   = mainGraph.addEdge (startNode, endNode);
        RandomEdgeSelector selector = new RandomEdgeSelector (mainGraph, null, new NumberGeneratorThatReturnsOne ());
        assertNotNull (selector.selectEdge (mainGraph, endNode));
    }
    
    @Test
    public void testSelectEdge_notDoneButNoNextNode () {
        DirectedGraph mainGraph     = new DirectedGraphImpl ("main graph");
        Node startNode              = mainGraph.addNode ("start node");
        RandomEdgeSelector selector = new RandomEdgeSelector (mainGraph, null, null);
        try {
            selector.selectEdge (mainGraph, startNode);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }
    
    private class NumberGeneratorThatReturnsOne implements RandomNumberGenerator {
        public int generate (int max) {
            return 1;
        }
    }
    
    private class RunTimeImpl implements RunTime {
        private final String mExpressionValue;
        
        RunTimeImpl (String expressionValue) {
            mExpressionValue = expressionValue;
        }
        
        public Context getContext() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void reportValueError(String expression, String actualValue, String expectedValue) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void reportError(String message) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void reportStackTrace(Exception e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void reportWarning(String message) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void reportValue(String expression, String value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void reportInfo(String message) {
            // ignore
        }

        public void reportLink(String url) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String evaluateExpression(String expression) {
            return mExpressionValue;
        }

        public Scope getGlobalScope() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Scope getCurrentScope() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Symbol getSymbol(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setValue(String name, String value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void copyStructure(String target, String source) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void clearStructure(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Roles getRoles() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean enterTestCase(String name, String description) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean leaveTestCase() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean addSharedObject(String name, Object object) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Object getSharedObject(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setBusinessDayChecker(BusinessDayChecker checker) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public TestRunResultPublisher getPublisher() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
