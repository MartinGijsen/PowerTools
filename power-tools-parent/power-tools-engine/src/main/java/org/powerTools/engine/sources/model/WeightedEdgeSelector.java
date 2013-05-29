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

import java.lang.RuntimeException;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;


/*
 * This edge selector uses 1000 as the fixed total weight for all edges.
 * The outgoing edges together must weigh 1000 if all edges have weights specified.
 * Edges without a weight weigh: remaining weight / nr of edges without a weight.
 */
final class WeightedEdgeSelector implements EdgeSelectionStrategy {
	final static String NAME = "weighted";

	private final static String DESCRIPTION = "a higher weight outgoing edge is more likely to be selected";


	WeightedEdgeSelector () {
		super ();
	}
	
	@Override
	public String getDescription() {
		return NAME + " (" + DESCRIPTION + ")";
	}

	@Override
	public Edge selectEdge (DirectedGraph graph, Node currentNode) {
		final Set<Edge> edges = graph.getEdges (currentNode);
		if (!edges.isEmpty ()) {
			return selectEdge (edges);
//		} else if (model.mCurrentNode.mLabel.equals (Model.END_NODE_LABEL)) {
//			throw new Model.DoneException ();
		} else {
			throw new RuntimeException (String.format ("no edges out of node %s", currentNode.mName));
		}
	}
	
	private Edge selectEdge (Set<Edge> edges) {
		int selectedWeight	= new Random ().nextInt (1000);
		int currentWeight	= 0;
		for (Edge edge : edges) {
			currentWeight += edge.mWeight;
			if (currentWeight > selectedWeight) {
				return edge;
			}
		}
		throw new RuntimeException ("did not find find an edge for weight " + selectedWeight);
	}
}