/*	Copyright 2013 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.sources.model;

import org.powertools.engine.ExecutionException;


final class DoneConditionFactory {
	private DoneConditionFactory () {
		// empty
	}

	static DoneCondition create (String conditionName, DirectedGraphImpl graph) {
		// TODO: pass end node label as parameter?
		if (NeverDone.NAME.equals (conditionName)) {
			return new NeverDone ();
		} else if (DoneWhenAllEdgesSeen.NAME.equals (conditionName)) {
			return new DoneWhenAllEdgesSeen (graph);
		} else if (DoneWhenAllNodesSeen.NAME.equals (conditionName)) {
			return new DoneWhenAllNodesSeen (graph);
		} else if (DoneWhenInEndNode.NAME.equals (conditionName)) {
			return new DoneWhenInEndNode (Model.END_NODE_LABEL);
		} else {
			throw new ExecutionException (String.format ("unknown condition: %s", conditionName));
		}
	}
}
