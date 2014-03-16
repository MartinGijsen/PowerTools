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

import java.util.HashSet;
import java.util.Set;

import org.powertools.engine.TestRunResultPublisher;


/*
 * A Model is read from a file and contains states and the transitions between them.
 * It generates a sequence of transitions.
 * It starts at the initial state and then travels from state to state.
 * It selects the next transition using the specified transition selection strategy.
 * The done condition will generate an exception when it is satisfied,
 * but this is only passed on to the test source once in an end state,
 * because an exception could break off current processing.
 */
public abstract class Model {
    static final String BEGIN_STATE_LABEL      = "begin";
    static final String END_STATE_LABEL        = "end";
    static final String SUBMODEL_ACTION_PREFIX = "submodel ";

    final Set<String>            mKnownModels;
    final TestRunResultPublisher mPublisher;
    final DoneCondition          mDoneCondition;
    final DirectedGraphImpl      mGraph;
    final String                 mPath;
    final String                 mFileName;

    State                       mCurrentState;
    boolean                     mAtState;
    TransitionSelectionStrategy mSelector;


    Model (String path, String fileName, DoneCondition doneCondition, TestRunResultPublisher publisher) {
        this (path, fileName, doneCondition, new HashSet<String> (), publisher);
    }

    Model (String path, String fileName, DoneCondition doneCondition, Set<String> knownModels, TestRunResultPublisher publisher) {
        mKnownModels   = knownModels;
        mPublisher     = publisher;
        mGraph         = new DirectedGraphImpl (fileName);
        mPath          = path;
        mFileName      = fileName;
        mDoneCondition = doneCondition;
        mAtState        = false;
    }

    public final void initialize () {
        mGraph.read (mPath, mFileName);
        mCurrentState = mGraph.getRootState ();

        mPublisher.publishCommentLine (String.format ("entering model '%s' at initial node '%s'", mGraph.getName (), mCurrentState.getDescription ()));
        reportStopConditionAndSelector ();

        // TODO: move to where graph is created?
        if (firstTime (mGraph.getName ())) {
            mGraph.reportStatesAndTransitions (mPublisher);
        }
    }

    void reportStopConditionAndSelector () {
        // nothing
    }

    private boolean firstTime (String name) {
        if (mKnownModels.contains (name)) {
            return false;
        } else {
            mKnownModels.add (name);
            return true;
        }
    }

    public final String getNextAction () {
        String action;
        do {
            if (mAtState) {
                Transition transition = getNextTransition ();
                if (transition == null) {
                    return null;
                } else {
                    mPublisher.publishAtTransition (transition.mSource.getName (), transition.mTarget.getName ());
                    action = transition.mAction;
                }
            } else {
                mPublisher.publishAtState (mCurrentState.getName ());
                action = mCurrentState.mAction;
            }
            mAtState = !mAtState;
        } while ("".equals (action));
        return action;
    }

    private Transition getNextTransition () {
        Transition transition = selectTransition ();
        if (transition != null) {
            mCurrentState = transition.mTarget;
            mPublisher.publishCommentLine ("next state: " + transition.mTarget.getDescription ());
        }
        return transition;
    }

    abstract Transition selectTransition ();
    
    public final void cleanup () {
        mPublisher.publishCommentLine (String.format ("leaving model '%s'", mGraph.getName ()));
    }
}
