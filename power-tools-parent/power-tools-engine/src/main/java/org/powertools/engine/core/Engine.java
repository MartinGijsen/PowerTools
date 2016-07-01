/* Copyright 2012-2014 by Martin Gijsen (www.DeAnalist.nl)
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

import org.powertools.engine.ExecutionException;
import org.powertools.engine.Function;
import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.instructions.Executor;
import org.powertools.engine.sources.Procedure;
import org.powertools.engine.sources.ProcedureException;
import org.powertools.engine.sources.TestLineImpl;
import org.powertools.engine.sources.TestSource;
import org.powertools.engine.util.StringUtil;


/**
 * The Engine runs a test by repeating three steps:
 * 1) get a test line,
 * 2) evaluate its expressions,
 * 3) execute it.
 * <BR/><BR/>
 * The engine obtains test lines from the test line source(s), one by one.
 * If a test source finds a procedure (i.e. a scripted instruction),
 * the engine adds it to the set of known procedures.
 * <BR/><BR/>
 * The Instructions class provides the executor for known instructions.
 * <BR/><BR/>
 * A test can run to completion or be aborted.
 * <BR/><BR/>
 * Most of the test state is contained in the RunTime,
 * so that it can be accessed by (user defined) instruction sets.
 */
public abstract class Engine {
    protected final RunTimeImpl mRunTime;
    protected final TestRunResultPublisher mPublisher;

    protected TestLineImpl mTestLine;

    private final Instructions mInstructions;
    
    private boolean mAborting;
    

    protected Engine (RunTimeImpl runTime) {
        mRunTime      = runTime;
        mPublisher    = runTime.getPublisher ();
        mInstructions = new Instructions (runTime, mPublisher);
        mAborting     = false;
        Function.setRunTime (runTime);
    }

    protected final void registerBuiltins () {
        BuiltinInstructions.register (mRunTime, mInstructions);
        new BuiltinFunctions (mRunTime.getFunctions (), mRunTime.getPowerToolsParser ());
    }


    public void run (String sourceName) {
        throw new ExecutionException ("not supported by this engine");
    }

    public final void abort () {
        mAborting = true;
        mRunTime.mSourceStack.abort ();
    }

    protected final void run (TestSource source) {
        mPublisher.start (mRunTime.getContext ().getStartTime ());
        mRunTime.invokeSource (source);
        run ();
        mInstructions.cleanup ();
        mPublisher.finish ();
    }

    protected final void run () {
        while (getTestLine ()) {
            if (evaluateTestLine ()) {
                if (isAnInstruction ()) {
                    processTestLine ();
                } else {
                    processComment ();
                }
            }
        }
        mPublisher.publishEndOfSection ();
    }

    private boolean getTestLine () {
        while (true) {
            try {
                mTestLine = mRunTime.mSourceStack.getTestLine ();
                return mTestLine != null;
            } catch (ProcedureException pe) {
                addProcedure (pe.getProcedure ());
            }
        }
    }

    protected final void addProcedure (Procedure procedure) {
        if (procedure != null) {
            mInstructions.addProcedure (procedure);
        }
    }

    protected final boolean evaluateTestLine () {
        try {
            mRunTime.evaluateExpressions (mTestLine);
            return true;
        } catch (ExecutionException ee) {
            mPublisher.publishTestLine (mTestLine);
            handleEngineException (ee);
            mPublisher.publishEndOfTestLine ();
        } catch (Exception e) {
            mPublisher.publishTestLine (mTestLine);
            handleOtherException (e);
            mPublisher.publishEndOfTestLine ();
        }
        return false;
    }

    private boolean isAnInstruction () {
        return !mTestLine.getPart (0).isEmpty ();
    }

    private void processTestLine () {
        try {
            mPublisher.publishTestLine (mTestLine);
            checkParts ();
            executeInstruction ();
        } catch (ExecutionException ee) {
            handleEngineException (ee);
        } catch (Exception e) {
            handleOtherException (e);
        }
        if (mAborting) {
            mPublisher.publishWarning ("aborting run");
        }
        mPublisher.publishEndOfTestLine ();
    }

    private void checkParts () {
        int nrOfParts = mTestLine.getNrOfParts ();
        for (int partNr = 0; partNr < nrOfParts; ++partNr) {
            String part  = mTestLine.getPart (partNr);
            int position = StringUtil.indexOfNonPrintableCharacter (part);
            if (position >= 0) {
                mPublisher.publishWarning (String.format ("unexpected character nr %d at position %d in '%s'", (int) part.charAt (position) + 1, position, part));
            }
        }
    }
    
    private void executeInstruction () {
        Executor executor = mInstructions.getExecutor (mTestLine.getPart (0));
        if (!executor.execute (mTestLine)) {
            mPublisher.publishError ("instruction returned 'false'");
        }
    }

    private void handleEngineException (ExecutionException ee) {
        mPublisher.publishError (ee.getMessage ());
        if (ee.hasStackTrace ()) {
            mPublisher.publishStackTrace (ee);
        }
    }

    private void handleOtherException (Exception e) {
        mPublisher.publishError (e.toString () + " caught: " + e.getMessage ());
        if (e.getStackTrace () != null) {
            mPublisher.publishStackTrace (e);
        }
    }

    private void processComment () {
        if (!mTestLine.isEmpty ()) {
            mPublisher.publishCommentLine (mTestLine);
        }
    }
    
    protected static void reportError (String message) {
        System.err.println ("error: " + message);
    }
}
