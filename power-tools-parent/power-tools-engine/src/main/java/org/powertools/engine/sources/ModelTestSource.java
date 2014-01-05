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

package org.powertools.engine.sources;

import java.util.LinkedList;
import java.util.List;

import org.powertools.engine.RunTime;
import org.powertools.engine.sources.model.DoneException;
import org.powertools.engine.sources.model.Model;


/*
 * This test source generates test lines from a model.
 * Processing starts with the start node of the model.
 * A strategy object determines the edge to the next node.
 * The strategy throws a DoneException to signal it is done.
 */
final class ModelTestSource extends TestSource {
    final Model mModel;

    private final String mFileName;
    private final String mSelector;
    private final String mDoneCondition;
    private final RunTime mRunTime;


    ModelTestSource (String fileName, String selector, String doneCondition, RunTime runTime) {
        super (runTime.getGlobalScope ());
        mModel         = new Model ();
        mFileName      = fileName;
        mSelector      = selector;
        mDoneCondition = doneCondition;
        mRunTime       = runTime;
    }

    @Override
    public void initialize () {
        mModel.initialize (mFileName, mSelector, mDoneCondition, mRunTime);
    }

    @Override
    public TestLineImpl getTestLine () {
        try {
            String instruction;
            do {
                instruction = mModel.getNextAction ();
            } while ("".equals (instruction));
            mTestLine.setParts (getParts (instruction));
            return mTestLine;
        } catch (DoneException de) {
            return null;
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
}
