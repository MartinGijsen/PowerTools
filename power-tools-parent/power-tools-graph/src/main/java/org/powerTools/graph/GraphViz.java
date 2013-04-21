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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;


public final class GraphViz implements Renderer {
	private final static String DEFAULT_DEFAULT_TYPE = "png";
	
	private final String path;

	private File file;
	private PrintWriter writer;
	private String defaultType;
	private boolean doCleanup;
	
	
	public GraphViz (String path) {	
		this (path, DEFAULT_DEFAULT_TYPE);
	}

	public GraphViz (String path, String defaultType) {
		this.path			= path;
		this.defaultType	= defaultType;
		this.doCleanup		= true;
	}

	@Override
	public void setCleanup (boolean value) {
		this.doCleanup = value;
	}

	@Override
	public void setDefaultType (String type) {
		this.defaultType = type;
	}

	@Override
	public void write (String filename, DirectedGraph graph) {
		write (filename, this.defaultType, graph);
	}

	@Override
	public void write (String filename, String type, DirectedGraph graph) {
		writeDotFile (graph);
		generateImage (filename, type);
	}

	@Override
	public void writeDirected (String filename, DirectedGraph graph) {
		writeDirected (filename, this.defaultType, graph);
	}

	@Override
	public void writeDirected (String filename, String type, DirectedGraph graph) {
		writeDotFile (graph);
		generateImageDirected (filename, type);
	}

	private void writeDotFile (DirectedGraph graph) {
		createDotFile (graph);
		writeClusters (graph);
		writeNodes (graph);
		writeEdges (graph);
		writeRanks (graph);
		closeFile ();
	}
	
	private void createDotFile (DirectedGraph graph) {
		try {
			this.file	= File.createTempFile ("PowerTools", ".dot");
			this.writer	= new PrintWriter (new FileOutputStream (this.file));
			this.writer.println ("digraph G {");
//			if (graph.distanceBetweenRanks > 0) {
//				writeGraphAttribute ("ranksep", Integer.toString (graph.distanceBetweenRanks));
//			}
			if (graph.concentrateEdges) {
				writeGraphAttribute ("concentrate", "true");
			}
			if (graph.direction != Direction.DEFAULT) {
				writeGraphAttribute ("rankdir", graph.direction.toString ());
			}
			if (!graph.attributes.label.isEmpty ()) {
				writeGraphAttribute ("label", graph.attributes.label);
			}
			if (graph.attributes.style != Style.DEFAULT) {
				writeGraphAttribute ("style", graph.attributes.style.toString ());
			}
			if (graph.attributes.fillColour != Colour.DEFAULT) {
				writeGraphAttribute ("bgcolor", graph.attributes.fillColour.toString ());
			}
			if (graph.attributes.textColour != Colour.DEFAULT) {
				writeGraphAttribute ("fontcolor", graph.attributes.textColour.toString ());
			}
			if (!graph.attributes.fontName.isEmpty ()) {
				writeGraphAttribute ("fontname", graph.attributes.fontName);
			}
			if (!graph.attributes.fontSize.isEmpty ()) {
				writeGraphAttribute ("fontsize", graph.attributes.fontSize);
			}
			
			writeDefaultNodeAttributes (graph.defaultNodeAttributes);
		} catch (IOException ioe) {
			throw new GraphException ("failed to create temporary file");
		}
	}

	private void writeGraphAttribute (String attributeName, String value) {
		this.writer.println (String.format ("\t%s = %s;", attributeName, value));
	}

	private void writeClusters (DirectedGraph graph) {
		int counter = 0;
		for (Cluster cluster : graph.clusters) {
			this.writer.println ("\tsubgraph cluster_" + Integer.toString (++counter) + " {");
			if (!cluster.label.isEmpty ()) {
				writeClusterAttribute ("label", "\"" + cluster.label + "\"");
			}
			if (cluster.attributes.style != Style.DEFAULT) {
				writeClusterAttribute ("style", cluster.attributes.style.toString ());
			}
			if (cluster.attributes.lineColour != Colour.DEFAULT) {
				writeClusterAttribute ("color", cluster.attributes.lineColour.toString ());
			}
			if (!cluster.attributes.lineWidth.isEmpty ()) {
				writeClusterAttribute ("penwidth", cluster.attributes.lineWidth);
			}
			if (cluster.attributes.fillColour != Colour.DEFAULT) {
				writeClusterAttribute ("fillcolor", cluster.attributes.fillColour.toString ());
			}
			if (cluster.attributes.textColour != Colour.DEFAULT) {
				writeClusterAttribute ("fontcolor", cluster.attributes.textColour.toString ());
			}
			if (!cluster.attributes.fontName.isEmpty ()) {
				writeClusterAttribute ("fontname", cluster.attributes.fontName);
			}
			if (!cluster.attributes.fontSize.isEmpty ()) {
				writeClusterAttribute ("fontsize", cluster.attributes.fontSize);
			}

			writeDefaultNodeAttributes (cluster.defaultNodeAttributes);
			
			for (Node node : cluster.nodes) {
				this.writer.println ("\t\t\"" + node.name + "\";");
			}
			this.writer.println ("\t}");
		}
	}

	private void writeClusterAttribute (String attributeName, String value) {
		this.writer.println (String.format ("\t\t%s = %s;", attributeName, value));
	}

	private void writeNodes (DirectedGraph graph) {
		for (Node node : graph.nodes.values ()) {
			if (!node.attributes.label.isEmpty ()) {
				writeNodeAttribute (node, "label", node.attributes.label);
			}
			if (node.attributes.shape != Shape.DEFAULT) {
				writeNodeAttribute (node, "shape", node.attributes.shape.toString ());
			}
			if (node.attributes.style != Style.DEFAULT) {
				writeNodeAttribute (node, "style", node.attributes.style.toString ());
			}
			if (node.attributes.lineColour != Colour.DEFAULT) {
				writeNodeAttribute (node, "color", node.attributes.lineColour.toString ());
			}
			if (!node.attributes.lineWidth.isEmpty ()) {
				writeNodeAttribute (node, "penwidth", node.attributes.lineWidth);
			}
			if (node.attributes.fillColour != Colour.DEFAULT) {
				writeNodeAttribute (node, "fillcolor", node.attributes.fillColour.toString ());
			}
			if (node.attributes.textColour != Colour.DEFAULT) {
				writeNodeAttribute (node, "fontcolor", node.attributes.textColour.toString ());
			}
			if (!node.attributes.fontName.isEmpty ()) {
				writeNodeAttribute (node, "fontname", node.attributes.fontName);
			}
			if (!node.attributes.fontSize.isEmpty ()) {
				writeNodeAttribute (node, "fontsize", node.attributes.fontSize);
			}
		}
	}

	private void writeNodeAttribute (Node node, String attributeName, String value) {
		this.writer.println (String.format ("\t\"%s\" [ %s = %s ];", node.name, attributeName, value));
	}
	
	private void writeEdges (DirectedGraph graph) {
		for (Node node : graph.nodes.values ()) {
			for (Edge edge : graph.getEdges (node)) {
				this.writer.append (String.format ("\t\"%s\" -> \"%s\"", edge.source.name, edge.target.name));
				if (edge.attributes.style != Style.DEFAULT) {
					writeEdgeAttribute ("style", edge.attributes.style.toString ());
				}
				if (edge.attributes.lineColour != Colour.DEFAULT) {
					writeEdgeAttribute ("color", edge.attributes.lineColour.toString ());
				}
				if (!edge.attributes.lineWidth.isEmpty ()) {
					writeEdgeAttribute ("penwidth", edge.attributes.lineWidth);
				}
				if (edge.attributes.fillColour != Colour.DEFAULT) {
					writeEdgeAttribute ("fillcolor", edge.attributes.fillColour.toString ());
				}
				if (edge.attributes.textColour != Colour.DEFAULT) {
					writeEdgeAttribute ("fontcolor", edge.attributes.textColour.toString ());
				}
				if (!edge.attributes.fontName.isEmpty ()) {
					writeEdgeAttribute ("fontname", edge.attributes.fontName);
				}
				if (!edge.attributes.fontSize.isEmpty ()) {
					writeEdgeAttribute ("fontsize", edge.attributes.fontSize);
				}
				this.writer.println (";");
			}
		}
	}
	
	private void writeEdgeAttribute (String attributeName, String value) {
		this.writer.append (String.format (" [ %s = %s ]", attributeName, value));
	}
	
	private void writeDefaultNodeAttributes (Attributes attributes) {
		if (attributes.shape != Shape.DEFAULT) {
			writeDefaultNodeAttribute ("shape", attributes.shape.toString ());
		}
		if (attributes.style != Style.DEFAULT) {
			writeDefaultNodeAttribute ("style", attributes.style.toString ());
		}
		if (attributes.lineColour != Colour.DEFAULT) {
			writeDefaultNodeAttribute ("color", attributes.lineColour.toString ());
		}
		if (!attributes.lineWidth.isEmpty ()) {
			writeDefaultNodeAttribute ("penwidth", attributes.lineWidth);
		}
		if (attributes.fillColour != Colour.DEFAULT) {
			writeDefaultNodeAttribute ("fillcolor", attributes.fillColour.toString ());
		}
		if (attributes.textColour != Colour.DEFAULT) {
			writeDefaultNodeAttribute ("fontcolor", attributes.textColour.toString ());
		}
		if (!attributes.fontName.isEmpty ()) {
			writeDefaultNodeAttribute ("fontname", attributes.fontName);
		}
		if (!attributes.fontSize.isEmpty ()) {
			writeDefaultNodeAttribute ("fontsize", attributes.fontSize);
		}
	}
	
	private void writeDefaultNodeAttribute (String attributeName, String value) {
		this.writer.println (String.format ("\t\tnode [ %s = %s ];", attributeName, value));
	}
	
	private void writeRanks (DirectedGraph graph) {
		for (Rank rank : graph.ranks.values ()) {
			if (!rank.nodes.isEmpty ()) {
				this.writer.append ("\t{");
				this.writer.println (String.format (" rank = %s ;", rank.type));
				for (Node node : rank.nodes) {
					this.writer.println (String.format (" \"%s\" ;", node.name));
				}
				this.writer.println (" }");
			}
		}
	}

	private void closeFile () {
		this.writer.println ("}");
		this.writer.close ();
	}
	
	private void generateImage (String filename, String type) {
		runTool ("dot", filename, type);
		runTool ("neato", filename, type);
		runTool ("twopi", filename, type);
	}
	
	private void generateImageDirected (String filename, String type) {
		runTool ("dot", filename, type);
	}
	
	private void runTool (String tool, String filename, String type) {
		try {
			String command = String.format ("\"%s/%s\" -Tpng -o %s.%s.%s %s", path, tool, filename, tool, type, this.file.getPath ());
//			System.out.println (command);
			Runtime.getRuntime ().exec (command).waitFor ();
			if (this.doCleanup) {
				this.file.delete ();
			}
		} catch (IOException ioe) {
			throw new GraphException ("failed to generate picture");
		} catch (InterruptedException ie) {
			throw new GraphException ("wait interrupted");
		}
	}
}