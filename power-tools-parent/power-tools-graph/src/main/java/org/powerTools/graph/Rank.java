package org.powerTools.graph;

import java.util.HashSet;
import java.util.Set;


public class Rank {
	final String name;
	final RankType type;
	final Set<Node> nodes;
	
	
	Rank (String name, RankType type) {
		this.name	= name;
		this.type	= type;
		this.nodes	= new HashSet<Node> ();
	}
	
	
	public void add (Node node) {
		this.nodes.add (node);
	}
}