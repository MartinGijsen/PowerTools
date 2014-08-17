/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import fit.Parse;
import fit.exception.FitParseException;
import fitnesse.testsystems.CompositeTestSystemListener;
import fitnesse.testsystems.TestPage;
import fitnesse.testsystems.TestSystem;
import fitnesse.testsystems.TestSystemListener;
import java.net.URLClassLoader;


public final class PowerToolsTestSystem implements TestSystem {
    private final CompositeTestSystemListener mTestSystemListener;
    //private final URLClassLoader mClassLoader;

    private FitNesseEngine2 mEngine;


    PowerToolsTestSystem (URLClassLoader classLoader) {
        //mClassLoader        = classLoader;
        mTestSystemListener = new CompositeTestSystemListener ();
        mEngine             = null;
        Thread.currentThread ().setContextClassLoader (classLoader);
    }

    @Override
    public void addTestSystemListener (TestSystemListener listener) {
        mTestSystemListener.addTestSystemListener (listener);
    }

    @Override
    public String getName () {
        return "PowerTools";
    }

    @Override
    public void start () throws IOException {
        mEngine = new FitNesseEngine2 ();
        mTestSystemListener.testSystemStarted (this);
    }

    @Override
    public boolean isSuccessfullyStarted () {
        return true;
    }

    @Override
    public void runTests (TestPage page) throws IOException {
        mTestSystemListener.testStarted (page);
        Parse table;
        try {
            table = new Parse (page.getDecoratedData ().getHtml ());
        } catch (FitParseException fpe) {
            return;
        }
        while (table != null) {
            mEngine.executeTable (table);
            mTestSystemListener.testOutputChunk (toHtml (table));
            table = table.more;
        }
        mTestSystemListener.testComplete (page, mEngine.getSummary ());
    }

    private String toHtml (Parse table) {
        StringWriter sw    = new StringWriter ();
        PrintWriter writer = new PrintWriter (sw);
        Parse nextTable    = table.more;
        table.more         = null;
        if (table.trailer == null) {
            table.trailer = "";
        }
        table.print (writer);
        table.more = nextTable;
        writer.close ();
        return sw.toString ();
    }

    @Override
    public void bye () {
        mEngine = null;
    }

    @Override
    public void kill () {
        if (mEngine != null) {
            mEngine.abort ();
        }
    }
}
