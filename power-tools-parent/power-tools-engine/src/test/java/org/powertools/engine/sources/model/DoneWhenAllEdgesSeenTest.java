/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools engine.
 *
 * The PowerTools engine is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine.sources.model;

import org.junit.Test;
import static org.junit.Assert.*;


public class DoneWhenAllEdgesSeenTest {
    @Test
    public void testProcessNewEdgeProcessAtEdge () {
        DoneWhenAllTransitionsSeen condition = new DoneWhenAllTransitionsSeen ();
        assertFalse (condition.isSatisfied ());
        condition.processNewTransition ("node 1", "node 2");
        condition.processNewTransition ("node 2", "node 1");
        assertFalse (condition.isSatisfied ());
        condition.processAtTransition ("node 1", "node 2");
        assertFalse (condition.isSatisfied ());
        condition.processAtTransition ("node 2", "node 1");
        assertTrue (condition.isSatisfied ());
        condition.processAtTransition ("node 1", "node 2");
    }
}
