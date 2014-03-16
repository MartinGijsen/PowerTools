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

package org.powertools.engine;

import java.util.Date;
import org.powertools.engine.reports.ModelSubscriber;
import org.powertools.engine.reports.TestCaseSubscriber;
import org.powertools.engine.reports.TestLineSubscriber;
import org.powertools.engine.reports.TestResultSubscriber;


public interface TestRunResultPublisher {
    void start (Date dateTime);
    void finish ();
    void reset ();

    // subscribe
    void subscribeToModel (ModelSubscriber subscriber);
    void subscribeToTestCases (TestCaseSubscriber subscriber);
    void subscribeToTestLines (TestLineSubscriber subscriber);
    void subscribeToTestResults (TestResultSubscriber subscriber);

    // test case
    void publishTestCaseBegin (String id, String description);
    void publishTestCaseEnd ();

    void publishTestLine (TestLine testLine);
    void publishCommentLine (String commentLine);
    void publishCommentLine (TestLine commentLine);
    void publishIncreaseLevel ();
    void publishDecreaseLevel ();
    void publishEndOfSection ();
    void publishEndOfTestLine ();

    // results
    void publishError (String message);
    void publishStackTrace (Exception e);
    void publishValueError (String expression, String actualValue, String expectedValue);
    void publishWarning (String message);
    void publishInfo (String message);
    void publishLink (String url);
    void publishValue (String expression, String value);

    // model events
    void publishNewState (String name);
    void publishNewTransition (String sourceName, String targetName);
    void publishAtState (String name);
    void publishAtTransition (String sourceName, String targetName);
}
