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

package org.powertools.engine.sources;

import java.util.LinkedList;
import java.util.List;
import org.powertools.engine.RunTime;
import org.powertools.engine.instructions.ProcedureRunner;
import org.powertools.engine.sources.model.Model;
import org.powertools.engine.sources.model.Submodel;


/*
 * This test source generates test lines from a model.
 * It moves from state to state, returning the instructions it encounters.
 * A strategy object selects the edge to the next node.
 */
class ModelTestSource extends TestSource {
    protected final RunTime mRunTime;
    protected final Model   mModel;

    private final ProcedureRunner mRunner;


    ModelTestSource (Model model, RunTime runTime, ProcedureRunner runner) {
        super (runTime.getGlobalScope ());
        mModel    = model;
        mRunTime  = runTime;
        mRunner   = runner;
    }

    @Override
    public void initialize () {
        mModel.initialize ();
    }

    @Override
    public final TestLineImpl getTestLine () {
        for ( ; ; ) {
            String action = mModel.getNextAction ();
            if (action == null) {
                return null;
            } else {
                List<String> parts     = getParts (action);
                String instructionName = parts.get (0);
                if (instructionName.startsWith ("submodel ")) {
                    return pushSubmodelTestSource (parts).getTestLine ();
                } else {
                    mTestLine.setParts (parts);
                    return mTestLine;
                }
            }
        }
    }
    
    private List<String> getParts (String instruction) {
        String[] partsArray = instruction.split ("\"");
        List<String> parts  = new LinkedList<String> ();

        parts.add (getInstructionName (partsArray));

        for (int partNr = 1; partNr < partsArray.length; partNr += 2) {
            parts.add (partsArray[partNr]);
        }

        return parts;
    }

    private String getInstructionName (String[] parts) {
        String instructionName = parts[0];
        for (int partNr = 2; partNr < parts.length; partNr += 2) {
            instructionName += " _ " + parts[partNr];
        }
        if (parts.length % 2 == 0) {
            instructionName += " _";
        }
        return instructionName;
    }

    private TestSource pushSubmodelTestSource (List<String> parts) {
        String fileName     = parts.get (1);
        TestSource submodel = new ModelTestSource (new Submodel (fileName, mModel), mRunTime, mRunner);
        mRunner.invokeSource (submodel);
        return submodel;
    }
    
    @Override
    public void cleanup () {
        mModel.cleanup ();
    }
}
