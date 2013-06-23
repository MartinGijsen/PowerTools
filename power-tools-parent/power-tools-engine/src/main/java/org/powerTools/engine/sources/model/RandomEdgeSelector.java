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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.powerTools.engine.core.RunTimeImpl;
import org.powerTools.engine.expression.ExpressionEvaluator;


final class RandomEdgeSelector implements EdgeSelectionStrategy {
	final static String NAME = "random";

	private final static String DESCRIPTION	= "select a random outgoing edge";
	private final static Random mRandom		= new Random ();

	private RunTimeImpl mRunTime;
	
	
	RandomEdgeSelector (RunTimeImpl runTime) {
		super ();
		mRunTime = runTime;
	}

	@Override
	public String getDescription () {
		return NAME + " (" + DESCRIPTION + ")";
	}

	@Override
	public Edge selectEdge (DirectedGraph graph, Node currentNode) {
		final Set<Edge> remainingEdges = new HashSet<Edge> (graph.getEdges (currentNode));
		while (!remainingEdges.isEmpty ()) {
			final Edge edge = removeRandomEdge (remainingEdges);
			if ("".equals (edge.mCondition) || returnsTrue (edge.mCondition)) {
				return edge;
			}
		}

		if (currentNode.mLabel.equals (Model.END_NODE_LABEL)) {
			// TODO prepare at the start
			return graph.addEdge (currentNode, graph.getRoot ());
		} else {
			// TODO check at the start (remove exception?)
			throw new RuntimeException (String.format ("no edges out of node %s", currentNode.mName));
		}
	}

	private Edge removeRandomEdge (Set<Edge> remainingEdges) {
		Edge edge					= null;
		final Iterator<Edge> iter	= remainingEdges.iterator ();
		final int number			= mRandom.nextInt (remainingEdges.size ());
		for (int counter = 0; counter <= number; ++counter) {
			edge = iter.next ();
		}
		iter.remove ();
		return edge;
	}

	private boolean returnsTrue (String condition) {
		String value = ExpressionEvaluator.evaluate (condition, mRunTime.getCurrentScope ());
		return value.equals ("true");
	}
}