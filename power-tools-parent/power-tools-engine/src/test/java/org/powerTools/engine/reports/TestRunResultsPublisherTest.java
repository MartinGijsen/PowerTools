package org.powerTools.engine.reports;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powerTools.engine.TestLine;


public class TestRunResultsPublisherTest {
	private TestRunResultPublisher mPublisher;
	private Subscriber mTestCasesSubscriber;
	private Subscriber mTestLinesSubscriber;
	private Subscriber mTestResultsSubscriber;

	@Before
	public void setUp () throws Exception {
		mPublisher				= TestRunResultPublisher.getInstance ();
		mTestCasesSubscriber	= new Subscriber ();
		mTestLinesSubscriber	= new Subscriber ();
		mTestResultsSubscriber	= new Subscriber ();
		
		mPublisher.subscribeToTestCases (mTestCasesSubscriber);
		mPublisher.subscribeToTestLines (mTestLinesSubscriber);
		mPublisher.subscribeToTestResults (mTestResultsSubscriber);
	}

	@After
	public void tearDown () throws Exception {
		mPublisher.clear ();
		mPublisher				= null;
		mTestCasesSubscriber	= null;
		mTestLinesSubscriber	= null;
		mTestResultsSubscriber	= null;
	}
	

	@Test
	public void testGetInstance () {
		assertNotNull (mPublisher);
	}

	@Test
	public void testSubscribeToTestCases () {
		final Subscriber subscriber = new Subscriber ();
		mPublisher.publishTestCaseBegin (null, null);
		assertEquals (Subscriber.Method.NONE, subscriber.getLastMethod ());
		mPublisher.start (null);
		assertEquals (Subscriber.Method.NONE, subscriber.getLastMethod ());
		mPublisher.subscribeToTestCases (subscriber);
		mPublisher.publishTestCaseBegin (null, null);
		assertEquals (Subscriber.Method.PROCESS_BEGIN, subscriber.getLastMethod ());
		mPublisher.start (null);
		assertEquals (Subscriber.Method.START, subscriber.getLastMethod ());
	}

	@Test
	public void testSubscribeToTestLines () {
		final Subscriber subscriber = new Subscriber ();
		mPublisher.publishTestLine (null);
		assertEquals (Subscriber.Method.NONE, subscriber.getLastMethod ());
		mPublisher.start (null);
		assertEquals (Subscriber.Method.NONE, subscriber.getLastMethod ());
		mPublisher.subscribeToTestLines (subscriber);
		mPublisher.publishTestLine (null);
		assertEquals (Subscriber.Method.PROCESS_TESTLINE, subscriber.getLastMethod ());
		mPublisher.start (null);
		assertEquals (Subscriber.Method.START, subscriber.getLastMethod ());
	}

	@Test
	public void testSubscribeToTestResults () {
		final Subscriber subscriber = new Subscriber ();
		mPublisher.publishError (null);
		assertEquals (Subscriber.Method.NONE, subscriber.getLastMethod ());
		mPublisher.start (null);
		assertEquals (Subscriber.Method.NONE, subscriber.getLastMethod ());
		mPublisher.subscribeToTestResults (subscriber);
		mPublisher.publishError (null);
		assertEquals (Subscriber.Method.PROCESS_ERROR, subscriber.getLastMethod ());
		mPublisher.start (null);
		assertEquals (Subscriber.Method.START, subscriber.getLastMethod ());
	}

	@Test
	public void testPublishCommentLineString () {
		mPublisher.publishCommentLine ("");
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_COMMENT_LINE_STRING, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishCommentLineITestLine () {
		TestLine testLine = null;
		mPublisher.publishCommentLine (testLine);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_COMMENT_LINE_TESTLINE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishTestLine () {
		mPublisher.publishTestLine (null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_TESTLINE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishEndOfSection () {
		mPublisher.publishEndOfSection ();
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_END_OF_SECTION, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishValueError () {
		mPublisher.publishValueError (null, null, null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_ERROR, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishError () {
		mPublisher.publishError (null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_ERROR, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishStackTrace () {
		mPublisher.subscribeToTestResults (mTestResultsSubscriber);
		mPublisher.publishStackTrace (new Exception ());
		assertEquals (Subscriber.Method.PROCESS_STACK_TRACE, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishWarning () {
		mPublisher.publishWarning (null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_WARNING, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishValue () {
		mPublisher.publishValue (null, null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_INFO, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishInfo () {
		mPublisher.publishInfo (null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_INFO, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishEndOfTestLine () {
		mPublisher.publishEndOfTestLine ();
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_END_OF_TESTLINE, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishIncreaseLevel () {
		mPublisher.publishIncreaseLevel ();
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_INCREASE_LEVEL, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishDecreaseLevel () {
		mPublisher.publishDecreaseLevel ();
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_DECREASE_LEVEL, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishTestCaseBegin () {
		mPublisher.publishTestCaseBegin (null, null);
		assertEquals (Subscriber.Method.PROCESS_BEGIN, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishTestCaseEnd () {
		mPublisher.publishTestCaseEnd ();
		assertEquals (Subscriber.Method.PROCESS_END, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testStart () {
		mPublisher.start (null);
		assertEquals (Subscriber.Method.START, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.START, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.START, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testFinish () {
		mPublisher.finish ();
		assertEquals (Subscriber.Method.FINISH, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.FINISH, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.FINISH, mTestResultsSubscriber.getLastMethod ());
	}
}