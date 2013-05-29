package org.powerTools.engine.sources.model;


final class ActiveGraph {
	DirectedGraph	mGraph;
	Node			mCurrentNode;
	
	ActiveGraph (DirectedGraph graph) {
		mGraph			= graph;
		mCurrentNode	= graph.getRoot ();
	}
}