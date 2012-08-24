/*	Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
 *
 *	This file is part of the PowerTools engine.
 *
 *	The PowerTools engine is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the License,
 *	or (at your option) any later version.
 *
 *	The PowerTools engine is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU Affero General Public License for more details.
 *
 *	You should have received a copy of the GNU Affero General Public License
 *	along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powerTools.engine.sources;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.powerTools.engine.ExecutionException;
import org.powerTools.engine.symbol.Scope;


abstract class ExcelTestSource extends TestSource {
	public final String mFileName;
	public final String mSheetName;

	private final Iterator<Row> mRowIter;

	
	protected static class Names {
		public final String mFileName;
		public final String mSheetName;
		
		public Names (String fileName, String sheetName) {
			mFileName	= fileName;
			mSheetName	= sheetName;
		}
	}
	

	protected ExcelTestSource (String fileName, String sheetName, Scope scope) {
		super (scope);
		mFileName			= fileName;
		Workbook workbook	= createWorkbook (fileName);
		mSheetName			= sheetName.isEmpty () ? workbook.getSheetName (0) : sheetName;
		Sheet sheet			= workbook.getSheet (mSheetName);
		if (sheet == null) {
			throw new ExecutionException ("sheet '" + fileName + "' does not exist");
		}
		mRowIter = sheet.rowIterator ();
	}

	protected abstract Workbook createWorkbook (String fileName);


	@Override
	public final void initialize () { }

	@Override
	public final TestLineImpl getTestLine () {
		while (mRowIter.hasNext ()) {
			final Row row = mRowIter.next ();
			final int nrOfParts = getNrOfParts (row);

			if (nrOfParts > 0) {
				mTestLine.createParts (nrOfParts);
	
				for (int cellNr = 0; cellNr < nrOfParts; ++cellNr) {
					mTestLine.setPart (cellNr, getValue (row.getCell (cellNr)).trim ());
				}
	
				if (!mTestLine.isEmpty ()) {
					return mTestLine;
				}
			}
		}

		return null;
	}


	protected static Names createNamesFromFileName (String sourceName) {
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