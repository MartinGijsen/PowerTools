/* Copyright 2013 by Martin Gijsen (www.DeAnalist.nl)
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
import java.util.Set;


final class DoneWhenAllNodesSeen extends DoneCondition {
    static final String NAME = "all nodes";

    private static final String DESCRIPTION = "stop after all nodes have been traversed";

    private boolean mDone;
    private final Set<Node> mUnseenNodes;

    DoneWhenAllNodesSeen (DirectedGraphImpl graph) {
        super ();
        mDone        = false;
        mUnseenNodes = new HashSet<Node> (graph.mNodes.values ());
        mUnseenNodes.remove (graph.getRoot ());
    }


    @Override
    DoneCondition create (DirectedGraphImpl graph) {
        return new DoneWhenAllNodesSeen (graph);
    }


    @Override
    void addSubModelGraph (DirectedGraphImpl graph) {
        mUnseenNodes.addAll (graph.mNodes.values ());
    }

    @Override
    String getDescription () {
        return DESCRIPTION;
    }

    @Override
    void markEdge (Edge edge) {
        mUnseenNodes.remove (edge.mTarget);
        mDone = mUnseenNodes.isEmpty ();
    }

    @Override
    void check () {
        if (mDone) {
            throw new DoneException ();
        }
    }
}
