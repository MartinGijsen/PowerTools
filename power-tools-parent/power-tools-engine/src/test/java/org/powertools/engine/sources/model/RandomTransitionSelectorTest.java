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
import org.powertools.engine.Currencies;
import org.powertools.engine.Functions;
import org.powertools.engine.symbol.Scope;


public class RandomTransitionSelectorTest {
    @Test
    public void testGetDescription () {
        assertTrue (new RandomTransitionSelector (null, null, null).getDescription ().startsWith (RandomTransitionSelector.NAME));
    }

    @Test
    public void testSelectTransition_doneWithSubgraph () {
        DirectedGraph mainGraph     = new DirectedGraphImpl ("main graph");
        DirectedGraph subGraph      = new DirectedGraphImpl ("subgraph");
        State endState              = subGraph.addState ("state name");
        RandomTransitionSelector selector = new RandomTransitionSelector (mainGraph, null, null);
        assertNull (selector.selectTransition (subGraph, endState));
    }
    
    @Test
    public void testSelectTransition_conditionTrue () {
        DirectedGraph mainGraph     = new DirectedGraphImpl ("main graph");
        State state1                = mainGraph.addState ("state 1");
        State state2                = mainGraph.addState ("state 2");
        Transition transition       = mainGraph.addTransition (state1, state2);
        transition.mCondition       = "?true";
        RunTime runTime             = new RunTimeImpl ("true");
        RandomTransitionSelector selector = new RandomTransitionSelector (mainGraph, runTime, new DefaultRandomNumberGenerator ());
        assertEquals (transition, selector.selectTransition (mainGraph, state1));
    }

    @Test
    public void testSelectTransition_conditionFalse () {
        DirectedGraph mainGraph     = new DirectedGraphImpl ("main graph");
        State state1                = mainGraph.addState ("state 1");
        State state2                = mainGraph.addState ("state 2");
        Transition transition       = mainGraph.addTransition (state1, state2);
        transition.mCondition       = "?false";
        RunTime runTime             = new RunTimeImpl ("false");
        RandomTransitionSelector selector = new RandomTransitionSelector (mainGraph, runTime, new DefaultRandomNumberGenerator ());
        try {
            selector.selectTransition (mainGraph, state1);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testSelectTransition_notDoneWithMainGraph () {
        DirectedGraph mainGraph           = new DirectedGraphImpl ("main graph");
        State state1                      = mainGraph.addState ("state 1");
        State state2                      = mainGraph.addState ("state 2");
        State state3                      = mainGraph.addState ("state 3");
        Transition transition1            = mainGraph.addTransition (state1, state2);
        Transition transition2            = mainGraph.addTransition (state1, state3);
        RandomTransitionSelector selector = new RandomTransitionSelector (mainGraph, null, new NumberGeneratorThatReturnsOne ());
        assertNotNull (selector.selectTransition (mainGraph, state1));
    }
    
    @Test
    public void testSelectTransition_notDoneWithSubgraph () {
        DirectedGraph mainGraph           = new DirectedGraphImpl ("main graph");
        DirectedGraph subGraph            = new DirectedGraphImpl ("subgraph");
        State state1                      = subGraph.addState ("state 1");
        State state2                      = subGraph.addState ("state 2");
        State state3                      = subGraph.addState ("state 3");
        Transition transition1            = subGraph.addTransition (state1, state2);
        Transition transition2            = subGraph.addTransition (state1, state3);
        RandomTransitionSelector selector = new RandomTransitionSelector (mainGraph, null, new NumberGeneratorThatReturnsOne ());
        assertNotNull (selector.selectTransition (subGraph, state1));
    }
    
    @Test
    public void testSelectTransition_loopToStartState () {
        DirectedGraph mainGraph           = new DirectedGraphImpl ("main graph");
        State startState                  = mainGraph.addState ("start state");
        State endState                    = mainGraph.addState ("end state");
        endState.mLabel                   = Model.END_STATE_LABEL;
        Transition transition             = mainGraph.addTransition (startState, endState);
        RandomTransitionSelector selector = new RandomTransitionSelector (mainGraph, null, new NumberGeneratorThatReturnsOne ());
        assertNotNull (selector.selectTransition (mainGraph, endState));
    }
    
    @Test
    public void testSelectTransition_notDoneButNoNextState () {
        DirectedGraph mainGraph     = new DirectedGraphImpl ("main graph");
        State startState            = mainGraph.addState ("start state");
        RandomTransitionSelector selector = new RandomTransitionSelector (mainGraph, null, null);
        try {
            selector.selectTransition (mainGraph, startState);
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

        public void enterTestCase(String name, String description) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void leaveTestCase() {
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
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void addCurrency (String name, int nrOfDecimals) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    
        public void addCurrencyAlias (String alias, String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void removeCurrency (String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Currencies getCurrencies () {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Functions getFunctions() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
