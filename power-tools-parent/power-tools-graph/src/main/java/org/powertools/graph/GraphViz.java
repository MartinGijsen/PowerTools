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
		mPath               = path;
		mDefaultFileType	= defaultType;
		mDoCleanup          = true;
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
		writeGraphAttribute (graph.getConcentrateEdges (), "concentrate", "true");
		writeGraphAttribute (graph.getRankDirection () != RankDirection.DEFAULT, "rankdir", graph.getRankDirection ().toString ());
		writeGraphAttribute (graph.getDistanceBetweenRanks () >= 0, "ranksep", "" + graph.getDistanceBetweenRanks ());
		writeGraphAttribute (graph.getDistanceBetweenNodes () >= 0, "nodesep", "" + graph.getDistanceBetweenNodes ());

		writeGraphAttribute (!graph.getLabel ().isEmpty (), "label", graph.getLabel ());
		writeGraphAttribute (graph.getStyle () != Style.DEFAULT, "style", graph.getStyle ().toString ());
		writeGraphAttribute (graph.getLineColour () != Colour.DEFAULT, "color", graph.getLineColour ().toString ());
		writeGraphAttribute (!graph.getLineWidth ().isEmpty (), "penwidth", graph.getLineWidth ());
		writeGraphAttribute (graph.getFillColour () != Colour.DEFAULT, "bgcolor", graph.getFillColour ().toString ());
		writeGraphAttribute (graph.getTextColour () != Colour.DEFAULT, "fontcolor", graph.getTextColour ().toString ());
		writeGraphAttribute (!graph.getFontName ().isEmpty (), "fontname", graph.getFontName ());
		writeGraphAttribute (!graph.getFontSize ().isEmpty (), "fontsize", graph.getFontSize ());

		writeDefaultNodeAttributes (graph);
	}

	private void writeGraphAttribute (boolean condition, String attributeName, String value) {
        if (condition) {
    		mWriter.println (String.format ("\t%s = %s;", attributeName, value));
        }
	}

	private void writeClusters (DirectedGraph graph) {
		int counter = 0;
		for (Cluster cluster : graph.mClusters) {
			mWriter.println ("\tsubgraph cluster_" + Integer.toString (++counter) + " {");
			writeClusterAttribute (!cluster.getLabel ().isEmpty (), "label", "\"" + cluster.getLabel () + "\"");

			writeClusterAttribute (cluster.getStyle () != Style.DEFAULT, "style", cluster.getStyle ().toString ());
			writeClusterAttribute (cluster.getLineColour () != Colour.DEFAULT, "color", cluster.getLineColour ().toString ());
			writeClusterAttribute (!cluster.getLineWidth ().isEmpty (), "penwidth", cluster.getLineWidth ());
			writeClusterAttribute (cluster.getFillColour () != Colour.DEFAULT, "fillcolor", cluster.getFillColour ().toString ());
			writeClusterAttribute (cluster.getTextColour () != Colour.DEFAULT, "fontcolor", cluster.getTextColour ().toString ());
			writeClusterAttribute (!cluster.getFontName ().isEmpty (), "fontname", cluster.getFontName ());
			writeClusterAttribute (!cluster.getFontSize ().isEmpty (), "fontsize", cluster.getFontSize ());

			writeDefaultNodeAttributes (cluster);
			
			for (Node node : cluster.getNodes ()) {
        		mWriter.println (String.format ("\t\t\"%s\";", node.getName ()));
			}
			mWriter.println ("\t}");
		}
	}

	private void writeClusterAttribute (boolean condition, String attributeName, String value) {
        if (condition) {
    		mWriter.println (String.format ("\t\t%s = %s;", attributeName, value));
        }
	}

	private void writeNodes (DirectedGraph graph) {
		for (Node node : graph.mNodes.values ()) {
			// TO DO: make sure node is written if it is not on any edge or in a cluster or has an attribute
			writeNodeAttribute (!node.getLabel ().isEmpty (), node, "label", node.getLabel ());
			writeNodeAttribute (node.getShape () != Shape.DEFAULT, node, "shape", node.getShape ().toString ());
			
			writeNodeAttribute (node.getStyle () != Style.DEFAULT, node, "style", node.getStyle ().toString ());
			writeNodeAttribute (node.getLineColour () != Colour.DEFAULT, node, "color", node.getLineColour ().toString ());
			writeNodeAttribute (!node.getLineWidth ().isEmpty (), node, "penwidth", node.getLineWidth ());
			writeNodeAttribute (node.getFillColour () != Colour.DEFAULT, node, "fillcolor", node.getFillColour ().toString ());
			writeNodeAttribute (node.getTextColour () != Colour.DEFAULT, node, "fontcolor", node.getTextColour ().toString ());
			writeNodeAttribute (!node.getFontName ().isEmpty (), node, "fontname", node.getFontName ());
			writeNodeAttribute (!node.getFontSize ().isEmpty (), node, "fontsize", node.getFontSize ());
		}
	}

	private void writeNodeAttribute (boolean condition, Node node, String attributeName, String value) {
        if (condition) {
    		mWriter.println (String.format ("\t\"%s\" [ %s = %s ];", node.getName (), attributeName, value));
        }
	}
	
	private void writeEdges (DirectedGraph graph) {
		for (Node node : graph.mNodes.values ()) {
			for (Edge edge : graph.getEdges (node)) {
				mWriter.append (String.format ("\t\"%s\" -> \"%s\"", edge.getSource ().getName (), edge.getTarget ().getName ()));
				writeEdgeAttribute (!edge.getLabel ().isEmpty (), "label", edge.getLabel ());

				writeEdgeAttribute (edge.getStyle () != Style.DEFAULT, "style", edge.getStyle ().toString ());
				writeEdgeAttribute (edge.getLineColour () != Colour.DEFAULT, "color", edge.getLineColour ().toString ());
				writeEdgeAttribute (!edge.getLineWidth ().isEmpty (), "penwidth", edge.getLineWidth ());
				writeEdgeAttribute (edge.getFillColour () != Colour.DEFAULT, "fillcolor", edge.getFillColour ().toString ());
				writeEdgeAttribute (edge.getTextColour () != Colour.DEFAULT, "fontcolor", edge.getTextColour ().toString ());
				writeEdgeAttribute (!edge.getFontName ().isEmpty (), "fontname", edge.getFontName ());
				writeEdgeAttribute (!edge.getFontSize ().isEmpty (), "fontsize", edge.getFontSize ());
				mWriter.println (";");
			}
		}
	}
	
	private void writeEdgeAttribute (boolean condition, String attributeName, String value) {
        if (condition) {
    		mWriter.append (String.format (" [ %s = %s ]", attributeName, value));
        }
	}
	
	private void writeDefaultNodeAttributes (AttributeSetWithDefaultNodeAttributes attributes) {
		writeDefaultNodeAttribute (attributes.getDefaultNodeShape () != Shape.DEFAULT, "shape", attributes.getDefaultNodeShape ().toString ());

		writeDefaultNodeAttribute (attributes.getDefaultNodeStyle () != Style.DEFAULT, "style", attributes.getDefaultNodeStyle ().toString ());
		writeDefaultNodeAttribute (attributes.getDefaultNodeLineColour () != Colour.DEFAULT, "color", attributes.getDefaultNodeLineColour ().toString ());
		writeDefaultNodeAttribute (!attributes.getDefaultNodeLineWidth ().isEmpty (), "penwidth", attributes.getDefaultNodeLineWidth ());
		writeDefaultNodeAttribute (attributes.getDefaultNodeFillColour () != Colour.DEFAULT, "fillcolor", attributes.getDefaultNodeFillColour ().toString ());
		writeDefaultNodeAttribute (attributes.getDefaultNodeTextColour () != Colour.DEFAULT, "fontcolor", attributes.getDefaultNodeTextColour ().toString ());
		writeDefaultNodeAttribute (!attributes.getDefaultNodeFontName ().isEmpty (), "fontname", attributes.getDefaultNodeFontName ());
		writeDefaultNodeAttribute (!attributes.getDefaultNodeFontSize ().isEmpty (), "fontsize", attributes.getDefaultNodeFontSize ());
	}
	
	private void writeDefaultNodeAttribute (boolean condition, String attributeName, String value) {
        if (condition) {
    		mWriter.println (String.format ("\t\tnode [ %s = %s ];", attributeName, value));
        }
	}
	
	private void writeRanks (DirectedGraph graph) {
		for (Rank rank : graph.mRanks.values ()) {
			if (!rank.nodes.isEmpty ()) {
				mWriter.append ("\t{");
				mWriter.print (String.format (" rank = %s ;", rank.type));
				for (Node node : rank.nodes) {
					mWriter.print (String.format (" \"%s\" ;", node.getName ()));
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
