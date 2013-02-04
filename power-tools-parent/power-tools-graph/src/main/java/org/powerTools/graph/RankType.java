package org.powerTools.graph;


public enum RankType {
	SAME	("same"),
	MINIMUM	("min"),
	SOURCE	("source"),
	MAXIMUM	("max"),
	SINK	("sink");
	
	
	private String text;
	
	private RankType (String text) {
		this.text = text;
	}
	
	@Override
	public String toString () {
		return this.text;
	}
}