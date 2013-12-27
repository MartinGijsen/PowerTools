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

package org.powertools.graph;

import org.junit.Test;
import static org.junit.Assert.*;


public class ClusterTest {
	private final String LABEL = "some label";
	
	private final Cluster mCluster = new Cluster (LABEL);
	
	
	@Test
	public void testSetStyleGetStyle () {
		mCluster.setStyle (Style.BOLD);
		assertEquals (Style.BOLD, mCluster.getStyle ());
	}

	@Test
	public void testSetLineColourGetLineColour () {
		mCluster.setLineColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mCluster.getLineColour ());
	}

	@Test
	public void testSetLineWidthGetLineWidth () {
		mCluster.setLineWidth (3);
		assertEquals ("3", mCluster.getLineWidth ());
	}

	
	@Test
	public void testAddNodeGetNode () {
		String NODE_NAME = "node name";
		assertTrue (mCluster.getNodes ().isEmpty ());
		mCluster.addNode (new Node ("some name"));
		assertNull (mCluster.getNode (NODE_NAME));
		Node node = new Node (NODE_NAME);
		mCluster.addNode (node);
		assertEquals (node, mCluster.getNode (NODE_NAME));
		assertTrue (mCluster.getNodes ().contains (node));
	}	
}
