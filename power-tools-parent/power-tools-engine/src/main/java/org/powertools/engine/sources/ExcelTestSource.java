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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.symbol.Scope;


abstract class ExcelTestSource extends TestSource {
    final String mFileName;
    final String mSheetName;

    private static final String DEFINE_PROCEDURE_INSTRUCTION = "define instruction";
    private static final String END_PROCEDURE_INSTRUCTION    = "end instruction";

    private final Iterator<Row> mRowIter;


    protected static class Names {
        private final String mFileName;
        private final String mSheetName;

        public Names (String fileName, String sheetName) {
            mFileName  = fileName;
            mSheetName = sheetName;
        }

        public String getFileName () {
            return mFileName;
        }

        public String getSheetName () {
            return mSheetName;
        }
    }


    protected ExcelTestSource (String fileName, String sheetName, Scope scope, TestRunResultPublisher publisher) {
        super (scope, publisher);
        mFileName         = fileName;
        Workbook workbook = createWorkbook (fileName);
        mSheetName        = sheetName.isEmpty () ? workbook.getSheetName (0) : sheetName;
        Sheet sheet       = workbook.getSheet (mSheetName);
        if (sheet == null) {
            throw new ExecutionException ("sheet '" + mSheetName + "' does not exist");
        }
        mRowIter = sheet.rowIterator ();
    }

    protected abstract Workbook createWorkbook (String fileName);


    @Override
    public final TestLineImpl getTestLine () {
        while (mRowIter.hasNext ()) {
            Row row       = mRowIter.next ();
            int nrOfParts = getNrOfParts (row);

            if (nrOfParts > 0) {
                mTestLine.createParts (nrOfParts);

                for (int cellNr = 0; cellNr < nrOfParts; ++cellNr) {
                    mTestLine.setPart (cellNr, getValue (row.getCell (cellNr)).trim ());
                }

                if (mTestLine.getPart(0).equals (DEFINE_PROCEDURE_INSTRUCTION)) {
                    processProcedureDefinition ();
                } else if (!mTestLine.isEmpty ()) {
                    return mTestLine;
                }
            }
        }

        return null;
    }

    private void processProcedureDefinition () {
        Procedure procedure = processProcedureHeader ();
        processProcedureBody (procedure);
        if (procedure != null) {
            throw new ProcedureException (procedure);
        }
    }

    private Procedure processProcedureHeader () {
        mPublisher.publishTestLine (mTestLine);
        int nrOfParts = mTestLine.getNrOfParts ();
        if (nrOfParts < 2 || mTestLine.getPart (1).isEmpty ()) {
            mPublisher.publishError ("missing instruction name");
            mPublisher.publishEndOfTestLine ();
            return null;
        } else {
            Procedure procedure = new Procedure (mTestLine.getPart (1));
            for (int partNr = 2; partNr < nrOfParts; ++partNr) {
                String parameterName = mTestLine.getPart (partNr);
                boolean isOutput = (parameterName.startsWith (OUTPUT_PARAMETER_PREFIX));
                String realParameterName = (isOutput ? parameterName.substring (OUTPUT_PARAMETER_PREFIX.length ()).trim () : parameterName);
                procedure.addParameter (realParameterName, isOutput);
            }
            mPublisher.publishEndOfTestLine ();
            return procedure;
        }
    }

    private void processProcedureBody (Procedure procedure) {
        List<String> parts = new LinkedList<String> ();
        while (readNextLine (parts)) {
            mTestLine.setParts (parts);
            mPublisher.publishTestLine (mTestLine);
            if (parts.get (0).equals (END_PROCEDURE_INSTRUCTION)) {
                mPublisher.publishEndOfTestLine ();
                return;
            } else if (procedure != null) {
                procedure.addInstruction (parts);
            }
            mPublisher.publishEndOfTestLine ();
            parts = new LinkedList<String> ();
        }
        throw new ExecutionException ("instruction incomplete at end of sheet");
    }

    private boolean readNextLine (List<String> parts) {
        while (mRowIter.hasNext ()) {
            Row row       = mRowIter.next ();
            int nrOfParts = getNrOfParts (row);

            if (nrOfParts > 0) {
                for (int cellNr = 0; cellNr < nrOfParts; ++cellNr) {
                    parts.add (cellNr, getValue (row.getCell (cellNr)).trim ());
                }
                return true;
            }
        }
        return false;
    }

    static final Names createNamesFromSourceName (String sourceName) {
        int separatorPosition = sourceName.indexOf ('@');
        if (separatorPosition > 0) {
            return new Names (sourceName.substring (0, separatorPosition), sourceName.substring (separatorPosition + 1));
        } else {
            return new Names (sourceName, "");
        }
    }

    protected final Names createNamesFromSheetName (String sourceName) {
        int separatorPosition = sourceName.indexOf ('@');
        if (separatorPosition > 0) {
            return new Names (sourceName.substring (0, separatorPosition), sourceName.substring (separatorPosition + 1));
        } else {
            return new Names (mFileName, sourceName);
        }
    }


    private int getNrOfParts (Row row) {
        int nrOfParts = row.getLastCellNum ();

        while (nrOfParts > 0) {
            Cell cell = row.getCell (nrOfParts - 1);
            if (cell != null && !cell.toString ().isEmpty ()) {
                break;
            } else {
                --nrOfParts;
            }
        }
        return nrOfParts;
    }

    private String getValue (Cell cell) {
        if (cell == null) {
            return "";
        } else {
            // remove the trailing ".0" if present
            String value = cell.toString ();
            if (value.length () > 2 && value.endsWith (".0")) {
                value = value.substring (0, value.length () - 2);
            }
            return value;
        }
    }
}
