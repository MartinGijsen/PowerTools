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

import org.powertools.engine.reports.TestRunResultPublisherImpl;


/*
 * A Model is read from a file and contains nodes and the edges between them.
 * It generates a sequence of edges.
 * It starts at the start node and then travels from node to node.
 * It selects the next edge using the specified edge selection strategy.
 * The done condition will generate an exception when it is satisfied,
 * but this is only passed on to the test source once in an end state,
 * because an exception could break off current processing.
 */
public abstract class Model {
    static final String START_NODE_LABEL       = "start";
    static final String END_NODE_LABEL         = "end";
    static final String SUBMODEL_ACTION_PREFIX = "submodel ";

    final Set<String>            mKnownModels;
    final TestRunResultPublisherImpl mPublisher;
    final DoneCondition          mDoneCondition;
    final DirectedGraphImpl      mGraph;
    final String                 mPath;
    final String                 mFileName;

    Node                  mCurrentNode;
    boolean               mAtNode;
    EdgeSelectionStrategy mSelector;


    Model (String path, String fileName, DoneCondition doneCondition) {
        this (path, fileName, doneCondition, new HashSet<String> ());
    }

    Model (String path, String fileName, DoneCondition doneCondition, Set<String> knownModels) {
        mKnownModels   = knownModels;
        mPublisher     = TestRunResultPublisherImpl.getInstance ();
        mGraph         = new DirectedGraphImpl (fileName);
        mPath          = path;
        mFileName      = fileName;
        mDoneCondition = doneCondition;
        mAtNode        = false;
    }

    public final void initialize () {
        mGraph.read (mPath, mFileName);
        mCurrentNode = mGraph.getRootNode ();

        mPublisher.publishCommentLine (String.format ("entering model '%s' at initial node '%s'", mGraph.getName (), mCurrentNode.getDescription ()));
        reportStopConditionAndSelector ();

        // TODO: move to where graph is created?
        if (firstTime (mGraph.getName ())) {
            mGraph.reportNodesAndEdges ();
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
            if (mAtNode) {
                Edge edge = getNextEdge ();
                if (edge == null) {
                    return null;
                } else {
                    mPublisher.publishAtEdge (edge.mSource.getName (), edge.mTarget.getName ());
                    action = edge.mAction;
                }
            } else {
                mPublisher.publishAtNode (mCurrentNode.getName ());
                action = mCurrentNode.mAction;
            }
            mAtNode = !mAtNode;
        } while ("".equals (action));
        return action;
    }

    private Edge getNextEdge () {
        Edge edge = selectEdge ();
        if (edge != null) {
            mCurrentNode = edge.mTarget;
            mPublisher.publishCommentLine ("next node: " + edge.mTarget.getDescription ());
        }
        return edge;
    }

    abstract Edge selectEdge ();
    
    public final void cleanup () {
        mPublisher.publishCommentLine (String.format ("leaving model '%s'", mGraph.getName ()));
    }
}
