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
import org.powertools.engine.reports.ModelSubscriber;


/*
 * Determines when the model (including any submodels) has been processed
 */
abstract class DoneCondition implements ModelSubscriber {
    boolean mDone;

    private final String mDescription;

    DoneCondition (String description) {
        mDescription = description;
        mDone        = false;
    }
    
    final String getDescription () {
        return mDescription;
    }
    
    boolean isSatisfied () {
        return mDone;
    }

    
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
        // ignore
    }

    public void processAtEdge (String sourceNodeName, String targetNodeName) {
        // ignore
    }
}
