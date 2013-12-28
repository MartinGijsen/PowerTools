/*	Copyright 2013 by Martin Gijsen (www.DeAnalist.nl)
 *
 *	This file is part of the PowerTools.
 *
 *	The PowerTools are free software: you can redistribute them and/or
 *	modify them under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the License,
 *	or (at your option) any later version.
 *
 *	The PowerTools are distributed in the hope that they will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU Affero General Public License for more details.
 *
 *	You should have received a copy of the GNU Affero General Public License
 *	along with the PowerTools. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public final class DirectedGraph extends AttributeSet3 {
	final Map<String, Node> mNodes;
	final Set<Cluster> mClusters;
	final Map<String, Rank> mRanks;

	private boolean mConcentrateEdges;
	private RankDirection mRankDirection;
	private float mDistanceBetweenRanks;
	private float mDistanceBetweenNodes;

	private Map<Node, Set<Edge>> mEdges;
	
	
	public DirectedGraph () {
		this (false);
	}

	public DirectedGraph (boolean concentrateEdges) {
		super ();
		mNodes		= new HashMap<String, Node> ();
		mEdges		= new HashMap<Node, Set<Edge>> ();
		mClusters	= new HashSet<Cluster> ();
		mRanks		= new HashMap<String, Rank> ();
		
		mConcentrateEdges		= concentrateEdges;
		mRankDirection			= RankDirection.DEFAULT;
		mDistanceBetweenRanks	= -1;
		mDistanceBetweenNodes	= -1;
	}


	public void setConcentrateEdges (boolean value) {
		mConcentrateEdges = value;
	}
	
	public boolean getConcentrateEdges () {
		return mConcentrateEdges;
	}
	
	public void setRankDirection (RankDirection direction) {
		mRankDirection = direction;
	}
	
	public RankDirection getRankDirection () {
		return mRankDirection;
	}
	
	public void setDistanceBetweenRanks (float distance) {
		mDistanceBetweenRanks = distance;
	}
	
	public float getDistanceBetweenRanks () {
		return mDistanceBetweenRanks;
	}
	
	public void setDistanceBetweenNodes (float distance) {
		mDistanceBetweenNodes = distance;
	}
	
	public float getDistanceBetweenNodes () {
		return mDistanceBetweenNodes;
	}
	

	public Node addNode (String name) {
		if (mNodes.containsKey (name)) {
			throw new GraphException (String.format ("node name %s not unique", name));
		} else {
			Node node = new Node (name);
			mNodes.put (name, node);
			return node;
		}
	}
	
	public Node addNode (String name, Cluster cluster) {
		Node node = addNode (name);
		cluster.addNode (node);
		return node;
	}

	public boolean hasNode (String name) {
		return mNodes.get (name) != null;
	}

	public Node getNode (String name) {
		Node node = mNodes.get (name);
		if (node == null) {
			throw new GraphException ("unknown node: " + name);
		}
		return node;
	}

	public Node getRoot () {
		Set<Node> startNodes = new HashSet<Node> ();
		startNodes.addAll (mNodes.values ());

		for (Set<Edge> edgeSet : mEdges.values ()) {
			for (Edge edge : edgeSet) {
				startNodes.remove (edge.getTarget ());
			}
		}

		switch (startNodes.size ()) {
		case 0:
			throw new GraphException ("no start node");
		case 1:
			return startNodes.iterator ().next ();
		default:
			throw new GraphException ("multiple start nodes");
		}
	}

	public Edge addEdge (String sourceName, String targetName) {
		return addEdge (getNode (sourceName), getNode (targetName));
	}

	public Edge addEdge (Node source, String targetName) {
		return addEdge (source, getNode (targetName));
	}

	public Edge addEdge (String sourceName, Node target) {
		return addEdge (getNode (sourceName), target);
	}

	public Edge addEdge (Node source, Node target) {
		Set<Edge> edgesFromSameSource = mEdges.get (source);
		if (edgesFromSameSource == null) {
			edgesFromSameSource = new HashSet<Edge> ();
			mEdges.put (source, edgesFromSameSource);
		} else {
			for (Edge edge : edgesFromSameSource) {
				if (edge.getTarget () == target) {
					throw new GraphException ("edge already exists");
				}
			}
		}

		Edge edge = new Edge (source, target);
		edgesFromSameSource.add (edge);
		return edge;
	}
	
	public boolean hasEdge (Node source, Node target) {
		try {
			getEdge (source, target);
			return true;
		} catch (GraphException ge) {
			return false;
		}
	}

	public Edge getEdge (Node source, Node target) {
		Set<Edge> edgesFromSameSource = mEdges.get (source);
		if (edgesFromSameSource != null) {
			for (Edge edge : edgesFromSameSource) {
				if (edge.getTarget () == target) {
					return edge;
				}
			}
		}
		throw new GraphException ("edge does not exist");
	}

	public Set<Edge> getEdges (Node source) {
		Set<Edge> edgesFromSameSource = mEdges.get (source);
		return edgesFromSameSource == null ? new HashSet<Edge> () : edgesFromSameSource;
	}

	public Cluster addCluster (String label) {
		Cluster cluster = new Cluster (label);
		mClusters.add (cluster);
		return cluster;
	}

	public Cluster getCluster (String label) {
		for (Cluster cluster : mClusters) {
			if (cluster.getLabel ().equals (label)) {
				return cluster;
			}
		}
		return null;
	}
	
	public Rank addRank (String name, RankType type) {
		if (mRanks.containsKey (name)) {
			throw new GraphException (String.format ("rank name %s not unique", name));
		} else {
			Rank rank = new Rank (name, type);
			mRanks.put (name, rank);
			return rank;
		}
	}

	public Rank getRank (String name) {
		return mRanks.get (name);
	}
	
	public Iterator<Node> nodeIterator () {
		return mNodes.values ().iterator ();
	}
}
