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


abstract class AttributeSet3 extends AttributeSet1 {
	protected Shape mDefaultNodeShape;
	protected Style mDefaultNodeStyle;
	protected Colour mDefaultNodeLineColour;
	protected String mDefaultNodeLineWidth;
	protected Colour mDefaultNodeFillColour;
	protected Colour mDefaultNodeTextColour;
	protected String mDefaultNodeFontName;
	protected String mDefaultNodeFontSize;
	
	protected AttributeSet3 () {
		super ();
		mDefaultNodeShape		= Shape.DEFAULT;
		mDefaultNodeStyle		= Style.DEFAULT;
		mDefaultNodeLineColour	= Colour.DEFAULT;
		mDefaultNodeLineWidth	= "";
		mDefaultNodeFillColour	= Colour.DEFAULT;
		mDefaultNodeTextColour	= Colour.DEFAULT;
		mDefaultNodeFontName	= "";
		mDefaultNodeFontSize	= "";
	}

	public void setDefaultNodeShape (Shape shape) {
		mDefaultNodeShape = shape;
	}

	public Shape getDefaultNodeShape () {
		return mDefaultNodeShape;
	}
	
	public void setDefaultNodeStyle (Style style) {
		mDefaultNodeStyle = style;
	}

	public Style getDefaultNodeStyle () {
		return mDefaultNodeStyle;
	}
	
	public void setDefaultNodeLineColour (Colour colour) {
		mDefaultNodeLineColour = colour;
	}

	public Colour getDefaultNodeLineColour () {
		return mDefaultNodeLineColour;
	}
	
	public void setDefaultNodeLineWidth (int width) {
		mDefaultNodeLineWidth = Integer.toString (width);
	}

	public String getDefaultNodeLineWidth () {
		return mDefaultNodeLineWidth;
	}

	public void setDefaultNodeFillColour (Colour colour) {
		mDefaultNodeFillColour = colour;
	}

	public Colour getDefaultNodeFillColour () {
		return mDefaultNodeFillColour;
	}

	public void setDefaultNodeTextColour (Colour colour) {
		mDefaultNodeTextColour = colour;
	}

	public Colour getDefaultNodeTextColour () {
		return mDefaultNodeTextColour;
	}

	public void setDefaultNodeFontName (String fontName) {
		mDefaultNodeFontName = fontName;
	}

	public String getDefaultNodeFontName () {
		return mDefaultNodeFontName;
	}

	public void setDefaultNodeFontSize (int fontSize) {
		mDefaultNodeFontSize = Integer.toString (fontSize);
	}

	public String getDefaultNodeFontSize () {
		return mDefaultNodeFontSize;
	}
}
