/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
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

import org.powertools.engine.RunTime;


public final class MainModel extends Model {
    public MainModel (String path, String fileName, String selector, String doneCondition, RunTime runTime) {
        super (path, fileName, new DoneConditionFactory ().create (doneCondition));
        mSelector = new EdgeSelectionStrategyFactory ().create (selector, mGraph, runTime);
    }

    // TODO: move into constructor?
    @Override
    void reportStopConditionAndSelector () {
        mPublisher.publishCommentLine ("stop condition: " + mDoneCondition.getDescription ());
        mPublisher.publishCommentLine ("edge selection: " + mSelector.getDescription ());
    }

    @Override
    Edge selectEdge () {
        if (mDoneCondition.isSatisfied ()) {
            return null;
        } else {
            return mSelector.selectEdge (mGraph, mCurrentNode);
        }
    }
}
