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

package org.powertools.engine.instructions;

import org.powertools.engine.ProcedureRunner;
import org.powertools.engine.TestLine;
import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.sources.Procedure;
import org.powertools.engine.sources.TestSourceFactory;


public final class ProcedureExecutor implements Executor {
    private final Procedure mProcedure;
    private final ProcedureRunner mRunner;
    private final TestRunResultPublisher mPublisher;


    public ProcedureExecutor (Procedure procedure, ProcedureRunner runner, TestRunResultPublisher publisher) {
        mProcedure = procedure;
        mRunner    = runner;
        mPublisher = publisher;
    }


    @Override
    public boolean execute (TestLine testLine) {
        mRunner.invokeSource (new TestSourceFactory ().createProcedureTestSource (mProcedure, mRunner.getCurrentScope (), testLine, mPublisher));
        return true;
    }
}
