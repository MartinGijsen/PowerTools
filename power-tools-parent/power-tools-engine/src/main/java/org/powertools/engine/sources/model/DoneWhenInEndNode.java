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


final class DoneWhenInEndNode implements DoneCondition {
    static final String NAME = "end node";

    private static final String DESCRIPTION = "stop after reaching an end node";

    private boolean mDone;


    DoneWhenInEndNode () {
        super ();
        mDone = false;
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
        // ignore
    }

    public void processNewEdge (String sourceNodeName, String targetNodeName) {
        // ignore
    }

    public void processAtNode (String name) {
        if (name.startsWith (Model.END_NODE_LABEL + " (")) {
            mDone = true;
        }
    }

    public void processAtEdge (String sourceNodeName, String targetNodeName) {
        // ignore
    }
}
