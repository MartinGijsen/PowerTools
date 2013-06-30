package org.powerTools.engine.sources;

import org.powerTools.engine.ExecutionException;
import org.powerTools.engine.TestLine;
import org.powerTools.engine.core.RunTimeImpl;
import org.powerTools.engine.sources.ExcelTestSource.Names;
import org.powerTools.engine.symbol.Scope;


public final class TestSourceFactory {
	private TestSourceFactory () {
		;
	}
	
	
	public static TestSource createExcelTestSource (String sourceName) {
		Names names = ExcelTestSource.createNamesFromSourceName (sourceName);

		if (names.mFileName.endsWith (".xls")) {
			return XlsTestSource.createTestSource (names.mFileName, names.mSheetName, Scope.getGlobalScope ());
		} else if (names.mFileName.endsWith (".xlsx")) {
			return XlsxTestSource.createTestSource (names.mFileName, names.mSheetName, Scope.getGlobalScope ());
		} else {
			throw new ExecutionException ("invalid file extension");
		}
	}

	public static TestSource createModelTestSource (String fileName, String selector, String doneCondition, Scope scope, RunTimeImpl runTime) {
		return new ModelTestSource (fileName, selector, doneCondition, scope, runTime);
	}
	
	public static TestSource createProcedureTestSource (Procedure procedure, Scope parentScope, TestLine testLine) {
		return new ProcedureTestSource (procedure, parentScope, testLine);
	}
}