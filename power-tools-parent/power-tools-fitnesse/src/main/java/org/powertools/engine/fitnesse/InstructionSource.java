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

import java.util.ArrayList;
import java.util.List;

import org.powertools.engine.ExecutionException;
import org.powertools.engine.sources.Procedure;
import org.powertools.engine.sources.TestLineImpl;
import org.powertools.engine.symbol.Scope;

import fit.Fixture;
import fit.Parse;


final class InstructionSource extends BaseTestSource {
    private final List<String> mParameterNames;

    private Procedure mProcedure;


    InstructionSource (Fixture fixture, Parse table, String logFilePath) {
        super (Scope.getGlobalScope (), fixture, table.parts, logFilePath);
        mParameterNames = new ArrayList<String> ();
    }


    @Override
    public void initialize () {
        processFixtureLine ();
        processHeaderLine ();
    }

    private void processHeaderLine () {
        mProcedure             = null;
        mRow                   = mRow.more;
        Parse currentCell      = mRow.parts;
        String instructionName = currentCell.text ();

        int position = 0;
        while ((currentCell = currentCell.more) != null) {
            final String text = currentCell.text();
            if (text.isEmpty ()) {
                mTestLine.setParts (readSentence (mRow.parts));
                mPublisher.publishTestLine (mTestLine);
                mPublisher.publishError ("empty cells not allowed");
                mPublisher.publishEndOfTestLine ();
                break;
            } else if (++position % 2 != 0) {
                mParameterNames.add (text);
                instructionName += " _";
            } else {
                instructionName += " " + text;
            }
        }

        linkToLogFile (mRow.parts);
        mTestLine.setParts (readSentence (mRow.parts));
        mPublisher.publishTestLine (mTestLine);

        mProcedure = new Procedure (instructionName);

        for (String parameterName : mParameterNames) {
            boolean isOutput = (parameterName.startsWith (OUTPUT_PARAMETER_PREFIX));
            String realParameterName = (isOutput ? parameterName.substring (OUTPUT_PARAMETER_PREFIX.length ()).trim () : parameterName);
            mProcedure.addParameter (realParameterName, isOutput);
        }
    }

    Procedure getProcedure () {
        if (mProcedure != null) {
            return mProcedure;
        } else {
            throw new ExecutionException ("no procedure created");
        }
    }

    @Override
    public TestLineImpl getTestLine () {
        // do not return instructions for execution
        // add them to the procedure instead, unevaluated
        // they are executed when the procedure is called
        while ((mRow = mRow.more) != null) {
            processSentence (readSentence (mRow.parts));
        }

        // indicate there are no instructions to execute from this source
        return null;
    }

    private void processSentence (List<String> parts) {
        if (!parts.isEmpty ()) {
            mProcedure.addInstruction (parts);
            linkToLogFile (mRow.parts);
            mTestLine.setParts (parts);
            mPublisher.publishTestLine (mTestLine);
        }

    }
}
