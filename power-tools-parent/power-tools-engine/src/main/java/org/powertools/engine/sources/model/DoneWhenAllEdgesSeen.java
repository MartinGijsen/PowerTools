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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


final class DoneWhenAllEdgesSeen implements DoneCondition {
    static final String NAME = "all edges";

    private static final String DESCRIPTION = "stop after all edges have been traversed";

    private final Set<String> mUnseenEdges;
    
    private boolean mDone;


    DoneWhenAllEdgesSeen (DirectedGraphImpl graph) {
        super ();
        mUnseenEdges = new HashSet<String> ();
        mDone = false;
    }


    public String getDescription () {
        return DESCRIPTION;
    }

    public boolean isSatisfied () {
        return mDone;
    }

    
    // model events
    public void start (Date dateTime) {
        // ignore
    }

    public void finish(Date dateTime) {
        // ignore
    }

    public void processNewNode (String name) {
        // ignore
    }

    public void processNewEdge (String sourceNodeName, String targetNodeName) {
        mUnseenEdges.add (sourceNodeName + "/" + targetNodeName);
    }

    public void processAtNode (String name) {
        // ignore
    }

    public void processAtEdge (String sourceNodeName, String targetNodeName) {
        mUnseenEdges.remove (sourceNodeName + "/" + targetNodeName);
        mDone = mDone || mUnseenEdges.isEmpty ();
    }
}
