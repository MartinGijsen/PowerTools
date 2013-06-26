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

package org.powerTools.engine.reports;

import java.io.PrintWriter;

import org.powerTools.engine.TestLine;


public class KeywordsHtmlLog extends BasicHtmlLog {
	KeywordsHtmlLog (PrintWriter writer, String title) {
		super (writer, title);
	}


	// the input
	@Override
	public void processTestLine (TestLine testLine) {
		mWriter.println ("<TABLE border=\"1\" cellspacing=\"0\">");
		writeTableRowStartWithTimestamp (mWriter);
		final int nrOfParts = testLine.getNrOfParts ();
		for (int argNr = 0; argNr < nrOfParts - 1; ++argNr) {
//			final String originalPart = testLine.getOriginalPart (argNr);
//			if (originalPart != null) {
//				mWriter.format("<TD>%s<HR/>%s</TD>", originalPart, testLine.getPart (argNr));
//			} else {
//				mWriter.format("<TD>%s</TD>", testLine.getPart (argNr));
//			}
			mWriter.format ("<TD>%s</TD>", getCell (testLine, argNr));
		}
		mWriter.append ("<TD colspan=\"10\">");
		if (mLevel == 0) {
			mWriter.format ("<A id=\"id%d\">", ++mLastId);
		}
//		mWriter.append (testLine.getPart (nrOfParts - 1)).println ("</TD></TR>");
		mWriter.append (getCell (testLine, nrOfParts - 1)).println ("</TD></TR>");
	}

	@Override
	public void processCommentLine (String testLine) {
		mWriter.println ("<TABLE border=\"1\" cellspacing=\"0\">");
		writeTableRowStartWithTimestamp (mWriter);
		mWriter.format ("<TD></TD><TD colspan=\"10\">%s</TD></TR>", testLine).println ();
		mWriter.println ("</TABLE><BR/>");
	}

	@Override
	public void processCommentLine (TestLine testLine) {
		mWriter.println ("<TABLE border=\"1\" cellspacing=\"0\">");
		writeTableRowStartWithTimestamp (mWriter);
		mWriter.append ("<TD></TD>");
		final int nrOfParts = testLine.getNrOfParts ();
		for (int partNr = 1; partNr < nrOfParts - 1; ++partNr) {
			mWriter.format ("<TD>%s</TD>", testLine.getPart (partNr));
		}
		mWriter.format ("<TD colspan=\"10\">%s</TD></TR>", testLine.getPart (nrOfParts - 1)).println ();
		mWriter.println ("</TABLE><BR/>");
	}

	@Override
	public void processEndSection () { }

	
	@Override
	public void processEndOfTestLine () {
		mWriter.println ("</TABLE><BR/>");
		mWriter.flush ();
	}
}