/*	Copyright 2013-2014 by Martin Gijsen (www.DeAnalist.nl)
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

import java.util.Iterator;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;


public class DirectedGraphTest {
	private final String NODE_NAME = "node name";
	private final String SOURCE_NODE_NAME = "source node name";
	private final String TARGET_NODE_NAME = "target node name";
	private final String CLUSTER_NAME = "cluster name";
	private final String EDGE_LABEL = "edge name";

	private DirectedGraph mGraph;

	
	@Before
	public void setup () {
		mGraph = new DirectedGraph ();
	}

	
	@Test
	public void testSetConcentrateEdgesGetConcentrateEdges () {
		mGraph.setConcentrateEdges (true);
		assertEquals (true, mGraph.getConcentrateEdges ());
	}

	@Test
	public void testSetRankDirectionGetRankDirection () {
		mGraph.setRankDirection (RankDirection.BOTTOM_TO_TOP);
		assertEquals (RankDirection.BOTTOM_TO_TOP, mGraph.getRankDirection ());
	}

	@Test
	public void testSetDistanceBetweenRanksGetDistanceBetweenRanks () {
		mGraph.setDistanceBetweenRanks (7);
		assertEquals (7.0f, mGraph.getDistanceBetweenRanks (), 0.001f);
	}

	@Test
	public void testSetDistanceBetweenNodesGetDistanceBetweenNodes () {
		mGraph.setDistanceBetweenNodes (7);
		assertEquals (7.0f, mGraph.getDistanceBetweenNodes (), 0.001f);
	}

	@Test
	public void testGetNode () {
		try {
			mGraph.getNode (NODE_NAME);
			fail ("no exception");
		} catch (GraphException ge) {
			// ok
		}
	}

	@Test
	public void testAddNode_String () {
		assertFalse (mGraph.hasNode (NODE_NAME));
		Node node = mGraph.addNode (NODE_NAME);
		assertTrue (mGraph.hasNode (NODE_NAME));
		assertEquals (node, mGraph.getNode (NODE_NAME));
		try {
			mGraph.addNode (NODE_NAME);
			fail ("no exception");
		} catch (GraphException ge) {
			// ok
		}
	}

	@Test
	public void testAddNode_String_Cluster() {
		Cluster cluster = mGraph.addCluster (CLUSTER_NAME);
		Node node = mGraph.addNode (NODE_NAME, cluster);
		assertEquals (node, mGraph.getNode (NODE_NAME));
		assertEquals (node, cluster.getNode (NODE_NAME));
	}

	@Test
	public void testGetRoot () {
		Node node1 = mGraph.addNode ("node1");
		mGraph.addNode ("node2");
		mGraph.addNode ("node3");
		mGraph.addEdge ("node1", "node2");
		mGraph.addEdge ("node1", "node3");
		mGraph.addEdge ("node2", "node3");
		assertEquals (node1, mGraph.getRoot ());
	}

	@Test
	public void testGetRoot_NoRoot () {
		mGraph.addNode ("node1");
		mGraph.addNode ("node2");
		mGraph.addNode ("node3");
		mGraph.addEdge ("node1", "node2");
		mGraph.addEdge ("node2", "node3");
		mGraph.addEdge ("node3", "node1");
		try {
			mGraph.getRoot ();
			fail ("no exception");
		} catch (GraphException ge) {
			assertTrue (ge.getMessage ().contains ("no start node"));
		}
	}

	@Test
	public void testGetRoot_MultiRoot () {
		Node node1 = mGraph.addNode ("node1");
		mGraph.addNode ("node2");
		mGraph.addNode ("node3");
		mGraph.addEdge ("node1", "node3");
		mGraph.addEdge ("node2", "node3");
		try {
			assertEquals (node1, mGraph.getRoot ());
			fail ("no exception");
		} catch (GraphException ge) {
			assertTrue (ge.getMessage ().contains ("multiple start nodes"));
		}
	}

	@Test
	public void testAddEdge_String_String () {
		Node source = mGraph.addNode (SOURCE_NODE_NAME);
		Node target = mGraph.addNode (TARGET_NODE_NAME);
		Edge edge = mGraph.addEdge (SOURCE_NODE_NAME, TARGET_NODE_NAME);
		assertEquals (source, edge.getSource ());
		assertEquals (target, edge.getTarget ());
		assertEquals (edge, mGraph.getEdge (source, target));

		try {
			mGraph.addEdge (SOURCE_NODE_NAME, TARGET_NODE_NAME);
			fail ("no exception");
		} catch (GraphException ge) {
			assertTrue (ge.getMessage ().contains ("already exists"));
		}
	}

	@Test
	public void testAddEdge_Node_String () {
		Node source = mGraph.addNode (SOURCE_NODE_NAME);
		Node target = mGraph.addNode (TARGET_NODE_NAME);
		assertFalse (mGraph.hasEdge (source, target));
		try {
			mGraph.getEdge (source, target);
			fail ("no exception");
		} catch (GraphException ge) {
			// ok
		}
		Edge edge = mGraph.addEdge (source, TARGET_NODE_NAME);
		assertEquals (source, edge.getSource ());
		assertEquals (target, edge.getTarget ());
		assertTrue (mGraph.hasEdge (source, target));
		assertEquals (edge, mGraph.getEdge (source, target));
		assertFalse (mGraph.hasEdge (source, mGraph.addNode ("some other node")));
	}

	@Test
	public void testAddEdge_String_Node () {
		Node source = mGraph.addNode (SOURCE_NODE_NAME);
		Node target = mGraph.addNode (TARGET_NODE_NAME);
		Edge edge = mGraph.addEdge (SOURCE_NODE_NAME, target);
		assertEquals (source, edge.getSource ());
		assertEquals (target, edge.getTarget ());
		assertEquals (edge, mGraph.getEdge (source, target));
	}

	@Test
	public void testAddEdge_Node_Node () {
		Node source = mGraph.addNode (SOURCE_NODE_NAME);
		Node target = mGraph.addNode (TARGET_NODE_NAME);
		Edge edge = mGraph.addEdge (source, target);
		assertEquals (source, edge.getSource ());
		assertEquals (target, edge.getTarget ());
		assertEquals (edge, mGraph.getEdge (source, target));
	}

	@Test
	public void testAddEdge_String_String_String () {
		Node source = mGraph.addNode (SOURCE_NODE_NAME);
		Node target = mGraph.addNode (TARGET_NODE_NAME);
		Edge edge = mGraph.addEdge (SOURCE_NODE_NAME, TARGET_NODE_NAME, EDGE_LABEL);
		assertEquals (source, edge.getSource ());
		assertEquals (target, edge.getTarget ());
		Edge retrievedEdge = mGraph.getEdge (source, target);
		assertEquals (edge, retrievedEdge);
		assertEquals (EDGE_LABEL, retrievedEdge.getLabel ());

		try {
			mGraph.addEdge (SOURCE_NODE_NAME, TARGET_NODE_NAME);
			fail ("no exception");
		} catch (GraphException ge) {
			assertTrue (ge.getMessage ().contains ("already exists"));
		}
	}

	@Test
	public void testAddEdge_Node_String_String () {
		Node source = mGraph.addNode (SOURCE_NODE_NAME);
		Node target = mGraph.addNode (TARGET_NODE_NAME);
		Edge edge = mGraph.addEdge (source, TARGET_NODE_NAME, EDGE_LABEL);
		assertEquals (source, edge.getSource ());
		assertEquals (target, edge.getTarget ());
		Edge retrievedEdge = mGraph.getEdge (source, target);
		assertEquals (edge, retrievedEdge);
		assertEquals (EDGE_LABEL, retrievedEdge.getLabel ());
	}

	@Test
	public void testAddEdge_String_Node_String () {
		Node source = mGraph.addNode (SOURCE_NODE_NAME);
		Node target = mGraph.addNode (TARGET_NODE_NAME);
		Edge edge = mGraph.addEdge (SOURCE_NODE_NAME, target, EDGE_LABEL);
		assertEquals (source, edge.getSource ());
		assertEquals (target, edge.getTarget ());
		Edge retrievedEdge = mGraph.getEdge (source, target);
		assertEquals (edge, retrievedEdge);
		assertEquals (EDGE_LABEL, retrievedEdge.getLabel ());
	}

	@Test
	public void testAddEdge_Node_Node_String () {
		Node source = mGraph.addNode (SOURCE_NODE_NAME);
		Node target = mGraph.addNode (TARGET_NODE_NAME);
		Edge edge = mGraph.addEdge (source, target, EDGE_LABEL);
		assertEquals (source, edge.getSource ());
		assertEquals (target, edge.getTarget ());
		Edge retrievedEdge = mGraph.getEdge (source, target);
		assertEquals (edge, retrievedEdge);
		assertEquals (EDGE_LABEL, retrievedEdge.getLabel ());
	}

	@Test
	public void testGetEdges () {
		Node node1 = mGraph.addNode ("node1");
		Node node2 = mGraph.addNode ("node2");
		Node node3 = mGraph.addNode ("node3");
		assertTrue (mGraph.getEdges (node1).isEmpty ());
		Edge edge1 = mGraph.addEdge ("node1", "node2");
		Edge edge2 = mGraph.addEdge ("node1", "node3");
		Edge edge3 = mGraph.addEdge ("node2", "node3");
		Set<Edge> edges = mGraph.getEdges (node1);
		assertTrue (edges.contains (edge1));
		assertTrue (edges.contains (edge2));
		assertFalse (edges.contains (edge3));
	}

	@Test
	public void testAddCluster_new () {
		mGraph.addCluster ("some cluster name");
		assertNull (mGraph.getCluster (CLUSTER_NAME));
		Cluster cluster = mGraph.addCluster (CLUSTER_NAME);
		assertEquals (cluster, mGraph.getCluster (CLUSTER_NAME));
	}

	@Test
	public void testAddCluster_existing () {
		mGraph.addCluster ("some cluster name");
		assertNull (mGraph.getCluster (CLUSTER_NAME));
		Cluster cluster = mGraph.addCluster (CLUSTER_NAME);
		assertEquals (cluster, mGraph.getCluster (CLUSTER_NAME));
   		try {
            mGraph.addCluster (CLUSTER_NAME);
            fail ("no exception");
        } catch (GraphException ge) {
            // ok
        }
		assertEquals (cluster, mGraph.getCluster (CLUSTER_NAME));
	}

	@Test
	public void testAddRank () {
		String RANK_NAME = "rank name";
		Rank rank = mGraph.addRank (RANK_NAME, RankType.SAME);
		assertEquals (rank, mGraph.getRank (RANK_NAME));
		try {
			mGraph.addRank (RANK_NAME, RankType.SAME);
			fail ("no exception");
		} catch (GraphException ge) {
			// ok
		}
	}

	@Test
	public void testNodeIterator () {
		Node node1 = mGraph.addNode ("node1");
		Node node2 = mGraph.addNode ("node2");
		Iterator<Node> iter = mGraph.nodeIterator ();
		Node result1 = iter.next ();
		Node result2 = iter.next ();
		assertTrue (result1 == node1 || result2 == node1);
		assertTrue (result1 == node2 || result2 == node2);
		assertFalse (iter.hasNext ());
	}
}
