/* Copyright 2013-2014 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.sources.model;

import org.powertools.engine.ExecutionException;
import org.powertools.engine.TestRunResultPublisher;


final class DoneConditionFactory {
    DoneCondition create (String conditionName, TestRunResultPublisher publisher) {
        // TODO: pass end state label as parameter?
        DoneCondition condition;
        if ("never".equals (conditionName)) {
            condition = new NeverDone ();
        } else if ("all transitions".equals (conditionName)) {
            condition = new DoneWhenAllTransitionsSeen ();
        } else if ("all states".equals (conditionName)) {
            condition = new DoneWhenAllStatesSeen ();
        } else if ("end state".equals (conditionName)) {
            condition = new DoneWhenInEndState ();
        } else {
            throw new ExecutionException ("unknown condition: %s", conditionName);
        }
        publisher.subscribeToModel (condition);
        return condition;
    }
}
