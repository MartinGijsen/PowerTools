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


public final class Node {
	private final String name;
	private final Attributes attributes;


	Node (String name) {
		if (name == null || name.isEmpty ()) {
			throw new GraphException ("empty node name");
		}
		this.name		= name;
		this.attributes	= new Attributes ();
	}

	public String getName () {
		return this.name;
	}
	
	public void setLabel (String label) {
		this.attributes.label = label;
	}

	public String getLabel () {
		return this.attributes.label;
	}

	public void setShape (Shape shape) {
		this.attributes.shape = shape;
	}

	public Shape getShape () {
		return this.attributes.shape;
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
}