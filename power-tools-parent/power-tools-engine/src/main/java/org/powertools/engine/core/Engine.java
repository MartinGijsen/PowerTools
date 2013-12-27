/*	Copyright 2012-2013 by Martin Gijsen (www.DeAnalist.nl)
 *
 *	This file is part of the PowerTools engine.
 *
 *	The PowerTools engine is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the License,
 *	or (at your option) any later version.
 *
 *	The PowerTools engine is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU Affero General Public License for more details.
 *
 *	You should have received a copy of the GNU Affero General Public License
 *	along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine.core;

import org.powertools.engine.ExecutionException;
import org.powertools.engine.reports.TestRunResultPublisher;
import org.powertools.engine.sources.Procedure;
import org.powertools.engine.sources.ProcedureException;
import org.powertools.engine.sources.TestSource;
import org.powertools.engine.sources.TestLineImpl;


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
 * Most of the test state is contained in the RunTime,
 * so that it can be accessed by (user defined) instruction sets.
 */
public abstract class Engine {
	protected final RunTimeImpl mRunTime;
	protected final TestRunResultPublisher mPublisher;

	protected TestLineImpl mTestLine;

	private final Instructions mInstructions;


	protected Engine (RunTimeImpl runTime) {
		mRunTime		= runTime;
		mInstructions	= new Instructions (runTime);
		mPublisher		= TestRunResultPublisher.getInstance ();
	}

	protected final void registerBuiltinInstructions () {
		BuiltinInstructions.register (mRunTime, mInstructions);
	}


	public void run (String sourceName) {
		throw new ExecutionException ("not supported by this engine");
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
		mInstructions.addProcedure (procedure);
	}
	
	protected final boolean evaluateTestLine () {
		try {
			mRunTime.evaluateExpressions (mTestLine);
			return true;
		} catch (ExecutionException ee) {
			mPublisher.publishTestLine (mTestLine);
			mPublisher.publishError (ee.getMessage ());
			mPublisher.publishEndOfTestLine ();
			return false;
		}
	}
	
	private boolean isAnInstruction () {
		return !mTestLine.getPart (0).isEmpty ();
	}
	
	private void processTestLine () {
		try {
			executeInstruction ();
		} catch (ExecutionException ee) {
			handleEngineException (ee);
		} catch (Exception e) {
			handleOtherException (e);
		}
		mPublisher.publishEndOfTestLine ();
	}

	private void executeInstruction () {
		mPublisher.publishTestLine (mTestLine);
		if (!mInstructions.getExecutor (mTestLine.getPart (0)).execute (mTestLine)) {
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
}