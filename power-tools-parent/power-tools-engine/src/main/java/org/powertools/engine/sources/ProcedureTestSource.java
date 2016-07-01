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

import java.util.Iterator;
import java.util.List;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.Scope;
import org.powertools.engine.TestLine;
import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.symbol.ScopeImpl;


final class ProcedureTestSource extends TestSource {
    private final Procedure mProcedure;
    private final Iterator<List<String>> mInstructionIter;


    ProcedureTestSource (Procedure procedure, Scope parentScope, TestLine testLine, TestRunResultPublisher publisher) {
        super (new ScopeImpl (parentScope), publisher);
        mProcedure = procedure;
        createParameters (testLine);
        mInstructionIter = procedure.instructionIterator ();
    }

    void createParameters (TestLine testLine) {
        List<ProcedureParameter> parameters = mProcedure.getParameters ();
        checkNrOfArguments (testLine.getNrOfParts () - 1, parameters.size ());
        createParameters (parameters, testLine);
    }

    private void checkNrOfArguments (int nrOfArguments, int nrOfParameters) {
        if (nrOfArguments != nrOfParameters) {
            throw new ExecutionException ("procedure %s expects %d arguments but receives %d", mProcedure.getName (), nrOfParameters, nrOfArguments);
        }
    }

    private void createParameters (List<ProcedureParameter> parameters, TestLine testLine) {
        int partNr = 0;
        for (ProcedureParameter parameter : parameters) {
            String argument = testLine.getPart (++partNr);
            if (parameter.isOutput ()) {
                mScope.createParameter (parameter.getName (), argument);
            } else {
                mScope.createConstant (parameter.getName (), argument);
            }
        }
    }
    
    @Override
    public void initialize () {
        mPublisher.publishIncreaseLevel ();
    }


    @Override
    public TestLineImpl getTestLine () {
        if (mInstructionIter.hasNext ()) {
            mTestLine.setParts (mInstructionIter.next ());
            return mTestLine;
        } else {
            return null;
        }
    }

    @Override
    public void cleanup () {
        for (ProcedureParameter parameter : mProcedure.getParameters ()) {
            if (parameter.isOutput ()) {
                try {
                    String name = parameter.getName ();
                    mPublisher.publishValue (name, mScope.get (name).getValue ());
                } catch (ExecutionException ee) {
                    // is a structure, just ignore
                }
            }
        }

        mPublisher.publishDecreaseLevel ();
    }
}
