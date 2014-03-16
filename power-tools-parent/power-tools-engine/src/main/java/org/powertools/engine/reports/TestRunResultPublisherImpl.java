/* Copyright 2012-2014 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools engine.
 *
 * The PowerTools engine is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine.reports;

import org.powertools.engine.TestRunResultPublisher;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.powertools.engine.TestLine;


/*
 * The TestRunResultPublisher passes test run information to any
 * interested parties. It implements the publish/subscriber design pattern.
 */
public final class TestRunResultPublisherImpl implements TestRunResultPublisher {
    private static final TestRunResultPublisherImpl mTheOne = new TestRunResultPublisherImpl ();

    private final List<TestLineSubscriber>   mTestLineSubscribers;
    private final List<TestResultSubscriber> mTestResultSubscribers;
    private final List<TestCaseSubscriber>   mTestCaseSubscribers;
    private final List<ModelSubscriber>      mModelSubscribers;
    private final Set<TestSubscriber>        mTestSubscribers;


    public TestRunResultPublisherImpl () {
        mTestLineSubscribers   = new LinkedList<TestLineSubscriber> ();
        mTestResultSubscribers = new LinkedList<TestResultSubscriber> ();
        mTestCaseSubscribers   = new LinkedList<TestCaseSubscriber> ();
        mModelSubscribers      = new LinkedList<ModelSubscriber> ();
        mTestSubscribers       = new HashSet<TestSubscriber> ();
    }

    @Deprecated
    public static TestRunResultPublisherImpl getInstance () {
        return mTheOne;
    }


    // subscribe methods for several kinds of events
    @Override
    public void subscribeToTestCases (TestCaseSubscriber subscriber) {
        if (subscriber != null) {
            subscribeToTestCaseEvents (subscriber);
            subscribeToTestEvents (subscriber);
        }
    }

    @Override
    public void subscribeToTestLines (TestLineSubscriber subscriber) {
        if (subscriber != null) {
            subscribeToTestLineEvents (subscriber);
            subscribeToTestEvents (subscriber);
        }
    }

    @Override
    public void subscribeToTestResults (TestResultSubscriber subscriber) {
        if (subscriber != null) {
            subscribeToTestResultEvents (subscriber);
            subscribeToTestEvents (subscriber);
        }
    }

    @Override
    public void subscribeToModel (ModelSubscriber subscriber) {
        if (subscriber != null) {
            subscribeToModelEvents (subscriber);
            subscribeToTestEvents (subscriber);
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

    private void subscribeToModelEvents (ModelSubscriber subscriber) {
        if (!mModelSubscribers.contains (subscriber)) {
            mModelSubscribers.add (subscriber);
        }
    }

    
    // publish methods
    @Override
    public void publishCommentLine (String commentLine) {
        for (TestLineSubscriber subscriber : mTestLineSubscribers) {
            subscriber.processCommentLine (commentLine);
        }
    }

    @Override
    public void publishCommentLine (TestLine commentLine) {
        for (TestLineSubscriber subscriber : mTestLineSubscribers) {
            subscriber.processCommentLine (commentLine);
        }
    }

    @Override
    public void publishTestLine (TestLine testLine) {
        for (TestLineSubscriber subscriber : mTestLineSubscribers) {
            subscriber.processTestLine (testLine);
        }
    }

    @Override
    public void publishEndOfSection () {
        for (TestLineSubscriber subscriber : mTestLineSubscribers) {
            subscriber.processEndSection ();
        }
    }

    @Override
    public void publishValueError (String expression, String actualValue, String expectedValue) {
        String message = "value of '" + expression + "' is '" + actualValue + "' (expected '" + expectedValue + "')";
        for (TestResultSubscriber subscriber : mTestResultSubscribers) {
            subscriber.processError (message);
        }
    }

    @Override
    public void publishError (String message) {
        if (mTestResultSubscribers.isEmpty ()) {
            System.err.println ("error: " + message);
        } else {
            for (TestResultSubscriber subscriber : mTestResultSubscribers) {
                subscriber.processError (message);
            }
        }
    }

    @Override
    public void publishStackTrace (Exception e) {
        String[] lines = createStackTraceLines (e);
        for (TestResultSubscriber subscriber : mTestResultSubscribers) {
            subscriber.processStackTrace (lines);
        }
    }

    private String[] createStackTraceLines (Exception e) {
        StackTraceElement[] elements = e.getStackTrace ();
        int nrOfElements             = elements.length;
        List<String> lines           = new LinkedList<String> ();
        for (int elementNr = 0; elementNr < nrOfElements; ++elementNr) {
            if (!addElement (lines, elements[elementNr])) {
                break;
            }
        }
        return lines.toArray (new String[0]);
    }

    private boolean addElement (List<String> lines, StackTraceElement element) {
        int lineNr = element.getLineNumber ();
        if (lineNr < 0) {
            return false;
        } else {
            lines.add (element.getClassName () + "." + element.getMethodName () + "() line " + lineNr);
            return true;
        }
    }

    @Override
    public void publishWarning (String message) {
        for (TestResultSubscriber subscriber : mTestResultSubscribers) {
            subscriber.processWarning (message);
        }
    }

    @Override
    public void publishValue (String expression, String value) {
        String message = String.format ("value of '%s' is '%s'", expression, value);
        for (TestResultSubscriber subscriber : mTestResultSubscribers) {
            subscriber.processInfo (message);
        }
    }

    @Override
    public void publishInfo (String message) {
        for (TestResultSubscriber subscriber : mTestResultSubscribers) {
            subscriber.processInfo (message);
        }
    }

    @Override
    public void publishLink (String url) {
        for (TestResultSubscriber subscriber : mTestResultSubscribers) {
            subscriber.processLink (url);
        }
    }

    @Override
    public void publishEndOfTestLine () {
        for (TestResultSubscriber subscriber : mTestResultSubscribers) {
            subscriber.processEndOfTestLine ();
        }
    }

    @Override
    public void publishIncreaseLevel () {
        for (TestResultSubscriber subscriber : mTestResultSubscribers) {
            subscriber.processIncreaseLevel ();
        }
    }


    @Override
    public void publishDecreaseLevel () {
        for (TestResultSubscriber subscriber : mTestResultSubscribers) {
            subscriber.processDecreaseLevel ();
        }
    }

    @Override
    public void publishTestCaseBegin (String id, String description) {
        for (TestCaseSubscriber subscriber : mTestCaseSubscribers) {
            subscriber.processBegin (id, description);
        }
    }

    @Override
    public void publishTestCaseEnd () {
        for (TestCaseSubscriber subscriber : mTestCaseSubscribers) {
            subscriber.processEnd ();
        }
    }

    
    @Override
    public void publishNewState (String name) {
        for (ModelSubscriber subscriber : mModelSubscribers) {
            subscriber.processNewState (name);
        }
    }
    
    @Override
    public void publishNewTransition (String sourceName, String targetName) {
        for (ModelSubscriber subscriber : mModelSubscribers) {
            subscriber.processNewTransition (sourceName, targetName);
        }
    }
    
    @Override
    public void publishAtState (String name) {
        for (ModelSubscriber subscriber : mModelSubscribers) {
            subscriber.processAtState (name);
        }
    }

    @Override
    public void publishAtTransition (String sourceName, String targetName) {
        for (ModelSubscriber subscriber : mModelSubscribers) {
            subscriber.processAtTransition (sourceName, targetName);
        }
    }


    @Override
    public void start (Date dateTime) {
        for (TestSubscriber subscriber : mTestSubscribers) {
            subscriber.start (dateTime);
        }
    }

    @Override
    public void finish () {
        Date dateTime = GregorianCalendar.getInstance ().getTime ();
        for (TestSubscriber subscriber : mTestSubscribers) {
            subscriber.finish (dateTime);
        }
    }
    
    @Override
    public void reset () {
        mTestLineSubscribers.clear ();
        mTestResultSubscribers.clear ();
        mTestCaseSubscribers.clear ();
        mModelSubscribers.clear ();
        mTestSubscribers.clear ();
    }
}
