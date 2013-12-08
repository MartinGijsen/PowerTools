package org.powertools.engine.reports;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powertools.engine.sources.TestLineImpl;


public class ConsoleTest {
	private Printer mPrinter;
	private Console mConsole;
	
	@Before
	public void setUp () throws Exception {
		mPrinter = new Printer ();
		mConsole = new Console (mPrinter);
	}

	@After
	public void tearDown () throws Exception {

	}

	
	@Test
	public void testConsole () {
		final Console console = new Console ();
		assertNotNull (console);
	}

	@Test
	public void testConsolePrintStream () {
		final Console console = new Console (System.out);
		assertNotNull (console);
	}

	@Test
	public void testProcessTestLine () {
		assertFalse (mPrinter.anyInput ());
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (1);
		testLine.setPart (0, "something");
		mConsole.processTestLine (testLine);
		assertTrue (mPrinter.anyInput ());
	}

	@Test
	public void testProcessCommentLineString () {
		assertFalse (mPrinter.anyInput ());
		mConsole.processCommentLine ("");
		assertTrue (mPrinter.anyInput ());
	}

	@Test
	public void testProcessCommentLineITestLine() {
		assertFalse (mPrinter.anyInput ());
		TestLineImpl testLine = new TestLineImpl ();
		testLine.createParts (2);
		testLine.setPart (0, "");
		testLine.setPart (1, "something");
		mConsole.processCommentLine (testLine);
		assertTrue (mPrinter.anyInput ());
	}

	@Test
	public void testProcessStackTrace () {
		assertFalse (mPrinter.anyInput ());
		mConsole.processStackTrace (new String[0]);
		assertTrue (mPrinter.anyInput ());
	}

	@Test
	public void testProcessError() {
		assertFalse (mPrinter.anyInput ());
		mConsole.processError (null);
		assertTrue (mPrinter.anyInput ());
	}

	@Test
	public void testProcessWarning() {
		assertFalse (mPrinter.anyInput ());
		mConsole.processWarning (null);
		assertTrue (mPrinter.anyInput ());
	}

	@Test
	public void testProcessInfo() {
		assertFalse (mPrinter.anyInput ());
		mConsole.processInfo (null);
		assertTrue (mPrinter.anyInput ());
	}

//	@Test
//	public void testProcessEndSection() {
//		assertFalse (mPrinter.anyInput ());
//		mConsole.processEndSection ();
//		assertTrue (mPrinter.anyInput ());
//	}
//
//	@Test
//	public void testProcessIncreaseLevel() {
//		assertFalse (mPrinter.anyInput ());
//		mConsole.processIncreaseLevel ();
//		assertTrue (mPrinter.anyInput ());
//	}
//
//	@Test
//	public void testProcessDecreaseLevel() {
//		assertFalse (mPrinter.anyInput ());
//		mConsole.processDecreaseLevel ();
//		assertTrue (mPrinter.anyInput ());
//	}
//
//	@Test
//	public void testProcessEndOfTestLine() {
//		assertFalse (mPrinter.anyInput ());
//		mConsole.processEndOfTestLine ();
//		assertTrue (mPrinter.anyInput ());
//	}

	@Test
	public void testStart() {
		assertFalse (mPrinter.anyInput ());
		mConsole.start (GregorianCalendar.getInstance ().getTime ());
		assertTrue (mPrinter.anyInput ());
	}

	@Test
	public void testFinish() {
		assertFalse (mPrinter.anyInput ());
		mConsole.finish (GregorianCalendar.getInstance ().getTime ());
		assertTrue (mPrinter.anyInput ());
	}
}