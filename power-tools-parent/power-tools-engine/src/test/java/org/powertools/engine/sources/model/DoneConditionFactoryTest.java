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


package org.powertools.engine.sources.model;

import java.util.Date;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.TestLine;
import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.reports.ModelSubscriber;
import org.powertools.engine.reports.TestCaseSubscriber;
import org.powertools.engine.reports.TestLineSubscriber;
import org.powertools.engine.reports.TestResultSubscriber;


public class DoneConditionFactoryTest {
    @Test
    public void testCreate () {
        TestRunResultPublisher publisher = new TestRunResultPublisherImpl ();
        DoneConditionFactory factory = new DoneConditionFactory ();
        assertTrue (factory.create ("never", publisher) instanceof NeverDone);
        assertTrue (factory.create ("end state", publisher) instanceof DoneWhenInEndState);
        assertTrue (factory.create ("all states", publisher) instanceof DoneWhenAllStatesSeen);
        assertTrue (factory.create ("all transitions", publisher) instanceof DoneWhenAllTransitionsSeen);
        try {
            factory.create ("unknown condition", publisher);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }
    
    private class TestRunResultPublisherImpl implements TestRunResultPublisher {
        public void start(Date dateTime) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void finish() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void reset() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void subscribeToModel(ModelSubscriber subscriber) {
            // ignore
        }

        public void subscribeToTestCases(TestCaseSubscriber subscriber) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void subscribeToTestLines(TestLineSubscriber subscriber) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void subscribeToTestResults(TestResultSubscriber subscriber) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishTestCaseBegin(String id, String description) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishTestCaseEnd() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishTestLine(TestLine testLine) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishCommentLine(String commentLine) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishCommentLine(TestLine commentLine) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishIncreaseLevel() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishDecreaseLevel() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishEndOfSection() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishEndOfTestLine() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishError(String message) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishStackTrace(Exception e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishValueError(String expression, String actualValue, String expectedValue) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishWarning(String message) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishInfo(String message) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishLink(String url) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishValue(String expression, String value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishNewState(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishNewTransition(String sourceName, String targetName) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishAtState(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void publishAtTransition(String sourceName, String targetName) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
