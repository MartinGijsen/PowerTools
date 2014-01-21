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
    private final String mFileName;
    private final String mSelectorString;
    private final String mDoneConditionString;
    private final RunTime mRunTime;
            
    public MainModel (String fileName, String selector, String doneCondition, RunTime runTime) {
        super ();
        mFileName            = fileName;
        mSelectorString      = selector;
        mDoneConditionString = doneCondition;
        mRunTime             = runTime;
        mAtNode              = false;
    }

    @Override
    public void initialize () {
        mGraph         = DirectedGraphImpl.createGraph (mFileName);
        mCurrentNode   = mGraph.getRootNode ();
        mDoneCondition = new DoneConditionFactory ().create (mDoneConditionString);
        mSelector      = new EdgeSelectionStrategyFactory ().create (mSelectorString, mGraph, mRunTime, mDoneCondition);

        mPublisher.publishCommentLine ("stop condition: " + mDoneCondition.getDescription ());
        mPublisher.publishCommentLine ("edge selection: " + mSelector.getDescription ());

        finishInit ();
    }
    
    Edge selectEdge () {
        return mSelector.selectEdge (mGraph, mCurrentNode);
    }
}
