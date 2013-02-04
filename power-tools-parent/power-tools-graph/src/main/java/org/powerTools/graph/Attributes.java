package org.powerTools.graph;


final class Attributes {
	String label;
	Shape shape;
	Style style;
	Colour lineColour;
	String lineWidth;
	Colour fillColour;
	Colour textColour;
	String fontName;
	String fontSize;
	
	Attributes () {
		this.label		= "";
		this.shape		= Shape.DEFAULT;
		this.style		= Style.DEFAULT;
		this.lineColour	= Colour.DEFAULT;
		this.lineWidth	= "";
		this.fillColour	= Colour.DEFAULT;
		this.textColour	= Colour.DEFAULT;
		this.fontName	= "";
		this.fontSize	= "";
	}
}