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


public class AttributeSet2Test {
	private final AttributeSet2Impl mAttributeSet = new AttributeSet2Impl ();
	
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


	private final class AttributeSet2Impl extends AttributeSet2 {
	}
}
