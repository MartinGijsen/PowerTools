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
import java.util.Random;
import java.util.Set;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.RunTime;


final class RandomEdgeSelector implements EdgeSelectionStrategy {
    static final String NAME = "random";

    private static final String DESCRIPTION = "select a random outgoing edge";
    private static final Random mRandom      = new Random ();

    private final RunTime mRunTime;


    RandomEdgeSelector (RunTime runTime) {
        super ();
        mRunTime = runTime;
    }

    @Override
    public String getDescription () {
        return NAME + " (" + DESCRIPTION + ")";
    }

    @Override
    public Edge selectEdge (DirectedGraphImpl graph, Node currentNode) {
        Set<Edge> remainingEdges = new HashSet<Edge> (graph.getEdges (currentNode));
        while (!remainingEdges.isEmpty ()) {
            Edge edge = removeRandomEdge (remainingEdges);
            if ("".equals (edge.mCondition) || returnsTrue (edge.mCondition)) {
                return edge;
            }
        }

        if (currentNode.mLabel.equalsIgnoreCase (Model.END_NODE_LABEL)) {
            return graph.addEdge (currentNode, graph.getStartNode ());
        } else {
            throw new ExecutionException (String.format ("no edges out of end node %s", currentNode.getName ()));
        }
    }

    private Edge removeRandomEdge (Set<Edge> remainingEdges) {
        Edge edge           = null;
        Iterator<Edge> iter = remainingEdges.iterator ();
        int number          = mRandom.nextInt (remainingEdges.size ());
        for (int counter = 0; counter <= number; ++counter) {
            edge = iter.next ();
        }
        iter.remove ();
        return edge;
    }

    private boolean returnsTrue (String condition) {
        String value = mRunTime.evaluateExpression (condition);
        mRunTime.reportInfo (String.format ("condition '%s' is %s", condition, value));
        return "true".equals (value);
    }
}
