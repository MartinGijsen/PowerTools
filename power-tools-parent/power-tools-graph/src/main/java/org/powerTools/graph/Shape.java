package org.powerTools.graph;


public enum Shape {
	DEFAULT				("<default>"),
	POLYGON				("polygon"),
	ELLIPSE				("ellipse"),
	OVAL				("oval"),
	CIRCLE				("circle"),
	POINT				("point"),
	EGG					("egg"),
	TRIANGLE			("triangle"),
	TEXT				("plaintext"),
	DIAMOND				("diamond"),
	TRAPEZIUM			("trapezium"),
	PARALLELOGRAM		("parallelogram"),
	HOUSE				("house"),
	PENTAGON			("pentagon"),
	HEXAGON				("hexagon"),
	SEPTAGON			("septagon"),
	OCTAGON				("octagon"),
	DOUBLE_CIRCLE		("doublecircle"),
	DOUBLE_OCTAGON		("doubleoctagon"),
	TRIPLE_OCTAGON		("tripleoctagon"),
	INVERTED_TRIANGLE	("invtriangle"),
	INVERTED_TRAPEZIUM	("invtrapezium"),
	INVERTED_HOUSE		("invhouse"),
	M_DIAMOND			("Mdiamond"),
	M_SQUARE			("Msquare"),
	M_CIRCLE			("Mcircle"),
	RECTANGLE			("rectangle"),
	SQUARE				("square"),
	CUBE				("box3d");
	
	private String text;
	
	private Shape (String text) {
		this.text = text;
	}
	
	@Override
	public String toString () {
		return this.text;
	}
}