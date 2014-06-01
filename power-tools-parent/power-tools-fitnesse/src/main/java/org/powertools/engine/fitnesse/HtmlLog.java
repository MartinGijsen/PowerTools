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

package org.powertools.engine.fitnesse;

import java.io.PrintWriter;
import java.util.Date;

import org.powertools.engine.TestLine;
import org.powertools.engine.reports.BasicHtmlLog;


final class HtmlLog extends BasicHtmlLog {
    private final Reference mReference;

    HtmlLog (PrintWriter writer, String title) {
        super (writer, title);
        mReference = new Reference ();
        mWriter.println ("<TABLE>");
    }

    Reference getReference () {
        return mReference;
    }
    

    // start and finish the test run
    @Override
    public void start (Date dateTime) {
        super.start (dateTime);
        mWriter.println ("<TABLE>");
    }

    @Override
    public void finish (Date dateTime) {
        mWriter.println ("</TABLE>");
        super.finish (dateTime);
    }


    // the input
    @Override
    public void processTestLine (TestLine testLine) {
        writeTableRowStartWithTimestamp (mWriter);
        final int nrOfParts = testLine.getNrOfParts ();
        for (int partNr = 0; partNr < nrOfParts - 1; ++partNr) {
            mWriter.format ("<TD>%s</TD>", getCell (testLine, partNr));
        }
        mWriter.append ("<TD colspan=\"10\">");
        if (mLevel == 0) {
            mWriter.format ("<A id=\"id%s\">", mReference);
        }
        mWriter.append (getCell (testLine, nrOfParts - 1)).println ("</TD></TR>");
    }

    @Override
    public void processCommentLine (String testLine) {
        writeTableRowStartWithTimestamp (mWriter);
        mWriter.format ("<TD></TD><TD colspan=\"10\">%s</TD></TR>", testLine).println ();
    }

    @Override
    public void processCommentLine (TestLine testLine) {
        writeTableRowStartWithTimestamp (mWriter);
        final int nrOfParts = testLine.getNrOfParts ();
        for (int partNr = 0; partNr < nrOfParts - 1; ++partNr) {
            mWriter.format ("<TD>%s</TD>", testLine.getPart (partNr));
        }
        mWriter.format ("<TD colspan=\"10\">%s</TD></TR>", testLine.getPart (nrOfParts - 1)).println ();
    }

    @Override
    public void processEndSection () {
        mWriter.println ("</TABLE><BR/><TABLE>");
        mWriter.flush ();
    }


    @Override
    public void processEndOfTestLine () {
        mWriter.flush ();
    }
}
