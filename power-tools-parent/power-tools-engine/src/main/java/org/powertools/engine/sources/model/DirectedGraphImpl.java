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

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import org.powertools.engine.ExecutionException;
import org.xml.sax.SAXException;


final class DirectedGraphImpl implements DirectedGraph {
	final String				mName;
	final Map<String, Node>		mNodes;
	final Map<Node, Set<Edge>>	mEdges;
	
	
	static DirectedGraphImpl createGraph (String name) {
		try {
			return new GraphMLParser ().parse (new DirectedGraphImpl (name));
		} catch (SAXException se) {
			throw new ExecutionException ("SAX exception");
		} catch (FileNotFoundException fnfe) {
			throw new ExecutionException ("file not found: " + name);
		} catch (IOException ioe) {
			throw new ExecutionException ("error reading file: " + name);
		}
	}

	private DirectedGraphImpl (String name) {
		mName	= name;
		mNodes	= new HashMap<String, Node> ();
		mEdges	= new HashMap<Node, Set<Edge>> ();
	}
	
	public Node addNode (String name) {
		if (mNodes.containsKey (name)) {
			throw new RuntimeException (String.format ("node name %s not unique", name));
		} else {
			Node node = new Node (name, this);
			mNodes.put (name, node);
			return node;
		}
	}
	
	public Node getNode (String name) {
		return mNodes.get (name);
	}

	public Node getRoot () {
		Node root = null;
		for (Node node : mNodes.values ()) {
			if (!node.mLabel.equals (Model.START_NODE_LABEL)) {
				// this is not a start node and can be ignored
			} else if (root != null) {
				throw new RuntimeException ("multiple start nodes");
			} else {
				root = node;
			}
		}

		if (root == null) {
			throw new RuntimeException ("no start node");
		}
		return root;
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
				throw new RuntimeException ("edge already exists");
			}
		}

		Edge edge = new Edge (source, target);
		edges.add (edge);
		return edge;
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
		throw new RuntimeException ("edge does not exist");
	}
	
	public Set<Edge> getEdges (Node source) {
		Set<Edge> edges = mEdges.get (source);
		if (edges == null) {
			return new HashSet<Edge> ();
		} else {
			return edges;
		}
	}
}