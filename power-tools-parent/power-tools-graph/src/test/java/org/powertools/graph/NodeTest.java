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


public class NodeTest {
	private final String NAME = "name";
	
	private final Node mNode = new Node (NAME);
	
	
	@Test
	public void testNodeEmptyName () {
		try {
			new Node (null);
			fail ("no exception");
		} catch (GraphException ge) {
			// ok
		}
		try {
			new Node ("");
			fail ("no exception");
		} catch (GraphException ge) {
			// ok
		}
	}

	@Test
	public void testGetName () {
		assertEquals (NAME, mNode.getName ());
	}

	@Test
	public void testSetLabelGetLabel () {
		mNode.setLabel ("some label");
		assertEquals ("some label", mNode.getLabel ());
	}

	@Test
	public void testSetShapeGetShape () {
		mNode.setShape (Shape.CIRCLE);
		assertEquals (Shape.CIRCLE, mNode.getShape ());
	}

	@Test
	public void testSetStyleGetStyle () {
		mNode.setStyle (Style.BOLD);
		assertEquals (Style.BOLD, mNode.getStyle ());
	}

	@Test
	public void testSetLineColourGetLineColour () {
		mNode.setLineColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mNode.getLineColour ());
	}

	@Test
	public void testSetLineWidthGetLineWidth () {
		mNode.setLineWidth (3);
		assertEquals ("3", mNode.getLineWidth ());
	}

	@Test
	public void testSetFillColourGetFillColour () {
		mNode.setFillColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mNode.getFillColour ());
	}

	@Test
	public void testSetTextColourGetTextColour () {
		mNode.setTextColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mNode.getTextColour ());
	}

	@Test
	public void testSetFontNameGetFontName () {
		mNode.setFontName ("Arial");
		assertEquals ("Arial", mNode.getFontName ());
	}

	@Test
	public void testSetFontSizeGetFontSize () {
		mNode.setFontSize (17);
		assertEquals ("17", mNode.getFontSize ());
	}
}
