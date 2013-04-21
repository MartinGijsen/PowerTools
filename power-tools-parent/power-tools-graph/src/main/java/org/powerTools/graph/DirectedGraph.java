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

package org.powerTools.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public final class DirectedGraph {
	final Map<String, Node> nodes;
	final Set<Cluster> clusters;
	final Map<String, Rank> ranks;
	final Attributes attributes;
	final Attributes defaultNodeAttributes;

	boolean concentrateEdges;
	Direction direction;
	int distanceBetweenRanks;

	private Map<Node, Set<Edge>> edges;
	
	
	public DirectedGraph () {
		this (false);
	}

	public DirectedGraph (boolean concentrateEdges) {
		this.nodes		= new HashMap<String, Node> ();
		this.edges		= new HashMap<Node, Set<Edge>> ();
		this.clusters	= new HashSet<Cluster> ();
		this.ranks		= new HashMap<String, Rank> ();
		
		this.concentrateEdges		= concentrateEdges;
		this.direction				= Direction.DEFAULT;
		this.distanceBetweenRanks	= 0;
		this.attributes				= new Attributes ();
		this.defaultNodeAttributes	= new Attributes ();
	}

	
	public void setConcentrateEdges (boolean value) {
		this.concentrateEdges = value;
	}
	
	public void setDirection (Direction direction) {
		this.direction = direction;
	}
	
	public void setDistanceBetweenRanks (int distance) {
		this.distanceBetweenRanks = distance;
	}
	
	public void setLabel (String label) {
		this.attributes.label = label;
	}
	
	public void setFillColour (Colour colour) {
		this.attributes.fillColour = colour;
	}
	
	public void setTextColour (Colour colour) {
		this.attributes.textColour = colour;
	}
	
	public void setFontName (String fontName) {
		this.attributes.fontName = fontName;
	}
	
	public void setFontSize (int fontSize) {
		this.attributes.fontSize = Integer.toString (fontSize);
	}
	
	public void setDefaultNodeShape (Shape shape) {
		this.defaultNodeAttributes.shape = shape;
	}

	public void setDefaultNodeStyle (Style style) {
		this.defaultNodeAttributes.style = style;
	}
	
	public void setDefaultNodeLineColour (Colour colour) {
		this.defaultNodeAttributes.lineColour = colour;
	}
	
	public void setDefaultNodeLineWidth (int width) {
		this.defaultNodeAttributes.lineWidth = Integer.toString (width);
	}
	
	public void setDefaultNodeFillColour (Colour colour) {
		this.defaultNodeAttributes.fillColour = colour;
	}
	
	public void setDefaultNodeTextColour (Colour colour) {
		this.defaultNodeAttributes.textColour = colour;
	}
	
	public void setDefaultNodeFontName (String fontName) {
		this.defaultNodeAttributes.fontName = fontName;
	}
	
	public void setDefaultNodeFontSize (String fontSize) {
		this.defaultNodeAttributes.fontSize = fontSize;
	}


	public Node addNode (String name) {
		if (this.nodes.containsKey (name)) {
			throw new GraphException (String.format ("node name %s not unique", name));
		} else {
			final Node node = new Node (name);
			this.nodes.put (name, node);
			return node;
		}
	}
	
	public Node addNode (String name, Cluster cluster) {
		final Node node = addNode (name);
		cluster.add (node);
		return node;
	}
	
	public Node getNode (String name) {
		Node node = this.nodes.get (name);
		if (node == null) {
			throw new GraphException ("unknown node: " + name);
		}
		return node;
	}

	public Node getRoot () {
		Set<Node> nodes = new HashSet<Node> ();
		nodes.addAll (this.nodes.values ());

		for (Set<Edge> edges : this.edges.values ()) {
			nodes.removeAll (edges);
		}

		switch (nodes.size ()) {
		case 0:
			throw new GraphException ("no start node");
		case 1:
			return nodes.iterator ().next ();
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
		Set<Edge> edges = this.edges.get (source);
		if (edges == null) {
			edges = new HashSet<Edge> ();
			this.edges.put (source, edges);
		}

		if (edges.contains (target)) {
			throw new GraphException ("edge already exists");
		}
		final Edge edge = new Edge (source, target);
		edges.add (edge);
		return edge;
	}
	
	public Edge getEdge (Node source, Node target) {
		final Set<Edge> edges = this.edges.get (source);
		if (edges == null) {
			throw new GraphException ("edge does not exist");
		}
		for (Edge edge : edges) {
			if (edge.target == target) {
				return edge;
			}
		}
		throw new GraphException ("edge does not exist");
	}

	public Set<Edge> getEdges (Node source) {
		final Set<Edge> edges = this.edges.get (source);
		if (edges == null) {
			return new HashSet<Edge> ();
		} else {
			return edges;
		}
	}

	public Cluster addCluster (String label) {
		final Cluster cluster = new Cluster (label);
		this.clusters.add (cluster);
		return cluster;
	}

	public Cluster getCluster (String label) {
		for (Cluster cluster : this.clusters) {
			if (cluster.label.equals (label)) {
				return cluster;
			}
		}
		return null;
	}
	
	public Rank addRank (String name, RankType type) {
		if (this.ranks.containsKey (name)) {
			throw new GraphException (String.format ("rank name %s not unique", name));
		} else {
			final Rank rank = new Rank (name, type);
			this.ranks.put (name, rank);
			return rank;
		}
	}

	public Rank getRank (String name) {
		return this.ranks.get (name);
	}
	
	public Iterator<Node> nodeIterator () {
		return this.nodes.values ().iterator ();
	}
}