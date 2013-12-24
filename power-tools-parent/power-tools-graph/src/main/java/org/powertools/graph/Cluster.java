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
import java.util.Iterator;
import java.util.Set;


public final class Cluster {
	private final Attributes attributes;
	final Attributes defaultNodeAttributes;
	
	private String label;
	private final Set<Node> nodes;


	Cluster (String label) {
		this.label					= label;
		this.attributes				= new Attributes ();
		this.defaultNodeAttributes	= new Attributes ();
		this.nodes					= new HashSet<Node> ();
	}

	public void setLabel (String label) {
		this.label = label;
	}

	public String getLabel () {
		return this.label;
	}

	public void setStyle (Style style) {
		this.attributes.style = style;
	}

	public Style getStyle () {
		return this.attributes.style;
	}

	public void setLineColour (Colour colour) {
		this.attributes.lineColour = colour;
	}

	public Colour getLineColour () {
		return this.attributes.lineColour;
	}

	public void setLineWidth (int width) {
		this.attributes.lineWidth = Integer.toString (width);
	}

	public String getLineWidth () {
		return this.attributes.lineWidth;
	}

	public void setFillColour (Colour colour) {
		this.attributes.fillColour = colour;
	}

	public Colour getFillColour () {
		return this.attributes.fillColour;
	}

	public void setTextColour (Colour colour) {
		this.attributes.textColour = colour;
	}
	
	public Colour getTextColour () {
		return this.attributes.textColour;
	}
	
	public void setFontName (String fontName) {
		this.attributes.fontName = fontName;
	}
	
	public String getFontName () {
		return this.attributes.fontName;
	}
	
	public void setFontSize (int fontSize) {
		this.attributes.fontSize = Integer.toString (fontSize);
	}

	public String getFontSize () {
		return this.attributes.fontSize;
	}

	
	public void setDefaultNodeShape (Shape shape) {
		this.defaultNodeAttributes.shape = shape;
	}

	public Shape getDefaultNodeShape () {
		return this.defaultNodeAttributes.shape;
	}

	public void setDefaultNodeStyle (Style style) {
		this.defaultNodeAttributes.style = style;
	}
	
	public Style getDefaultNodeStyle () {
		return this.defaultNodeAttributes.style;
	}
	
	public void setDefaultNodeLineColour (Colour colour) {
		this.defaultNodeAttributes.lineColour = colour;
	}
	
	public Colour getDefaultNodeLineColour () {
		return this.defaultNodeAttributes.lineColour;
	}
	
	public void setDefaultNodeLineWidth (int width) {
		this.defaultNodeAttributes.lineWidth = Integer.toString (width);
	}
	
	public String getDefaultNodeLineWidth () {
		return this.defaultNodeAttributes.lineWidth;
	}
	
	public void setDefaultNodeFillColour (Colour colour) {
		this.defaultNodeAttributes.fillColour = colour;
	}
	
	public Colour getDefaultNodeFillColour () {
		return this.defaultNodeAttributes.fillColour;
	}
	
	public void setDefaultNodeTextColour (Colour colour) {
		this.defaultNodeAttributes.textColour = colour;
	}
	
	public Colour getDefaultNodeTextColour () {
		return this.defaultNodeAttributes.textColour;
	}
	
	public void setDefaultNodeFontName (String fontName) {
		this.defaultNodeAttributes.fontName = fontName;
	}
	
	public String getDefaultNodeFontName () {
		return this.defaultNodeAttributes.fontName;
	}
	
	public void setDefaultNodeFontSize (String fontSize) {
		this.defaultNodeAttributes.fontSize = fontSize;
	}
	public String getDefaultNodeFontSize () {
		return this.defaultNodeAttributes.fontSize;
	}

	
	void add (Node node) {
		this.nodes.add (node);
	}
	
	Set<Node> getNodes () {
		return nodes;
	}
}