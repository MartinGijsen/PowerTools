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

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.powerTools.engine.TestLine;


final class Console implements TestLineSubscriber, TestResultSubscriber {
	private static final SimpleDateFormat mDateFormat = new SimpleDateFormat ("yyyy.MM.dd-HH.mm.ss");

	private final PrintStream mStream;


	Console () {
		this (System.out);
	}
	
	Console (PrintStream stream) {
		mStream = stream;
	}
	

	@Override
	public void start (Date dateTime) {
		mStream.println ("start: " + mDateFormat.format (dateTime));
	}
	
	@Override
	public void finish (Date dateTime) {
		mStream.println ("finish: " + mDateFormat.format (dateTime));
	}

	
	@Override
	public void processTestLine (final TestLine testLine) {
		final int nrOfParts = testLine.getNrOfParts ();
		for (int partNr = 0; partNr < nrOfParts; ++partNr) {
			mStream.print ("'" + testLine.getPart (partNr) + "' ");
		}
		mStream.println ();
	}

	@Override
	public void processCommentLine (final String testLine) {
		mStream.println ("comment: " + testLine);
	}

	@Override
	public void processCommentLine (final TestLine testLine) {
		mStream.print ("comment: ");
		final int nrOfParts = testLine.getNrOfParts ();
		for (int partNr = 1; partNr < nrOfParts; ++partNr) {
			mStream.print ("'" + testLine.getPart (partNr) + "' ");
		}
	}


	@Override
	public void processStackTrace (String[] stackTraceLines) {
		mStream.println ("stack trace:");
		int nrOfElements = stackTraceLines.length;
		for (int elementNr = 0; elementNr < nrOfElements; ++elementNr) {
			mStream.println ("\t" + stackTraceLines[elementNr]);
		}
	}
	
	@Override
	public void processError (final String error) {
		mStream.println ("error: " + error);
	}

	@Override
	public void processWarning (final String warning) {
		mStream.println ("warning: " + warning);
	}

	@Override
	public void processInfo (final String message) {
		mStream.println ("info: " + message);
	}

	@Override
	public void processEndSection () {
		mStream.println ();
	}

	
	// ignored events
	@Override
	public void processIncreaseLevel () { }
	@Override
	public void processDecreaseLevel () { }
	
	@Override
	public void processEndOfTestLine () { }	
}