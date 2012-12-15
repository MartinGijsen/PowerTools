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


final class Model {
	final static String START_NODE_LABEL	= "start";
	final static String END_NODE_LABEL		= "end";
	
	DirectedGraph<String> mGraph;
	Node mCurrentNode;

	private final String mFileName;
	private final EdgeSelectionStrategy mStrategy;

	private Node mRoot;


	final static class DoneException extends RuntimeException {
		DoneException () {
			super ();
		}
	}


	Model (String fileName, EdgeSelectionStrategy strategy) {
		mFileName = fileName;
		mStrategy = strategy;
		try {
			GraphMLParser parser	= new GraphMLParser ();
			mGraph					= parser.parse (fileName);
		} catch (Exception e) {
			;
		}
	}

	void initialize () {
		mRoot			= mGraph.getRoot ();
		mCurrentNode	= mRoot;
	}

	Edge getNextEdge () {
		Edge edge		= mStrategy.selectEdge (this);
		mCurrentNode	= edge.mTarget;
		return edge;
	}
}