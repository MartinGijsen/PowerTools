/* Copyright 2012-1013 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.reports;

import org.powertools.engine.TestResultSubscriber;
import org.powertools.engine.TestLineSubscriber;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.powertools.engine.TestLine;


public abstract class BasicHtmlLog implements TestLineSubscriber, TestResultSubscriber {
    protected final SimpleDateFormat mDateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

    protected final PrintWriter mWriter;

    protected int mLevel;


    public BasicHtmlLog (PrintWriter writer, String title) {
        mLevel  = 0;

        mWriter = writer;
        mWriter.format ("<HTML><HEAD><TITLE>%s</TITLE>\n", title);
        mWriter.println ("<STYLE type=\"text/css\">");
        mWriter.println ("table { border-collapse:collapse; }");
        mWriter.println ("td { border:1px solid black; padding:0 3px; }");
        mWriter.println ("div td { border-style:none; }");
        mWriter.println ("hr { margin:0; }");
        mWriter.println ("</STYLE>");
        mWriter.println ("</HEAD>");
        mWriter.println ("<BODY>");
    }


    // start and finish the test run
    @Override
    public void start (Date dateTime) {
        writeTableRowStartWithTimestamp (mWriter);
        mWriter.format ("<TD>started at %s</TD></TR></TABLE><BR/>\n", mDateFormat.format (dateTime));
    }

    @Override
    public void finish (Date dateTime) {
        writeTableRowStartWithTimestamp (mWriter);
        mWriter.format ("<TD>finished at %s</TD></TR></TABLE>\n", mDateFormat.format (dateTime));
        mWriter.println ("</BODY></HTML>");
        mWriter.close ();
    }


    protected final String getCell (TestLine testLine, int partNr) {
        String originalPart = testLine.getOriginalPart (partNr);
        if (originalPart == null) {
            return testLine.getPart (partNr);
        } else {
            StringBuilder sb = new StringBuilder ();
            sb.append (originalPart).append ("<HR/>");
            renderSymbolValues (sb, testLine.getSymbolValues (partNr));
            sb.append (testLine.getPart (partNr));
            return sb.toString ();
        }
    }

    private static void renderSymbolValues (StringBuilder sb, Map<String, String> symbolValues) {
        if (symbolValues != null) {
            sb.append("<DIV><TABLE>\n");
            for (String symbolName : symbolValues.keySet ()) {
                sb.append ("<TR><TD>").append (symbolName).append ("</TD><TD>= ")
                  .append (symbolValues.get (symbolName)).append ("</TD></TR>\n");
            }
            sb.append ("</TABLE></DIV><HR/>\n");
        }
    }

    // the results
    @Override
    public void processStackTrace (String[] stackTraceLines) {
        writeTableRowStartWithTimestamp (mWriter);
        mWriter.println ("<TD colspan=\"10\">stack trace:<BR/>");
        int nrOfElements = stackTraceLines.length;
        for (int elementNr = 0; elementNr < nrOfElements; ++elementNr) {
            mWriter.append (stackTraceLines[elementNr]).println ("<BR/>");
        }
        mWriter.println ("</TD></TR>");
    }

    @Override
    public void processError (String message) {
        writeTableRowStartWithTimestamp (mWriter);
        mWriter.format ("<TD colspan=\"10\" style=\"background-color:#FFAAAA\">%s</TD></TR>", formatMessage (message)).println ();
    }

    @Override
    public void processWarning (String message) {
        writeTableRowStartWithTimestamp (mWriter);
        mWriter.format ("<TD colspan=\"10\" style=\"background-color:#FFFFAA\">%s</TD></TR>", formatMessage (message)).println ();
    }

    @Override
    public void processInfo (String message) {
        writeTableRowStartWithTimestamp (mWriter);
        mWriter.format ("<TD colspan=\"10\">%s</TD></TR>", formatMessage (message)).println ();
    }

    @Override
    public void processLink (String url) {
        writeTableRowStartWithTimestamp (mWriter);
        mWriter.format ("<TD colspan=\"10\">url: <A href=\"%s\">%s</A></TD></TR>", url, url).println ();
    }

    @Override
    public void processIncreaseLevel () {
        ++mLevel;
        mWriter.println ("<TR><TD colspan=\"10\"><BR/><TABLE>");
    }

    @Override
    public void processDecreaseLevel () {
        --mLevel;
        mWriter.println ("</TABLE><BR/></TD></TR>");
    }

    protected void writeTableRowStartWithTimestamp (PrintWriter writer) {
        Calendar c = Calendar.getInstance ();
        writer.format("<TR><TD width='50'>%1$tT</TD>", c);
    }
    
    private String formatMessage (String message) {
        return message.replaceAll ("\n", "<BR/>\n");
    }
}
