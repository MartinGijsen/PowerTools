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


public final class Edge {
	final Node source;
	final Node target;
	final Attributes attributes;


	Edge (Node source, Node target) {
		this.source		= source;
		this.target		= target;
		this.attributes	= new Attributes ();
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
}