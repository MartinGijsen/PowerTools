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


final class RandomEdgeSelector implements EdgeSelectionStrategy {
	public Edge selectEdge (Model model) {
		final Set<Edge> edges = model.mGraph.getEdges (model.mCurrentNode);
		if (!edges.isEmpty ()) {
			Random random = new Random ();
			Edge edge = null;
			final Iterator<Edge> iter = edges.iterator ();
			int number = random.nextInt (edges.size ());
			for (int counter = 0; counter <= number; ++counter) {
				edge = iter.next ();
			}
			return edge;
		} else if (model.mCurrentNode.mLabel.equals (Model.END_NODE_LABEL)) {
			throw new Model.DoneException ();
		} else {
			throw new RuntimeException (String.format ("no edges out of node %s", model.mCurrentNode.mName));
		}
	}
}