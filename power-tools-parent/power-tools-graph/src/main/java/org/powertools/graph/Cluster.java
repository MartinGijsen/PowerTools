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

import java.util.HashSet;
import java.util.Set;


public final class Cluster extends AttributeSet3 {
	private Colour mLineColour;
	private String mLineWidth;
	private final Set<Node> mNodes;


	Cluster (String label) {
		super ();
		mLabel		= label;
		mLineColour	= Colour.DEFAULT;
		mLineWidth	= "";
		mNodes		= new HashSet<Node> ();
	}

	public void setLineColour (Colour colour) {
		mLineColour = colour;
	}

	public Colour getLineColour () {
		return mLineColour;
	}
	
	public void setLineWidth (int width) {
		mLineWidth = Integer.toString (width);
	}

	public String getLineWidth () {
		return mLineWidth;
	}

	void addNode (Node node) {
		mNodes.add (node);
	}
	
	Node getNode (String name) {
		for (Node node : mNodes) {
			if (node.getName ().equals (name)) {
				return node;
			}
		}
		return null;
	}
	
	Set<Node> getNodes () {
		return mNodes;
	}
}