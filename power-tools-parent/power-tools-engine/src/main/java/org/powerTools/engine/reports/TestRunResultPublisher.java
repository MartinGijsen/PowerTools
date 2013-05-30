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

package org.powerTools.engine.reports;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.powerTools.engine.TestLine;


/*
 * The singleton TestRunResultPublisher passes test run information to any
 * interested parties. It implements the publish/subscriber design pattern.
 */
public final class TestRunResultPublisher {
	private static final TestRunResultPublisher mTheOne = new TestRunResultPublisher ();
	
	private final List<TestLineSubscriber>		mTestLineSubscribers;
	private final List<TestResultSubscriber>	mTestResultSubscribers;
	private final List<TestCaseSubscriber>		mTestCaseSubscribers;
	private final Set<TestSubscriber>			mTestSubscribers;


	private TestRunResultPublisher () {
		mTestLineSubscribers	= new LinkedList<TestLineSubscriber> ();
		mTestResultSubscribers	= new LinkedList<TestResultSubscriber> ();
		mTestCaseSubscribers	= new LinkedList<TestCaseSubscriber> ();
		mTestSubscribers		= new HashSet<TestSubscriber> ();
	}

	public static TestRunResultPublisher getInstance () {
		return mTheOne;
	}


	// subscribe methods for several kinds of events
	public void subscribeToTestCases (TestCaseSubscriber subscriber) {
		if (subscriber != null) {
			subscribeToTestCaseEvents (subscriber);
			subscribeToTestEvents (subscriber);
		}
	}

	public void subscribeToTestLines (TestLineSubscriber subscriber) {
		if (subscriber != null) {
			subscribeToTestLineEvents (subscriber);
			subscribeToTestEvents (subscriber);
		}
	}
	
	public void subscribeToTestResults (TestResultSubscriber subscriber) {
		if (subscriber != null) {
			subscribeToTestResultEvents (subscriber);
			subscribeToTestEvents (subscriber);
		}
	}
	
	
	// publish methods
	public void publishCommentLine (String commentLine) {
		for (TestLineSubscriber subscriber : mTestLineSubscribers) {
			subscriber.processCommentLine (commentLine);
		}
	}

	public void publishCommentLine (TestLine commentLine) {
		for (TestLineSubscriber subscriber : mTestLineSubscribers) {
			subscriber.processCommentLine (commentLine);
		}
	}
	
	public void publishTestLine (TestLine testLine) {
		for (TestLineSubscriber subscriber : mTestLineSubscribers) {
			subscriber.processTestLine (testLine);
		}
	}

	public void publishEndOfSection () {
		for (TestLineSubscriber subscriber : mTestLineSubscribers) {
			subscriber.processEndSection ();
		}
	}

	public void publishValueError (String expression, String actualValue, String expectedValue) {
		final String message = "value of '" + expression + "' is '" + actualValue + "' (expected '" + expectedValue + "')";
		for (TestResultSubscriber subscriber : mTestResultSubscribers) {
			subscriber.processError (message);
		}
	}

	public void publishError (String message) {
		if (mTestResultSubscribers.isEmpty ()) {
			System.err.println ("error: " + message);
		} else {
			for (TestResultSubscriber subscriber : mTestResultSubscribers) {
				subscriber.processError (message);
			}
		}
	}

	public void publishStackTrace (Exception e) {
		final String[] lines = createStackTraceLines (e);
		for (TestResultSubscriber subscriber : mTestResultSubscribers) {
			subscriber.processStackTrace (lines);
		}
	}

	public void publishWarning (String message) {
		for (TestResultSubscriber subscriber : mTestResultSubscribers) {
			subscriber.processWarning (message);
		}
	}

	public void publishValue (String expression, String value) {
		final String message = String.format ("value of '%s' is '%s'", expression, value);
		for (TestResultSubscriber subscriber : mTestResultSubscribers) {
			subscriber.processInfo (message);
		}
	}

	public void publishInfo (String message) {
		for (TestResultSubscriber subscriber : mTestResultSubscribers) {
			subscriber.processInfo (message);
		}
	}

	public void publishLink (String url) {
		for (TestResultSubscriber subscriber : mTestResultSubscribers) {
			subscriber.processLink (url);
		}
	}

	public void publishEndOfTestLine () {
		for (TestResultSubscriber subscriber : mTestResultSubscribers) {
			subscriber.processEndOfTestLine ();
		}
	}

	public void publishIncreaseLevel () {
		for (TestResultSubscriber subscriber : mTestResultSubscribers) {
			subscriber.processIncreaseLevel ();
		}
	}
	

	public void publishDecreaseLevel () {
		for (TestResultSubscriber subscriber : mTestResultSubscribers) {
			subscriber.processDecreaseLevel ();
		}
	}
	
	public void publishTestCaseBegin (String id, String description) {
		for (TestCaseSubscriber subscriber : mTestCaseSubscribers) {
			subscriber.processBegin (id, description);
		}
	}
	
	public boolean publishTestCaseEnd () {
		boolean ok = true;
		for (TestCaseSubscriber subscriber : mTestCaseSubscribers) {
			ok = subscriber.processEnd () && ok;
		}
		return ok;
	}


	public void start (Date dateTime) {
		for (TestSubscriber subscriber : mTestSubscribers) {
			subscriber.start (dateTime);
		}
	}

	public void finish () {
		final Date dateTime = GregorianCalendar.getInstance ().getTime ();
		for (TestSubscriber subscriber : mTestSubscribers) {
			subscriber.finish (dateTime);
		}
	}


	private void subscribeToTestEvents (TestSubscriber subscriber) {
		if (!mTestSubscribers.contains (subscriber)) {
			mTestSubscribers.add (subscriber);
		}
	}

	private void subscribeToTestCaseEvents (TestCaseSubscriber subscriber) {
		if (!mTestCaseSubscribers.contains (subscriber)) {
			mTestCaseSubscribers.add (subscriber);
		}
	}

	private void subscribeToTestLineEvents (TestLineSubscriber subscriber) {
		if (!mTestLineSubscribers.contains (subscriber)) {
			mTestLineSubscribers.add (subscriber);
		}
	}

	private void subscribeToTestResultEvents (TestResultSubscriber subscriber) {
		if (!mTestResultSubscribers.contains (subscriber)) {
			mTestResultSubscribers.add (subscriber);
		}
	}
	
	private String[] createStackTraceLines (Exception e) {
		final StackTraceElement[] elements	= e.getStackTrace ();
		final int nrOfElements				= elements.length;
		final List<String> lines			= new LinkedList<String> ();
		for (int elementNr = 0; elementNr < nrOfElements; ++elementNr) {
			if (!elementCanBeAdded (lines, elements[elementNr])) {
				break;
			}
		}
		return lines.toArray (new String[0]);
	}
	
	private boolean elementCanBeAdded (List<String> lines, StackTraceElement element) {
		final int lineNr = element.getLineNumber ();
		if (lineNr < 0) {
			return false;
		} else {
			lines.add (element.getClassName () + "." + element.getMethodName () + "() line " + lineNr);
		}
		return true;
	}
	
	public void clear () {
		mTestLineSubscribers.clear ();
		mTestResultSubscribers.clear ();
		mTestCaseSubscribers.clear ();
		mTestSubscribers.clear ();
	}
}