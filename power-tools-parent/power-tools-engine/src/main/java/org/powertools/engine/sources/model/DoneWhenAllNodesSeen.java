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
import org.powertools.engine.reports.TestRunResultPublisher;


final class DoneWhenAllNodesSeen implements DoneCondition {
    static final String NAME = "all nodes";

    private static final String DESCRIPTION = "stop after all nodes have been traversed";

    private boolean mDone;
    private final Set<String> mUnseenNodes;

    DoneWhenAllNodesSeen (DirectedGraphImpl graph) {
        super ();
        mDone        = false;
        mUnseenNodes = new HashSet<String> ();
        
        TestRunResultPublisher.getInstance ().subscribeToModel (this);
    }


    public String getDescription () {
        return DESCRIPTION;
    }

    public boolean isSatisfied () {
        return mDone;
    }

    
    // model events
    public void start(Date dateTime) {
        // ignore
    }

    public void finish(Date dateTime) {
        // ignore
    }

    public void processNewNode (String name) {
        mUnseenNodes.add (name);
    }

    public void processNewEdge (String sourceNodeName, String targetNodeName) {
        // ignore
    }

    public void processAtNode (String name) {
        mUnseenNodes.remove (name);
        mDone = mUnseenNodes.isEmpty ();
    }

    public void processAtEdge (String sourceNodeName, String targetNodeName) {
        // ignore
    }
}
