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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;


public final class GraphViz implements Renderer {
	private static final String DEFAULT_DEFAULT_TYPE = "png";
	
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
			if (!cluster.getLabel ().isEmpty ()) {
				writeClusterAttribute ("label", "\"" + cluster.getLabel () + "\"");
			}
			if (cluster.getStyle () != Style.DEFAULT) {
				writeClusterAttribute ("style", cluster.getStyle ().toString ());
			}
			if (cluster.getLineColour () != Colour.DEFAULT) {
				writeClusterAttribute ("color", cluster.getLineColour ().toString ());
			}
			if (!cluster.getLineWidth ().isEmpty ()) {
				writeClusterAttribute ("penwidth", cluster.getLineWidth ());
			}
			if (cluster.getFillColour () != Colour.DEFAULT) {
				writeClusterAttribute ("fillcolor", cluster.getFillColour ().toString ());
			}
			if (cluster.getTextColour () != Colour.DEFAULT) {
				writeClusterAttribute ("fontcolor", cluster.getTextColour ().toString ());
			}
			if (!cluster.getFontName ().isEmpty ()) {
				writeClusterAttribute ("fontname", cluster.getFontName ());
			}
			if (!cluster.getFontSize ().isEmpty ()) {
				writeClusterAttribute ("fontsize", cluster.getFontSize ());
			}

			writeDefaultNodeAttributes (cluster.defaultNodeAttributes);
			
			for (Node node : cluster.getNodes ()) {
				this.writer.println ("\t\t\"" + node.getName () + "\";");
			}
			this.writer.println ("\t}");
		}
	}

	private void writeClusterAttribute (String attributeName, String value) {
		this.writer.println (String.format ("\t\t%s = %s;", attributeName, value));
	}

	private void writeNodes (DirectedGraph graph) {
		for (Node node : graph.nodes.values ()) {
			if (!node.getLabel ().isEmpty ()) {
				writeNodeAttribute (node, "label", node.getLabel ());
			}
			if (node.getShape () != Shape.DEFAULT) {
				writeNodeAttribute (node, "shape", node.getShape ().toString ());
			}
			if (node.getStyle () != Style.DEFAULT) {
				writeNodeAttribute (node, "style", node.getStyle ().toString ());
			}
			if (node.getLineColour () != Colour.DEFAULT) {
				writeNodeAttribute (node, "color", node.getLineColour ().toString ());
			}
			if (!node.getLineWidth ().isEmpty ()) {
				writeNodeAttribute (node, "penwidth", node.getLineWidth ());
			}
			if (node.getFillColour () != Colour.DEFAULT) {
				writeNodeAttribute (node, "fillcolor", node.getFillColour ().toString ());
			}
			if (node.getTextColour () != Colour.DEFAULT) {
				writeNodeAttribute (node, "fontcolor", node.getTextColour ().toString ());
			}
			if (!node.getFontName ().isEmpty ()) {
				writeNodeAttribute (node, "fontname", node.getFontName ());
			}
			if (!node.getFontSize ().isEmpty ()) {
				writeNodeAttribute (node, "fontsize", node.getFontSize ());
			}
		}
	}

	private void writeNodeAttribute (Node node, String attributeName, String value) {
		this.writer.println (String.format ("\t\"%s\" [ %s = %s ];", node.getName (), attributeName, value));
	}
	
	private void writeEdges (DirectedGraph graph) {
		for (Node node : graph.nodes.values ()) {
			for (Edge edge : graph.getEdges (node)) {
				this.writer.append (String.format ("\t\"%s\" -> \"%s\"", edge.getSource ().getName (), edge.getTarget ().getName ()));
				if (edge.getStyle () != Style.DEFAULT) {
					writeEdgeAttribute ("style", edge.getStyle ().toString ());
				}
				if (edge.getLineColour () != Colour.DEFAULT) {
					writeEdgeAttribute ("color", edge.getLineColour ().toString ());
				}
				if (!edge.getLineWidth ().isEmpty ()) {
					writeEdgeAttribute ("penwidth", edge.getLineWidth ());
				}
				if (edge.getFillColour () != Colour.DEFAULT) {
					writeEdgeAttribute ("fillcolor", edge.getFillColour ().toString ());
				}
				if (edge.getTextColour () != Colour.DEFAULT) {
					writeEdgeAttribute ("fontcolor", edge.getTextColour ().toString ());
				}
				if (!edge.getFontName ().isEmpty ()) {
					writeEdgeAttribute ("fontname", edge.getFontName ());
				}
				if (!edge.getFontSize ().isEmpty ()) {
					writeEdgeAttribute ("fontsize", edge.getFontSize ());
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
					this.writer.println (String.format (" \"%s\" ;", node.getName ()));
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