/*	Copyright 2013 by Martin Gijsen (www.DeAnalist.nl)
 *
 *	This file is part of the PowerTools engine.
 *
 *	The PowerTools engine is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the License,
 *	or (at your option) any later version.
 *
 *	The PowerTools engine is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU Affero General Public License for more details.
 *
 *	You should have received a copy of the GNU Affero General Public License
 *	along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.graph;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.Test;
import static org.junit.Assert.*;


public class GraphVizTest {
	private final String DOT_FILE_FOR_SIMPLE_GRAPH_WITH_ONLY_DEFAULT_VALUES =
			"digraph G {\r\n" +
			"	subgraph cluster_1 {\r\n" +
			"		label = \"cluster\";\r\n" +
			"		\"node1\";\r\n" +
			"	}\r\n" +
			"	\"node1\" -> \"node2\";\r\n" +
			"}\r\n";
	private final String DOT_FILE_FOR_SIMPLE_GRAPH_WITH_NO_DEFAULT_VALUES =
			"digraph G {\r\n" +
			"	concentrate = true;\r\n" +
			"	label = graph label;\r\n" +
			"	style = dashed;\r\n" +
			"	bgcolor = aqua;\r\n" +
			"	fontcolor = red;\r\n" +
			"	fontname = Arial;\r\n" +
			"	fontsize = 8;\r\n" +
			"		node [ shape = diamond ];\r\n" +
			"		node [ style = dashed ];\r\n" +
			"		node [ color = red ];\r\n" +
			"		node [ penwidth = 1 ];\r\n" +
			"		node [ fillcolor = beige ];\r\n" +
			"		node [ fontcolor = black ];\r\n" +
			"		node [ fontname = Times-Roman ];\r\n" +
			"		node [ fontsize = 9 ];\r\n" +
			"	subgraph cluster_1 {\r\n" +
			"		label = \"cluster label\";\r\n" +
			"		style = bold;\r\n" +
			"		color = azure;\r\n" +
			"		penwidth = 2;\r\n" +
			"		fillcolor = blue;\r\n" +
			"		fontcolor = brown;\r\n" +
			"		fontname = Cambria;\r\n" +
			"		fontsize = 10;\r\n" +
			"		node [ shape = hexagon ];\r\n" +
			"		node [ style = dotted ];\r\n" +
			"		node [ color = coral ];\r\n" +
			"		node [ penwidth = 3 ];\r\n" +
			"		node [ fillcolor = crimson ];\r\n" +
			"		node [ fontcolor = cyan ];\r\n" +
			"		node [ fontname = Courier new ];\r\n" +
			"		node [ fontsize = 11 ];\r\n" +
			"		\"node1\";\r\n" +
			"	}\r\n" +
			"	\"node1\" [ label = edge label ];\r\n" +
			"	\"node1\" [ shape = octagon ];\r\n" +
			"	\"node1\" [ style = filled ];\r\n" +
			"	\"node1\" [ color = fuchsia ];\r\n" +
			"	\"node1\" [ penwidth = 4 ];\r\n" +
			"	\"node1\" [ fillcolor = gold ];\r\n" +
			"	\"node1\" [ fontcolor = gray ];\r\n" +
			"	\"node1\" [ fontname = Something ];\r\n" +
			"	\"node1\" [ fontsize = 12 ];\r\n" +
			"	\"node1\" -> \"node2\" [ label = edge label ] [ style = invis ] [ color = green ] [ penwidth = 5 ] [ fillcolor = indigo ] [ fontcolor = ivory ] [ fontname = Something else ] [ fontsize = 13 ];\r\n" +
			"}\r\n";

//	
//	public GraphVizTest() {
//	}
//	
//	@BeforeClass
//	public static void setUpClass() {
//	}
//	
//	@AfterClass
//	public static void tearDownClass() {
//	}
//	
//	@Before
//	public void setUp() {
//	}
//	
//	@After
//	public void tearDown() {
//	}

//	@Test
//	public void testSetDefaultType() {
//		System.out.println("setDefaultType");
//		String type = "";
//		GraphViz instance = null;
//		instance.setDefaultType(type);
//		fail("The test case is a prototype.");
//	}
//
//	@Test
//	public void testWrite_String_DirectedGraph() {
//		System.out.println("write");
//		String filename = "";
//		DirectedGraph graph = null;
//		GraphViz instance = null;
//		instance.write(filename, graph);
//		fail("The test case is a prototype.");
//	}
//
//	@Test
//	public void testWrite_3args() {
//		System.out.println("write");
//		String filename = "";
//		String type = "";
//		DirectedGraph graph = null;
//		GraphViz instance = null;
//		instance.write(filename, type, graph);
//		fail("The test case is a prototype.");
//	}
//
//	@Test
//	public void testSetCleanup() {
//		System.out.println("setCleanup");
//		boolean value = false;
//		GraphViz instance = null;
//		instance.setCleanup(value);
//		fail("The test case is a prototype.");
//	}
//
//	@Test
//	public void testWriteDirected_DirectedGraph_PrintWriter() {
//		System.out.println("writeDirected");
//		DirectedGraph graph = null;
//		PrintWriter writer = null;
//		GraphViz instance = null;
//		instance.writeDirected(graph, writer);
//		fail("The test case is a prototype.");
//	}
//
//	@Test
//	public void testWriteDirected_DirectedGraph_String() {
//		System.out.println("writeDirected");
//		DirectedGraph graph = null;
//		String filename = "";
//		GraphViz instance = null;
//		instance.writeDirected(graph, filename);
//		fail("The test case is a prototype.");
//	}
//
//	@Test
//	public void testWriteDirected_3args() {
//		System.out.println("writeDirected");
//		DirectedGraph graph = null;
//		String filename = "";
//		String type = "";
//		GraphViz instance = null;
//		instance.writeDirected(graph, filename, type);
//		fail("The test case is a prototype.");
//	}

	@Test
	public void testWriteSimpleGraphWithOnlyDefaultValues () {
		DirectedGraph graph = generateSimpleGraphWithOnlyDefaultValues ();
		StringWriter writer = new StringWriter ();
		new GraphViz ("").writeDirected (graph, new PrintWriter (writer));
		assertEquals (DOT_FILE_FOR_SIMPLE_GRAPH_WITH_ONLY_DEFAULT_VALUES, writer.toString ());
	}
	
	private DirectedGraph generateSimpleGraphWithOnlyDefaultValues () {
		DirectedGraph graph = new DirectedGraph ();
		Node node1 = graph.addNode ("node1");
		Node node2 = graph.addNode ("node2");
		graph.addEdge (node1, node2);
		
		Cluster cluster = graph.addCluster ("cluster");
		cluster.addNode (node1);
		return graph;
	}

	@Test
	public void testWriteSimpleGraphWithNoDefaultValues () {
		DirectedGraph graph = generateSimpleGraphWithNoDefaultValues ();
		StringWriter writer = new StringWriter ();
		new GraphViz ("").writeDirected (graph, new PrintWriter (writer));
		assertEquals (DOT_FILE_FOR_SIMPLE_GRAPH_WITH_NO_DEFAULT_VALUES, writer.toString ());
	}
	
	private DirectedGraph generateSimpleGraphWithNoDefaultValues () {
		DirectedGraph graph = new DirectedGraph ();
		graph.setConcentrateEdges (true);
		graph.setLabel ("graph label");
		graph.setStyle (Style.DASHED);
		graph.setFillColour (Colour.AQUA);
		graph.setTextColour (Colour.RED);
		graph.setFontName ("Arial");
		graph.setFontSize (8);

		graph.setDefaultNodeShape (Shape.DIAMOND);
		graph.setDefaultNodeStyle (Style.DASHED);
		graph.setDefaultNodeLineColour (Colour.RED);
		graph.setDefaultNodeLineWidth (1);
		graph.setDefaultNodeFillColour (Colour.BEIGE);
		graph.setDefaultNodeTextColour (Colour.BLACK);
		graph.setDefaultNodeFontName ("Times-Roman");
		graph.setDefaultNodeFontSize (9);
		
		Cluster cluster = graph.addCluster ("cluster");
		cluster.setLabel ("cluster label");
		cluster.setStyle (Style.BOLD);
		cluster.setLineColour (Colour.AZURE);
		cluster.setLineWidth (2);
		cluster.setFillColour (Colour.BLUE);
		cluster.setTextColour (Colour.BROWN);
		cluster.setFontName ("Cambria");
		cluster.setFontSize (10);

		cluster.setDefaultNodeShape (Shape.HEXAGON);
		cluster.setDefaultNodeStyle (Style.DOTTED);
		cluster.setDefaultNodeLineColour (Colour.CORAL);
		cluster.setDefaultNodeLineWidth (3);
		cluster.setDefaultNodeFillColour (Colour.CRIMSON);
		cluster.setDefaultNodeTextColour (Colour.CYAN);
		cluster.setDefaultNodeFontName ("Courier new");
		cluster.setDefaultNodeFontSize (11);

		Node node1 = graph.addNode ("node1");
		node1.setLabel ("edge label");
		node1.setShape (Shape.OCTAGON);
		node1.setStyle (Style.FILLED);
		node1.setLineColour (Colour.FUCHSIA);
		node1.setLineWidth (4);
		node1.setFillColour (Colour.GOLD);
		node1.setTextColour (Colour.GRAY);
		node1.setFontName ("Something");
		node1.setFontSize (12);

		Node node2 = graph.addNode ("node2");

		Edge edge = graph.addEdge (node1, node2);
		edge.setLabel ("edge label");
		edge.setStyle (Style.INVISIBLE);
		edge.setLineColour (Colour.GREEN);
		edge.setLineWidth (5);
		edge.setFillColour (Colour.INDIGO);
		edge.setTextColour (Colour.IVORY);
		edge.setFontName ("Something else");
		edge.setFontSize (13);

		cluster.addNode (node1);
		return graph;
	}
}
