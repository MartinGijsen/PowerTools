package org.powertools.engine.reports;

import java.io.PrintStream;


final class Printer extends PrintStream {
	private boolean mAnyInput = false;
	

	public Printer () {
		super (System.out);
	}
	
	@Override
	public void print (String s) {
		mAnyInput = true;
	}

	@Override
	public void println (String s) {
		mAnyInput = true;
	}
	
	boolean anyInput () {
		return mAnyInput;
	}
}