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

import org.powerTools.engine.core.RunTimeImpl;


final class EdgeSelectionStrategyFactory {
	private final static String RANDOM_EDGE_STRATEGY	= "random";
	private final static String WEIGHTED_EDGE_STRATEGY	= "weighted";


	private EdgeSelectionStrategyFactory () { }

	static EdgeSelectionStrategy create (String selector, RunTimeImpl runTime) {
		if (RANDOM_EDGE_STRATEGY.equals (selector)) {
			return new RandomEdgeSelector (runTime);
		} else if (WEIGHTED_EDGE_STRATEGY.equals (selector)) {
			return new WeightedEdgeSelector ();
		} else {
			throw new RuntimeException (String.format ("unknown edge selection strategy: %s", selector));
		}
	}
}