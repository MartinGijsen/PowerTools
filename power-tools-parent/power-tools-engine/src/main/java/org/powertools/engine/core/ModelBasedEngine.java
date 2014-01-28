/* Copyright 2013-2014 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.core;

import java.io.File;
import org.powertools.engine.Context;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.reports.ReportFactory;
import org.powertools.engine.sources.TestSourceFactory;
import org.powertools.graph.GraphException;


/**
 * The model based engine obtains instructions from a model.
 * It reports to an HTML log.
 */
public class ModelBasedEngine extends Engine {
    public static void main (String[] args) {
        switch (args.length) {
        case 0:
            ModelRunner.run ();
            break;
        case 2:
            new ModelBasedEngine (args[0]).run (args[1]);
            break;
        case 4:
            new ModelBasedEngine (args[0]).run (args[0], args[1], args[2], args[3]);
            break;
        default:
            reportError ("Please specify a directory, model file name, selection strategy and stop condition");
            reportError ("\tselection strategies: random, weighted");
            reportError ("\tstop conditions: all nodes, all edges, end node, never");
        }
    }


    protected ModelBasedEngine (String resultsDirectory) {
        this (new RunTimeImpl (Context.create (resultsDirectory)));
    }

    protected ModelBasedEngine (RunTimeImpl runTime) {
        super (runTime);

        if (!new ReportFactory ().createKeywordsHtmlLog (runTime.getContext ())) {
            mPublisher.publishError ("could not open HTML log");
        }
        new ReportFactory ().createModelCoverageGraph (mRunTime.getContext ().getResultsDirectory ());

        registerBuiltinInstructions ();
    }

    @Override
    public final void run (String fileName) {
        File file = new File (fileName);
        if (!file.exists () || !file.isFile ()) {
            reportError ("file does not exist or is a directory");
        } else {
            run (file.getParent (), file.getName (), "random", "all edges");
        }
    }

    protected void run (String path, String fileName, String selector, String condition) {
        try {
            run (new TestSourceFactory ().createModelTestSource (path, fileName, selector, condition, mRunTime, mRunTime));
        } catch (GraphException ge) {
            reportError ("error: " + ge.getMessage ());
        } catch (ExecutionException ee) {
            reportError ("error: " + ee.getMessage ());
        } finally {
            mRunTime.getPublisher ().reset ();
        }
    }
}
