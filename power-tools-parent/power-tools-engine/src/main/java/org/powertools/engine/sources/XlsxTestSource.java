/* Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.symbol.Scope;


final class XlsxTestSource extends ExcelTestSource {
    private XlsxTestSource (String fileName, String sheetName, Scope scope, TestRunResultPublisher publisher) {
        super (fileName, sheetName, scope, publisher);
    }


    static XlsxTestSource createTestSource (String fileName, String sheetName, Scope scope, TestRunResultPublisher publisher) {
        return new XlsxTestSource (fileName, sheetName, scope, publisher);
    }

    @Override
    public TestSource create (String sourceName) {
        Names names = createNamesFromSheetName (sourceName);
        return new XlsxTestSource (names.getFileName (), names.getSheetName (), mScope, mPublisher);
    }

    @Override
    protected Workbook createWorkbook (String fileName) {
        try {
            return new XSSFWorkbook (new FileInputStream (fileName));
        } catch (FileNotFoundException fnfe) {
            throw new ExecutionException ("file '" + fileName + "' not found");
        } catch (IOException ioe) {
            throw new ExecutionException ("IO exception");
        }
    }
}
