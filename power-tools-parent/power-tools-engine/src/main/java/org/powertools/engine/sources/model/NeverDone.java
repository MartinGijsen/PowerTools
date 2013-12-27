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


/*
 * The NeverDone condition never indicates that model processing is done.
 * So it can ignore submodels and processed edges.
 */
final class NeverDone extends DoneCondition {
	static final String NAME = "never";

	private static final String DESCRIPTION = "never stop";

	
	NeverDone () {
		super ();
	}
	
	
	@Override
	DoneCondition create (DirectedGraphImpl graph) {
		return new NeverDone ();
	}

	
	@Override
	void addSubModelGraph (DirectedGraphImpl graph) {
		// empty
	}

	@Override
	String getDescription () {
		return DESCRIPTION;
	}

	@Override
	void markEdge (Edge edge) {
		// empty
	}

	@Override
	void check () {
		// empty
	}
}