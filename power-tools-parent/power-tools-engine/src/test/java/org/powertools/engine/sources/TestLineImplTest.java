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

package org.powertools.engine.sources;

import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;


public class TestLineImplTest {
	@Test
	public void testTestLineImpl () {
		TestLineImpl testLine = new TestLineImpl (new String[] { "something", "something else" });
		assertEquals ("something", testLine.getPart (0));
		assertEquals ("something else", testLine.getPart (1));
	}

	@Test
	public void testSetParts () {
		List<String> list = new LinkedList<String> ();
		list.add ("something");
		list.add ("something else");
		TestLineImpl testLine = new TestLineImpl ();
		testLine.setParts (list);
		assertEquals ("something", testLine.getPart (0));
		assertEquals ("something else", testLine.getPart (1));
	}

	@Test
	public void testGetNrOfParts () {
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (2);
		testLine.setPart (0, "something");
		testLine.setPart (1, "something else");
		assertEquals (2, testLine.getNrOfParts ());
	}

	@Test
	public void testSetPartGetPart () {
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (1);
		testLine.setPart (0, "something");
		assertEquals ("something", testLine.getPart (0));
	}

	@Test
	public void testSetEvaluatedPartGetOriginalPart () {
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (1);
		testLine.setPart (0, "something");
		testLine.setEvaluatedPart (0, "something else");
		assertEquals ("something", testLine.getOriginalPart (0));
		assertEquals ("something else", testLine.getPart (0));
	}

	@Test
	public void testIsEmptyEmpty () {
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (2);
		testLine.setPart (0, "");
		testLine.setPart (1, "");
		assertTrue (testLine.isEmpty ());
	}

	@Test
	public void testIsEmptyNotEmpty () {
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (2);
		testLine.setPart (0, "");
		testLine.setPart (1, "something");
		assertFalse (testLine.isEmpty ());
	}
}
