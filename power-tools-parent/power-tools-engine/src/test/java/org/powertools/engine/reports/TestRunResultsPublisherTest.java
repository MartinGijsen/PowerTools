/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools.
 *
 * The PowerTools are free software: you can redistribute them and/or
 * modify them under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools are distributed in the hope that they will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine.reports;

import org.powertools.engine.TestRunResultPublisher;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powertools.engine.TestLine;


public class TestRunResultsPublisherTest {
	private TestRunResultPublisher mPublisher;
	private Subscriber mTestCasesSubscriber;
	private Subscriber mTestLinesSubscriber;
	private Subscriber mTestResultsSubscriber;
	private Subscriber mModelSubscriber;

	@Before
	public void setUp () throws Exception {
		mPublisher				= TestRunResultPublisherImpl.getInstance ();
		mTestCasesSubscriber	= new Subscriber ();
		mTestLinesSubscriber	= new Subscriber ();
		mTestResultsSubscriber	= new Subscriber ();
		mModelSubscriber        = new Subscriber ();
		
		mPublisher.subscribeToTestCases (mTestCasesSubscriber);
		mPublisher.subscribeToTestLines (mTestLinesSubscriber);
		mPublisher.subscribeToTestResults (mTestResultsSubscriber);
		mPublisher.subscribeToModel (mModelSubscriber);
	}

	@After
	public void tearDown () throws Exception {
		mPublisher.finish ();
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
	public void testNoSubscribeToTestCases () {
		Subscriber subscriber = new Subscriber ();
		mPublisher.publishTestCaseBegin (null, null);
		assertEquals (Subscriber.Method.NONE, subscriber.getLastMethod ());
	}

	@Test
	public void testSubscribeToTestCases () {
		Subscriber subscriber = new Subscriber ();
		mPublisher.subscribeToTestCases (subscriber);
		mPublisher.publishTestCaseBegin (null, null);
		assertEquals (Subscriber.Method.PROCESS_BEGIN, subscriber.getLastMethod ());
	}

	@Test
	public void testDoubleSubscribeToTestCases () {
		Subscriber subscriber = new Subscriber ();
		mPublisher.subscribeToTestCases (subscriber);
		mPublisher.subscribeToTestCases (subscriber);
		mPublisher.subscribeToTestCases (null);
		mPublisher.publishTestCaseBegin (null, null);
		assertEquals (Subscriber.Method.PROCESS_BEGIN, subscriber.getLastMethod ());
	}

	@Test
	public void testNoSubscribeToTestLines () {
		Subscriber subscriber = new Subscriber ();
		mPublisher.publishTestLine (null);
		assertEquals (Subscriber.Method.NONE, subscriber.getLastMethod ());
	}

	@Test
	public void testSubscribeToTestLines () {
		Subscriber subscriber = new Subscriber ();
		mPublisher.subscribeToTestLines (subscriber);
		mPublisher.publishTestLine (null);
		assertEquals (Subscriber.Method.PROCESS_TESTLINE, subscriber.getLastMethod ());
	}

	@Test
	public void testDoubleSubscribeToTestLines () {
		Subscriber subscriber = new Subscriber ();
		mPublisher.subscribeToTestLines (subscriber);
		mPublisher.subscribeToTestLines (subscriber);
		mPublisher.subscribeToTestLines (null);
		mPublisher.publishTestLine (null);
		assertEquals (Subscriber.Method.PROCESS_TESTLINE, subscriber.getLastMethod ());
	}

	@Test
	public void testNoSubscribeToTestResults () {
		Subscriber subscriber = new Subscriber ();
		mPublisher.publishError (null);
		assertEquals (Subscriber.Method.NONE, subscriber.getLastMethod ());
	}

	@Test
	public void testSubscribeToTestResults () {
		Subscriber subscriber = new Subscriber ();
		mPublisher.subscribeToTestResults (subscriber);
		mPublisher.publishError (null);
		assertEquals (Subscriber.Method.PROCESS_ERROR, subscriber.getLastMethod ());
	}

	@Test
	public void testDoubleSubscribeToTestResults () {
		Subscriber subscriber = new Subscriber ();
		mPublisher.subscribeToTestResults (subscriber);
		mPublisher.subscribeToTestResults (subscriber);
		mPublisher.subscribeToTestResults (null);
		mPublisher.publishError (null);
		assertEquals (Subscriber.Method.PROCESS_ERROR, subscriber.getLastMethod ());
	}

	@Test
	public void testNoSubscribeToModel () {
		Subscriber subscriber = new Subscriber ();
		mPublisher.publishNewEdge (null, null);
		assertEquals (Subscriber.Method.NONE, subscriber.getLastMethod ());
	}

	@Test
	public void testSubscribeToModel () {
		Subscriber subscriber = new Subscriber ();
		mPublisher.subscribeToModel (subscriber);
		mPublisher.publishAtNode (null);
		assertEquals (Subscriber.Method.PROCESS_AT_NODE, subscriber.getLastMethod ());
	}

	@Test
	public void testDoubleSubscribeToModel () {
		Subscriber subscriber = new Subscriber ();
		mPublisher.subscribeToModel (subscriber);
		mPublisher.subscribeToModel (subscriber);
		mPublisher.subscribeToModel (null);
		mPublisher.publishAtEdge (null, null);
		assertEquals (Subscriber.Method.PROCESS_AT_EDGE, subscriber.getLastMethod ());
	}

	@Test
	public void testPublishCommentLineString () {
		mPublisher.publishCommentLine ("");
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_COMMENT_LINE_STRING, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishCommentLineITestLine () {
		TestLine testLine = null;
		mPublisher.publishCommentLine (testLine);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_COMMENT_LINE_TESTLINE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishTestLine () {
		mPublisher.publishTestLine (null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_TESTLINE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishEndOfSection () {
		mPublisher.publishEndOfSection ();
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_END_OF_SECTION, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishValueError () {
		mPublisher.publishValueError (null, null, null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_ERROR, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishError () {
		mPublisher.publishError (null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_ERROR, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishStackTrace () {
		mPublisher.subscribeToTestResults (mTestResultsSubscriber);
		StackTraceElement[] stackTrace = new StackTraceElement[1];
		stackTrace[0] = new StackTraceElement ("", "", "", 0);
		Exception e = new Exception ();
		e.setStackTrace (stackTrace);
		mPublisher.publishStackTrace (e);
		assertEquals (Subscriber.Method.PROCESS_STACK_TRACE, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishStackTraceNoLineNr () {
		mPublisher.subscribeToTestResults (mTestResultsSubscriber);
		StackTraceElement[] stackTrace = new StackTraceElement[1];
		stackTrace[0] = new StackTraceElement ("", "", "", -1);
		Exception e = new Exception ();
		e.setStackTrace (stackTrace);
		mPublisher.publishStackTrace (e);
		assertEquals (Subscriber.Method.PROCESS_STACK_TRACE, mTestResultsSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishWarning () {
		mPublisher.publishWarning (null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_WARNING, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishValue () {
		mPublisher.publishValue (null, null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_INFO, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishInfo () {
		mPublisher.publishInfo (null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_INFO, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishLink () {
		mPublisher.publishLink (null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_LINK, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishEndOfTestLine () {
		mPublisher.publishEndOfTestLine ();
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_END_OF_TESTLINE, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishIncreaseLevel () {
		mPublisher.publishIncreaseLevel ();
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_INCREASE_LEVEL, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishDecreaseLevel () {
		mPublisher.publishDecreaseLevel ();
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_DECREASE_LEVEL, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishTestCaseBegin () {
		mPublisher.publishTestCaseBegin (null, null);
		assertEquals (Subscriber.Method.PROCESS_BEGIN, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishTestCaseEnd () {
		mPublisher.publishTestCaseEnd ();
		assertEquals (Subscriber.Method.PROCESS_END, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishNewNode () {
		mPublisher.publishNewNode (null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_NEW_NODE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishNewEdge () {
		mPublisher.publishNewEdge (null, null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_NEW_EDGE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishAtNode () {
		mPublisher.publishAtNode (null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_AT_NODE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testPublishAtEdge () {
		mPublisher.publishAtEdge (null, null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.PROCESS_AT_EDGE, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testStart () {
		mPublisher.start (null);
		assertEquals (Subscriber.Method.START, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.START, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.START, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.START, mModelSubscriber.getLastMethod ());
	}

	@Test
	public void testFinish () {
		mPublisher.finish ();
		assertEquals (Subscriber.Method.FINISH, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.FINISH, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.FINISH, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.FINISH, mModelSubscriber.getLastMethod ());
	}

    @Test
	public void testReset () {
		mPublisher.reset ();
		mPublisher.start (null);
		assertEquals (Subscriber.Method.NONE, mTestCasesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestLinesSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mTestResultsSubscriber.getLastMethod ());
		assertEquals (Subscriber.Method.NONE, mModelSubscriber.getLastMethod ());
	}
}