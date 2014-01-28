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
import org.powertools.engine.reports.TestRunResultPublisherImpl;
import org.xml.sax.SAXException;


final class DirectedGraphImpl implements DirectedGraph {
    static final String FILE_EXTENSION = ".graphml";
    
    private final String               mName;
    private final Map<String, Node>    mNodes;
    private final Map<Node, Set<Edge>> mEdges;


    DirectedGraphImpl (String name) {
        mName  = removeExtension (name);
        mNodes = new HashMap<String, Node> ();
        mEdges = new HashMap<Node, Set<Edge>> ();
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
    
    public Node addNode (String name) {
        if (mNodes.containsKey (name)) {
            throw new ExecutionException (String.format ("node name '%s' not unique", name));
        } else {
            Node node = new Node (name, this);
            mNodes.put (name, node);
            return node;
        }
    }

    public Node getNode (String name) {
        return mNodes.get (name);
    }

    public Node getNodeByLabel (String label) {
        for (Node node : mNodes.values ()) {
            if (node.mLabel.equalsIgnoreCase (label)) {
                return node;
            }
        }
        return null;
    }

    public Node getRootNode () {
        // TODO: determine at initialization, during or after validation
        Set<Node> nodes = new HashSet<Node> ();
        nodes.addAll (mNodes.values ());
        for (Set<Edge> set : mEdges.values ()) {
            for (Edge edge : set) {
                nodes.remove (edge.mTarget);
            }
        }
        int nrOfRoots = nodes.size();
        switch (nrOfRoots) {
        case 0:
            throw new ExecutionException ("no root node");
        case 1:
            return nodes.iterator ().next ();
        default:
            throw new ExecutionException ("multiple root nodes");
        }
    }

    public Node getStartNode () {
        // TODO: determine at initialization, during or after validation
        return getNodeByLabel (Model.START_NODE_LABEL);
    }
    
    public Edge addEdge (String sourceName, String targetName) {
        return addEdge (getNode (sourceName), getNode (targetName));
    }

    public Edge addEdge (Node source, Node target) {
        Set<Edge> edges = mEdges.get (source);
        if (edges == null) {
            edges = new HashSet<Edge> ();
            mEdges.put (source, edges);
        }

        for (Edge edge : edges) {
            if (edge.mTarget == target) {
                throw new ExecutionException ("edge already exists");
            }
        }

        Edge edge = new Edge (source, target);
        edges.add (edge);
        return edge;
    }

    public Edge getEdge (String sourceName, String targetName) {
        return getEdge (getNode (sourceName), getNode (targetName));
    }

    public Edge getEdge (Node source, Node target) {
        Set<Edge> edges = mEdges.get (source);
        if (edges != null) {
            for (Edge edge : edges) {
                if (edge.mTarget == target) {
                    return edge;
                }
            }
        }
        throw new ExecutionException ("edge does not exist");
    }

    public Set<Edge> getEdges (String sourceName) {
        return getEdges (getNode (sourceName));
    }

    public Set<Edge> getEdges (Node source) {
        Set<Edge> edges = mEdges.get (source);
        if (edges == null) {
            return new HashSet<Edge> ();
        } else {
            return edges;
        }
    }

    // TODO: move elsewhere so this class does not need the publisher
    void reportNodesAndEdges () {
        TestRunResultPublisher publisher = TestRunResultPublisherImpl.getInstance ();
        for (Node node : mNodes.values ()) {
            publisher.publishNewNode (node.getName ());
        }
        for (Set<Edge> set : mEdges.values ()) {
            for (Edge edge : set) {
                publisher.publishNewEdge (edge.mSource.getName (), edge.mTarget.getName ());
            }
        }
    }
}
