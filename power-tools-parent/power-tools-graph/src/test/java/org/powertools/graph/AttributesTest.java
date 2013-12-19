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

public class AttributesTest {
	@Test
	public void testSomeMethod() {
		Attributes attributes = new Attributes ();
		assertEquals (attributes.label, "");
		assertEquals (attributes.shape, Shape.DEFAULT);
		assertEquals (attributes.style, Style.DEFAULT);
		assertEquals (attributes.lineColour, Colour.DEFAULT);
		assertEquals (attributes.lineWidth, "");
		assertEquals (attributes.fillColour, Colour.DEFAULT);
		assertEquals (attributes.textColour, Colour.DEFAULT);
		assertEquals (attributes.fontName, "");
		assertEquals (attributes.fontSize, "");
	}
}
