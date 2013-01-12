/*	Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
 *
 *	This file is part of the PowerTools engine.
 *
 *	The PowerTools engine is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the License,
 *	or (at your option) any later version.
 *
 *	The PowerTools engine is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU Affero General Public License for more details.
 *
 *	You should have received a copy of the GNU Affero General Public License
 *	along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powerTools.engine.sources.model;

import java.util.Set;

import org.powerTools.engine.core.RunTimeImpl;
import org.powerTools.engine.reports.TestRunResultPublisher;


final class Model {
	final static String START_NODE_LABEL	= "start";
	final static String END_NODE_LABEL		= "end";
	
	private final String					mFileName;
	private final EdgeSelectionStrategy		mSelector;
	private final DoneCondition				mDoneCondition;
	private final TestRunResultPublisher	mPublisher;

	private DirectedGraph					mGraph;
	private Node							mStartNode;
	private Node							mCurrentNode;


	Model (String fileName, String selector, String doneCondition, RunTimeImpl runTime) {
		mFileName	= fileName;
		mSelector	= EdgeSelectionStrategyFactory.create (selector, runTime);
		mPublisher	= TestRunResultPublisher.getInstance ();

		DoneCondition tmpCondition = null;
		try {
			mGraph			= new GraphMLParser ().parse (fileName);
			mStartNode		= mGraph.getRoot ();
			mCurrentNode	= mStartNode;
			tmpCondition	= DoneConditionFactory.create (doneCondition, mGraph);
		} catch (Exception e) {
			;
		}
		mDoneCondition = tmpCondition;
	}

	DirectedGraph getGraph () {
		return mGraph;
	}
	
	Node getStartNode () {
		return mStartNode;
	}
	
	Node getCurrentNode () {
		return mCurrentNode;
	}
	
	Edge getNextEdge () {
		mDoneCondition.check ();
		Edge edge		= mSelector.selectEdge (this);
		mCurrentNode	= edge.mTarget;
		mDoneCondition.markEdge (edge);
		mPublisher.publishCommentLine (String.format("next edge: %s -> %s", edge.mSource.mName, edge.mTarget.mName));
		return edge;
	}
}