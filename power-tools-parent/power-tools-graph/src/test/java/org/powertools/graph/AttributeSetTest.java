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
import static org.junit.Assert.assertEquals;


public class AttributeSetTest {
	private final AttributeSetImpl mAttributeSet = new AttributeSetImpl ();

	
	@Test
	public void testSetLabelGetLabel () {
		mAttributeSet.setLabel ("some label");
		assertEquals ("some label", mAttributeSet.getLabel ());
	}

	
	@Test
	public void testSetStyleGetStyle () {
		mAttributeSet.setStyle (Style.BOLD);
		assertEquals (Style.BOLD, mAttributeSet.getStyle ());
	}

	@Test
	public void testSetLineColourGetLineColour () {
		mAttributeSet.setLineColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mAttributeSet.getLineColour ());
	}

	@Test
	public void testSetLineWidthGetLineWidth () {
		mAttributeSet.setLineWidth (3);
		assertEquals ("3", mAttributeSet.getLineWidth ());
	}

    @Test
	public void testSetFillColourGetFillColour () {
		mAttributeSet.setFillColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mAttributeSet.getFillColour ());
	}

	@Test
	public void testSetTextColourGetTextColour () {
		mAttributeSet.setTextColour (Colour.ALICE_BLUE);
		assertEquals (Colour.ALICE_BLUE, mAttributeSet.getTextColour ());
	}

	@Test
	public void testSetFontNameGetFontName () {
		mAttributeSet.setFontName ("Arial");
		assertEquals ("Arial", mAttributeSet.getFontName ());
	}

	@Test
	public void testSetFontSizeGetFontSize () {
		mAttributeSet.setFontSize (17);
		assertEquals ("17", mAttributeSet.getFontSize ());
	}

	
	private final class AttributeSetImpl extends AttributeSet {
	}
}
