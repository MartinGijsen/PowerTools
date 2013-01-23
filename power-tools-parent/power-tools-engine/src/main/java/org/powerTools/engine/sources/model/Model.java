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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import org.xml.sax.SAXException;

import org.powerTools.engine.ExecutionException;
import org.powerTools.engine.core.RunTimeImpl;
import org.powerTools.engine.reports.TestRunResultPublisher;


final class Model {
	final static String START_NODE_LABEL	= "start";
	final static String END_NODE_LABEL		= "end";
	
	private final String					mFileName;
	private final EdgeSelectionStrategy		mSelector;
	private final DoneCondition				mDoneCondition;
	private final TestRunResultPublisher	mPublisher;
	private final DirectedGraph				mGraph;
	private final Node						mStartNode;
	
	private Node mCurrentNode;


	Model (String fileName, String selector, String doneCondition, RunTimeImpl runTime) {
		try {
			mFileName		= fileName;
			mPublisher		= TestRunResultPublisher.getInstance ();
			mSelector		= EdgeSelectionStrategyFactory.create (selector, runTime);
			mPublisher.publishCommentLine ("edge selection: " + mSelector.getDescription ());

			mGraph			= new GraphMLParser ().parse (fileName);
			mDoneCondition	= DoneConditionFactory.create (doneCondition, mGraph);
			mPublisher.publishCommentLine ("stop condition: " + mDoneCondition.getDescription ());

			mStartNode		= mGraph.getRoot ();
			mCurrentNode	= mStartNode;
			mPublisher.publishCommentLine ("start node: " + mStartNode.getDescription ());
		} catch (SAXException se) {
			throw new ExecutionException ("SAX exception");
		} catch (FileNotFoundException fnfe) {
			throw new ExecutionException ("file not found: " + fileName);
		} catch (IOException ioe) {
			throw new ExecutionException ("IO exception");
		}
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
		mPublisher.publishCommentLine ("next node: " + edge.mTarget.getDescription ());
		return edge;
	}
}