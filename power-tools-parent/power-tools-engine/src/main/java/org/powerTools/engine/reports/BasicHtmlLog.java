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
import java.util.Date;


public abstract class BasicHtmlLog implements TestLineSubscriber, TestResultSubscriber {
	protected final PrintWriter mWriter;

	protected int mLevel;
	protected int mLastId;

	
	public BasicHtmlLog (PrintWriter writer, String title) {
		mLevel	= 0;
		mLastId	= 0;
		
		mWriter = writer;
		mWriter.format ("<HTML><HEAD><TITLE>%s</TITLE>", title).println ();
		mWriter.println ("<STYLE type=\"text/css\">");
		mWriter.println ("table { border-width: 1px 1px 0px 0px }");
		mWriter.println ("td { border-width: 0px 0px 1px 1px }");
		mWriter.println ("</STYLE>");
		mWriter.println ("</HEAD>");
		mWriter.println ("<BODY>");
	}


	// start and finish the test run
	@Override
	public void start (Date dateTime) { }
	@Override
	public void finish (Date dateTime) { }


	// the input
//	@Override
//	public void processCommentLine (String testLine) {
//		mWriter.format ("<TR><TD></TD><TD colspan=\"10\">%s</TD></TR>", testLine).println ();
//	}

//	@Override
//	public void processCommentLine (ITestLine testLine) {
//		mWriter.append ("<TR><TD></TD>");
//		final int nrOfParts = testLine.getNrOfParts ();
//		for (int partNr = 1; partNr < nrOfParts - 1; ++partNr) {
//			mWriter.format ("<TD>%s</TD>", testLine.getEvaluatedPart (partNr));
//		}
//		mWriter.format ("<TD colspan=\"10\">%s</TD></TR>", testLine.getEvaluatedPart (nrOfParts - 1)).println ();
//	}

//	@Override
//	public void processEndOfSection () { }


	// the results
	@Override
	public void processStackTrace (String[] stackTraceLines) {
		mWriter.println ("<TR><TD colspan=\"10\">stack trace:<BR/>");
		int nrOfElements = stackTraceLines.length;
		for (int elementNr = 0; elementNr < nrOfElements; ++elementNr) {
			mWriter.append (stackTraceLines[elementNr]).println ("<BR/>");
		}
		mWriter.println ("</TD></TR>");
	}

	@Override
	public void processError (String message) {
		mWriter.format ("<TR><TD colspan=\"10\" style=\"background-color:#FFAAAA\">%s</TD></TR>", message).println ();
	}

	@Override
	public void processWarning (String message) {
		mWriter.format ("<TR><TD colspan=\"10\" style=\"background-color:#FFFFAA\">%s</TD></TR>", message).println ();
	}

	@Override
	public void processInfo (String message) {
		mWriter.format ("<TR><TD colspan=\"10\">%s</TD></TR>", message).println ();
	}

	@Override
	public void processIncreaseLevel () {
		++mLevel;
		mWriter.println ("<TR><TD colspan=\"10\"><BR/><TABLE border=\"1\" cellspacing=\"0\">");
	}
	
	@Override
	public void processDecreaseLevel () {
		--mLevel;
		mWriter.println ("</TABLE><BR/></TD></TR>");
	}
	
//	@Override
//	public void processEndOfTestLine () {
//		mWriter.println ("</TABLE><BR/>");
//		mWriter.flush ();
//	}
}