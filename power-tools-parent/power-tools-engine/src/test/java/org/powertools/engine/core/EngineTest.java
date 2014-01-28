/*	Copyright 2013-2014 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.sources.TestLineImpl;
import org.powertools.engine.sources.TestSource;


public class EngineTest {
//	@BeforeClass
//	public static void setUpClass() {
//	}
//	
//	@AfterClass
//	public static void tearDownClass() {
//	}
//	
//	@Before
//	public void setUp() {
//	}
//	
//	@After
//	public void tearDown() {
//	}

//	@Test
//	public void testRun_String () {
//		try {
//			Engine engine = new EngineImpl ();
//			engine.run ("something");
//			fail ("no exception");
//		} catch (ExecutionException ee) {
//			assertTrue (ee.getMessage ().contains ("not supported"));
//		}
//	}

//	@Test
//	public void testRun_TestSource () {
//		TestSourceImpl source = new TestSourceImpl ();
//		source.add (new TestLineImpl (new String[] { "x", "y" }));
//		Engine engine = new EngineImpl ();
//		engine.run (source);
//	}

//	@Test
//	public void testRun_0args () {
//		Engine engine = new EngineImpl ();
//		engine.run ();
//		fail("The test case is a prototype.");
//	}
//
//	@Test
//	public void testAddProcedure () {
//		Procedure procedure = new Procedure ("procedure name");
//		Engine engine = new EngineImpl ();
//		engine.addProcedure (procedure);
//		assert ();
//	}
//
//	@Test
//	public void testEvaluateTestLine() {
//		System.out.println("evaluateTestLine");
//		Engine instance = null;
//		boolean expResult = false;
//		boolean result = instance.evaluateTestLine();
//		assertEquals(expResult, result);
//		fail("The test case is a prototype.");
//	}

//	private class EngineImpl extends Engine {
//		public EngineImpl () {
//			super (null);
//		}
//	}
	
//	private class MyRunTimeImpl implements RunTime {
//		MyRunTimeImpl () {
//			super ();
//		}
//
//		public Context getContext() {
//			return new Context ("");
//		}
//
//		public void reportValueError(String expression, String actualValue, String expectedValue) {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public void reportError(String message) {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public void reportStackTrace(Exception e) {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public void reportWarning(String message) {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public void reportValue(String expression, String value) {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public void reportInfo(String message) {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public void reportLink(String url) {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public Scope getGlobalScope() {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public Scope getCurrentScope() {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public Symbol getSymbol(String name) {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public void setValue(String name, String value) {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public void copyStructure(String target, String source) {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public void clearStructure(String name) {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public Roles getRoles() {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public boolean enterTestCase(String name, String description) {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public boolean leaveTestCase() {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public boolean addSharedObject(String name, Object object) {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public Object getSharedObject(String name) {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//
//		public void setBusinessDayChecker(BusinessDayChecker checker) {
//			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//		}
//	}
	
	private class TestSourceImpl extends TestSource {
		private final List<TestLineImpl> mTestLines;
		private Iterator<TestLineImpl> mIter;
		
		TestSourceImpl () {
			super (null);
			mTestLines = new LinkedList<TestLineImpl> ();
		}
		
		void add (TestLineImpl testLine) {
			mTestLines.add (testLine);
		}

		@Override
		public void initialize () {
			mIter = mTestLines.iterator ();
		}

		@Override
		public TestLineImpl getTestLine () {
			return mIter.hasNext () ? mIter.next () : null;
		}
	}
}
