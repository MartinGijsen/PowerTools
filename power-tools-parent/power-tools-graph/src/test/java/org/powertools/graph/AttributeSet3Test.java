/*	Copyright 2013 by Martin Gijsen (www.DeAnalist.nl)
 *
 *	This file is part of the PowerTools.
 *
 *	The PowerTools are free software: you can redistribute them and/or
 *	modify them under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the License,
 *	or (at your option) any later version.
 *
 *	The PowerTools are distributed in the hope that they will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU Affero General Public License for more details.
 *
 *	You should have received a copy of the GNU Affero General Public License
 *	along with the PowerTools. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.graph;

import org.junit.Test;
import static org.junit.Assert.*;


public class AttributeSet3Test {
	private AttributeSet3Impl mAttributeSet = new AttributeSet3Impl ();
	
	@Test
	public void testSetDefaultNodeShapeGetDefaultNodeShape() {
		mAttributeSet.setDefaultNodeShape (Shape.CIRCLE);
		assertEquals (Shape.CIRCLE, mAttributeSet.getDefaultNodeShape ());
	}

	@Test
	public void testSetDefaultNodeStyleGetDefaultNodeStyle () {
		mAttributeSet.setDefaultNodeStyle (Style.BOLD);
		assertEquals (Style.BOLD, mAttributeSet.getDefaultNodeStyle ());
	}

	@Test
	public void testSetDefaultNodeLineColourGetDefaultNodeLineColour () {
		mAttributeSet.setDefaultNodeLineColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mAttributeSet.getDefaultNodeLineColour ());
	}

	@Test
	public void testSetDefaultNodeLineWidthGetDefaultNodeLineWidth () {
		mAttributeSet.setDefaultNodeLineWidth (3);
		assertEquals ("3", mAttributeSet.getDefaultNodeLineWidth ());
	}

	@Test
	public void testSetDefaultNodeFillColourGetDefaultNodeFillColour () {
		mAttributeSet.setDefaultNodeFillColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mAttributeSet.getDefaultNodeFillColour ());
	}

	@Test
	public void testSetDefaultNodeTextColourGetDefaultNodeTextColour () {
		mAttributeSet.setDefaultNodeTextColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mAttributeSet.getDefaultNodeTextColour ());
	}

	@Test
	public void testSetDefaultNodeFontNameGetDefaultNodeFontName () {
		mAttributeSet.setDefaultNodeFontName ("Arial");
		assertEquals ("Arial", mAttributeSet.getDefaultNodeFontName ());
	}

	@Test
	public void testSetDefaultNodeFontSizeGetDefaultNodeFontSize () {
		mAttributeSet.setDefaultNodeFontSize (17);
		assertEquals ("17", mAttributeSet.getDefaultNodeFontSize ());
	}
	
	
	private final class AttributeSet3Impl extends AttributeSet3 {
	}
}
