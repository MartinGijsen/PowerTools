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

import java.util.HashSet;
import java.util.Set;


public final class Cluster {
	final Attributes attributes;
	final Attributes defaultNodeAttributes;
	
	String label;
	final Set<Node> nodes;


	Cluster (String label) {
		this.label					= label;
		this.attributes				= new Attributes ();
		this.defaultNodeAttributes	= new Attributes ();
		this.nodes					= new HashSet<Node> ();
	}

	public void setLabel (String label) {
		this.label = label;
	}

	public void setStyle (Style style) {
		this.attributes.style = style;
	}

	public void setLineColour (Colour colour) {
		this.attributes.lineColour = colour;
	}

	public void setLineWidth (int width) {
		this.attributes.lineWidth = Integer.toString (width);
	}

	public void setFillColour (Colour colour) {
		this.attributes.fillColour = colour;
	}

	public void setTextColour (Colour colour) {
		this.attributes.textColour = colour;
	}
	
	public void setFontName (String fontName) {
		this.attributes.fontName = fontName;
	}
	
	public void setFontSize (int fontSize) {
		this.attributes.fontSize = Integer.toString (fontSize);
	}

	
	public void setDefaultNodeShape (Shape shape) {
		this.defaultNodeAttributes.shape = shape;
	}

	public void setDefaultNodeStyle (Style style) {
		this.defaultNodeAttributes.style = style;
	}
	
	public void setDefaultNodeLineColour (Colour colour) {
		this.defaultNodeAttributes.lineColour = colour;
	}
	
	public void setDefaultNodeLineWidth (int width) {
		this.defaultNodeAttributes.lineWidth = Integer.toString (width);
	}
	
	public void setDefaultNodeFillColour (Colour colour) {
		this.defaultNodeAttributes.fillColour = colour;
	}
	
	public void setDefaultNodeTextColour (Colour colour) {
		this.defaultNodeAttributes.textColour = colour;
	}
	
	public void setDefaultNodeFontName (String fontName) {
		this.defaultNodeAttributes.fontName = fontName;
	}
	
	public void setDefaultNodeFontSize (String fontSize) {
		this.defaultNodeAttributes.fontSize = fontSize;
	}

	
	void add (Node node) {
		this.nodes.add (node);
	}
}