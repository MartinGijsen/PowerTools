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
	
	private final String mPath;

	private File mFile;
	private PrintWriter mWriter;
	private String mDefaultFileType;
	private boolean mDoCleanup;
	
	
	public GraphViz (String path) {	
		this (path, DEFAULT_DEFAULT_TYPE);
	}

	public GraphViz (String path, String defaultType) {
		mPath			= path;
		mDefaultFileType	= defaultType;
		mDoCleanup		= true;
	}

	@Override
	public void setCleanup (boolean value) {
		mDoCleanup = value;
	}

	@Override
	public void setDefaultFileType (String fileType) {
		mDefaultFileType = fileType;
	}

	@Override
	public String getDefaultFileType () {
		return mDefaultFileType;
	}

	@Override
	public void write (String filename, DirectedGraph graph) {
		write (filename, mDefaultFileType, graph);
	}

	@Override
	public void write (String filename, String fileType, DirectedGraph graph) {
		writeDotFile (graph);
		generateImages (filename, fileType);
	}

	@Override
	public void writeDirected (DirectedGraph graph, PrintWriter writer) {
		mWriter = writer;
		writeDotFile (graph);
	}

	@Override
	public void writeDirected (DirectedGraph graph, String filename) {
		writeDirected (graph, filename, mDefaultFileType);
	}

	@Override
	public void writeDirected (DirectedGraph graph, String filename, String fileType) {
		openDotFile ();
		writeDotFile (graph);
		generateImageDirected (filename, fileType);
	}

	private void writeDotFile (DirectedGraph graph) {
		startDotFile ();
		writeGraph (graph);
		writeClusters (graph);
		writeNodes (graph);
		writeEdges (graph);
		writeRanks (graph);
		closeDotFile ();
	}
	
	private void openDotFile () {
		try {
			mFile	= File.createTempFile ("PowerTools", ".dot");
			mWriter	= new PrintWriter (new FileOutputStream (mFile));
		} catch (IOException ioe) {
			throw new GraphException ("failed to create temporary file");
		}
	}

	private void startDotFile () {
		mWriter.println ("digraph G {");
	}

	private void writeGraph (DirectedGraph graph) {
		if (graph.getConcentrateEdges ()) {
			writeGraphAttribute ("concentrate", "true");
		}
		if (graph.getRankDirection () != RankDirection.DEFAULT) {
			writeGraphAttribute ("rankdir", graph.getRankDirection ().toString ());
		}
		if (graph.getDistanceBetweenRanks () >= 0) {
			writeGraphAttribute ("ranksep", "" + graph.getDistanceBetweenRanks ());
		}
		if (graph.getDistanceBetweenNodes () >= 0) {
			writeGraphAttribute ("nodesep", "" + graph.getDistanceBetweenNodes ());
		}

		if (!graph.getLabel ().isEmpty ()) {
			writeGraphAttribute ("label", graph.getLabel ());
		}
		if (graph.getStyle () != Style.DEFAULT) {
			writeGraphAttribute ("style", graph.getStyle ().toString ());
		}
		if (graph.getFillColour () != Colour.DEFAULT) {
			writeGraphAttribute ("bgcolor", graph.getFillColour ().toString ());
		}
		if (graph.getTextColour () != Colour.DEFAULT) {
			writeGraphAttribute ("fontcolor", graph.getTextColour ().toString ());
		}
		if (!graph.getFontName ().isEmpty ()) {
			writeGraphAttribute ("fontname", graph.getFontName ());
		}
		if (!graph.getFontSize ().isEmpty ()) {
			writeGraphAttribute ("fontsize", graph.getFontSize ());
		}

		writeDefaultNodeAttributes (graph);
	}

	private void writeGraphAttribute (String attributeName, String value) {
		mWriter.println (String.format ("\t%s = %s;", attributeName, value));
	}

	private void writeClusters (DirectedGraph graph) {
		int counter = 0;
		for (Cluster cluster : graph.mClusters) {
			mWriter.println ("\tsubgraph cluster_" + Integer.toString (++counter) + " {");
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

			writeDefaultNodeAttributes (cluster);
			
			for (Node node : cluster.getNodes ()) {
				mWriter.println ("\t\t\"" + node.getName () + "\";");
			}
			mWriter.println ("\t}");
		}
	}

	private void writeClusterAttribute (String attributeName, String value) {
		mWriter.println (String.format ("\t\t%s = %s;", attributeName, value));
	}

	private void writeNodes (DirectedGraph graph) {
		for (Node node : graph.mNodes.values ()) {
			// TO DO: make sure node is written if it is not on any edge or in a cluster or has an attribute
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
		mWriter.println (String.format ("\t\"%s\" [ %s = %s ];", node.getName (), attributeName, value));
	}
	
	private void writeEdges (DirectedGraph graph) {
		for (Node node : graph.mNodes.values ()) {
			for (Edge edge : graph.getEdges (node)) {
				mWriter.append (String.format ("\t\"%s\" -> \"%s\"", edge.getSource ().getName (), edge.getTarget ().getName ()));
				if (!edge.getLabel ().isEmpty ()) {
					writeEdgeAttribute ("label", edge.getLabel ());
				}

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
				mWriter.println (";");
			}
		}
	}
	
	private void writeEdgeAttribute (String attributeName, String value) {
		mWriter.append (String.format (" [ %s = %s ]", attributeName, value));
	}
	
	private void writeDefaultNodeAttributes (AttributeSet3 attributes) {
		if (attributes.getDefaultNodeShape () != Shape.DEFAULT) {
			writeDefaultNodeAttribute ("shape", attributes.getDefaultNodeShape ().toString ());
		}

		if (attributes.getDefaultNodeStyle () != Style.DEFAULT) {
			writeDefaultNodeAttribute ("style", attributes.getDefaultNodeStyle ().toString ());
		}
		if (attributes.getDefaultNodeLineColour () != Colour.DEFAULT) {
			writeDefaultNodeAttribute ("color", attributes.getDefaultNodeLineColour ().toString ());
		}
		if (!attributes.getDefaultNodeLineWidth ().isEmpty ()) {
			writeDefaultNodeAttribute ("penwidth", attributes.getDefaultNodeLineWidth ());
		}
		if (attributes.getDefaultNodeFillColour () != Colour.DEFAULT) {
			writeDefaultNodeAttribute ("fillcolor", attributes.getDefaultNodeFillColour ().toString ());
		}
		if (attributes.getDefaultNodeTextColour () != Colour.DEFAULT) {
			writeDefaultNodeAttribute ("fontcolor", attributes.getDefaultNodeTextColour ().toString ());
		}
		if (!attributes.getDefaultNodeFontName ().isEmpty ()) {
			writeDefaultNodeAttribute ("fontname", attributes.getDefaultNodeFontName ());
		}
		if (!attributes.getDefaultNodeFontSize ().isEmpty ()) {
			writeDefaultNodeAttribute ("fontsize", attributes.getDefaultNodeFontSize ());
		}
	}
	
	private void writeDefaultNodeAttribute (String attributeName, String value) {
		mWriter.println (String.format ("\t\tnode [ %s = %s ];", attributeName, value));
	}
	
	private void writeRanks (DirectedGraph graph) {
		for (Rank rank : graph.mRanks.values ()) {
			if (!rank.nodes.isEmpty ()) {
				mWriter.append ("\t{");
				mWriter.println (String.format (" rank = %s ;", rank.type));
				for (Node node : rank.nodes) {
					mWriter.println (String.format (" \"%s\" ;", node.getName ()));
				}
				mWriter.println (" }");
			}
		}
	}

	private void closeDotFile () {
		mWriter.println ("}");
		mWriter.close ();
	}
	
	private void generateImages (String filename, String fileType) {
		runTool ("dot", filename, fileType);
		runTool ("neato", filename, fileType);
		runTool ("twopi", filename, fileType);
	}
	
	private void generateImageDirected (String filename, String fileType) {
		runTool ("dot", filename, fileType);
	}
	
	private void runTool (String tool, String filename, String fileType) {
		try {
			String command = String.format ("\"%s/%s\" -Tpng -o %s.%s.%s %s", mPath, tool, filename, tool, fileType, mFile.getPath ());
//			System.out.println (command);
			Runtime.getRuntime ().exec (command).waitFor ();
			if (mDoCleanup) {
				mFile.delete ();
			}
		} catch (IOException ioe) {
			throw new GraphException ("could not write picture");
		} catch (InterruptedException ie) {
			throw new GraphException ("wait interrupted");
		}
	}
}
