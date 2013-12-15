/*	Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestLineTest {
	private static final String VALUE1 = "value1";
	private static final String VALUE2 = "value2";
	
	private TestLineImpl mTestLine;
	
	
	@Before
	public void setUp () throws Exception {
		mTestLine = new TestLineImpl ();
	}

	@After
	public void tearDown () throws Exception {
		mTestLine = null;
	}

	
	@Test (expected=NullPointerException.class)
	public void testSetPartsNull () {
		mTestLine.setParts (null);
		mTestLine.getPart (0);
	}

	@Test
	public void testSetPartsEmpty () {
		List<String> list = new LinkedList<String> ();
		mTestLine.setParts (list);
		assertEquals (0, mTestLine.getNrOfParts ());
		assertEquals ("", mTestLine.getPart (0));
	}

	@Test
	public void testSetPartsOnePart () {
		List<String> list = new LinkedList<String> ();
		list.add (VALUE1);
		mTestLine.setParts (list);
		assertEquals (1, mTestLine.getNrOfParts ());
		assertEquals (VALUE1, mTestLine.getPart (0));
		assertEquals ("", mTestLine.getPart (1));
	}

	@Test
	public void testSetPartsTwoParts () {
		List<String> list = new LinkedList<String> ();
		list.add (VALUE1);
		list.add (VALUE2);
		mTestLine.setParts (list);
		assertEquals (2, mTestLine.getNrOfParts ());
		assertEquals (VALUE1, mTestLine.getPart (0));
		assertEquals (VALUE2, mTestLine.getPart (1));
		assertEquals ("", mTestLine.getPart (2));
	}

	@Test
	public void testCreatePartsZero () {
		mTestLine.createParts (0);
		assertEquals ("", mTestLine.getPart (0));
		assertEquals (0, mTestLine.getNrOfParts ());
	}

	@Test
	public void testCreatePartsOne () {
		mTestLine.createParts (1);
		assertEquals ("", mTestLine.getPart (0));
		assertEquals ("", mTestLine.getPart (1));
		assertEquals (1, mTestLine.getNrOfParts ());
	}

	@Test (expected=NullPointerException.class)
	public void testSetPartInvalid () {
		mTestLine.setPart (0, VALUE1);
	}

	@Test
	public void testSetPart () {
		mTestLine.createParts (1);
		mTestLine.setPart (0, VALUE1);
		assertEquals (VALUE1, mTestLine.getPart (0));
	}

	@Test
	public void testGetNrOfPartsZero () {
		mTestLine.createParts (0);
		assertEquals (0, mTestLine.getNrOfParts ());
	}

	@Test
	public void testGetNrOfPartsOne () {
		mTestLine.createParts (1);
		assertEquals (1, mTestLine.getNrOfParts ());
	}

	@Test
	public void testGetNrOfPartsTwo () {
		mTestLine.createParts (2);
		assertEquals (2, mTestLine.getNrOfParts ());
	}

	@Test
	public void testGetPartZero () {
		assertEquals ("", mTestLine.getPart (0));
	}

	@Test
	public void testGetPartOne () {
		mTestLine.createParts (1);
		mTestLine.setPart (0, VALUE1);
		assertEquals (VALUE1, mTestLine.getPart (0));
		assertEquals ("", mTestLine.getPart (1));
	}

	@Test
	public void testIsEmptyTrue () {
		assertTrue (new TestLineImpl ().isEmpty ());
	}

	@Test
	public void testIsEmptyFalse () {
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (1);
		testLine.setPart (0, "something");
		assertFalse (testLine.isEmpty ());
	}
}