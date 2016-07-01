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

import java.util.Random;
import java.util.Set;
import org.powertools.engine.ExecutionException;


/*
 * This transition selector uses 1000 as the fixed total weight for all transitions.
 * The outgoing transition together must weigh 1000 if all transition have weights specified.
 * Transitions without a weight weigh: remaining weight / nr of transitions without a weight.
 */
final class WeightedTransitionSelector implements TransitionSelectionStrategy {
    static final String NAME = "weighted";

    private static final String DESCRIPTION = "a higher weight outgoing transition is more likely to be selected";


    WeightedTransitionSelector () {
        super ();
    }

    @Override
    public String getDescription () {
        return NAME + " (" + DESCRIPTION + ")";
    }

    @Override
    public Transition selectTransition (DirectedGraph graph, State currentState) {
        Set<Transition> transitions = graph.getTransitions (currentState);
        if (!transitions.isEmpty ()) {
            return selectTransition (transitions);
        } else {
            throw new ExecutionException ("no transitions out of node %s", currentState.getName ());
        }
    }

    private Transition selectTransition (Set<Transition> transitions) {
        int selectedWeight = new Random ().nextInt (1000);
        int currentWeight  = 0;
        for (Transition transition : transitions) {
            currentWeight += transition.mWeight;
            if (currentWeight > selectedWeight) {
                return transition;
            }
        }
        throw new ExecutionException ("did not find find a transition for weight '%d'", selectedWeight);
    }
}
