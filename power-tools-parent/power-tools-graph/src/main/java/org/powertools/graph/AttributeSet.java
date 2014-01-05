/* Copyright 2013 by Martin Gijsen (www.DeAnalist.nl)
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


abstract class AttributeSet {
    protected String mLabel;
    protected Style mStyle;
    protected Colour mLineColour;
    protected String mLineWidth;
    protected Colour mFillColour;
    protected Colour mTextColour;
    protected String mFontName;
    protected String mFontSize;

    protected AttributeSet () {
        mLabel      = "";
        mStyle      = Style.DEFAULT;
        mLineColour = Colour.DEFAULT;
        mLineWidth  = "";
        mFillColour = Colour.DEFAULT;
        mTextColour = Colour.DEFAULT;
        mFontName   = "";
        mFontSize   = "";
    }

    public void setLabel (String label) {
        mLabel = label;
    }

    public String getLabel () {
        return mLabel;
    }

    public void setStyle (Style style) {
        mStyle = style;
    }

    public Style getStyle () {
        return mStyle;
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

    public void setFillColour (Colour colour) {
        mFillColour = colour;
    }

    public Colour getFillColour () {
        return mFillColour;
    }

    public void setTextColour (Colour colour) {
        mTextColour = colour;
    }

    public Colour getTextColour () {
        return mTextColour;
    }

    public void setFontName (String fontName) {
        mFontName = fontName;
    }

    public String getFontName () {
        return mFontName;
    }

    public void setFontSize (int fontSize) {
        mFontSize = Integer.toString (fontSize);
    }

    public String getFontSize () {
        return mFontSize;
    }
}
