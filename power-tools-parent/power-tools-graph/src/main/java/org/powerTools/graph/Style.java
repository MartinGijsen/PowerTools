package org.powerTools.graph;


public enum Style {
	DEFAULT		("<default>"),
	SOLID		("solid"),
	DASHED		("dashed"),
	DOTTED		("dotted"),
	INVISIBLE	("invis"),
	BOLD		("bold"),
	FILLED		("filled"),
	ROUNDED		("rounded");
	
	private final String text;
	
	private Style (String text) {
		this.text = text;
	}
	
	@Override
	public String toString () {
		return this.text;
	}
}