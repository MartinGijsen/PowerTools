package org.powerTools.graph;


public enum Direction {
	DEFAULT ("<default>"),
	TOP_TO_BOTTOM ("TB"),
	BOTTOM_TO_TOP ("BT"),
	LEFT_TO_RIGHT ("LR"),
	RIGHT_TO_LEFT ("RL");
	
	private String text;
	
	private Direction (String text) {
		this.text = text;
	}
	
	@Override
	public String toString () {
		return this.text;
	}
}