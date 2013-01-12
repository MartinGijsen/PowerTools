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


final class DoneConditionFactory {
	private final static String NEVER_DONE_CONDITION	= "never";
	private final static String ALL_EDGES_CONDITION		= "all edges";
	private final static String ALL_NODES_CONDITION		= "all nodes";
	private final static String END_NODE_CONDITION		= "end node";


	private DoneConditionFactory () { }

	static DoneCondition create (String conditionName, DirectedGraph graph) {
		if (NEVER_DONE_CONDITION.equals (conditionName)) {
			return new NeverDone ();
		} else if (ALL_EDGES_CONDITION.equals (conditionName)) {
			return new DoneWhenAllEdgesSeen (graph);
		} else if (ALL_NODES_CONDITION.equals (conditionName)) {
			return new DoneWhenAllNodesSeen (graph);
		} else if (END_NODE_CONDITION.equals (conditionName)) {
			return new DoneWhenInEndNode ();
		} else {
			throw new RuntimeException (String.format ("unknown condition: %s", conditionName));
		}
	}
}