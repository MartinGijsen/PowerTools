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

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import org.powertools.engine.ExecutionException;
import org.powertools.engine.TestRunResultPublisher;
import org.xml.sax.SAXException;


final class DirectedGraphImpl implements DirectedGraph {
    static final String FILE_EXTENSION = ".graphml";
    
    private final String                      mName;
    private final Map<String, State>          mStates;
    private final Map<State, Set<Transition>> mTransitions;


    DirectedGraphImpl (String name) {
        mName        = removeExtension (name);
        mStates      = new HashMap<String, State> ();
        mTransitions = new HashMap<State, Set<Transition>> ();
    }
    
    private String removeExtension (String fileName) {
        if (fileName.endsWith (FILE_EXTENSION)) {
            return fileName.substring (0, fileName.length () - FILE_EXTENSION.length ());
        } else {
            return fileName;
        }
    }

    public void read (String path, String fileName) {
        try {
            new GraphMLParser ().parse (this, path, fileName);
        } catch (SAXException se) {
            throw new ExecutionException ("SAX exception");
        } catch (FileNotFoundException fnfe) {
            throw new ExecutionException ("file not found: " + mName);
        } catch (IOException ioe) {
            throw new ExecutionException ("error reading file: " + mName);
        }
    }

    public String getName () {
        return mName;
    }
    
    public State addState (String name) {
        if (mStates.containsKey (name)) {
            throw new ExecutionException (String.format ("state name '%s' not unique", name));
        } else {
            State state = new State (name, this);
            mStates.put (name, state);
            return state;
        }
    }

    public State getState (String name) {
        return mStates.get (name);
    }

    public State getStateByLabel (String label) {
        for (State state : mStates.values ()) {
            if (state.mLabel.equalsIgnoreCase (label)) {
                return state;
            }
        }
        return null;
    }

    public State getRootState () {
        // TODO: determine at initialization, during or after validation
        Set<State> states = new HashSet<State> ();
        states.addAll (mStates.values ());
        for (Set<Transition> set : mTransitions.values ()) {
            for (Transition transition : set) {
                states.remove (transition.mTarget);
            }
        }
        int nrOfRoots = states.size();
        switch (nrOfRoots) {
        case 0:
            throw new ExecutionException ("no root state");
        case 1:
            return states.iterator ().next ();
        default:
            throw new ExecutionException ("multiple root states");
        }
    }

    public State getBeginState () {
        // TODO: determine at initialization, during or after validation
        return getStateByLabel (Model.BEGIN_STATE_LABEL);
    }
    
    public Transition addTransition (String sourceName, String targetName) {
        return addTransition (getState (sourceName), getState (targetName));
    }

    public Transition addTransition (State source, State target) {
        Set<Transition> transitions = mTransitions.get (source);
        if (transitions == null) {
            transitions = new HashSet<Transition> ();
            mTransitions.put (source, transitions);
        }

        for (Transition transition : transitions) {
            if (transition.mTarget == target) {
                throw new ExecutionException ("transition already exists");
            }
        }

        Transition transition = new Transition (source, target);
        transitions.add (transition);
        return transition;
    }

    public Transition getTransition (String sourceName, String targetName) {
        return getTransition (getState (sourceName), getState (targetName));
    }

    public Transition getTransition (State source, State target) {
        Set<Transition> transitions = mTransitions.get (source);
        if (transitions != null) {
            for (Transition transition : transitions) {
                if (transition.mTarget == target) {
                    return transition;
                }
            }
        }
        throw new ExecutionException ("transition does not exist");
    }

    public Set<Transition> getTransitions (String sourceName) {
        return getTransitions (getState (sourceName));
    }

    public Set<Transition> getTransitions (State source) {
        Set<Transition> transitions = mTransitions.get (source);
        if (transitions == null) {
            return new HashSet<Transition> ();
        } else {
            return transitions;
        }
    }

    // TODO: move elsewhere so this class does not need the publisher
    void reportStatesAndTransitions (TestRunResultPublisher publisher) {
        for (State state : mStates.values ()) {
            publisher.publishNewState (state.getName ());
        }
        for (Set<Transition> set : mTransitions.values ()) {
            for (Transition transition : set) {
                publisher.publishNewTransition (transition.mSource.getName (), transition.mTarget.getName ());
            }
        }
    }
}
