package org.powerTools.graph;


public interface Renderer {
	void setDefaultType (String type);
	void write (String filename, DirectedGraph graph);
	void write (String filename, String type, DirectedGraph graph);
	void writeDirected (String filename, DirectedGraph graph);
	void writeDirected (String filename, String type, DirectedGraph graph);
}