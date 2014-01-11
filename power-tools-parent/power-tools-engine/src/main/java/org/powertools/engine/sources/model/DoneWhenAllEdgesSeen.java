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
import java.util.Set;


final class DoneWhenAllEdgesSeen implements DoneCondition {
    static final String NAME = "all edges";

    private static final String DESCRIPTION = "stop after all edges have been traversed";

    private final Set<Edge> mUnseenEdges;
    
    private boolean mDone;


    DoneWhenAllEdgesSeen (DirectedGraphImpl graph) {
        super ();
        mUnseenEdges = new HashSet<Edge> ();
        for (Set<Edge> edges : graph.mEdges.values ()) {
            mUnseenEdges.addAll (edges);
        }
        mDone = false;
    }


    public String getDescription () {
        return DESCRIPTION;
    }

    public void addSubModelGraph (DirectedGraphImpl graph) {
        for (Set<Edge> edges : graph.mEdges.values ()) {
            mUnseenEdges.addAll (edges);
        }
    }

    public void markEdge (Edge edge) {
        mUnseenEdges.remove (edge);
        mDone = mUnseenEdges.isEmpty ();
    }

    public boolean isSatisfied () {
        return mDone;
    }
}
