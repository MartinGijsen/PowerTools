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


public class EdgeTest {
	private final String SOURCE_NAME = "source name";
	private final String TARGET_NAME = "target name";
	
	private final Node mSource = new Node (SOURCE_NAME);
	private final Node mTarget = new Node (TARGET_NAME);
	private final Edge mEdge   = new Edge (mSource, mTarget);

	@Test
	public void testGetSourceGetTarget () {
		assertEquals (mSource, mEdge.getSource ());
		assertEquals (mTarget, mEdge.getTarget ());
	}

	@Test
	public void testSetStyleGetStyle () {
		mEdge.setStyle (Style.BOLD);
		assertEquals (Style.BOLD, mEdge.getStyle ());
	}

	@Test
	public void testSetLineColourGetLineColour () {
		mEdge.setLineColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mEdge.getLineColour ());
	}

	@Test
	public void testSetLineWidthGetLineWidth () {
		mEdge.setLineWidth (3);
		assertEquals ("3", mEdge.getLineWidth ());
	}

	@Test
	public void testSetFillColourGetFillColour () {
		mEdge.setFillColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mEdge.getFillColour ());
	}

	@Test
	public void testSetTextColourGetTextColour () {
		mEdge.setTextColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mEdge.getTextColour ());
	}

	@Test
	public void testSetFontNameGetFontName () {
		mEdge.setFontName ("Arial");
		assertEquals ("Arial", mEdge.getFontName ());
	}

	@Test
	public void testSetFontSizeGetFontSize () {
		mEdge.setFontSize (17);
		assertEquals ("17", mEdge.getFontSize ());
	}
}
