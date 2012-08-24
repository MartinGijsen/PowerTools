package org.powerTools.engine.instructions;

import java.util.Date;


final class MethodExecutorObject {
	MethodExecutorObject () {
		;
	}
	
	void voidReturningMethod () {
		;
	}
	
	boolean failingMethod () {
		return false;
	}
	
	boolean noParametersMethod () {
		return true;
	}
	
	boolean intParameterMethod (int i) {
		return true;
	}
	
	boolean longParameterMethod (long l) {
		return true;
	}
	
	boolean booleanParameterMethod (boolean b) {
		return true;
	}
	
	boolean floatParameterMethod (float f) {
		return true;
	}
	
	boolean doubleParameterMethod (double d) {
		return true;
	}

	boolean dateParameterMethod (Date d) {
		return true;
	}
}