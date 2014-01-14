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

import org.powertools.engine.RunTime;
import org.powertools.engine.reports.TestRunResultPublisher;


/*
 * A Model is read from a file and contains nodes and the edges between them.
 * It generates a sequence of edges.
 * It starts at the start node and then travels from node to node.
 * It selects the next edge using the specified edge selection strategy.
 * The done condition will generate an exception when it is satisfied,
 * but this is only passed on to the test source once in an end state,
 * because an exception could break off current processing.
 */
public final class Model {
    static final String START_NODE_LABEL       = "start";
    static final String END_NODE_LABEL         = "end";
    static final String SUBMODEL_ACTION_PREFIX = "submodel ";

    private static Set<String>           mKnownModels = new HashSet<String> ();
    
    private final TestRunResultPublisher mPublisher;
    private final Model                  mParent;
    private final boolean                mIsMainModel;

    private DirectedGraphImpl            mGraph;
    private Node                         mCurrentNode;
    private boolean                      mAtNode;
    private EdgeSelectionStrategy        mSelector;
    private DoneCondition                mDoneCondition;
    

    public Model () {
        mPublisher   = TestRunResultPublisher.getInstance ();
        mAtNode      = false;
        mParent      = null;
        mIsMainModel = true;
    }

    public Model (Model parent) {
        mPublisher   = TestRunResultPublisher.getInstance ();
        mAtNode      = false;
        mParent      = parent;
        mIsMainModel = false;
    }

    public void initialize (String name, String selector, String doneCondition, RunTime runTime) {
        mGraph         = DirectedGraphImpl.createGraph (name);
        mDoneCondition = new DoneConditionFactory ().create (doneCondition, mGraph);
        mSelector      = new EdgeSelectionStrategyFactory ().create (selector, runTime, mDoneCondition);
        mCurrentNode   = mGraph.getRootNode ();

        mPublisher.publishCommentLine ("stop condition: " + mDoneCondition.getDescription ());
        mPublisher.publishCommentLine ("edge selection: " + mSelector.getDescription ());
        mPublisher.publishCommentLine ("initial node: " + mCurrentNode.getDescription ());

        // TODO: move to where graph is created?
        if (firstTime (name)) {
            mGraph.reportNodesAndEdges ();
        }
    }
    
    private boolean firstTime (String name) {
        if (mKnownModels.contains (name)) {
            return false;
        } else {
            mKnownModels.add (name);
            return true;
        }
    }

    public void initialize (String name) {
        mGraph         = DirectedGraphImpl.createGraph (name);
        mCurrentNode   = mGraph.getRootNode ();
        mSelector      = mParent.mSelector;
        mDoneCondition = mParent.mDoneCondition;

        // TODO: move to where graph is created?
        if (firstTime (name)) {
            mGraph.reportNodesAndEdges ();
        }
    }
    
    public String getNextAction () {
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
        Edge edge = mSelector.selectEdge (mGraph, mCurrentNode, mIsMainModel);
        if (edge != null) {
            mCurrentNode = edge.mTarget;
            mPublisher.publishCommentLine ("next node: " + edge.mTarget.getDescription ());
        }
        return edge;
    }
}
