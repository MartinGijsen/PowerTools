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

package org.powertools.engine.fitnesse;

import java.util.ArrayList;
import java.util.List;

import org.powertools.engine.sources.TestSource;
import org.powertools.engine.symbol.Scope;

import fit.Fixture;
import fit.Parse;


abstract class BaseTestSource extends TestSource {
	protected Parse mRow;

	private final Fixture mFixture;
	private final String mLogFilePath;

	private static int mLastId = 0;


	protected BaseTestSource (Scope scope, Fixture fixture, Parse row, String logFilePath) {
		super (scope);
		mFixture		= fixture;
		mRow			= row;
		String rootDir	= FitNesseEngine.ROOT_DIRECTORY;
		mLogFilePath	= logFilePath.startsWith (rootDir) ? logFilePath.substring (rootDir.length ()) : logFilePath;
	}

	
	protected final void processFixtureLine () {
		mTestLine.createParts (1);
		mTestLine.setPart (0, mRow.parts.text ());
		linkToLogFile (mRow.parts);
		mPublisher.publishTestLine (mTestLine);
		if (mTestLine.getNrOfParts () != 1) {
			mPublisher.publishError ("fixture line must have one cell");
		}
		mPublisher.publishEndOfTestLine ();
	}


	protected final void processError () {
		mFixture.wrong (mRow);
	}

	protected final void processFinished (boolean anyErrors) {
		if (!anyErrors) {
			mFixture.right (mRow);
		}
	}


	protected final void linkToLogFile (Parse cell) {
		cell.body = String.format ("<A href=\"%s#id%d\">%s</A>", mLogFilePath, ++mLastId, cell.body);
	}

	protected final void skipLinkingToLogFile () {
		++mLastId;
	}

	
	protected final List<String> readSentence (Parse instructionNameCell) {
		String instructionName	 = instructionNameCell.text ();
		Parse currentCell		 = instructionNameCell;

		final List<String> parts = new ArrayList<String> ();
		parts.add ("");
		boolean isAParameter = true;
		while ((currentCell = currentCell.more) != null) {
			final String text = currentCell.text ();
			if (isAParameter) {
				parts.add (text);
				if (!instructionName.isEmpty ()) {
					instructionName += " _";
				}
			} else if (!text.isEmpty ()) {
				instructionName += " " + text;
			}
			isAParameter = !isAParameter;
		}
		parts.set (0, instructionName);
		return parts;
	}
}