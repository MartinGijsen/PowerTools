/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.demo;

import org.powertools.graph.Cluster;
import org.powertools.graph.Colour;
import org.powertools.graph.DirectedGraph;
import org.powertools.graph.Edge;
import org.powertools.graph.GraphViz;
import org.powertools.graph.Node;
import org.powertools.graph.Shape;
import org.powertools.graph.Style;


public class Graph {
    private Graph () {
        // do nothing
    }
    
    public static void main (String[] args) {
        createGraphWithoutEdgeLabels ();
        createGraphWithEdgeLabels ();
        createGraphWithLabelAndBackgroundColour ();
        createGraphWithDefaultNodeAttributes ();
        createGraphWithClusters ();
        createGraphWithClusterWithLabelAndBackgroundColour ();
    }
    
    public static void createGraphWithoutEdgeLabels () {
        DirectedGraph graph = new DirectedGraph (false);
        
        graph.addNode ("node 1");
        graph.addNode ("node 2");
        graph.addEdge ("node 1", "node 2");
        graph.addNode ("node 3");
        graph.addEdge ("node 1", "node 3");

        writeGraph (graph, "graph1");
    }

    public static void createGraphWithEdgeLabels () {
        DirectedGraph graph = new DirectedGraph (false);

        Node node1 = graph.addNode ("node 1");
        Node node2 = graph.addNode ("node 2");
        Edge edge1 = graph.addEdge (node1, node2);
        edge1.setLabel ("edge 1");
        Node node3 = graph.addNode ("node 3");
        Edge edge2 = graph.addEdge (node1, node3);
        edge2.setLabel ("edge 2");

        writeGraph (graph, "graph2");
    }

    public static void createGraphWithLabelAndBackgroundColour () {
        DirectedGraph graph = new DirectedGraph (true);

        graph.setLabel ("graph label");
        graph.setFillColour (Colour.LIGHT_GRAY);
        graph.setTextColour (Colour.RED);
        graph.setFontName ("Arial");
        graph.setFontSize (24);

        graph.addNode ("node 1");
        graph.addNode ("node 2");
        graph.addEdge ("node 1", "node 2");
        graph.addNode ("node 3");
        graph.addEdge ("node 1", "node 3");

        writeGraph (graph, "graph3");
    }

    public static void createGraphWithDefaultNodeAttributes () {
        DirectedGraph graph = new DirectedGraph (true);
        
        // set the default node attributes
        graph.setDefaultNodeShape (Shape.CIRCLE);
        graph.setDefaultNodeStyle (Style.FILLED);
        graph.setDefaultNodeLineColour (Colour.RED);
        graph.setDefaultNodeLineWidth (2);
        graph.setDefaultNodeFillColour (Colour.PURPLE);
        graph.setDefaultNodeTextColour (Colour.YELLOW);
        graph.setDefaultNodeFontName ("Arial");
        graph.setDefaultNodeFontSize (14);
        
        // create nodes and edges
        graph.addNode ("node 1");
        graph.addNode ("node 2").setLineColour (Colour.GREEN);
        graph.addEdge ("node 1", "node 2");
        Node node3 = graph.addNode ("node 3");
        node3.setShape (Shape.OCTAGON);
        node3.setLineWidth (4);
        graph.addEdge ("node 1", "node 3");
        
        writeGraph (graph, "graph4");
    }

    public static void createGraphWithClusters () {
        DirectedGraph graph = new DirectedGraph (true);
        Cluster cluster1    = graph.addCluster ("cluster 1");
        Cluster cluster2    = graph.addCluster ("cluster 2", cluster1);

        // create nodes and edges
        graph.addNode ("node 1", cluster1);
        graph.addNode ("node 2", cluster2);
        graph.addEdge ("node 1", "node 2");
        graph.addNode ("node 3");
        graph.addEdge ("node 1", "node 3");
        
        writeGraph (graph, "graph5");
    }

    public static void createGraphWithClusterWithLabelAndBackgroundColour () {
        // create graph and set some default attributes
        DirectedGraph graph = new DirectedGraph (true);
        graph.setDefaultNodeShape (Shape.OVAL);
        graph.setDefaultNodeFontName ("Arial");

        // create cluster and set some attributes
        Cluster cluster = graph.addCluster ("cluster");
        cluster.setLabel ("cluster label");
        cluster.setStyle (Style.FILLED);
        cluster.setFillColour (Colour.YELLOW);
        cluster.setLineColour (Colour.RED);
        cluster.setDefaultNodeShape (Shape.SQUARE);
        
        // create nodes and edges
        Node node1 = graph.addNode ("node 1", cluster);
        graph.addNode ("node 2", cluster);
        graph.addEdge ("node 1", "node 2");
        graph.addNode ("node 3");
        graph.addEdge ("node 1", "node 3");

        // set node attributes
        node1.setShape (Shape.DIAMOND);
        node1.setStyle (Style.FILLED);
        node1.setFillColour (Colour.BLUE);
        node1.setTextColour (Colour.WHITE);

        writeGraph (graph, "graph6");
    }

    private static void writeGraph (DirectedGraph graph, String fileName) {
        new GraphViz ().writeDirected (graph, fileName);
    }
}
