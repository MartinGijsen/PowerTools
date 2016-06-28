/* Copyright 2013-2014 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools.
 *
 * The PowerTools are free software: you can redistribute them and/or
 * modify them under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools are distributed in the hope that they will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.graph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;


public final class GraphViz implements Renderer {
    private static final String DEFAULT_DEFAULT_TYPE = "png";
    private static final String LABEL                = "label";
    private static final String SHAPE                = "shape";
    private static final String COLOR                = "color";
    private static final String STYLE                = "style";
    private static final String PEN_WIDTH            = "penwidth";
    private static final String FILL_COLOR           = "fillcolor";
    private static final String FONT_COLOR           = "fontcolor";
    private static final String FONT_NAME            = "fontname";
    private static final String FONT_SIZE            = "fontsize";

    private final String mPath;

    private File mFile;
    private PrintWriter mWriter;
    private String mDefaultFileType;
    private boolean mInDebugMode;
    private int mLastClusterNr;


    public GraphViz () {
        this (null);
    }

    public GraphViz (String path) {
        this (path, DEFAULT_DEFAULT_TYPE);
    }

    public GraphViz (String path, String defaultType) {
        mPath            = path;
        mDefaultFileType = defaultType;
        mInDebugMode     = false;
    }

    public void setDebugMode (boolean value) {
        mInDebugMode = value;
    }

    public boolean inDebugMode () {
        return mInDebugMode;
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
        openDotFile ();
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
            mFile   = File.createTempFile ("PowerTools", ".dot", new File ("."));
            mWriter = new PrintWriter (new FileOutputStream (mFile));
        } catch (IOException ioe) {
            throw new GraphException ("failed to create temporary file");
        }
    }

    private void startDotFile () {
        mWriter.println ("digraph G {");
    }

    private void writeGraph (DirectedGraph graph) {
        mLastClusterNr = 0;
        
        writeGraphAttribute (graph.getConcentrateEdges (), "concentrate", "true");
        writeGraphAttribute (graph.getRankDirection () != RankDirection.DEFAULT, "rankdir", graph.getRankDirection ().toString ());
        writeGraphAttribute (graph.getDistanceBetweenRanks () >= 0, "ranksep", "" + graph.getDistanceBetweenRanks ());
        writeGraphAttribute (graph.getDistanceBetweenNodes () >= 0, "nodesep", "" + graph.getDistanceBetweenNodes ());

        writeGraphAttribute (!graph.getLabel ().isEmpty (), LABEL, graph.getLabel ());
        writeGraphAttribute (graph.getStyle () != Style.DEFAULT, STYLE, graph.getStyle ().toString ());
        writeGraphAttribute (graph.getLineColour () != Colour.DEFAULT, COLOR, graph.getLineColour ().toString ());
        writeGraphAttribute (!graph.getLineWidth ().isEmpty (), PEN_WIDTH, graph.getLineWidth ());
        writeGraphAttribute (graph.getFillColour () != Colour.DEFAULT, "bgcolor", graph.getFillColour ().toString ());
        writeGraphAttribute (graph.getTextColour () != Colour.DEFAULT, FONT_COLOR, graph.getTextColour ().toString ());
        writeGraphAttribute (!graph.getFontName ().isEmpty (), FONT_NAME, graph.getFontName ());
        writeGraphAttribute (!graph.getFontSize ().isEmpty (), FONT_SIZE, graph.getFontSize ());

        writeDefaultNodeAttributes (graph);
    }

    private void writeGraphAttribute (boolean condition, String attributeName, String value) {
        if (condition) {
            mWriter.println (String.format ("\t%s = \"%s\";", attributeName, value));
        }
    }

    private void writeClusters (DirectedGraph graph) {
        for (Cluster cluster : graph.mClusters.values ()) {
            writeCluster (cluster);
        }
    }

    private void writeClusters (Cluster cluster) {
        for (Cluster subCluster : cluster.mSubClusters.values ()) {
            writeCluster (subCluster);
        }
    }

    private void writeCluster (Cluster cluster) {
        mWriter.println ("\tsubgraph cluster_" + Integer.toString (++mLastClusterNr) + " {");
        writeClusterAttribute (!cluster.getLabel ().isEmpty (), LABEL, cluster.getLabel ());

        writeClusterAttribute (cluster.getStyle () != Style.DEFAULT, STYLE, cluster.getStyle ().toString ());
        writeClusterAttribute (cluster.getLineColour () != Colour.DEFAULT, COLOR, cluster.getLineColour ().toString ());
        writeClusterAttribute (!cluster.getLineWidth ().isEmpty (), PEN_WIDTH, cluster.getLineWidth ());
        writeClusterAttribute (cluster.getFillColour () != Colour.DEFAULT, FILL_COLOR, cluster.getFillColour ().toString ());
        writeClusterAttribute (cluster.getTextColour () != Colour.DEFAULT, FONT_COLOR, cluster.getTextColour ().toString ());
        writeClusterAttribute (!cluster.getFontName ().isEmpty (), FONT_NAME, cluster.getFontName ());
        writeClusterAttribute (!cluster.getFontSize ().isEmpty (), FONT_SIZE, cluster.getFontSize ());

        writeDefaultNodeAttributes (cluster);

        writeClusters (cluster);

        for (Node node : cluster.getNodes ()) {
            mWriter.println (String.format ("\t\t\"%s\";", node.getName ()));
        }
        mWriter.println ("\t}");
    }

    private void writeClusterAttribute (boolean condition, String attributeName, String value) {
        if (condition) {
            mWriter.println (String.format ("\t\t%s = \"%s\";", attributeName, value));
        }
    }

    private void writeNodes (DirectedGraph graph) {
        for (Node node : graph.mNodes.values ()) {
            // TO DO: make sure node is written if it is not on any edge or in a cluster or has an attribute
            writeNodeAttribute (!node.getLabel ().isEmpty (), node, LABEL, node.getLabel ());
            writeNodeAttribute (node.getShape () != Shape.DEFAULT, node, SHAPE, node.getShape ().toString ());

            writeNodeAttribute (node.getStyle () != Style.DEFAULT, node, STYLE, node.getStyle ().toString ());
            writeNodeAttribute (node.getLineColour () != Colour.DEFAULT, node, COLOR, node.getLineColour ().toString ());
            writeNodeAttribute (!node.getLineWidth ().isEmpty (), node, PEN_WIDTH, node.getLineWidth ());
            writeNodeAttribute (node.getFillColour () != Colour.DEFAULT, node, FILL_COLOR, node.getFillColour ().toString ());
            writeNodeAttribute (node.getTextColour () != Colour.DEFAULT, node, FONT_COLOR, node.getTextColour ().toString ());
            writeNodeAttribute (!node.getFontName ().isEmpty (), node, FONT_NAME, node.getFontName ());
            writeNodeAttribute (!node.getFontSize ().isEmpty (), node, FONT_SIZE, node.getFontSize ());
        }
    }

    private void writeNodeAttribute (boolean condition, Node node, String attributeName, String value) {
        if (condition) {
            mWriter.println (String.format ("\t\"%s\" [ %s = \"%s\" ];", node.getName (), attributeName, value));
        }
    }

    private void writeEdges (DirectedGraph graph) {
        for (Node node : graph.mNodes.values ()) {
            for (Edge edge : graph.getEdgesFrom (node)) {
                mWriter.append (String.format ("\t\"%s\" -> \"%s\"", edge.getSource ().getName (), edge.getTarget ().getName ()));
                writeEdgeAttribute (!edge.getLabel ().isEmpty (), LABEL, edge.getLabel ());

                writeEdgeAttribute (edge.getStyle () != Style.DEFAULT, STYLE, edge.getStyle ().toString ());
                writeEdgeAttribute (edge.getLineColour () != Colour.DEFAULT, COLOR, edge.getLineColour ().toString ());
                writeEdgeAttribute (!edge.getLineWidth ().isEmpty (), PEN_WIDTH, edge.getLineWidth ());
                writeEdgeAttribute (edge.getFillColour () != Colour.DEFAULT, FILL_COLOR, edge.getFillColour ().toString ());
                writeEdgeAttribute (edge.getTextColour () != Colour.DEFAULT, FONT_COLOR, edge.getTextColour ().toString ());
                writeEdgeAttribute (!edge.getFontName ().isEmpty (), FONT_NAME, edge.getFontName ());
                writeEdgeAttribute (!edge.getFontSize ().isEmpty (), FONT_SIZE, edge.getFontSize ());
                mWriter.println (";");
            }
        }
    }

    private void writeEdgeAttribute (boolean condition, String attributeName, String value) {
        if (condition) {
            mWriter.append (String.format (" [ %s = \"%s\" ]", attributeName, value));
        }
    }

    private void writeDefaultNodeAttributes (AttributeSetWithDefaultNodeAttributes attributes) {
        writeDefaultNodeAttribute (attributes.getDefaultNodeShape () != Shape.DEFAULT, SHAPE, attributes.getDefaultNodeShape ().toString ());

        writeDefaultNodeAttribute (attributes.getDefaultNodeStyle () != Style.DEFAULT, STYLE, attributes.getDefaultNodeStyle ().toString ());
        writeDefaultNodeAttribute (attributes.getDefaultNodeLineColour () != Colour.DEFAULT, COLOR, attributes.getDefaultNodeLineColour ().toString ());
        writeDefaultNodeAttribute (!attributes.getDefaultNodeLineWidth ().isEmpty (), PEN_WIDTH, attributes.getDefaultNodeLineWidth ());
        writeDefaultNodeAttribute (attributes.getDefaultNodeFillColour () != Colour.DEFAULT, FILL_COLOR, attributes.getDefaultNodeFillColour ().toString ());
        writeDefaultNodeAttribute (attributes.getDefaultNodeTextColour () != Colour.DEFAULT, FONT_COLOR, attributes.getDefaultNodeTextColour ().toString ());
        writeDefaultNodeAttribute (!attributes.getDefaultNodeFontName ().isEmpty (), FONT_NAME, attributes.getDefaultNodeFontName ());
        writeDefaultNodeAttribute (!attributes.getDefaultNodeFontSize ().isEmpty (), FONT_SIZE, attributes.getDefaultNodeFontSize ());
    }

    private void writeDefaultNodeAttribute (boolean condition, String attributeName, String value) {
        if (condition) {
            mWriter.println (String.format ("\t\tnode [ %s = \"%s\" ];", attributeName, value));
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
            String command = String.format ("\"%s%s\" -Tpng -o %s.%s.%s %s", getPath (), tool, filename, tool, fileType, mFile.getPath ());
            Runtime.getRuntime ().exec (command).waitFor ();
            if (!mInDebugMode) {
                mFile.delete ();
            }
        } catch (IOException ioe) {
            throw new GraphException ("could not write picture: " + ioe.getMessage ());
        } catch (InterruptedException ie) {
            throw new GraphException ("wait interrupted");
        }
    }

    private String getPath () {
        if (mPath == null || "".equals (mPath)) {
            return "";
        } else {
            return mPath + "\\";
        }
    }
}
