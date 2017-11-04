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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.RunTime;


final class RandomTransitionSelector implements TransitionSelectionStrategy {
    static final String NAME = "random";
    
    private static final String DESCRIPTION = "select a random outgoing transition";

    private final DirectedGraph mMainGraph;
    private final RunTime mRunTime;
    private final RandomNumberGenerator mNumberGenerator;


    RandomTransitionSelector (DirectedGraph mainGraph, RunTime runTime, RandomNumberGenerator numberGenerator) {
        super ();
        mMainGraph       = mainGraph;
        mRunTime         = runTime;
        mNumberGenerator = numberGenerator;
    }

    @Override
    public String getDescription () {
        return NAME + " (" + DESCRIPTION + ")";
    }

    @Override
    public Transition selectTransition (DirectedGraph graph, State currentState) {
        boolean isMainGraph = graph.equals (mMainGraph);
        boolean atEndState  = false;
        if (isMainGraph) {
            if (currentState.mLabel.equalsIgnoreCase (Model.END_STATE_LABEL)) {
                atEndState = true;
            }
        } else {
            if (graph.getTransitions (currentState).isEmpty ()) {
                return null;
            }
        }

        Set<Transition> remainingTransitions = new HashSet<Transition> (graph.getTransitions (currentState));
        while (!remainingTransitions.isEmpty ()) {
            Transition transition = removeRandomTransition (remainingTransitions);
            if (conditionNoProblem (transition)) {
                return transition;
            }
        }

        if (atEndState) {
            return graph.addTransition (currentState, graph.getBeginState ());
        } else {
            throw new ExecutionException ("no transitions out of end node '%s'", currentState.getName ());
        }
    }

    private boolean conditionNoProblem (Transition transition) {
        if ("".equals (transition.mCondition)) {
            return true;
        } else {
            String value = mRunTime.evaluateExpression (transition.mCondition).getValue ();
            mRunTime.reportInfo (String.format ("condition '%s' is %s", transition.mCondition, value));
            return "true".equals (value);
        }
    }

    private Transition removeRandomTransition (Set<Transition> remainingTransitions) {
        Transition transition     = null;
        Iterator<Transition> iter = remainingTransitions.iterator ();
        int number                = mNumberGenerator.generate (remainingTransitions.size ());
        for (int counter = 0; counter <= number; ++counter) {
            transition = iter.next ();
        }
        iter.remove ();
        return transition;
    }
}
