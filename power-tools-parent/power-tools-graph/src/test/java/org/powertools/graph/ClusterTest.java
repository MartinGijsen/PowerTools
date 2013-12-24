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
	public void testSetLabelGetLabel () {
		mCluster.setLabel ("some label");
		assertEquals (LABEL, mCluster.getLabel ());
	}

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
	public void testSetFillColourGetFillColour () {
		mCluster.setFillColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mCluster.getFillColour ());
	}

	@Test
	public void testSetTextColourGetTextColour () {
		mCluster.setTextColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mCluster.getTextColour ());
	}

	@Test
	public void testSetFontNameGetFontName () {
		mCluster.setFontName ("Arial");
		assertEquals ("Arial", mCluster.getFontName ());
	}

	@Test
	public void testSetFontSizeGetFontSize () {
		mCluster.setFontSize (17);
		assertEquals ("17", mCluster.getFontSize ());
	}

	@Test
	public void testSetDefaultNodeShapeGetDefaultNodeShape() {
		mCluster.setDefaultNodeShape (Shape.CIRCLE);
		assertEquals (Shape.CIRCLE, mCluster.getDefaultNodeShape ());
	}

	@Test
	public void testSetDefaultNodeStyleGetDefaultNodeStyle () {
		mCluster.setDefaultNodeStyle (Style.BOLD);
		assertEquals (Style.BOLD, mCluster.getDefaultNodeStyle ());
	}

	@Test
	public void testSetDefaultNodeLineColourGetDefaultNodeLineColour () {
		mCluster.setDefaultNodeLineColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mCluster.getDefaultNodeLineColour ());
	}

	@Test
	public void testSetDefaultNodeLineWidthGetDefaultNodeLineWidth () {
		mCluster.setDefaultNodeLineWidth (3);
		assertEquals ("3", mCluster.getDefaultNodeLineWidth ());
	}

	@Test
	public void testSetDefaultNodeFillColourGetDefaultNodeFillColour () {
		mCluster.setDefaultNodeFillColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mCluster.getDefaultNodeFillColour ());
	}

	@Test
	public void testSetDefaultNodeTextColourGetDefaultNodeTextColour () {
		mCluster.setDefaultNodeTextColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mCluster.getDefaultNodeTextColour ());
	}

	@Test
	public void testSetDefaultNodeFontNameGetDefaultNodeFontName () {
		mCluster.setDefaultNodeFontName ("Arial");
		assertEquals ("Arial", mCluster.getDefaultNodeFontName ());
	}

	@Test
	public void testSetDefaultNodeFontSizeGetDefaultNodeFontSize () {
		mCluster.setDefaultNodeFontSize ("17");
		assertEquals ("17", mCluster.getDefaultNodeFontSize ());
	}
	
	@Test
	public void testAdd () {
		Node node = new Node ("node name");
		assertTrue (mCluster.getNodes ().isEmpty ());
		mCluster.add (node);
		assertTrue (mCluster.getNodes ().contains (node));
	}	
}
