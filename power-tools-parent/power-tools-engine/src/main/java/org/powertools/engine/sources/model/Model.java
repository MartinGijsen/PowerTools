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

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.powertools.engine.RunTime;
import org.powertools.engine.reports.TestRunResultPublisher;


/*
 * A Model is read from a file and contains nodes and the edges between them.
 * It generates a sequence of edges.
 * It starts at the start node and then travels from node to node.
 * It selects the next edge using the specified edge selection strategy.
 * The done condition will generate an exception when it is satisfied,
 * but this is only passed on to the test source once in a final state,
 * because an exception could break off current processing.
 */
public final class Model {
    static final String START_NODE_LABEL       = "start";
    static final String END_NODE_LABEL         = "end";
    static final String SUBMODEL_ACTION_PREFIX = "submodel ";
	
    private final TestRunResultPublisher         mPublisher;

    private final EdgeSelectionStrategy          mSelector;
    private final DoneCondition                  mDoneCondition;
    private final Stack<ActiveGraph>             mGraphStack;
    private final Map<String, DirectedGraphImpl> mSubModels;

    private boolean                              mDoneConditionSatisfied;


    public Model (String name, String selector, String doneCondition, RunTime runTime) {
        mPublisher              = TestRunResultPublisher.getInstance ();
        mDoneConditionSatisfied = false;
        mSubModels              = new HashMap<String, DirectedGraphImpl> ();

        mSelector = EdgeSelectionStrategyFactory.create (selector, runTime);
        mPublisher.publishCommentLine ("edge selection: " + mSelector.getDescription ());

        mGraphStack                 = new Stack<ActiveGraph> ();
        DirectedGraphImpl mainGraph = DirectedGraphImpl.createGraph (name);
        ActiveGraph activeGraph     = new ActiveGraph (mainGraph);
        mGraphStack.push (activeGraph);
        mDoneCondition = DoneConditionFactory.create (doneCondition, mainGraph);
        mPublisher.publishCommentLine ("stop condition: " + mDoneCondition.getDescription ());

        mPublisher.publishCommentLine ("start node: " + activeGraph.mCurrentNode.getDescription ());
    }

    public String getNextAction () {
        return getNextEdge ().mAction;
    }

    private Edge getNextEdge () {
        ActiveGraph currentGraph = mGraphStack.peek ();
        Node currentNode         = currentGraph.mCurrentNode;
        if (doneConditionIsSatisfied () && mGraphStack.size () == 1 && currentNode.mLabel.equalsIgnoreCase (END_NODE_LABEL)) {
            throw new DoneException ();
        } else if (mGraphStack.size () != 1 && currentNode.mLabel.equalsIgnoreCase (END_NODE_LABEL)) {
            mGraphStack.pop ();
            currentGraph = mGraphStack.peek ();
        } else if (currentNode.mAction.startsWith (SUBMODEL_ACTION_PREFIX)) {
            int begin        = currentNode.mAction.indexOf ('"') + 1;
            int end          = currentNode.mAction.indexOf ('"', begin);
            String modelName = currentNode.mAction.substring (begin, end);
            currentGraph     = new ActiveGraph (getGraph (modelName));
            mGraphStack.push (currentGraph);
        }

        //TODO: pass ActiveGraph to selector?
        Edge edge                 = mSelector.selectEdge (currentGraph.mGraph, currentGraph.mCurrentNode);
        currentGraph.mCurrentNode = edge.mTarget;
        mDoneCondition.markEdge (edge);
        mPublisher.publishCommentLine ("next node: " + edge.mTarget.getDescription ());
        return edge;
    }

    private boolean doneConditionIsSatisfied () {
        if (!mDoneConditionSatisfied) {
            try {
                mDoneCondition.check ();
            } catch (DoneException de) {
                mDoneConditionSatisfied = true;
                mPublisher.publishCommentLine ("done condition is satisfied");
            }
        }
        return mDoneConditionSatisfied;
    }

    private DirectedGraphImpl getGraph (String name) {
        DirectedGraphImpl subModel = mSubModels.get (name);
        if (subModel == null) {
            subModel = DirectedGraphImpl.createGraph (name);
            mDoneCondition.addSubModelGraph (subModel);
            mSubModels.put (name, subModel);
        }
        return subModel;
    }
}
