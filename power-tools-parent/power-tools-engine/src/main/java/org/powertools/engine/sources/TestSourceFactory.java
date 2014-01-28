/* Copyright 2013-1014 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools engine.
 *
 * The PowerTools engine is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine.sources;

import org.powertools.engine.ExecutionException;
import org.powertools.engine.TestLine;
import org.powertools.engine.RunTime;
import org.powertools.engine.instructions.ProcedureRunner;
import org.powertools.engine.sources.ExcelTestSource.Names;
import org.powertools.engine.sources.model.MainModel;
import org.powertools.engine.symbol.Scope;


public final class TestSourceFactory {
    public TestSource createExcelTestSource (String sourceName, Scope globalScope) {
        Names names = ExcelTestSource.createNamesFromSourceName (sourceName);

        String fileName = names.getFileName ();
        if (fileName.endsWith (".xls")) {
            return XlsTestSource.createTestSource (fileName, names.getSheetName (), globalScope);
        } else if (fileName.endsWith (".xlsx")) {
            return XlsxTestSource.createTestSource (fileName, names.getSheetName (), globalScope);
        } else {
            throw new ExecutionException ("invalid file extension");
        }
    }

    public TestSource createModelTestSource (String path, String fileName, String selector, String doneCondition, RunTime runTime, ProcedureRunner runner) {
        return new ModelTestSource (new MainModel (path, fileName, selector, doneCondition, runTime), runTime, runner);
    }

    public TestSource createProcedureTestSource (Procedure procedure, Scope parentScope, TestLine testLine) {
        return new ProcedureTestSource (procedure, parentScope, testLine);
    }
}
